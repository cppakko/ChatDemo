package xyz.akko.projectchat.views.ui

import FaIcons
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.guru.fontawesomecomposelib.FaIcon
import xyz.akko.projectchat.views.theme.GreenTheme
import xyz.akko.projectchat.views.theme.green700

@Composable
fun EmptyTopAppBar(title: String)
{
    TopAppBar(
        title = {
            Text(text = title)
        }
    )
}

@ExperimentalAnimationApi
@Composable
fun BaseAnimatedVisibility(state: Boolean,content: @Composable () -> Unit)
{
    AnimatedVisibility(
        visible = state,
        enter = slideInVertically(
            initialOffsetY = { -40 }
        ) + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut(),
        content = content)
}

@Composable
fun DeleteMsgButton()
{
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .height(85.dp)
            .width(70.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xffF44336))
    ) {
        FaIcon(faIcon = FaIcons.Trash)
    }
}

@Composable
fun UnreadMsgCountTag(text: String) {
    val tagModifier = Modifier
        .padding(4.dp)
        .clickable(onClick = {})
        .clip(RoundedCornerShape(4.dp))
        .background(GreenTheme.Dark.copy(alpha = 0.2f))
        .padding(horizontal = 8.dp, vertical = 4.dp)

    Text(
        text = text,
        color = green700,
        modifier = tagModifier,
        style = typography.body2.copy(fontWeight = FontWeight.Bold)
    )
}

@ExperimentalFoundationApi
@Composable
fun LongClickButton(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    val contentColor by colors.contentColor(enabled)
    Surface(
        shape = shape,
        color = colors.backgroundColor(enabled).value,
        contentColor = contentColor.copy(alpha = 1f),
        border = border,
        elevation = elevation?.elevation(enabled, interactionSource)?.value ?: 0.dp,
        modifier = modifier.combinedClickable(
            onLongClick = onLongClick,
            onClick = onClick,
            enabled = enabled,
            role = Role.Button,
            interactionSource = interactionSource,
            indication = null
        )
    ) {
        CompositionLocalProvider(LocalContentAlpha provides contentColor.alpha) {
            ProvideTextStyle(
                value = typography.button
            ) {
                Row(
                    Modifier
                        .defaultMinSize(
                            minWidth = ButtonDefaults.MinWidth,
                            minHeight = ButtonDefaults.MinHeight
                        )
                        .indication(interactionSource, rememberRipple())
                        .padding(contentPadding),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}