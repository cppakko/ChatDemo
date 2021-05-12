package xyz.akko.projectchat.views.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import xyz.akko.projectchat.views.theme.GreenTheme
import xyz.akko.projectchat.views.theme.ProjectChatTheme
import xyz.akko.projectchat.views.ui.BaseAnimatedVisibility
import xyz.akko.projectchat.views.ui.chatMain.ChatMain

class LoginActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = LoginViewModel()
        setContent {
            ProjectChatTheme {
                Scaffold {
                    Login(viewModel)
                }
            }
        }
        viewModel.loginFinished.observe(this,
            {
                if (it == true)
                {
                    val intent = Intent(this,ChatMain::class.java)
                    intent.putExtra("uid",1234567890L)
                    startActivity(intent)
                    this.finish()
                }
            }
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun Login(viewModel: LoginViewModel) {
    var userName by remember { mutableStateOf(TextFieldValue("")) }
    var passWord by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPassWord by remember { mutableStateOf(TextFieldValue("")) }
    var isRegistering by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(Color(0xff80d6ff))
            .fillMaxHeight()
            .fillMaxWidth()
    ) {

        TextField(
            value = userName,
            onValueChange = { value ->
                userName = value
            },
            modifier = Modifier.width(250.dp),
            label = {
                Text(text = "UserName")
            },
            placeholder = {},
        )
        Spacer(modifier = Modifier.padding(10.dp))
        TextField(
            value = passWord,
            onValueChange = { value ->
                passWord = value
            },
            modifier = Modifier.width(250.dp),
            label = {
                Text(text = "Password")
            },
            placeholder = {}
        )
        Spacer(modifier = Modifier.padding(10.dp))

        BaseAnimatedVisibility(state = isRegistering) {
            TextField(
                value = confirmPassWord,
                onValueChange = { value ->
                    confirmPassWord = value
                },
                modifier = Modifier.width(250.dp),
                label = {
                    Text(text = "ConfirmPassword")
                },
                placeholder = {}
            )
        }
        if (isRegistering) Spacer(modifier = Modifier.padding(10.dp))
        BaseAnimatedVisibility(state = !viewModel.isLoading.collectAsState().value) {
            Column {
                Button(
                    onClick = {
                        isRegistering = true

                    },
                    modifier = Modifier.width(250.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = GreenTheme.sLight
                    )
                ) {
                    Text(text = "Register")
                }
                Spacer(modifier = Modifier.padding(5.dp))
                BaseAnimatedVisibility(state = !isRegistering) {
                    Button(
                        onClick = { viewModel.login() },
                        modifier = Modifier.width(250.dp)
                    ) {
                        Text(text = "Login")
                    }
                }
                BaseAnimatedVisibility(state = isRegistering) {
                    Button(
                        onClick = { isRegistering = false },
                        modifier = Modifier.width(250.dp)
                    ) {
                        Text(text = "Back")
                    }
                }
            }
        }

        if (viewModel.isLoading.collectAsState().value)
        {
            AFloatingActionButton()
        }
    }
}

//TODO RENAME
@Composable
fun AFloatingActionButton() {
    FloatingActionButton(
        content = {CircularProgressIndicator()},
        onClick = {}
    )
}