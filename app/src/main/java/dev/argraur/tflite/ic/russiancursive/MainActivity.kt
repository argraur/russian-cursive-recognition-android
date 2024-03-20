package dev.argraur.tflite.ic.russiancursive

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import dev.argraur.tflite.ic.russiancursive.ml.RussianCursiveClassifier
import dev.argraur.tflite.ic.russiancursive.ui.theme.TFLRussianCursiveRecognizerTheme
import org.tensorflow.lite.support.label.Category

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val classifier = RussianCursiveClassifier(this)

        enableEdgeToEdge()

        setContent {
            TFLRussianCursiveRecognizerTheme {
                var bitmap: Bitmap? by remember { mutableStateOf(null) }

                val imageCropLauncher =
                    rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
                        if (result.isSuccessful) {
                            result.uriContent?.let {
                                val source = ImageDecoder.createSource(this.contentResolver, it)
                                bitmap = ImageDecoder.decodeBitmap(source)
                            }

                        } else {
                            println("ImageCropping error: ${result.error}")
                        }
                    }

                var result by remember { mutableStateOf(listOf<Category>()) }

                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Russian Cursive Recognition", style = MaterialTheme.typography.titleLarge) },
                        actions = {
                            IconButton(onClick = { bitmap = null }) { Icon(Icons.Filled.Refresh, "Reset") }
                            IconButton(
                                onClick = {
                                    val cropOptions = CropImageContractOptions(null, CropImageOptions())
                                    imageCropLauncher.launch(cropOptions)
                                }) {
                                    Icon(painterResource(R.drawable.ic_photo_camera_24), "Add from camera")
                                }
                        },
                    )
                }) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding).fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (bitmap == null)
                            Text("Use a camera button at the top to add your image", fontFamily = FontFamily.Monospace, modifier = Modifier.padding(64.dp), textAlign = TextAlign.Center)
                        bitmap?.let {
                            Image(bitmap = bitmap!!.asImageBitmap(), contentDescription = "Chosen image", modifier = Modifier
                                .height(164.dp)
                                .width(164.dp).clip(MaterialTheme.shapes.extraLarge))

                            ExtendedFloatingActionButton(
                                onClick = { result = classifier.calcProbabilityForBitmap(bitmap!!) },
                                icon = { Icon(painterResource(R.drawable.ic_auto_awesome_24), "Detect letter") },
                                text = { Text("Detect letter") }
                            )

                            Column(horizontalAlignment = Alignment.Start) {
                                if (result.isNotEmpty()) {
                                    result.reversed().take(10).forEach {
                                        Text(
                                            stringResource(R.string.result_entry, it.label, it.score),
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TFLRussianCursiveRecognizerTheme {
        Greeting("Android")
    }
}