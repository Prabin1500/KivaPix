package com.example.kivapix.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kivapix.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SearchBar() {
    val robotoFontFamily = FontFamily(
        Font(R.font.robotomono_bold),
    )
    val robotoFontFamilyRegular = FontFamily(
        Font(R.font.robotomono_regular),
    )
    var searchText by remember { mutableStateOf("") }
    val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("'Today,' d MMMM"))
    Column {
        Row(modifier = Modifier.padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Column {
                Text(
                    text = "$currentDate",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = robotoFontFamilyRegular,
                    color = Color(0xFF047E2D),
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                )
                Text(
                    text = "Good Morning Guest",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = robotoFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp, top = 10.dp, bottom = 10.dp)
                )
            }
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Event Image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(45.dp)
                .background(Color.White, RoundedCornerShape(50.dp))
                .border(width = 1.dp, Color.LightGray, RoundedCornerShape(50.dp)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray,
                modifier = Modifier.padding(12.dp)
            )
            BasicTextField(
                value = searchText,
                onValueChange = { searchText = it },
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 12.dp)
                    .fillMaxHeight(),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (searchText.isEmpty()) {
                            Text(
                                text = "Search events",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(start = 4.dp)
                            )
                        }
                        Box(modifier = Modifier.align(Alignment.CenterStart)) {
                            innerTextField()
                        }
                    }
                }
            )
        }
    }
}