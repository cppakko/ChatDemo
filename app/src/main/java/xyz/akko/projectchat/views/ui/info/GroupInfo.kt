package xyz.akko.projectchat.views.ui.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.google.accompanist.glide.rememberGlidePainter
import xyz.akko.projectchat.data.dao.Group
import xyz.akko.projectchat.data.dao.User
import xyz.akko.projectchat.views.theme.GreenTheme

@Composable
fun GroupInfo(state: MutableState<Boolean>,loadingState: MutableState<Boolean>? = null,group: Group?,groupUserList: SnapshotStateMap<Long, User>) {
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
            if (group == null) {
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
                    painter = rememberGlidePainter(request = group.IconUrl),
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
                                brush = Brush.linearGradient(colors = listOf(GreenTheme.Primary, GreenTheme.Light, GreenTheme.sLight, GreenTheme.Secondary), start = Offset(250f, 75f), end = Offset(0f, 75f))
                            )
                        )
                        .clickable {
                            /*TODO*/
                        }
                )
                Card(
                    Modifier
                        .fillMaxWidth()
                        .height(410.dp)
                        .zIndex(0f)
                        .padding(top = 120.dp)
                ) {
                    Column {
                        Text(
                            text = group.Name,
                            modifier = Modifier.padding(top = 40.dp,start = 16.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp
                        )
                        Text(
                            text = "GID: ${group.gid}",
                            modifier = Modifier.padding(top = 0.dp,start = 16.dp),
                            fontWeight = FontWeight.Light,
                        )
                        Spacer(modifier = Modifier.padding(top = 5.dp))
                        LazyRow(
                            modifier = Modifier.padding(start = 20.dp)
                        ) {
                            items(groupUserList.values.toList()) { groupUser ->
                                Image(
                                    painter = rememberGlidePainter(request = groupUser.IconUrl),
                                    contentDescription = "icon",
                                    Modifier
                                        .size(60.dp)
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
                                )
                            }
                        }
                        Spacer(modifier = Modifier.padding(top = 5.dp))
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

                }
            }
        }
    }
}