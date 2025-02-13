package com.nevaDev.padeliummarhaba.ui.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nevadev.padeliummarhaba.R
import android.util.Base64
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.text.TextStyle
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.SpanStyle
import androidx.hilt.navigation.compose.hiltViewModel
import com.nevaDev.padeliummarhaba.viewmodels.LogoutViewModel
import com.nevaDev.padeliummarhaba.viewmodels.UserViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.logoutRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest


@Composable
fun CopyrightText() {
    val context = LocalContext.current

    // Create an annotated string with clickable text
    val annotatedString = buildAnnotatedString {
        append("Copyright © 2025 ")
        // Add an annotation for "SPOFUN" to make it clickable
        pushStringAnnotation(tag = "SPOFUN", annotation = "https://spofun.tn/")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
            append("SPOFUN")
        }
        pop()
        append(" | Tous droits réservés.")
    }

    // Use ClickableText to make "SPOFUN" clickable
    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            // Get the clicked annotation
            val clickedTag = annotatedString.getStringAnnotations(offset, offset).firstOrNull()
            // If the clicked part is "SPOFUN", open the URL
            clickedTag?.let {
                if (it.tag == "SPOFUN") {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.item))
                    context.startActivity(intent)
                }
            }
        },
        style = TextStyle(color = Color.White, fontSize = 10.sp) // Use TextStyle here
    )
}


