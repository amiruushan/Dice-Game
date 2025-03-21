package com.example.dicegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

val diceImages = listOf(
    R.drawable.one,
    R.drawable.two,
    R.drawable.three,
    R.drawable.four,
    R.drawable.five,
    R.drawable.six
)

val diceValues = listOf(1, 2, 3, 4, 5, 6)

class NewGame : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Game()
        }
    }
}

@Composable
fun Game() {
    val playerDiceState = remember { mutableStateOf(List(5) { diceImages.random() }) }
    val computerDiceState = remember { mutableStateOf(List(5) { diceImages.random() }) }
    val currentPlayerValueState = remember { mutableStateOf(MutableList(5) { diceValues[0] }) }
    val currentComputerValueState = remember { mutableStateOf(MutableList(5) { diceValues[0] }) }

    var playerScore by remember { mutableStateOf(0) }
    var computerScore by remember { mutableStateOf(0) }
    var canScore by remember { mutableStateOf(false) }
    var throwType by remember { mutableStateOf("Throw") }
    var reRollCount by remember { mutableStateOf(0) }

    // Track which dice are selected
    val selectedDice = remember { mutableStateListOf(false, false, false, false, false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.TopEnd).padding(30.dp)
        ) {
            Text("Computer Score: $computerScore")
            Text("Player Score: $playerScore")
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(50.dp))

            Text("Computer")
            Spacer(Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                computerDiceState.value.forEach { computerDieceResource ->
                    Image(
                        painter = painterResource(id = computerDieceResource),
                        contentDescription = "Dice",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(8.dp)
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            Text("Player")
            Spacer(Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                playerDiceState.value.forEachIndexed { index, diceResource ->
                    Image(
                        painter = painterResource(id = diceResource),
                        contentDescription = "Dice",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(8.dp)
                            .border(2.dp, if (selectedDice[index]) Color.Green else Color.Transparent)
                            .clickable {
                                if (throwType == "ReRoll")
                                selectedDice[index] = !selectedDice[index] // Toggle selection
                            }
                    )
                }
            }

            Row {
                // Throw / ReRoll button
                Button(onClick = {
                    if (throwType == "Throw") {
                        playerDiceState.value = List(5) { diceImages.random() }
                        computerDiceState.value = List(5) { diceImages.random() }

                        valuesOfCurrentDices(computerDiceState.value, currentComputerValueState.value)
                        valuesOfCurrentDices(playerDiceState.value, currentPlayerValueState.value)

                        canScore = true
                        throwType = "ReRoll"
                    } else if (throwType == "ReRoll") {
                        playerDiceState.value = playerDiceState.value.mapIndexed { index, dice ->
                            if (selectedDice[index]) dice else diceImages.random()
                        }
                        valuesOfCurrentDices(playerDiceState.value, currentPlayerValueState.value)

                        canScore = true
                        reRollCount++
                        if (reRollCount == 2) {
                            throwType = "Throw"
                            reRollCount = 0
                            computerScore = addComputerValues(currentComputerValueState.value, computerScore)
                            playerScore = addPlayerValues(currentPlayerValueState.value, playerScore)

                            // Reset selected dice after scoring
                            selectedDice.fill(false)
                        }
                    }
                }) {
                    Text(if (throwType == "Throw") "Throw" else "ReRoll")
                }

                Spacer(Modifier.width(20.dp))

                // Score button
                Button(onClick = {
                    if (canScore) {
                        computerScore = addComputerValues(currentComputerValueState.value, computerScore)
                        playerScore = addPlayerValues(currentPlayerValueState.value, playerScore)

                        canScore = false
                        throwType = "Throw"

                        // Reset selected dice after scoring
                        selectedDice.fill(false)
                    }
                }) {
                    Text("Score")
                }
            }
        }
    }
}

fun valuesOfCurrentDices(diceState: List<Int>, currentValueState: MutableList<Int>) {
    for (i in diceState.indices) {
        val index = diceImages.indexOf(diceState[i])
        if (index != -1) {
            currentValueState[i] = diceValues[index]
        }
    }
}

fun addPlayerValues(currentValueState: List<Int>, playerScore: Int): Int {
    return playerScore + currentValueState.sum()
}

fun addComputerValues(currentValueState: List<Int>, computerScore: Int): Int {
    return computerScore + currentValueState.sum()
}
