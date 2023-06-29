package com.seda.tictactoegame_compose

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.runtime.Composable

import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.seda.tictactoegame_compose.ui.GameViewModel
import com.seda.tictactoegame_compose.ui.theme.TicTacToeGameComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeGameComposeTheme {
                // A surface container using the 'background' color from the theme
                val viewModel = viewModel<GameViewModel> ()
                GameScreen(viewModel = viewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TicTacToeGameComposeTheme {
    }
}