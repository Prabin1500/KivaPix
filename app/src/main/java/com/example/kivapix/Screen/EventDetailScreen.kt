package com.example.kivapix.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kivapix.R
import androidx.navigation.NavHostController
import com.example.kivapix.utils.Event
import com.example.kivapix.viewmodel.EventRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EventDetailScreen(navController: NavHostController, documentId : String?) {
    val eventRepository = EventRepository()

    var event by remember { mutableStateOf<Event?>(null) }

    LaunchedEffect(Unit) {
        documentId?.let {
            eventRepository.getEventByDocumentId(documentId) { e ->
                event = e  // Update the event state with the fetched event
            }
        }
    }

    Scaffold(
        bottomBar = { BottomAppBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF8F9CFA),
                            Color(0xFFDBBDE7)
                        )
                    )
                )
                .padding(innerPadding)
        ) {
            CardSection(event)
            LazyColumn(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxSize()
                    .weight(1f)
            ) {
                item { DescriptionSection(event) }
                item { LocationSection() }
                item { BottomSection(navController, documentId) }
            }
        }
    }
}

@Composable
fun CardSection(event : Event?){

    var formattedDate by remember { mutableStateOf<String?>(null) }

    if (event != null) {
        val date: Date? = event?.date?.toDate()  // Assuming `event?.date` is a timestamp
        formattedDate = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(date)
    }

    Box(modifier = Modifier.height(275.dp)){
        Image(
            painter = painterResource(id = R.drawable.event1),
            contentDescription = "Event Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.BottomCenter)
                .height(150.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(){
                    Text(
                        text = "${event?.name}",
                        color = Color.Blue,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favourite"
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = Color.Black)
                    Text(text = "${event?.location}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 16.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
                    Icon(Icons.Default.DateRange, contentDescription = "Date", tint = Color.Black)
                    Text(text = "$formattedDate", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 16.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Group, contentDescription = "Participants", tint = Color.Black)
                    Text(text = "${event?.participants} Participants", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}

@Composable
fun DescriptionSection(event: Event?) {
    var expanded by remember { mutableStateOf(false) }
    val descriptionText = "${event?.description}"
    val displayText = if (expanded) descriptionText else descriptionText.take(45) // Adjust the number for truncation

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Description",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = buildAnnotatedString {
                append(displayText)
                pushStyle(SpanStyle(color = Color.Black, fontSize = 12.sp))
                append(
                    if (expanded) " Show Less" else "...Show More")
                pop()
            },
            style = MaterialTheme.typography.bodyMedium,
            maxLines = if (expanded) Int.MAX_VALUE else 1, // Limit to one line when collapsed
            overflow = TextOverflow.Ellipsis, // Show ellipsis when collapsed
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(top = 10.dp)
        )
    }
}

@Composable
fun LocationSection(){
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Location",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
        )
        Image(
            painter = painterResource(id = R.drawable.map),
            contentDescription = "Map",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}

@Composable
fun BottomSection(navController: NavHostController, documentId: String?){
    val robotoFontFamily = FontFamily(
        Font(R.font.robotomono_bold),
    )
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { /* Accept Event */ },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(text = "+ Accept this event",
                fontFamily = robotoFontFamily,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        OutlinedButton(
            onClick = {
                navController.navigate("Gallery/${documentId}")
            },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(
                text = "View Gallery",
                fontFamily = robotoFontFamily,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
