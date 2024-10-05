@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.bacaberita

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kwabenaberko.newsapilib.models.Article
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomePage(navController: NavController, newsViewModel: NewsViewModel) {
    val articles by newsViewModel.articles.observeAsState(emptyList())
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val selectedArticle by newsViewModel.selectedArticle.observeAsState(null)
    val shareText = "Check out this news at ${selectedArticle?.url}"

    fun setSelectedEmpty() {
        newsViewModel.onArticleSelected(null)
    }

    fun onShareClicked() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        val chooser = Intent.createChooser(intent, "Share this news with")
        context.startActivity(chooser)
    }



    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "BACA",
                        color = Color(0xFF328603),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 25.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                    Spacer(modifier = Modifier.width(1.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.news_logo),
                        tint = Color(0xFF328603),
                        contentDescription = "News Logo"
                    )
                }
            },
            actions = {
                IconButton(onClick = { navController.navigate(Route.ABOUT_PAGE) }) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Localized description"
                    )
                }
            },
        )
    }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            item {
                NewsSlider(newsViewModel = newsViewModel)
                Spacer(modifier = Modifier.height(8.dp))

            }

            stickyHeader {
                CategoriesBar(
                    newsViewModel = newsViewModel,
                )
            }
            items(articles) { article ->
                if (!article.title.equals("[Removed]")) {
                    ArticleItem(article = article, onArticleSelected = {
                        newsViewModel.onArticleSelected(article)
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                }

            }

        }
        BottomSheet(sheetState = sheetState, article = selectedArticle, setSelectedEmpty = {
            setSelectedEmpty()
        }, onReadMoreClicked = {
            navController.navigate(Route.DETAIL_NEWS_PAGE)
        }, onShareClicked = { onShareClicked() })
    }
}


@Composable
fun ArticleItem(article: Article, onArticleSelected: () -> Unit) {
    Card(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = {
            onArticleSelected()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = article.urlToImage
                    ?: Constant.defaultBanner,
                contentDescription = "Article Image",
                modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,

                )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp)
            ) {

                // Parsing the input date string into a ZonedDateTime
                val zonedDateTime = ZonedDateTime.parse(article.publishedAt)

                // Define the output format
                val outputFormatter =
                    DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale.ENGLISH)

                // Format the ZonedDateTime to the desired output string
                val formattedDate = zonedDateTime.format(outputFormatter)

                Text(
                    text = article.source.name,
                    maxLines = 1,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(text = article.title, fontWeight = FontWeight.Bold, maxLines = 2)
                Text(text = formattedDate, maxLines = 1, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun CategoriesBar(newsViewModel: NewsViewModel) {
    val categoriesList = listOf(
        "GENERAL",
        "BUSINESS",
        "ENTERTAINMENT",
        "HEALTH",
        "SCIENCE",
        "SPORTS",
        "TECHNOLOGY"
    )

    val currentCategory by newsViewModel.currentCategory.observeAsState("GENERAL")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categoriesList.forEach { category ->
            Button(
                onClick = { newsViewModel.fetchNewsTopHeadlines(category) },
                modifier = Modifier.padding(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentCategory == category) Color(
                        0xFF328603
                    ) else Color(0xFFF3F2F2),

                    ),
            ) {
                Text(
                    text = category.lowercase().replaceFirstChar { it.uppercase() },
                    fontSize = 14.sp,
                    color = if (currentCategory == category) Color.White else Color.DarkGray
                )


            }
        }
    }
}