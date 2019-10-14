package com.example.evoke.utils


import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import java.util.*


class QrCodeAnalyzer(
    private val onQrCodesDetected: (qrCodes: List<FirebaseVisionBarcode>) -> Unit,
    private val onImageDetected: (name: String) -> Unit,
    val context: Context
) : ImageAnalysis.Analyzer {


    override fun analyze(image: ImageProxy, rotationDegrees: Int) {
        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
            .build()

        val detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)

        val rotation = rotationDegreesToFirebaseRotation(rotationDegrees)
        val visionImage = FirebaseVisionImage.fromMediaImage(image.image!!, rotation)

        // each ten times call this function
        // so we do not over load the server with new images
        // this values can increased from server side
        // so the performance match the server balance
        if (gotoImage == 0) {
            sendImage(visionImage.bitmap)
            gotoImage = 10
        } else {
            gotoImage--
        }
        detector.detectInImage(visionImage)
            .addOnSuccessListener { barcodes ->
                onQrCodesDetected(barcodes)


            }
            .addOnFailureListener {
                Log.e("QrCodeAnalyzer", "something went wrong", it)
            }

    }

    private fun rotationDegreesToFirebaseRotation(rotationDegrees: Int): Int {
        return when (rotationDegrees) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> throw IllegalArgumentException("Not supported")
        }
    }

    private fun sendImage(bitmap: Bitmap) {
        var st = SendTest.SendImageRequest(bitmap, context)
//        Timber.i("send image to server")
        onImageDetected("test")

    }

    companion object {
        var gotoImage = 10
    }
}