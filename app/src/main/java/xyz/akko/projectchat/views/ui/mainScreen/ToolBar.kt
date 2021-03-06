package xyz.akko.projectchat.views.ui.mainScreen

import android.content.Context
import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.akko.projectchat.R
import xyz.akko.projectchat.data.dao.User
import xyz.akko.projectchat.utils.UtilObject
import xyz.akko.projectchat.views.ui.addPage.AddSearchActivity
import xyz.akko.projectchat.views.ui.friendslist.FriendsList
import xyz.akko.projectchat.views.ui.info.UserInfo

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun ToolBar(scope: CoroutineScope, viewModel: ChatViewModel,context: Context) {
    TopAppBar(
        navigationIcon = {
            val state = remember {
                mutableStateOf(false)
            }
            IconButton(
                //TODO δΌεεη¨
                onClick = {
                    state.value = true
                }
            ) {
                Image(
                    painter = rememberGlidePainter(request = UtilObject.myUser.IconUrl),
                    contentDescription = "user_icon",
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .clip(CircleShape)
                )
                val user = remember {
                    mutableStateOf<User?>(null)
                }
                scope.launch {
                    user.value = viewModel.getUserInfo(UtilObject.myUid)
                }
                if (state.value)
                {
                    if (state.value) UserInfo(state = state, user = user.value,context = LocalContext.current)
                }
            }
        },
        actions = {
            IconButton(onClick = {
                val intent = Intent(context, FriendsList::class.java)
                context.startActivity(intent)
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_contacts_24), contentDescription = "contacts")
            }
            IconButton(onClick = {
                val intent = Intent(context, AddSearchActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_add_24), contentDescription = "add_friend_group")
            }
        },
        title = {
            Text(text = viewModel.connectionStatus.value)
        }
    )
}