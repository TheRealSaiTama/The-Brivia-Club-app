package com.briviaclub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.briviaclub.app.ui.navigation.BriviaNavGraph
import com.briviaclub.app.ui.theme.BriviaClubAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BriviaClubAppTheme {
                BriviaNavGraph()
            }
        }
    }
}
