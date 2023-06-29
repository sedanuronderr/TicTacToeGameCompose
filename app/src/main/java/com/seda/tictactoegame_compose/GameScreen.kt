package com.seda.tictactoegame_compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.seda.tictactoegame_compose.ui.BoardCellValue
import com.seda.tictactoegame_compose.ui.GameState
import com.seda.tictactoegame_compose.ui.GameViewModel
import com.seda.tictactoegame_compose.ui.VictoryType
import com.seda.tictactoegame_compose.ui.theme.BlueCustom
import com.seda.tictactoegame_compose.ui.theme.GrayBackground
import com.seda.tictactoegame_compose.ui.theme.TicTacToeGameComposeTheme
import com.seda.tictactoegame_compose.ui.userActions

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel
){
    val state = viewModel.state
    Column(modifier = Modifier
        .fillMaxSize()
        .background(GrayBackground)
        .padding(30.dp),
    verticalArrangement = Arrangement.SpaceEvenly,
    horizontalAlignment = Alignment.CenterHorizontally
    ) {
           
        Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
           Text(text = "Player 'O':${state.playerCircleCount} ", fontSize = 16.sp)
            Text(text = "Draw: ${state.drawCount} ", fontSize = 16.sp)
            Text(text = "Player 'X':${state.playerCrossCount} ", fontSize = 16.sp)

        }
        Text(
            text = "Tic Tac Toe",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive,
            color = BlueCustom
            )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(GrayBackground),
            contentAlignment = Alignment.Center
        ){
            BoardBase()
            
            LazyVerticalGrid(
                modifier= Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1f),
                columns =GridCells.Fixed(3)
            ){
             viewModel.boardItems.forEach{ cellNo,boardCellValue->
                 item {
                     Column(
                         modifier = Modifier
                             .fillMaxWidth()
                             .aspectRatio(1f)
                             .clickable {
                                 viewModel.onAction(userActions.BoardTapped(cellNo))
                             },
                         horizontalAlignment = Alignment.CenterHorizontally,
                         verticalArrangement = Arrangement.Center
                     ) {
                         AnimatedVisibility(
                             visible = viewModel.boardItems[cellNo]!=BoardCellValue.NONE,
                             enter =   scaleIn(tween(1000))
                             ) {
                             if (boardCellValue == BoardCellValue.CIRCLE) {
                                 Circle()
                             } else if (boardCellValue == BoardCellValue.CROSS) {
                                 Cross()
                             }
                         }

                     }
                 }
             }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(
                    visible = state.hasWon,
                    enter = fadeIn(tween(2000))
                ) {
                    DrawVictoryLine(state = state)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
            ){
             Text(
                 text = state.hintText,
                 fontSize = 24.sp,
                 fontStyle = FontStyle.Italic
                )
             Button(
                 onClick = {
                         viewModel.onAction(action = userActions.PlayAgainButtonClicked)
                 },
                 shape = RoundedCornerShape(5.dp),
                 elevation = ButtonDefaults.buttonElevation(5.dp),
                 colors = ButtonDefaults.buttonColors(containerColor = BlueCustom, contentColor = Color.White)
                 ) {
                  Text(text = "Play Again", fontSize = 16.sp)
             }
            
         }

  
    }
}
@Composable
fun DrawVictoryLine(
    state: GameState
) {
    when (state.victoryType) {
        VictoryType.HORIZONTAL1 -> WinHorizantalLine()
        VictoryType.HORIZONTAL2 -> WinHorizantalLine2()
        VictoryType.HORIZONTAL3 -> WinHorizantalLine3()
        VictoryType.VERTICAL1 -> WinVerticalLine()
        VictoryType.VERTICAL2 -> WinVerticalLine1()
        VictoryType.VERTICAL3 -> WinVerticalLine2()
        VictoryType.DIAGONAL1 -> WinDiagonalLine1()
        VictoryType.DIAGONAL2 -> WinDiagonalLine2()
        VictoryType.NONE -> {}
    }
}
@Preview(showBackground = true)
@Composable
fun Greeting() {
    TicTacToeGameComposeTheme {
        GameScreen(viewModel = GameViewModel())
    }
}