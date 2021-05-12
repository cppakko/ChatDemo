package xyz.akko.projectchat.views.ui.setting

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import xyz.akko.projectchat.views.theme.GreenTheme
import xyz.akko.projectchat.views.theme.ProjectChatTheme

class Setting : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectChatTheme {
                // A surface container using the 'background' color from the theme
                val color = remember {
                    mutableStateOf(Color(0xF129AA27))
                }
                //TODO CLEAN CODE
                Scaffold(
                    topBar = { TopAppBar {

                    }}
                ) {
                    Column(
                        modifier = Modifier.background(color = color.value).fillMaxHeight()
                    ) {
                        //val color by GreenTheme.Primary.observeAsState(Color(0xff66bb6a))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                //.background(color)
                        ) {
                            Button(onClick = {
                                color.value = Color(0xFFFF5722)
                                Toast.makeText(this@Setting,GreenTheme.Primary.value.toString(),Toast.LENGTH_LONG).show()
                            }) {
                                Text(text = "Change Color")
                            }
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                            //.background(color)
                        ) {
                            Button(onClick = {
                                color.value = Color(0xFFAD20E0)
                                Toast.makeText(this@Setting,GreenTheme.Primary.value.toString(),Toast.LENGTH_LONG).show()
                            }) {
                                Text(text = "Change Color2")
                            }
                        }
                    }
                }
            }
        }
    }
}