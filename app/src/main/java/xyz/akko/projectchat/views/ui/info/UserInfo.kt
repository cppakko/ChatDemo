package xyz.akko.projectchat.views.ui.info

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.accompanist.glide.rememberGlidePainter
import xyz.akko.projectchat.data.dao.User
import xyz.akko.projectchat.utils.UtilObject
import xyz.akko.projectchat.views.theme.GreenTheme


@ExperimentalAnimationApi
@Composable
fun UserInfo(state: MutableState<Boolean>,loadingState: MutableState<Boolean>? = null, user: User?,context: Context)
{
    Dialog(onDismissRequest = {
        state.value = false
        if (loadingState != null) {
            loadingState.value = false
        }
    }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            if (user == null) {
                Card(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "未查询到该用户")
                    }
                }
            } else {
                Image(
                    painter = rememberGlidePainter(request = user.IconUrl),
                    contentDescription = "icon",
                    Modifier
                        .padding(start = 20.dp, top = 80.dp)
                        .size(80.dp)
                        .zIndex(1f)
                        .clip(CircleShape)
                        .border(
                            shape = CircleShape,
                            border = BorderStroke(
                                width = 3.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        GreenTheme.Primary,
                                        GreenTheme.Light,
                                        GreenTheme.sLight,
                                        GreenTheme.Secondary
                                    ),
                                    start = Offset(
                                        250f,
                                        75f
                                    ),
                                    end = Offset(
                                        0f,
                                        75f
                                    )
                                )
                            )
                        )
                        .clickable {
                            if (user.uid == UtilObject.myUid) {
                                val intent = Intent(Intent.ACTION_PICK)
                                intent.type = "image/*"
                                startActivityForResult(context as Activity, intent, 200, null)
                            }
                        }
                )
                Card(
                    Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .zIndex(0f)
                        .padding(top = 120.dp)
                ) {
                    Column {
                        Text(
                            text = user.Name,
                            modifier = Modifier.padding(top = 40.dp,start = 16.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp
                        )
                        Text(
                            text = "UID: ${user.uid}",
                            modifier = Modifier.padding(top = 0.dp,start = 16.dp),
                            fontWeight = FontWeight.Light,
                        )

                        Text(
                            //TODO
                            text = "这个人很懒 他什么都没有写(又或是什么都写了",
                            modifier = Modifier.padding(top = 0.dp,start = 16.dp),
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (user.uid == UtilObject.myUid) {
                        val editDialogState = remember {
                            mutableStateOf(false)
                        }
                        val editDialogString = remember {
                            mutableStateOf("")
                        }
                        IconButton(
                            onClick = { editDialogState.value = true },
                            Modifier
                                .padding(top = 295.dp, end = 20.dp)
                                .clip(CircleShape)
                                .background(GreenTheme.Secondary),
                        ) {
                            Icon(
                                painter = painterResource(id = xyz.akko.projectchat.R.drawable.ic_baseline_edit_24),
                                contentDescription = "edit"
                            )
                        }
                        if (editDialogState.value) {
                            Dialog(onDismissRequest = { editDialogState.value = false }) {
                                Card(
                                    Modifier
                                        .width(360.dp)
                                        .height(360.dp)
                                ) {
                                    Column {
                                        TextField(
                                            value = editDialogString.value,
                                            onValueChange = { string ->
                                                editDialogString.value = string
                                            },
                                            modifier = Modifier.width(250.dp),
                                            label = {
                                                Text(text = "输入简介")
                                            },
                                            placeholder = {}
                                        )
                                        Row {
                                            Button(onClick = { /*TODO WAIT FOR NETWORK PART*/ }) {
                                                Text(text = "提交")
                                            }
                                            Button(onClick = { editDialogState.value = false }) {
                                                Text(text = "取消")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        IconButton(
                            onClick = { /*TODO*/ },
                            Modifier
                                .padding(top = 295.dp, end = 20.dp)
                                .clip(CircleShape)
                                .background(GreenTheme.Secondary),
                        ) {
                            Icon(
                                painter = painterResource(id = xyz.akko.projectchat.R.drawable.ic_baseline_add_24),
                                contentDescription = "add_friend"
                            )
                        }
                    }
                }
            }
        }
    }
}