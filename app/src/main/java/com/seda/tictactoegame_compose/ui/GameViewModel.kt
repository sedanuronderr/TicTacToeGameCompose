package com.seda.tictactoegame_compose.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.booleanResource
import androidx.lifecycle.ViewModel

class GameViewModel :ViewModel() {

    var state by mutableStateOf(GameState())


    val boardItems: MutableMap<Int, BoardCellValue> = mutableMapOf(
        1 to BoardCellValue.NONE,
        2 to BoardCellValue.NONE,
        3 to BoardCellValue.NONE,
        4 to BoardCellValue.NONE,
        5 to BoardCellValue.NONE,
        6 to BoardCellValue.NONE,
        7 to BoardCellValue.NONE,
        8 to BoardCellValue.NONE,
        9 to BoardCellValue.NONE,
    )

    fun onAction(action: userActions) {
        when (action) {
            is userActions.BoardTapped -> {
                addValueToBoard(action.cellNo)
            }
            userActions.PlayAgainButtonClicked -> {
               gameReset()
            }
        }
    }

    private fun gameReset() {
        boardItems.forEach { i, _ ->
            boardItems[i]=  BoardCellValue.NONE
        }
        state = state.copy(
            hintText = "Player 'O' turn",
            currentTurn = BoardCellValue.CIRCLE,
            victoryType = VictoryType.NONE,
            hasWon = false
        )
    }

    private fun addValueToBoard(cellNo: Int) {
        if (boardItems[cellNo] != BoardCellValue.NONE) {
            return
        }

        if (state.currentTurn == BoardCellValue.CIRCLE){
            boardItems[cellNo] = BoardCellValue.CIRCLE
            if(hasBoardFull()){
                state = state.copy(
                    hintText = "Game Draw",
                    drawCount = state.drawCount+1
                )
            }else{
                state = state.copy(
                    hintText = "Player 'X' turn",
                    currentTurn = BoardCellValue.CROSS
                )
            }


        }else if (state.currentTurn == BoardCellValue.CROSS){
            boardItems[cellNo] = BoardCellValue.CROSS
            if(hasBoardFull()){
                state = state.copy(
                    hintText = "Game Draw",
                    drawCount = state.drawCount+1
                )
            }else{
                state = state.copy(
                    hintText = "Player 'O' turn",
                    currentTurn = BoardCellValue.CIRCLE
                )
            }
            state = state.copy(
                hintText = "Player 'O' turn",
                currentTurn = BoardCellValue.CIRCLE
            )
        }
    }

    private fun hasBoardFull(): Boolean {
       if(boardItems.containsValue(BoardCellValue.NONE)) return false
        return true
    }

}