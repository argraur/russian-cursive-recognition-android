package dev.argraur.tflite.ic.russiancursive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import dev.argraur.tflite.ic.russiancursive.ml.RussianCursiveML
import dev.argraur.tflite.ic.russiancursive.ml.data.testImages
import dev.argraur.tflite.ic.russiancursive.ui.theme.TFLRussianCursiveRecognizerTheme
import org.tensorflow.lite.support.label.Category

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val classifier = RussianCursiveML(this)
        enableEdgeToEdge()
        setContent {
            TFLRussianCursiveRecognizerTheme {
                var dropdownMenuExpanded by remember { mutableStateOf(false) }
                var image by remember { mutableStateOf(testImages[0]) }
                var result by remember { mutableStateOf(listOf<Category>()) }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(bitmap = ImageBitmap.imageResource(image.resId), contentDescription = "Chosen image", modifier = Modifier
                            .height(128.dp)
                            .width(128.dp))

                        ExposedDropdownMenuBox(expanded = dropdownMenuExpanded, onExpandedChange = { dropdownMenuExpanded = it }) {
                            OutlinedTextField(value = image.letter, onValueChange = {}, readOnly = true, singleLine = true, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownMenuExpanded) }, modifier = Modifier.menuAnchor())
                            ExposedDropdownMenu(expanded = dropdownMenuExpanded, onDismissRequest = { dropdownMenuExpanded = false }) {
                                testImages.forEach {
                                    DropdownMenuItem(text = { Text(it.letter) }, onClick = { image = it; dropdownMenuExpanded = false})
                                }
                            }
                        }

                        ExtendedFloatingActionButton(onClick = { result = classifier.calcProbabilityForBitmap(getDrawable(image.resId)?.toBitmap()!!) }) {
                            Text(stringResource(R.string.detect_letter_button))
                        }

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