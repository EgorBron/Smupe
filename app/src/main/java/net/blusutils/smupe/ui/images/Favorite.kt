package net.blusutils.smupe.ui.images

import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import kotlinx.coroutines.delay
import net.blusutils.smupe.R
import net.blusutils.smupe.data.room.util.FavesUtil
import net.blusutils.smupe.ui.misc.CenteredContainer

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Favorite(imageUrl: Uri, isHeartVisible: Boolean, setHeartVisible: (Boolean)->Unit) {


    Log.d("FavComposable", "Appeared")

    var heartRsrc by remember { mutableIntStateOf(R.drawable.baseline_favorite_24) }

    // another bad trick...
//    var faveState by rememberSaveable { mutableStateOf(false) }

    CenteredContainer(columnModifier = Modifier.fillMaxSize(), rowModifier = Modifier.fillMaxSize()) {


        Log.d("FavComposable", "In container")

        AnimatedVisibility(
            isHeartVisible,
            Modifier.scale(4f),
            scaleIn(tween(500)), scaleOut(tween(500))
        ) {
            var imageState by remember { mutableStateOf(true) }
            
            LaunchedEffect(isHeartVisible) {

                Log.d("FavComposable", "image=$imageState, isheart=$isHeartVisible")
                if (isHeartVisible) {
                    imageState = FavesUtil.tryAddFave(imageUrl.toString())
                    delay(400)
                    setHeartVisible(false)
                }
            }
            
            val imageResource by remember { derivedStateOf { if (imageState) R.drawable.baseline_favorite_24 else R.drawable.baseline_heart_broken_24 } }
            Image(
                ImageVector.vectorResource(imageResource),
                contentDescription = null
            )
        }

//        if (isHeartVisible) faveState = true
//
//        if (faveState) LaunchedEffect(isHeartVisible) {
//            if (isHeartVisible) {
//                val notExist = !FavesUtil.getFaveById(imageUrl.toString())
//                heartRsrc =
//                    if (notExist)
//                        R.drawable.baseline_favorite_24
//                    else
//                        R.drawable.baseline_heart_broken_24
//                delay(400)
//                setHeartVisible(false)
//                imageState = notExist
//            } else imageState = !FavesUtil.tryAddFave(imageUrl.toString())
//            Log.d("FavComposable", "States: faveState=$faveState, isHeartVisible=$isHeartVisible, imageState=$imageState")
//        }
    }
}