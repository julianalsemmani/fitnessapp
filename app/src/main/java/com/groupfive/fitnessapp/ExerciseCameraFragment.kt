package com.groupfive.fitnessapp

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import com.groupfive.fitnessapp.databinding.FragmentExerciseCameraBinding
import com.groupfive.fitnessapp.exercise.ExerciseDetector
import com.groupfive.fitnessapp.exercise.SquatExerciseDetector
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalGetImage
class ExerciseCameraFragment : Fragment() {

    private lateinit var binding: FragmentExerciseCameraBinding

    // Camera
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var imageAnalysis: ImageAnalysis

    // ML-kit
    private lateinit var poseDetector: PoseDetector

    private lateinit var exerciseImageAnalyzer: ExerciseImageAnalyzer
    private var exerciseDetector: ExerciseDetector = SquatExerciseDetector()

    private var reps = 0

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
     * Called when a new exercise repition has been detected
     */
    private fun onRepetition() {
        reps++
        binding.repView.text = reps.toString()

    }

    /**
     * Notifies whether user is in frame or not
     */
    private fun onWithinFrame(withinFrame: Boolean) {
        if(withinFrame) {
            binding.repView.setTextColor(Color.GREEN)
        } else {
            binding.repView.setTextColor(Color.RED)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Request camera permission
        permissionResultLauncher.launch(Manifest.permission.CAMERA)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExerciseCameraBinding.inflate(layoutInflater)
        return binding.root
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
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // Pose detector image analysis
            poseDetector = PoseDetection.getClient(
                PoseDetectorOptions.Builder()
                    .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                    .build())
            exerciseImageAnalyzer = ExerciseImageAnalyzer(poseDetector, exerciseDetector,
                onRepetition = this::onRepetition,
                onWithinFrame = this::onWithinFrame
            )
            imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            cameraExecutor = Executors.newSingleThreadExecutor()

            imageAnalysis.setAnalyzer(cameraExecutor, exerciseImageAnalyzer)

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

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

    private class ExerciseImageAnalyzer(
        val poseDetector: PoseDetector,
        val exerciseDetector: ExerciseDetector,
        val onRepetition: ()->Unit,
        val onWithinFrame: (Boolean)->Unit) : ImageAnalysis.Analyzer {

        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                poseDetector.process(image)
                    .addOnSuccessListener { pose ->
                        if(pose.allPoseLandmarks.isNotEmpty()) {
                            // Check that user is likely within frame before detection
                            if(pose.allPoseLandmarks.all { it.inFrameLikelihood >= 0.9 }) {
                                if(exerciseDetector.detectRepetition(pose)) {
                                    onRepetition()
                                }
                                onWithinFrame(true)
                            } else {
                                onWithinFrame(false)
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e(javaClass.name, "ml-kit failed to process image from camera: " + exception.message)
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            }
        }
    }
}

