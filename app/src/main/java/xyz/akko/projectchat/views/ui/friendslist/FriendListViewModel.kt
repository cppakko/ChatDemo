package xyz.akko.projectchat.views.ui.friendslist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import xyz.akko.projectchat.data.GroupMember
import xyz.akko.projectchat.data.dao.AppDataBase
import xyz.akko.projectchat.data.dao.User

class FriendListViewModel: ViewModel() {
    lateinit var user: User
    var gid: Long = 0L
    private val userList  = mutableStateListOf<User>()
    var grouped = mutableStateMapOf<Char,List<User>>()
    private val db = AppDataBase.getDatabase()!!

    fun getFriendList() = viewModelScope.launch(Dispatchers.IO) {
        userList.addAll(db.useUser().getAll())
        grouped.putAll(userList.groupBy { it.Name[0] })
    }
    fun getUser(uid: Long) = viewModelScope.launch(Dispatchers.IO) {
        user = db.useUser().getByUid(uid)
    }

    fun getGroupMemberByGid(gid: Long) = viewModelScope.launch(Dispatchers.IO) {
        val temp = Json.decodeFromString<List<GroupMember>>(string = db.useGroup().getByGid(gid).memberList)
        for (user in temp) {
            userList.add(db.useUser().getByUid(user.uid))
        }
        grouped.putAll(userList.groupBy { it.Name[0] })
    }
}