package com.howellkeatingfonseca.findanddine.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.howellkeatingfonseca.findanddine.R
import com.howellkeatingfonseca.findanddine.ui.theme.FindAndDineTheme

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            // Image to represent the logo
            Image(
                painter = painterResource(R.drawable.findanddine),
                contentDescription = "Find & Dine Logo",
                modifier = modifier
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    FindAndDineTheme {
        SplashScreen()
    }
}