package xyz.akko.projectchat.data

enum class MessageTypeEnum {
    StringMessage,
    ReplyMessage,
    ImageMessage,
    VoiceMessage
}

data class StringMessage (
    val stringContent: String
    )

data class ReplyMessage(
    val toReplyMessageId: Long,
    val stringContent: String
)

data class VoiceMessage(
    val voiceId: Long
)

data class ImageMessage(
    val imageId: Long
)