package br.com.apkdoandroid.autoscrollinglist

import androidx.compose.foundation.Image
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val DELAY_BETWEN_SCROLL_MS = 8L
private const val SCROLL_DX = 1F


@Composable
fun AutoScrollingList(list : List<AutoScrollingModel>) {
    var itemListState  by remember { mutableStateOf(list) }
    val lazyListState = rememberLazyListState()
    
    
    LazyRow(
        modifier = Modifier,
        state =  lazyListState ){
        items(itemListState.size){
            AutoScrollingName(autoScrollingModel = itemListState[it])
            Spacer(modifier = Modifier.width(1.dp))
            if(itemListState[it] == itemListState.last()){
                val currentList = itemListState
                val secondPart = currentList.subList(0,lazyListState.firstVisibleItemIndex)
                val firstPart = currentList.subList(lazyListState.firstVisibleItemIndex,currentList.size)

                rememberCoroutineScope().launch {
                    lazyListState.scrollToItem(0,lazyListState.firstVisibleItemScrollOffset - SCROLL_DX.toInt())
                }
                itemListState = firstPart + secondPart
            }
        }
    }
    LaunchedEffect(Unit ){
        autoScroll(lazyListState)
    }

}

private tailrec suspend fun autoScroll(lazyListState: LazyListState){
    lazyListState.scroll(MutatePriority.PreventUserInput){
        scrollBy(SCROLL_DX)
    }

    delay(DELAY_BETWEN_SCROLL_MS)

    autoScroll(lazyListState)
}

@Composable
fun AutoScrollingName(autoScrollingModel: AutoScrollingModel) {

    Card(
        modifier = Modifier.size(180.dp)
            .aspectRatio(1f)
            .background(Color.White)
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = autoScrollingModel.iconResource),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(135.dp)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = autoScrollingModel.contentDescription,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
    
}