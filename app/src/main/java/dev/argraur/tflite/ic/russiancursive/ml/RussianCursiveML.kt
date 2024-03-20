package dev.argraur.tflite.ic.russiancursive.ml

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category

class RussianCursiveML(context: Context) {
    private val model = RussianCursive.newInstance(context)

    fun calcProbabilityForBitmap(bitmap: Bitmap): List<Category> {
        val tensorImage = TensorImage.fromBitmap(bitmap)

        val outputs = model.process(tensorImage)

        Log.i("RussianCursiveClassifier", outputs.probabilityAsCategoryList.toString())

        return outputs.probabilityAsCategoryList.sortedBy { it.score }
    }
}