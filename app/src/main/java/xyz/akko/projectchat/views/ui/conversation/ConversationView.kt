package xyz.akko.projectchat.views.ui.conversation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.glide.rememberGlidePainter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import xyz.akko.projectchat.R
import xyz.akko.projectchat.data.FriendListItem
import xyz.akko.projectchat.data.GroupListItem
import xyz.akko.projectchat.utils.UtilObject
import xyz.akko.projectchat.views.theme.GreenTheme
import xyz.akko.projectchat.views.theme.ProjectChatTheme
import xyz.akko.projectchat.views.theme.defaultChatBackground
import xyz.akko.projectchat.views.ui.chatMain.ConversationNavType.Friends
import xyz.akko.projectchat.views.ui.chatMain.ConversationNavType.Groups
import xyz.akko.projectchat.views.ui.chatMain.INTENT_TAG
import xyz.akko.projectchat.views.ui.friendslist.FriendsList
import xyz.akko.projectchat.views.ui.friendslist.GROUP_GID_TAG
import xyz.akko.projectchat.views.ui.info.GroupInfo
import xyz.akko.projectchat.views.ui.info.UserInfo

const val CURRENT_UID = "CURRENT_UID"

class ConversationView : ComponentActivity() {
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProjectChatTheme {
                //TODO 优化逻辑 clean code
                val viewModel: ConversationViewModel = viewModel()
                //TODO NULL CHECK
                val myUid = UtilObject.myUid
                //TODO NULL CHECK
                viewModel.type = if (intent.getStringExtra(CURRENT_UID) != null) Friends else Groups
                when (viewModel.type) {
                    Friends -> {
                        viewModel.friendConversationInfo = intent.getStringExtra(INTENT_TAG)
                            ?.let { it1 -> Json.decodeFromString<FriendListItem>(string = it1) }!!
                        viewModel.uid = viewModel.friendConversationInfo.senderId
                    }
                    Groups -> {
                        viewModel.groupConversationInfo = intent.getStringExtra(INTENT_TAG)
                            ?.let { it1 -> Json.decodeFromString<GroupListItem>(string = it1) }!!
                        viewModel.gid = viewModel.groupConversationInfo.GroupId
                    }
                }

                Scaffold(
                    topBar = { AppTopbar(viewModel) },
                    bottomBar = {
                        BottomAppBar(
                            modifier = Modifier
                                .background(defaultChatBackground)
                        ) {
                            InputLabel(viewModel)
                        }
                    }
                ) {
                    viewModel.userOrGroupInfoInit()
                    LaunchedEffect(true)
                    {
                        viewModel.dbInit()
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .background(defaultChatBackground)
                    ) {
                        when (viewModel.type) {
                            Friends -> {
                                items(viewModel.messageList) { item ->
                                    MessageRow(item = item, viewModel = viewModel)
                                }
                            }
                            Groups -> {
                                items(viewModel.groupMessageList) { item ->
                                    GroupMessageRow(item = item, viewModel = viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

//TODO 弹出键盘时固定顶栏
@ExperimentalAnimationApi
@Composable
private fun AppTopbar(viewModel: ConversationViewModel) {
    val context = LocalContext.current
    val infoState = remember {
        mutableStateOf(false)
    }
    TopAppBar(
        navigationIcon = {
            when (viewModel.type) {
                Friends -> if (infoState.value) UserInfo(infoState, user = viewModel.user,context = context)
                Groups -> { if (infoState.value) GroupInfo(state = infoState, group = viewModel.group,
                    groupUserList = viewModel.groupUserList
                ) }
            }
            IconButton(onClick = { (context as Activity).finish() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                    contentDescription = "back_button"
                )
            }
        },
        actions = {
            IconButton(onClick = { viewModel.dropDownMenuState.value = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
                    contentDescription = "more_button"
                )
            }
            DropdownMenu(expanded = viewModel.dropDownMenuState.value, onDismissRequest = { viewModel.dropDownMenuState.value = false }) {
                DropdownMenuItem(onClick = {
                    /*TODO*/
                    Toast.makeText(context,"TODO",Toast.LENGTH_SHORT).show()
                }) {
                    Text(text = "清空聊天记录")
                }
                if (viewModel.type == Groups) {
                    DropdownMenuItem(onClick = {
                        val intent = Intent(context,FriendsList::class.java)
                        intent.putExtra(GROUP_GID_TAG,viewModel.gid)
                        context.startActivity(intent)
                    }) {
                        Text(text = "群员列表")
                    }
                }
            }
        },
        title = {
            Image(
                painter = rememberGlidePainter(
                    request = when (viewModel.type) {
                        Friends -> viewModel.friendConversationInfo.IconUrl
                        Groups -> viewModel.groupConversationInfo.IconUrl
                    }
                ),
                contentDescription = "user_icon",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .border(
                        shape = CircleShape,
                        border = BorderStroke(
                            width = 3.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(GreenTheme.Light, GreenTheme.sLight)
                            )
                        )
                    )
                    .clickable { infoState.value = true }
            )
            Spacer(modifier = Modifier.padding(start = 10.dp))
            Text(
                text = when (viewModel.type) {
                    Friends -> viewModel.friendConversationInfo.Name
                    Groups -> viewModel.groupConversationInfo.Name
                }
            )
        }
    )
}