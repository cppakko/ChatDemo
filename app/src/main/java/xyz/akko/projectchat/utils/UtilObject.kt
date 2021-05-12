package xyz.akko.projectchat.utils

import xyz.akko.projectchat.data.dao.User

object UtilObject {
    var myUid: Long = 0L
    var myUser = User(1,"null","null","null")
    lateinit var myFriendList: MutableList<User>
}