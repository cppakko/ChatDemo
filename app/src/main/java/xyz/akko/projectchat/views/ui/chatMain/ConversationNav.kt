package xyz.akko.projectchat.views.ui.chatMain

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import xyz.akko.projectchat.data.FriendListItem
import xyz.akko.projectchat.data.GroupListItem
import xyz.akko.projectchat.views.theme.GreenTheme
import xyz.akko.projectchat.views.theme.defaultChatBackground
import xyz.akko.projectchat.views.ui.DeleteMsgButton
import xyz.akko.projectchat.views.ui.UnreadMsgCountTag
import xyz.akko.projectchat.views.ui.chatMain.ConversationNavType.Friends
import xyz.akko.projectchat.views.ui.chatMain.ConversationNavType.Groups
import xyz.akko.projectchat.views.ui.conversation.ConversationView
import kotlin.math.roundToInt

//TODO RENAME
const val INTENT_TAG = "CONVERSATION_INFO"
const val CURRENT_UID = "CURRENT_UID"
const val CURRENT_GID = "CURRENT_GID"

class ConversationNav {

    @ExperimentalMaterialApi
    @Composable
    fun View(viewModel: ChatViewModel, width: Float, type: ConversationNavType) {
        Column(
            modifier = Modifier
                .background(defaultChatBackground)
                .fillMaxHeight()
        ) {
            LazyColumn(
                Modifier.fillMaxWidth()
            ) {
                val itemList = when (type) {
                    Friends -> viewModel.friendConversationList
                    Groups -> viewModel.groupConversationList
                }
                items(itemList) { Conversation ->
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            when (type) {
                                Friends -> ConversationItem(Conversation as FriendListItem, viewModel, width, LocalContext.current,Conversation.senderId.toString(),type)
                                Groups -> ConversationItem(Conversation as GroupListItem, viewModel, width, LocalContext.current,Conversation.GroupId.toString(),type)
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
private fun ConversationItem(
    Conversation: FriendListItem,
    viewModel: ChatViewModel,
    width: Float,
    context: Context,
    uidOrGid: String,
    type: ConversationNavType
) {
    val unreadCountMap = when (type.String) {
        "Friends" -> viewModel.friendUnreadCountMap
        "Groups" -> viewModel.groupUnreadCountMap
        else -> null
    }

    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { -65.dp.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)

    Row(
        modifier = Modifier
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .offset {
                IntOffset(
                    swipeableState.offset.value.roundToInt(),
                    0
                )
            }
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .width(Dp(width))
                .clickable { /* TODO */
                    val intent = Intent(context, ConversationView::class.java)
                    intent.putExtra(INTENT_TAG, Json.encodeToString(Conversation))
                    if (type == Friends) intent.putExtra(CURRENT_UID,uidOrGid)
                    else intent.putExtra(CURRENT_GID,uidOrGid)
                    context.startActivity(intent)
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
            Column {
                Text(
                    text = Conversation.Name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            Row(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
                    .padding(end = 5.dp, top = 25.dp)
            ) {
                //TODO 检查数值绑定
                fun unreadCount(): String {
                    val unreadValue = unreadCountMap?.get(Conversation.senderId)?.value
                    return if (unreadValue != null) {
                        when {
                            unreadValue > 99 -> "99+"
                            unreadValue == 0 -> ""
                            else -> unreadValue.toString()
                        }
                    } else "null"
                }
                UnreadMsgCountTag(
                    //TODO 为0时的逻辑
                    text = unreadCount()
                )
            }
        }
        DeleteMsgButton()
    }
}

@ExperimentalMaterialApi
@Composable
private fun ConversationItem(
    Conversation: GroupListItem,
    viewModel: ChatViewModel,
    width: Float,
    context: Context,
    uidOrGid: String,
    type: ConversationNavType
) {
    val unreadCountMap = when (type.String) {
        "Friends" -> viewModel.friendUnreadCountMap
        "Groups" -> viewModel.groupUnreadCountMap
        else -> null
    }

    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { -65.dp.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)

    Row(
        modifier = Modifier
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .offset {
                IntOffset(
                    swipeableState.offset.value.roundToInt(),
                    0
                )
            }
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .width(Dp(width))
                .clickable { /* TODO */
                    val intent = Intent(context, ConversationView::class.java)
                    intent.putExtra(INTENT_TAG, Json.encodeToString(Conversation))
                    if (type == Friends) intent.putExtra(CURRENT_UID,uidOrGid)
                    else intent.putExtra(CURRENT_GID,uidOrGid)
                    context.startActivity(intent)
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
            Column {
                Text(
                    text = Conversation.Name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            Row(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
                    .padding(end = 5.dp, top = 25.dp)
            ) {
                //TODO 检查数值绑定
                fun unreadCount(): String {
                    val unreadValue = unreadCountMap?.get(Conversation.GroupId)?.value
                    return if (unreadValue != null) {
                        when {
                            unreadValue > 99 -> "99+"
                            unreadValue == 0 -> ""
                            else -> unreadValue.toString()
                        }
                    } else "null"
                }
                UnreadMsgCountTag(
                    //TODO 为0时的逻辑
                    text = unreadCount()
                )
            }
        }
        DeleteMsgButton()
    }
}