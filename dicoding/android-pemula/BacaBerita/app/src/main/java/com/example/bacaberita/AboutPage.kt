@file:OptIn(ExperimentalFoundationApi::class)

package com.example.bacaberita

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutPage(navController: NavController) {
    val context = LocalContext.current
    fun onContactClicked() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constant.linkedin))
        context.startActivity(intent)
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "ABOUT", maxLines = 1, color = Color(0xFF328603),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 25.sp,
                    fontFamily = FontFamily.SansSerif
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            },
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ProfileCard(onContactClicked = { onContactClicked() })
        }

    }
}


@Composable
fun ProfileCard(onContactClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        // Profile Image
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFF69B4), Color(0xFF0000FF))
                    ),
                    shape = CircleShape
                )
        ) {


            AsyncImage(
                model = Constant.avatarUrl,
                contentDescription = Constant.name,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Name
        Text(
            text = Constant.name,
            color = Color(0xFF328603),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        // Member status
        Text(
            text = Constant.email,
            color = Color.Gray,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Follower stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row {
                Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "Location")
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = Constant.location,
                    color = Color.DarkGray,
                    fontSize = 16.sp
                )
            }
            Row {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = "DOB")
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = Constant.dob,
                    color = Color.DarkGray,
                    fontSize = 16.sp
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF328603),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            onClick = { onContactClicked() }) {
            Icon(imageVector = Icons.Filled.Call, contentDescription = "Contact Me")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Contact Me")
        }
    }
}