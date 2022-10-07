package com.groupfive.fitnessapp

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.core.impl.Config
import androidx.camera.core.impl.UseCaseConfig
import androidx.camera.core.impl.UseCaseConfigFactory
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import com.groupfive.fitnessapp.databinding.FragmentExerciseCameraBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.atan2

@ExperimentalGetImage
class ExerciseCameraFragment : Fragment() {

    companion object {
        private const val TAG = "ExerciseCameraFragment"
    }

    private lateinit var binding: FragmentExerciseCameraBinding

    // Camera
    private lateinit var cameraExecutor: ExecutorService

    // ML-kit
    private lateinit var poseDetector: PoseDetector
    private lateinit var imageAnalyzer: ImageAnalyzer
    private lateinit var imageAnalysis: ImageAnalysis

    // Permission handler
    private val activityResultLauncher =
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Request camera permission
        activityResultLauncher.launch(Manifest.permission.CAMERA)
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
            imageAnalyzer = ImageAnalyzer(poseDetector)
            imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            cameraExecutor = Executors.newSingleThreadExecutor()

            imageAnalysis.setAnalyzer(cameraExecutor, imageAnalyzer)

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalysis)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private class ImageAnalyzer(val poseDetector: PoseDetector) : ImageAnalysis.Analyzer {
        fun getAngle(firstPoint: PoseLandmark, midPoint: PoseLandmark, lastPoint: PoseLandmark): Double {
            var result = Math.toDegrees(
                (atan2(lastPoint.position.y - midPoint.position.y,
                    lastPoint.position.x - midPoint.position.x)
                        - atan2(firstPoint.position.y - midPoint.position.y,
                    firstPoint.position.x - midPoint.position.x)).toDouble()
            )
            result = abs(result) // Angle should never be negative
            if (result > 180) {
                result = 360.0 - result // Always get the acute representation of the angle
            }
            return result
        }

        fun getAngle(pose: Pose, firstPoint: Int, midPoint: Int, lastPoint: Int) : Double {
            return getAngle(
                pose.getPoseLandmark(firstPoint)!!,
                pose.getPoseLandmark(midPoint)!!,
                pose.getPoseLandmark(lastPoint)!!)
        }

        var armUp = false
        var reps = 0

        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                poseDetector.process(image)
                    .addOnSuccessListener { pose ->
                        if(pose.allPoseLandmarks.isNotEmpty()) {
//                            Log.w(TAG, pose.allPoseLandmarks.map{ it.position3D.toString() }.toString())

                            val rightHipAngle = getAngle(pose,
                                PoseLandmark.RIGHT_WRIST,
                                PoseLandmark.RIGHT_SHOULDER,
                                PoseLandmark.RIGHT_HIP)

                            if(armUp) {
                                if(rightHipAngle > 120) {
                                    armUp = false
                                }
                            } else {
                                if(rightHipAngle < 30) {
                                    armUp = true
                                    reps++
                                    Log.w(TAG, "Reps: $reps")
                                }
                            }

//                            Log.w(TAG, "Right hip $rightHipAngle")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "ml-kit failed to process image from camera: " + exception.message)
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            }
        }
    }
}

