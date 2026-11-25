package com.howellkeatingfonseca.findanddine.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.howellkeatingfonseca.findanddine.R
import com.howellkeatingfonseca.findanddine.ui.theme.FindAndDineTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit, modifier: Modifier = Modifier) {
    // The timer for the splash screen is defined in the LaunchedEffect
    LaunchedEffect(true) {
        delay(2500) // 2.5s delay before timeout
        onTimeout()
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Image to represent the logo
            Image(
                painter = painterResource(R.drawable.findanddine),
                contentDescription = "Find & Dine Logo",
                modifier = modifier
            )

            // Loading bar for show only
            CircularProgressIndicator(
                modifier = Modifier.padding(top = 32.dp)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    FindAndDineTheme {
        SplashScreen({})
    }
}