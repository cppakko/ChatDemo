package xyz.akko.projectchat.data

import kotlinx.serialization.Serializable

//TODO REFACTOR
@Serializable
data class FriendListItem(
    val IconUrl: String,
    val senderId: Long,
    val Name: String,
    //TODO
    val lastMsg:String? = null
)

@Serializable
data class GroupListItem(
    val IconUrl: String,
    val GroupId: Long,
    val Name: String,
    //TODO
    val lastMsg:String? = null
)

@Serializable
data class GroupMember(
    val uid:Long
)