package xyz.akko.projectchat.views.ui.chatMain

import FaIcons
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guru.fontawesomecomposelib.FaIcon
import kotlinx.coroutines.runBlocking
import xyz.akko.projectchat.utils.UtilObject
import xyz.akko.projectchat.views.theme.ProjectChatTheme
import xyz.akko.projectchat.views.ui.chatMain.ConversationNavType.Friends
import xyz.akko.projectchat.views.ui.chatMain.ConversationNavType.Groups

//TODO RENAME
class ChatMain : ComponentActivity() {

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dm = DisplayMetrics()
        //TODO SDK30+时使用新方法
        windowManager.defaultDisplay.getMetrics(dm)
        setContent {
            ProjectChatTheme {
                val scope = rememberCoroutineScope()
                val deviceWidth = dm.widthPixels / dm.density
                //TODO CLEAN CODE
                val viewModel:ChatViewModel = viewModel()
                val uid = intent.getLongExtra("uid",1000L)
                UtilObject.myUid = uid
                viewModel.roomInit(uid,this@ChatMain)

                Scaffold(
                    topBar = { ToolBar(scope, viewModel, this) }
                ) {
                    Column {
                        TabRow(viewModel)
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                        ) {
                            Crossfade(targetState = viewModel.switchIndex.value) {
                                when (it) {
                                    Friends -> ConversationNav().View(
                                        viewModel,
                                        deviceWidth,
                                        ConversationNavType.valueOf("Friends")
                                    )
                                    Groups -> ConversationNav().View(
                                        viewModel,
                                        deviceWidth,
                                        ConversationNavType.valueOf("Groups")
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @ExperimentalMaterialApi
    @Composable
    private fun TabRow(viewModel: ChatViewModel) {
        TabRow(
            selectedTabIndex = viewModel.switchIndex.value.ordinal,
            modifier = Modifier.height(50.dp)
        ) {
            //TODO 优化协程
            Tab(
                selected = viewModel.switchIndex.value.String == "Friends",
                onClick = {
                    runBlocking {
                        viewModel.switchIndex.value = Friends
                    }
                },
            ) {
                Row {
                    FaIcon(faIcon = FaIcons.UserFriends)
                }
            }
            //TODO 优化协程
            Tab(selected = viewModel.switchIndex.value.String == "Groups", onClick = {
                runBlocking {
                    viewModel.switchIndex.value = Groups
                }
            }) {
                Row {
                    FaIcon(faIcon = FaIcons.Users)
                }
            }
        }
    }
}

enum class ConversationNavType(val String: String) {
    Friends("Friends"),
    Groups("Groups")
}
