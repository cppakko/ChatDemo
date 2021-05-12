package xyz.akko.projectchat.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import xyz.akko.projectchat.data.MessageTypeEnum

@Serializable
@Entity(tableName = "user_info")
data class User(
    @ColumnInfo(name = "uid",index = true) @PrimaryKey val uid: Long,
    @ColumnInfo(name = "name") val Name: String,
    @ColumnInfo(name = "icon_url") val IconUrl: String,
    @ColumnInfo(name = "introduction") val Introduction: String? = null
)

@Entity(tableName = "group_info")
data class Group(
    @ColumnInfo(name = "gid",index = true) @PrimaryKey val gid: Long,
    @ColumnInfo(name = "name") val Name: String,
    @ColumnInfo(name = "icon_url") val IconUrl: String,
    @ColumnInfo(name = "member_list_json") val memberList: String
)

@Entity(
    tableName = "message_json",
/*    foreignKeys = [
        ForeignKey(
            entity = User::class,
            childColumns = ["sender_uid"],
            parentColumns = ["uid"]
        )
    ]*/
)
data class MessageEntity(
    @ColumnInfo(name = "mid",index = true) @PrimaryKey val mid: Long,
    @ColumnInfo(name = "send_time") val sendTime: Long,
    @ColumnInfo(name = "sender_uid",index = true) val senderUid: Long,
    @ColumnInfo(name = "receiver_uid") val receiverUid: Long,
    @ColumnInfo(name = "message_type") val type: MessageTypeEnum,
    @ColumnInfo(name = "content_json") val content: String
)

@Entity(
    tableName = "group_message_json",
/*    foreignKeys = [
        ForeignKey(
            entity = User::class,
            childColumns = ["sender_uid"],
            parentColumns = ["uid"]
        ),
        ForeignKey(
            entity = Group::class,
            childColumns = ["send_group_gid"],
            parentColumns = ["gid"]
        )
    ]*/
)
data class GroupMessageEntity(
    @ColumnInfo(name = "gmid",index = true) @PrimaryKey val gmid: Long,
    @ColumnInfo(name = "send_time") val sendTime: Long,
    @ColumnInfo(name = "sender_uid",index = true) val senderUid: Long,
    @ColumnInfo(name = "send_group_gid",index = true) val groupGid: Long,
    @ColumnInfo(name = "message_type") val type: MessageTypeEnum,
    @ColumnInfo(name = "content_json") val content: String
)