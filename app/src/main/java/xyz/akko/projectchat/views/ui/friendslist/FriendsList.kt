package xyz.akko.projectchat.views.ui.friendslist

import android.os.Bundle
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.glide.rememberGlidePainter
import xyz.akko.projectchat.R
import xyz.akko.projectchat.data.dao.User
import xyz.akko.projectchat.views.theme.GreenTheme
import xyz.akko.projectchat.views.theme.ProjectChatTheme
import xyz.akko.projectchat.views.ui.info.UserInfo

const val GROUP_GID_TAG = "GROUP_GID_TAG"

class FriendsList : ComponentActivity() {
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectChatTheme {
                // A surface container using the 'background' color from the theme
                val viewModel: FriendListViewModel = viewModel()
                if (intent.getLongExtra(GROUP_GID_TAG,-1) != -1L) {
                    viewModel.gid = intent.getLongExtra(GROUP_GID_TAG,-1)
                    LaunchedEffect(key1 = true){
                        viewModel.getGroupMemberByGid(viewModel.gid)
                    }
                } else {
                    LaunchedEffect(key1 = true){
                        viewModel.getFriendList()
                    }
                }
                Scaffold(
                    topBar = {
                        TopAppBar(
                            navigationIcon = {
                                IconButton(onClick = {
                                    this.finish()
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                                        contentDescription = "back"
                                    )
                                }
                            },
                            title = { Text(text = if (viewModel.gid != 0L) "群员列表" else "好友列表") },
                            actions = {}
                        )
                    }
                ) {
                    LazyColumn {
                        viewModel.grouped.forEach { (initial, contactsForInitial) ->
                            stickyHeader {
                                Text(
                                    text = initial.toString(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xfff2f5fa))
                                        .padding(start = 10.dp),
                                    color = Color(0xFF3A7BE7)
                                )
                            }
                            items(contactsForInitial) { contact ->
                                ConversationItem(
                                    Conversation = contact,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @Composable
    private fun ConversationItem(
        Conversation: User,
        viewModel: FriendListViewModel,
    ) {
        val context = LocalContext.current
        Row {
            val userInfoState = remember {
                mutableStateOf(false)
            }
            if (userInfoState.value)
            {
                UserInfo(state = userInfoState, user = viewModel.user,context = context)
            }
            Row(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .clickable {
                        viewModel.getUser(Conversation.uid)
                        userInfoState.value = true
                    }
            ) {
                Image(
                    painter = rememberGlidePainter(Conversation.IconUrl),
                    contentDescription = "friend_icon",
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .size(64.dp)
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
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 28.dp, start = 8.dp)
                    ,
                ) {
                    Text(
                        text = Conversation.Name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                    )
                }
            }
        }
    }
}