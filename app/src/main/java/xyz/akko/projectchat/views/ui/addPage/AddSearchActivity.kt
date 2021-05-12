package xyz.akko.projectchat.views.ui.addPage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.ui.res.painterResource
import xyz.akko.projectchat.R
import xyz.akko.projectchat.views.theme.ProjectChatTheme

class AddSearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectChatTheme {
                Scaffold(
                    topBar = { TopAppBar (
                        navigationIcon = { IconButton(onClick = { this.finish() }) {
                            Icon(painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24), contentDescription = null)
                        }},
                        actions = {},
                        title = { Text("搜索好友或群组") }
                    )}
                ) {

                }
            }
        }
    }
}