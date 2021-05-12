package xyz.akko.projectchat

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import xyz.akko.projectchat.views.theme.BootTheme
import xyz.akko.projectchat.views.ui.login.LoginActivity

//Boot Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BootTheme {
                startActivity(Intent(this, LoginActivity::class.java))
                this.finish()
            }
        }
    }
}