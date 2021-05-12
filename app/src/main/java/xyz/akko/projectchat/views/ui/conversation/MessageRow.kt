package xyz.akko.projectchat.views.ui.conversation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import xyz.akko.projectchat.data.MessageTypeEnum.*
import xyz.akko.projectchat.data.dao.MessageEntity
import xyz.akko.projectchat.utils.UtilObject
import xyz.akko.projectchat.views.theme.GreenTheme
import xyz.akko.projectchat.views.ui.LongClickButton
import xyz.akko.projectchat.views.ui.info.UserInfo


@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun MessageRow(item: MessageEntity, viewModel: ConversationViewModel) {
    val context = LocalContext.current
    val userInfoState = remember {
        mutableStateOf(false)
    }
    if (userInfoState.value) {
        LaunchedEffect(true)
        {
            viewModel.uid = item.senderUid
            viewModel.userOrGroupInfoInit()
        }
        UserInfo(state = userInfoState, user = viewModel.user, context = context)
    }
    when (item.senderUid) {
        //TODO FIX NULL
        UtilObject.myUid -> {
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (item.type) {
                    StringMessage -> {
                        CustomLongClickButton(item = item, content = {
                            Text(
                                text = item.content,
                                fontSize = 16.sp
                            )
                        })
                    }
                    ReplyMessage -> {
                        /*TODO*/
                    }
                    ImageMessage -> {
                        Image(
                            painter = rememberGlidePainter(request = item.content),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.small)
                                .border(
                                    shape = MaterialTheme.shapes.small,
                                    border = BorderStroke(
                                        width = 3.dp,
                                        brush = Brush.linearGradient(
                                            colors = listOf(GreenTheme.Primary, GreenTheme.Primary)
                                        )
                                    )
                                )
                        )
                    }
                    VoiceMessage -> {
                        /*TODO*/
                    }
                }
                Image(
                    //TODO
                    painter = rememberGlidePainter(request = UtilObject.myUser.IconUrl),
                    contentDescription = "user_icon",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(8.dp)
                        .clickable { /*TODO*/ }
                        .clip(CircleShape)
                        .border(
                            shape = CircleShape,
                            border = BorderStroke(
                                width = 3.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(GreenTheme.Light, GreenTheme.sLight)
                                )
                            )
                        )
                )
            }
        }
        else -> {
            Row(
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberGlidePainter(request = viewModel.friendConversationInfo.IconUrl),
                    contentDescription = "user_icon",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(8.dp)
                        .clickable { userInfoState.value = true }
                        .clip(CircleShape)
                        .border(
                            shape = CircleShape,
                            border = BorderStroke(
                                width = 3.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(GreenTheme.Light, GreenTheme.sLight)
                                )
                            )
                        )
                )
                Column {
                    Text(text = viewModel.friendConversationInfo.Name )
                    when (item.type) {
                        StringMessage -> {
                            CustomLongClickButton(item = item, content = {
                                Text(
                                    text = item.content,
                                    fontSize = 16.sp
                                )
                            })
                        }
                        ReplyMessage -> {
                            /*TODO*/
                        }
                        ImageMessage -> {
                            Image(
                                painter = rememberGlidePainter(request = item.content),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.small)
                                    .border(
                                        shape = MaterialTheme.shapes.small,
                                        border = BorderStroke(
                                            width = 3.dp,
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    GreenTheme.Primary,
                                                    GreenTheme.Primary
                                                )
                                            )
                                        )
                                    )
                            )
                        }
                        VoiceMessage -> {
                            /*TODO*/
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun CustomLongClickButton(
    item: MessageEntity,
    content: @Composable RowScope.() -> Unit
) {
    val context = LocalContext.current
    LongClickButton(
        onLongClick = {
            Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
            (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)?.setPrimaryClip(
                ClipData.newPlainText("label", item.content)
            )
        },
        onClick = { /*DO NOTHING*/ },
        modifier = Modifier
            .padding(end = 8.dp)
            .clip(CircleShape),
        content = content
        //TODO CHANGE COLOR
        //colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF7AA7FF)),
    )
}