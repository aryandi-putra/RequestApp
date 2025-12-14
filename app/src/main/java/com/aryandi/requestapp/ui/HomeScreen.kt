package com.aryandi.requestapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.clip

@Composable
fun HomeScreen() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlue),
        color = LightBlue
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(top = 48.dp)
        ) {
            Text(
                text = "Home",
                fontSize = 36.sp,
                color = TitleBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start).padding(start = 24.dp)
            )
            Spacer(modifier = Modifier.height(48.dp))

            // Logo placeholder
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "VIA", // Replace with Image painterResource if asset available
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 44.sp
                )
            }
            Spacer(modifier = Modifier.height(72.dp))
            Button(
                onClick = { /* TODO: Action */ },
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .shadow(4.dp, RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = TitleBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Create new request", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

private val LightBlue = Color(0xF0F8FFFF) // Very light blue
private val TitleBlue = Color(0xFF396882) // Matches your mockup

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}
