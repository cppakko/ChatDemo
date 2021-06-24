package xyz.akko.projectchat.data

import kotlinx.serialization.Serializable

@Serializable
data class ListItem (
    val IconUrl: String,
    val senderId: Long,
    val GroupId: Long,
    val name: String,
    val listType: ListItemType,
    //TODO
    val lastMsg:String? = null
)

@Serializable
data class GroupMember(
    val uid:Long
)

enum class ListItemType{
    FriendMessage,
    GroupMessage
}