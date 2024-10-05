@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.bacaberita

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kwabenaberko.newsapilib.models.Article
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun BottomSheet(
    sheetState: SheetState,
    article: Article?,
    setSelectedEmpty: () -> Unit,
    onReadMoreClicked: () -> Unit,
    onShareClicked: () -> Unit
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    if (article != null) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(if(isLandscape) 0.9F else 0.65f),
            sheetState = sheetState,
            onDismissRequest = {
                setSelectedEmpty()
            },

            ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = article.urlToImage ?: Constant.defaultBanner,
                    contentDescription = "Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(170.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(16.dp))

                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = article.title ?: "No title",
                    fontSize = 20.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = article.author ?: "No author",
                        fontSize = 14.sp,

                        )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = article.publishedAt?.let {
                            ZonedDateTime.parse(it).format(
                                DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
                            )
                        } ?: "No date",
                        fontSize = 14.sp,

                        )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = article.description ?: "No description",
                    maxLines = 4,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF328603),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth(0.8F),
                        onClick = { onReadMoreClicked() }) {
                        Text(text = "Read More")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { onShareClicked() }) {
                        Icon(imageVector = Icons.Filled.Share, contentDescription = "Share" )
                    }
                }
            }
        }
    }
}