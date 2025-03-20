package com.example.dicegame

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GUI()
        }
    }
}

@Composable
fun GUI(){
    val openDialog = remember {mutableStateOf(false)}

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        val context = LocalContext.current
       Button(onClick = {
           println("New Game")
           val i = Intent(context, NewGame::class.java)
           context.startActivity(i)
       }) {
           Text("New Game")
       }

       Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
          openDialog.value = true
           println("About")
        }) {
            Text("About")
        }
        aboutDialog(openDialog)
    }
}

@Composable
fun aboutDialog(openDialog: MutableState<Boolean>) {
    if(openDialog.value){
       AlertDialog(
           title = {Text("About")},
           text = {Text("I confirm that I understand what plagiarism is and have read and\n" +
                   "understood the section on Assessment Offences in the Essential\n" +
                   "Information for Students. The work that I have submitted is\n" +
                   "entirely my own. Any work from other authors is duly referenced\n" +
                   "and acknowledged.")},
           onDismissRequest = {
               openDialog.value = false
           },
           confirmButton = {
               Button(onClick = {
                   openDialog.value = false
               }) {
                   Text("Close")
               }
           }
       )
    }
}

