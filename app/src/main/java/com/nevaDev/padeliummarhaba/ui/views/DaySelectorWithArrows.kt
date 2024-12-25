package com.nevaDev.padeliummarhaba.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaySelectorWithArrows2(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
) {
    val dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.FRENCH)
    val dateFormatter = DateTimeFormatter.ofPattern("d", Locale.FRENCH)
    val monthFormatter = DateTimeFormatter.ofPattern("MMM", Locale.FRENCH)

    val daysInWeek = (0..6).map { offset ->
        selectedDate.minusDays(selectedDate.dayOfWeek.value.toLong() - 1L).plusDays(offset.toLong())
    }
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH)


    val listState = rememberLazyListState()

    LaunchedEffect(selectedDate) {
        val selectedIndex = daysInWeek.indexOf(selectedDate)
        if (selectedIndex != -1) {
            listState.animateScrollToItem(
                index = selectedIndex,
                scrollOffset = -listState.layoutInfo.viewportEndOffset / 2
            )
        }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        // Month-Year Header
        Text(
            text = monthYearFormatter.format(selectedDate).uppercase(Locale.FRENCH),
            color = Color(0xFF0054D8),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        // Navigation Row with Arrows and "AUJOURD'HUI"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Arrow
            IconButton(onClick = { onDateSelected(selectedDate.minusDays(1)) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous day", tint = Color.Gray)
            }

            // "AUJOURD'HUI" Button
            Row(
                modifier = Modifier
                    .clickable { onDateSelected(LocalDate.now()) }
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AUJOURD'HUI",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0054D8),
                    fontSize = 14.sp,
                )
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = "Today",
                    tint = Color(0xFF0054D8),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(20.dp)
                )
            }

            // Right Arrow
            IconButton(onClick = { onDateSelected(selectedDate.plusDays(1)) }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next day", tint = Color.Gray)
            }
        } }
    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp) // No space between boxes, add gray line manually
    ) {
        items(daysInWeek) { day ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Box for each day with reduced size
                Column(
                    modifier = Modifier
                        .clickable { onDateSelected(day) }
                        .background(
                            color = if (day == selectedDate) Color(0xFF0054D8) else Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = if (day == selectedDate) 0.dp else 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 8.dp, horizontal = 8.dp) // Reduced padding
                        .width(60.dp),  // Smaller width
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Abbreviated day name (e.g., "Sam")
                    Text(
                        text = dayFormatter.format(day).uppercase(),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = if (day == selectedDate) Color.White else Color.Gray,
                        fontSize = 12.sp  // Smaller font size for day name
                    )

                    // Day of the month (e.g., "29")
                    Text(
                        text = dateFormatter.format(day),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = if (day == selectedDate) Color.White else Color.Black,
                        fontSize = 18.sp  // Smaller font size for day number
                    )

                    // Abbreviated month name (e.g., "Nov")
                    Text(
                        text = monthFormatter.format(day).uppercase(),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        color = if (day == selectedDate) Color.White else Color.Gray,
                        fontSize = 12.sp  // Smaller font size for month
                    )
                }

                // Gray line separator (except after the last item)
                if (day != daysInWeek.last()) {
                    Box(
                        modifier = Modifier
                            .height(40.dp) // Match the reduced height of the day box
                            .width(1.dp)
                            .background(Color.Gray)
                    )
                }
            }
        }
    }
}