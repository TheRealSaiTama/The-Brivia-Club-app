package com.briviaclub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.briviaclub.app.ui.navigation.BriviaNavGraph
import com.briviaclub.app.ui.theme.BriviaClubAppTheme
import com.briviaclub.app.ui.viewmodel.BriviaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: BriviaViewModel = viewModel()
            val isDark by viewModel.isDarkTheme.collectAsState()

            BriviaClubAppTheme(darkTheme = isDark) {
                BriviaNavGraph(viewModel = viewModel)
            }
        }
    }
}
