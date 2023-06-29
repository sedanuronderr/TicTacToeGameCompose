package com.seda.tictactoegame_compose.ui

sealed class userActions{
    object PlayAgainButtonClicked:userActions()
    data class BoardTapped(val cellNo:Int):userActions()

}
