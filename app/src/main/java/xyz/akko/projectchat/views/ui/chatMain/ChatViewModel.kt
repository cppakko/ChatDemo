package xyz.akko.projectchat.views.ui.chatMain

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.akko.projectchat.data.FriendListItem
import xyz.akko.projectchat.data.GroupListItem
import xyz.akko.projectchat.data.MessageTypeEnum
import xyz.akko.projectchat.data.dao.*
import xyz.akko.projectchat.utils.UtilObject
import java.util.*

class ChatViewModel : ViewModel() {
    lateinit var db: AppDataBase

    //TODO RENAME
    val switchIndex = mutableStateOf(ConversationNavType.Friends)
    val connectionStatus = mutableStateOf("")
    val friendConversationList = mutableStateListOf<FriendListItem>()

    val groupConversationList = mutableStateListOf<GroupListItem>()

    val friendUnreadCountMap = HashMap<Long, MutableLiveData<Int>>()
    val groupUnreadCountMap = HashMap<Long, MutableLiveData<Int>>()

    private fun firstRun(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("FirstRun",0)
        return if (sharedPreferences.getBoolean("First",true)){
            sharedPreferences.edit().putBoolean("First",false).apply()
            true
        } else {
            false
        }
    }

    fun roomInit(uid: Long,context: Context) = viewModelScope.launch(Dispatchers.IO) {
        db = AppDataBase.getDatabase(context, uid)!!
        //TODO CLEAN CODE
        //*TEST CODE*//
        if (firstRun(context)) {
            db.useUser().insertUser(User(123,"nono","https://i.loli.net/2021/05/12/tzixEAY8qKSeJlP.png"))
            db.useUser().insertUser(User(1234,"akko","https://i.loli.net/2021/05/12/VBIwH7ONlqQx4Ae.png"))
            db.useUser().insertUser(User(1234567,"lol","https://i.loli.net/2021/05/12/bRySqLZcfG9dhn3.png"))

            db.useGroup().insertGroup(Group(123,"TestGroup","https://i.loli.net/2021/05/12/ryKszgY9dlX2S3a.jpg", "[{\"uid\":1234567890},{\"uid\":123},{\"uid\":1234},{\"uid\":1234567}]"))
            db.userGroupMessage().insertGroupMessage(GroupMessageEntity(901L,System.currentTimeMillis(),123L,123,MessageTypeEnum.StringMessage,"wei 这河里吗"))
            db.userGroupMessage().insertGroupMessage(GroupMessageEntity(902L,System.currentTimeMillis(),1234L,123,MessageTypeEnum.StringMessage,"bu 这恒河里吗"))
            db.userGroupMessage().insertGroupMessage(GroupMessageEntity(903L,System.currentTimeMillis(),1234567890L,123,MessageTypeEnum.ImageMessage,"https://i.loli.net/2021/05/12/ryKszgY9dlX2S3a.jpg"))
            db.userGroupMessage().insertGroupMessage(GroupMessageEntity(904L,System.currentTimeMillis(),1234567L,123,MessageTypeEnum.StringMessage,"みゃ～"))



            db.useMessage().insertMessage(MessageEntity(400L,System.currentTimeMillis(),1234L,1234567890L,MessageTypeEnum.StringMessage,"Test"))
            db.useMessage().insertMessage(MessageEntity(401L,System.currentTimeMillis(),1234L,1234567890L,MessageTypeEnum.StringMessage,"wei zaima"))
            db.useMessage().insertMessage(MessageEntity(402L,System.currentTimeMillis(),1234L,1234567890L, MessageTypeEnum.StringMessage,"红红火火恍恍惚惚嘿嘿嘿"))
            db.useUser().insertUser(User(1234567890,"gg","https://i.loli.net/2021/05/12/AuL9OstUrclvo36.jpg"))
            db.useMessage().insertMessage(MessageEntity(403L,System.currentTimeMillis(),1234567890L,1234L,MessageTypeEnum.StringMessage,"buzai cnm"))
            db.useMessage().insertMessage(MessageEntity(404L,System.currentTimeMillis(),1234L,1234567890L,MessageTypeEnum.ImageMessage,"https://i.loli.net/2021/05/12/ryKszgY9dlX2S3a.jpg"))
        }

        UtilObject.myUser = db.useUser().getByUid(uid)
        for (user in db.useUser().getAll()) {
            friendConversationList.add(FriendListItem(user.IconUrl,user.uid,user.Name))
        }

        for (group in db.useGroup().getAll())
        {
            groupConversationList.add(GroupListItem(group.IconUrl,group.gid,group.Name))
        }

        /*TEST CODE*/
        friendUnreadCountMap[123] = MutableLiveData<Int>(23)
        friendUnreadCountMap[1234] = MutableLiveData<Int>(123)
        friendUnreadCountMap[1234567] = MutableLiveData<Int>(13)
        friendUnreadCountMap[1234567890] = MutableLiveData<Int>(1)
        groupUnreadCountMap[123] = MutableLiveData<Int>(3)
    }

    suspend fun getUserInfo(uid: Long): User =
        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            db.useUser().getByUid(uid)
        }

}
