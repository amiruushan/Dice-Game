package com.example.dicegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dicegame.ui.theme.DiceGameTheme
import java.nio.file.Files.list


val diceImages = listOf(
    R.drawable.one,
    R.drawable.two,
    R.drawable.three,
    R.drawable.four,
    R.drawable.five,
    R.drawable.six
)

val diceValues = listOf(1,2,3,4,5,6)

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
fun Game(){
    // setting state for five dices
    val playerDiceState = remember{
        mutableStateOf(List(5){diceImages[0]})
    }
    // setting state for five dices computer
    val computerDiceState = remember{
        mutableStateOf(List(5){diceImages[0]})
    }

    val currentValueState = remember{
        mutableStateOf(MutableList(5){diceValues[0]})
    }
    var playerScore by remember { mutableStateOf(0) }
    var computerScore by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        val context = LocalContext.current
        Column (
            modifier = Modifier.align(Alignment.TopEnd).padding(30.dp)
        ){
            Text("Player Score: $playerScore")
            Text("Computer Score: $computerScore")
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(Modifier.height(50.dp))

            // row to display dice images of human player
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
                playerDiceState.value.forEach { dieceResource ->
                    Image(
                        painter = painterResource(id = dieceResource),
                        contentDescription = "Dice",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(8.dp)
                    )

                }
            }

            Row {
                // Throw button
                Button(onClick = {
                    playerDiceState.value = List(5) { diceImages.random() }
                    computerDiceState.value = List(5) { diceImages.random() }
                    valuesOfCurrentDices(playerDiceState.value, currentValueState.value)
                    println(currentValueState)
                }) {
                    Text("Throw")
                }

                Spacer(Modifier.width(20.dp))
                // Score button
                Button(onClick = {
                    println("Score")
                    playerScore = addPlayerValues(currentValueState.value, playerScore)
                    println(playerScore)
                }) {
                    Text("Score")
                }
            }

        }
    }
}


fun valuesOfCurrentDices(diceState: List<Int>, currentValueState: MutableList<Int>) {
    for (i in diceState.indices) {
        val index = diceImages.indexOf(diceState[i]) // Find index in diceImages
        if (index != -1) {
            currentValueState[i] = diceValues[index] // Map to corresponding diceValues
        }
    }
}

fun addPlayerValues(currentValueState: List<Int>, playerScore: Int): Int {
    return playerScore + currentValueState.sum()
}
