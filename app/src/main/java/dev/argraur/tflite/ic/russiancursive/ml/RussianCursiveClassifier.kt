package dev.argraur.tflite.ic.russiancursive.ml

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category

class RussianCursiveClassifier(context: Context) {
    private val model = RussianCursive.newInstance(context)

    fun calcProbabilityForBitmap(bitmap: Bitmap): List<Category> {
        val tensorImage = TensorImage.fromBitmap(bitmap.copy(Bitmap.Config.ARGB_8888,true))

        val outputs = model.process(tensorImage)

        Log.i("RussianCursiveClassifier", outputs.probabilityAsCategoryList.toString())

        return outputs.probabilityAsCategoryList.sortedBy { it.score }
    }
}