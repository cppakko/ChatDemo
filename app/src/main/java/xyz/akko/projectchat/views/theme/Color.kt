package xyz.akko.projectchat.views.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val green200 = Color(0xffa5d6a7)
val green500 = Color(0xff4caf50)
val green700 = Color(0xff388e3c)

val defaultChatBackground = Color(0xfff5f6f8)

class GreenTheme{
    companion object{
        var Primary = Color(0xff66bb6a)
        val Light = Color(0xff98ee99)
        val Dark = Color(0xff338a3e)
        val Secondary = Color(0xffab47bc)
        val sLight = Color(0xffdf78ef)
        val sDark = Color(0xff790e8b)
    }
}

class GreenThemeBak{
    companion object{
        val Primary = Color(0xff66bb6a)
        val Light = Color(0xff98ee99)
        val Dark = Color(0xff338a3e)
        val Secondary = Color(0xff29b6f6)
        val sLight = Color(0xff73e8ff)
        val sDark = Color(0xff0086c3)
    }
}