package com.seda.tictactoegame_compose

import android.content.Context
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.seda.tictactoegame_compose.ui.BoardCellValue
import com.seda.tictactoegame_compose.ui.GameState
import com.seda.tictactoegame_compose.ui.GameViewModel
import com.seda.tictactoegame_compose.ui.VictoryType
import com.seda.tictactoegame_compose.ui.theme.Aqua
import com.seda.tictactoegame_compose.ui.theme.BlueCustom
import com.seda.tictactoegame_compose.ui.theme.GrayBackground
import com.seda.tictactoegame_compose.ui.theme.GreenishYellow
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
       Column() {
           Box(modifier = Modifier
               .size(109.dp, 40.dp)
               .clip(RoundedCornerShape(20.dp))
               .background(

                   Aqua
               )
               ) {
               Column(modifier = Modifier.padding(8.dp)) {
                   Text(text = "Player 'O': ${state.playerCircleCount} ", fontSize = 16.sp)

               }

           }

       }
            Column() {
                Box(modifier = Modifier
                    .size(109.dp, 40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Color.LightGray)
                ) {
                    Column(modifier = Modifier.padding(20.dp,8.dp)) {
                        Text(text = "Draw: ${state.drawCount} ", fontSize = 16.sp)

                    }

                }

            }
            Column() {
                Box(modifier = Modifier
                    .size(109.dp, 40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(GreenishYellow)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Player 'X': ${state.playerCrossCount} ", fontSize = 16.sp)

                    }

                }

            }

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
                .size(300.dp)
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
             viewModel.boardItems.forEach{ (cellNo, boardCellValue) ->
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
                    Loader()
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
       // bannersAds(LocalContext.current)

  
    }
}

@Composable
fun Loader() {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation))
    LottieAnimation(composition)
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

@Composable
fun bannersAds(context: Context) {
    // on below line creating a variable for location.
    // on below line creating a column for our maps.



        // on below line adding admob banner ads.
        AndroidView(
            // on below line specifying width for ads.
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                // on below line specifying ad view.
                AdView(context).apply {
                    // on below line specifying ad size
                    setAdSize(AdSize.BANNER)                    // on below line specifying ad unit id
                    // currently added a test ad unit id.
                    adUnitId = "ca-app-pub-9199482281864772/4388051856"
                    // calling load ad to load our ad.
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Greeting() {
    TicTacToeGameComposeTheme {
        GameScreen(viewModel = GameViewModel())
    }
}