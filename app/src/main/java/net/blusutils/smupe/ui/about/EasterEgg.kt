package net.blusutils.smupe.ui.about

import android.graphics.Typeface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import net.blusutils.smupe.R
import kotlin.random.Random

private val easterEggs = listOf<@Composable ()->Unit>(
    { // recipe of meat dumplings!
        val recipe = stringResource(R.string.dumplings)
        Text(
            recipe,
            Modifier
                .padding(4.dp, 16.dp)
                .fillMaxHeight(.9f)
                .verticalScroll(rememberScrollState()),
            style = MaterialTheme.typography.bodyMedium
                .copy(
                    fontFeatureSettings = "thum",
                    fontFamily = FontFamily(
                        Typeface.MONOSPACE
                    )
                )
        )
    },
    { // singularity has no limits!
        Card {
            val textMeasurer = rememberTextMeasurer()
            Canvas(modifier = Modifier.fillMaxWidth().fillMaxHeight(.8f)) {
                drawRect(color = Color.Black)
                drawText(
                    textMeasurer = textMeasurer,
                    text = "The Event Horizon?",
                    size = Size(3f, 3f)
                )
                repeat(500) {
                    val x = Random.nextInt(size.width.toInt())
                    val y = Random.nextInt(size.height.toInt())
                    val radius = Random.nextInt(5, 10)
                    drawCircle(
                        color = Color.White,
                        radius = radius.toFloat(),
                        center = Offset(x.toFloat(), y.toFloat())
                    )
                }

            }
        }
    },
    { // there's no rick roll!
        Card {
            AsyncImage("https://i.imgur.com/hPVaM4J.jpeg", null)
        }
    },
//    { // trumpet skeleton!
//        AndroidView(
//            factory = { context ->
//                WebView(context).apply {
//                    settings.javaScriptEnabled = true
//                    webViewClient = WebViewClient()
//                    settings.loadWithOverviewMode = true
//                    settings.useWideViewPort = true
//                    settings.setSupportZoom(true)
//                }
//            },
//            update = { webView ->
//                webView.loadData("""<!DOCTYPE html><html><body><iframe
//                    width="1280"
//                    height="720"
//                    src="https://www.youtube.com/embed/HMdMx9na96U"
//                    title="Мистер дудец"
//                    frameborder="0"
//                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share">
//
//                    </iframe>
//                    </body></html>
//                """.trimIndent(), "text/html", "UTF-8")
//            }
//        )
//    }
)

@Composable
fun EasterEgg(close: () -> Unit) {
    Dialog(onDismissRequest = close) {
        Card {
            Column(Modifier.padding(16.dp)) {
                easterEggs.random()()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    TextButton(
                        onClick = close,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(stringResource(R.string.dismiss_dialog))
                    }
                }
            }
        }
    }
}