package xyz.akko.projectchat.views.ui.mainScreen

import android.os.Build
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.akko.projectchat.R
import xyz.akko.projectchat.data.ListItemType.*
import xyz.akko.projectchat.utils.UtilObject
import xyz.akko.projectchat.views.theme.ProjectChatTheme

class MainScreen : ComponentActivity() {

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val deviceWidth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = windowManager.currentWindowMetrics
            (metrics.bounds.width() / this.resources.displayMetrics.density).dp
        } else {
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dm)
            (dm.widthPixels / dm.density).dp
        }
        setContent {
            ProjectChatTheme {
                val scope = rememberCoroutineScope()
                //TODO CLEAN CODE
                val viewModel: ChatViewModel = viewModel()
                val uid = intent.getLongExtra("uid", 1000L)
                UtilObject.myUid = uid
                viewModel.roomInit(uid, this@MainScreen)

                Scaffold(
                    topBar = { ToolBar(scope, viewModel, this) }
                ) {
                    Column {
                        TabRow(viewModel)
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Crossfade(targetState = viewModel.switchIndex.value) {
                                when (it) {
                                    FriendMessage -> ConversationNav().View(
                                        viewModel,
                                        deviceWidth,
                                        FriendMessage
                                    )
                                    GroupMessage -> ConversationNav().View(
                                        viewModel,
                                        deviceWidth,
                                        GroupMessage
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
            Tab(
                selected = viewModel.switchIndex.value == FriendMessage,
                onClick = {
                    viewModel.switchIndex.value = FriendMessage
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_person_24),
                    contentDescription = "person_icon"
                )
            }
            Tab(selected = viewModel.switchIndex.value == GroupMessage,
                onClick = {
                    viewModel.switchIndex.value = GroupMessage
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_group_24),
                    contentDescription = "group_icon"
                )
            }
        }
    }
}
