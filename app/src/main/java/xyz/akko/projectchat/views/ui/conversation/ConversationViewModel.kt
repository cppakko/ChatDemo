package xyz.akko.projectchat.views.ui.conversation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import xyz.akko.projectchat.data.GroupMember
import xyz.akko.projectchat.data.ListItem
import xyz.akko.projectchat.data.ListItemType
import xyz.akko.projectchat.data.ListItemType.FriendMessage
import xyz.akko.projectchat.data.ListItemType.GroupMessage
import xyz.akko.projectchat.data.dao.*
import xyz.akko.projectchat.utils.UtilObject

//TODO CLEAN CODE
class ConversationViewModel : ViewModel() {
    lateinit var ConversationInfo: ListItem
    lateinit var type: ListItemType
    lateinit var group: Group
    lateinit var user: User
    var uid: Long = 1L
    var gid: Long = 1L

    val groupUserList = mutableStateMapOf<Long,User>()
    val dropDownMenuState = mutableStateOf(false)
    val dbLoadingState = mutableStateOf(false)
    private val db: AppDataBase = AppDataBase.getDatabase()!!

    //TODO
    val messageList: SnapshotStateList<MessageEntity> = mutableStateListOf()
    val groupMessageList: SnapshotStateList<GroupMessageEntity> = mutableStateListOf()
    //TODO RENAME
    fun dbInit() = viewModelScope.launch(Dispatchers.IO) {
        when (type) {
            FriendMessage -> {
                messageList.addAll(db.useMessage().getMessageByUid(ConversationInfo.senderId))
                messageList.addAll(db.useMessage().getMyMessageByUid(ConversationInfo.senderId, UtilObject.myUid))
            }
            GroupMessage -> {
                groupMessageList.addAll(db.userGroupMessage().getGroupMessageByGid(gid))
            }
        }

        messageList.sortBy {
            it.sendTime
        }
    }

    fun userInit() = viewModelScope.launch(Dispatchers.IO) {
        user = db.useUser().getByUid(uid)
        dbLoadingState.value = true
    }

    fun userOrGroupInfoInit() = viewModelScope.launch(Dispatchers.IO) {
        when (type) {
            FriendMessage -> user = db.useUser().getByUid(uid)
            GroupMessage -> {
                group = db.useGroup().getByGid(gid)
                for (user in Json.decodeFromString<List<GroupMember>>(group.memberList)) {
                    groupUserList[user.uid] = db.useUser().getByUid(user.uid)
                }
            }
        }
    }

    val inputText = mutableStateOf("")
}