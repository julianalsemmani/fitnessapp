package com.groupfive.fitnessapp.screens.exercisecamera

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.groupfive.fitnessapp.R
import com.groupfive.fitnessapp.databinding.FragmentExerciseCameraBinding
import com.groupfive.fitnessapp.exercise.ExerciseDetector
import com.groupfive.fitnessapp.exercise.ExerciseDetectorFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalGetImage
class ExerciseCameraFragment : Fragment() {

    private val args: ExerciseCameraFragmentArgs by navArgs()
    private val viewModel: ExerciseCameraViewModel by viewModels()

    private lateinit var binding: FragmentExerciseCameraBinding

    // Camera
    private lateinit var cameraExecutor: ExecutorService

    private val imageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()

    private var lensFacing = CameraSelector.LENS_FACING_BACK

    // ML-kit
    private val poseDetector: PoseDetector  = PoseDetection.getClient(
        AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
            .build()
    )

    private lateinit var exerciseDetector: ExerciseDetector

    // Permission handler
    private val permissionResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Handle Permission granted/rejected
            if (isGranted) {
                // Permission is granted
                startCamera()
            } else {
                // Permission is denied
                Toast.makeText(context,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
            }
        }

    /**
     * Notifies whether user is in frame or not
     */
    private fun onWithinFrame(withinFrame: Boolean) {
        if(withinFrame) {
            binding.repView.setTextColor(Color.GREEN)
        } else {
            binding.repView.setTextColor(Color.RED)

            binding.poseView.setPose(null)
        }
    }

    /**
     * Called when a pose has been detected
     */
    private fun onPoseDetected(pose: Pose)  {
        // Check if we have a repetition
        val repetitionResult = exerciseDetector.detectRepetition(pose)
        if (repetitionResult.repetition) {
            viewModel.addRep()
        }

        // Send failing and passing checks to pose view
        binding.poseView.setConstraintsResult(repetitionResult)

        // Send the pose to pose view to for drawing
        binding.poseView.setPose(pose)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Request camera permission
        permissionResultLauncher.launch(Manifest.permission.CAMERA)

        exerciseDetector = ExerciseDetectorFactory.create(args.workoutType)
        viewModel.setWorkoutType(args.workoutType)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExerciseCameraBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchCameraButton.setOnClickListener {
            lensFacing =
                if(lensFacing == CameraSelector.LENS_FACING_BACK)
                    CameraSelector.LENS_FACING_FRONT
                else
                    CameraSelector.LENS_FACING_BACK

            startCamera()
        }

        binding.completeFAB.setOnClickListener {
            viewModel.submitWorkoutSession()
            findNavController().navigate(R.id.action_global_homeFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()


            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            cameraExecutor = Executors.newSingleThreadExecutor()

            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy: ImageProxy ->
                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                    poseDetector.process(image)
                        .addOnSuccessListener { pose ->
                            // Pass image info to pose view such that it can transform properly
                            val isImageFlipped = lensFacing == CameraSelector.LENS_FACING_FRONT
                            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                            if (rotationDegrees == 0 || rotationDegrees == 180) {
                                binding.poseView.setImageSourceInfo(imageProxy.width, imageProxy.height, isImageFlipped)
                            } else {
                                binding.poseView.setImageSourceInfo(imageProxy.height, imageProxy.width, isImageFlipped)
                            }

                            // Perform pose detection if detected
                            if (pose.allPoseLandmarks.isNotEmpty()) {
                                // Check that user is likely within frame before detection
                                if (pose.allPoseLandmarks.all { it.inFrameLikelihood >= 0.9 }) {
                                    onWithinFrame(true)
                                    onPoseDetected(pose)
                                } else {
                                    onWithinFrame(false)
                                }
                            } else {
                                onWithinFrame(false)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e(
                                javaClass.name,
                                "ml-kit failed to process image from camera: " + exception.message
                            )
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                }
            }

            // Select camera based on desired lens facing
            val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

            try {

                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalysis)

            } catch(exc: Exception) {
                Log.e(javaClass.name, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }
}

