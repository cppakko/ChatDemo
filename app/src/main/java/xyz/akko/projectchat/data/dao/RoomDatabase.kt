package xyz.akko.projectchat.data.dao

import android.content.Context
import androidx.room.*
import xyz.akko.projectchat.utils.UtilObject

var db_INSTANCE: AppDataBase? = null

@Database(
    entities = [
        Group::class,
        User::class,
        MessageEntity::class,
        GroupMessageEntity::class,
    ], version = 1, exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun useUser(): UserDao
    abstract fun useGroup(): GroupDao
    abstract fun useMessage(): MessageDao
    abstract fun userGroupMessage(): GroupMessageDao

    companion object{
        fun getDatabase(context: Context? = null, uid: Long? = null): AppDataBase? {
            if (db_INSTANCE == null && context != null && uid != null) {
                synchronized(AppDataBase::class.java) {
                    if (db_INSTANCE == null) {
                        db_INSTANCE = Room.databaseBuilder(
                            context,
                            AppDataBase::class.java, uid.toString()
                        ).build()
                    }
                }
            }
            return db_INSTANCE
        }
    }
}

@Dao
abstract class UserDao {
    @Query("SELECT * FROM user_info")
    abstract suspend fun getAll(): List<User>

    @Query("SELECT * FROM user_info WHERE user_info.uid = :uid")
    abstract suspend fun getByUid(uid: Long): User

    @Insert
    abstract suspend fun insertUser(user: User)
}

@Dao
abstract class GroupDao {
    @Query("SELECT * FROM group_info")
    abstract suspend fun getAll(): List<Group>

    @Query("SELECT * FROM group_info WHERE group_info.gid = :gid")
    abstract suspend fun getByGid(gid: Long): Group

    @Insert
    abstract suspend fun insertGroup(group: Group)
}

@Dao
abstract class MessageDao {
    @Query("SELECT * FROM message_json WHERE message_json.sender_uid = :senderUid")
    abstract suspend fun getMessageByUid(senderUid: Long): List<MessageEntity>

    @Query("SELECT * FROM message_json WHERE message_json.sender_uid = :myUid AND message_json.receiver_uid = :senderUid")
    abstract suspend fun getMyMessageByUid(senderUid: Long,myUid: Long = UtilObject.myUid): List<MessageEntity>

    @Insert
    abstract suspend fun insertMessage(msg: MessageEntity)
}

@Dao
abstract class GroupMessageDao {
    @Query("SELECT * FROM group_message_json WHERE group_message_json.send_group_gid = :gid")
    abstract suspend fun getGroupMessageByGid(gid: Long): List<GroupMessageEntity>

    @Insert
    abstract suspend fun insertGroupMessage(msg: GroupMessageEntity)
}