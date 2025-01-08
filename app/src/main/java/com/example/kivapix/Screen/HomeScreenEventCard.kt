package com.example.kivapix.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kivapix.R
import com.example.kivapix.utils.Event
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreenEventCard(navController: NavHostController, event: Event) {
    val robotoFontFamily = FontFamily(
        Font(R.font.robotomono_bold),
    )

    val robotoFontFamilyRegular = FontFamily(
        Font(R.font.robotomono_regular),
    )
    val date: Date = event.date.toDate()
    val formattedDate = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(date)

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, top = 10.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(6.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Event Image",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp)) // Spacer to add space between image and text content

            // Right Section - Event name, date, and button
            Column(
                modifier = Modifier
                    .weight(1f) // Makes the right section take the remaining space
                    .padding(8.dp) // Optional padding between image and text
            ) {
                Text(
                    text = "${event.name}",
                    fontWeight = FontWeight.Bold,
                    fontFamily = robotoFontFamily,
                    style = MaterialTheme.typography.titleMedium,
                )
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 5.dp)) {
                    Text("📅", color = Color.Black)
                    Text(text = formattedDate, fontFamily = robotoFontFamilyRegular, style = MaterialTheme.typography.bodyMedium, color = Color.Black, modifier = Modifier.padding(start = 10.dp, top = 8.dp))
                }
                Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 5.dp)) {
                    //Text("", color = Color.Black)
                    Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = Color(0xFF637468)
                    )
                    Text(
                        text = "${event.location}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        fontFamily = robotoFontFamilyRegular,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        navController.navigate("Details/${event.id}")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Show Details")
                }
            }
        }
    }
}