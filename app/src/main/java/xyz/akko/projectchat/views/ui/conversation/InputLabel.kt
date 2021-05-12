package xyz.akko.projectchat.views.ui.conversation

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import xyz.akko.projectchat.R

@ExperimentalAnimationApi
@Composable
fun InputLabel(viewModel: ConversationViewModel)
{
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = {
            /*TODO*/
            Toast.makeText(context,"TODO", Toast.LENGTH_SHORT).show()
        }) {
            Icon(
                painter = painterResource(
                    id = R.drawable.ic_baseline_keyboard_voice_24),
                contentDescription = "voice_button",
                Modifier.size(32.dp).weight(1f)
            )
        }
        TextField(
            value = viewModel.inputText.value,
            onValueChange = { string ->
                viewModel.inputText.value = string
            },
            placeholder = { Text(text = "输入消息")},
            //colors = TextFieldDefaults.textFieldColors(backgroundColor = defaultChatBackground),
            modifier = Modifier.weight(10f)
        )
        AnimatedVisibility(visible = (viewModel.inputText.value == "")) {
            IconButton(onClick = {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                ActivityCompat.startActivityForResult(context as Activity, intent, 200, null)
                Toast.makeText(context,"TODO", Toast.LENGTH_SHORT).show()
            }) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_baseline_image_24
                    ),
                    contentDescription = "image_button",
                    Modifier.size(32.dp).weight(1f)
                )
            }
        }
        AnimatedVisibility(visible = viewModel.inputText.value != "") {
            IconButton(onClick = {
                /*TODO*/
                Toast.makeText(context,"TODO", Toast.LENGTH_SHORT).show()
            }) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_baseline_send_24),
                    contentDescription = "sendMsg_button",
                    Modifier.size(32.dp).weight(1f)
                )
            }
        }
    }
}