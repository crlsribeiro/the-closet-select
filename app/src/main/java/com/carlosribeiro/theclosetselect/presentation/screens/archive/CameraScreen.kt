package com.carlosribeiro.theclosetselect.presentation.screens.archive

import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File
import java.util.concurrent.Executors

private val CameraGold = Color(0xFFB8972A)
private val CameraBackground = Color(0xFF0D0D0D)

@Composable
fun CameraScreen(
    onPhotoCaptured: (Uri) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var isCapturing by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose { cameraExecutor.shutdown() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CameraBackground)
    ) {
        CameraPreview(
            imageCapture = imageCapture,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(0.85f)
                .align(Alignment.Center)
                .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
        )

        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Text(
            text = "Position garment in natural light",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 13.sp,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 260.dp)
                .fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "CAPTURE NEW GARMENT",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(if (isCapturing) Color.Gray else CameraGold)
                    .border(3.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isCapturing) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(28.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    IconButton(
                        onClick = {
                            isCapturing = true
                            capturePhoto(
                                context = context,
                                imageCapture = imageCapture,
                                executor = cameraExecutor,
                                onPhotoCaptured = { uri ->
                                    isCapturing = false
                                    onPhotoCaptured(uri)
                                },
                                onError = { isCapturing = false }
                            )
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_camera),
                            contentDescription = "Capture",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CameraPreview(
    imageCapture: ImageCapture,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val previewView = remember {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.PERFORMANCE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    LaunchedEffect(imageCapture) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    )
}

private fun capturePhoto(
    context: Context,
    imageCapture: ImageCapture,
    executor: java.util.concurrent.Executor,
    onPhotoCaptured: (Uri) -> Unit,
    onError: () -> Unit
) {
    val photoDir = File(context.cacheDir, "camera").also { it.mkdirs() }
    val photoFile = File(photoDir, "garment_${System.currentTimeMillis()}.jpg")

    val photoUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        photoFile
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                onPhotoCaptured(photoUri)
            }

            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
                onError()
            }
        }
    )
}