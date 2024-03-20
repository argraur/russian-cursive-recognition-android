package dev.argraur.tflite.ic.russiancursive.ml.data

import dev.argraur.tflite.ic.russiancursive.R

data class TestImage(
    val resId: Int,
    val letter: String
)

val testImages = listOf(
    TestImage(R.drawable.k, "к"),
    TestImage(R.drawable.m, "м"),
    TestImage(R.drawable.r, "р"),
    TestImage(R.drawable.a, "а"),
    TestImage(R.drawable.ae, "э"),
    TestImage(R.drawable.b, "б"),
    TestImage(R.drawable.cz, "Ц"),
    TestImage(R.drawable.g, "Г"),
    TestImage(R.drawable.i, "и"),
    TestImage(R.drawable.l, "л"),
    TestImage(R.drawable.n, "н"),
    TestImage(R.drawable.o, "о"),
    TestImage(R.drawable.p, "п"),
    TestImage(R.drawable.q, "ъ"),
    TestImage(R.drawable.t, "Т"),
    TestImage(R.drawable.t2, "Т (тип 2)"),
    TestImage(R.drawable.yo, "ё"),
    TestImage(R.drawable.z, "з"),


)