@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.bacaberita

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

@Composable
fun NewsSlider(newsViewModel: NewsViewModel) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 5 })

    LaunchedEffect(Unit) {
        while (true) {
            yield()
            delay(5000L)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % (pagerState.pageCount)
            )
        }
    }

    Column {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
                .height(196.dp)
                .fillMaxWidth(),
            pageSpacing = 8.dp,
        ) { page ->
            val article = newsViewModel.articles.value?.get(page)
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    newsViewModel.onArticleSelected(article)
                }
            ) {
                AsyncImage(
                    model = article?.urlToImage,
                    contentDescription = "Slider",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}