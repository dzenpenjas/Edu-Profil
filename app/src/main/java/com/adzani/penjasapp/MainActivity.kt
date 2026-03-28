package com.adzani.penjasapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.adzani.penjasapp.data.PenjasViewModel
import com.adzani.penjasapp.ui.PenjasApp
import com.adzani.penjasapp.ui.theme.PenjasAppTheme

class MainActivity : ComponentActivity() {
    private val viewModel: PenjasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PenjasAppTheme {
                PenjasApp(viewModel = viewModel)
            }
        }
    }
}
