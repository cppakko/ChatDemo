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
import xyz.akko.projectchat.data.FriendListItem
import xyz.akko.projectchat.data.GroupListItem
import xyz.akko.projectchat.data.GroupMember
import xyz.akko.projectchat.data.dao.*
import xyz.akko.projectchat.utils.UtilObject
import xyz.akko.projectchat.views.ui.chatMain.ConversationNavType
import xyz.akko.projectchat.views.ui.chatMain.ConversationNavType.Friends
import xyz.akko.projectchat.views.ui.chatMain.ConversationNavType.Groups

//TODO CLEAN CODE
class ConversationViewModel : ViewModel() {
    lateinit var friendConversationInfo: FriendListItem
    lateinit var groupConversationInfo: GroupListItem
    lateinit var type: ConversationNavType
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
            Friends -> {
                messageList.addAll(db.useMessage().getMessageByUid(friendConversationInfo.senderId))
                messageList.addAll(db.useMessage().getMyMessageByUid(friendConversationInfo.senderId, UtilObject.myUid))
            }
            Groups -> {
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
            Friends -> user = db.useUser().getByUid(uid)
            Groups -> {
                group = db.useGroup().getByGid(gid)
                for (user in Json.decodeFromString<List<GroupMember>>(group.memberList)) {
                    groupUserList[user.uid] = db.useUser().getByUid(user.uid)
                }
            }
        }
    }

    val inputText = mutableStateOf("")
}