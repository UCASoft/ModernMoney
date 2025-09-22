package com.ucasoft.modernMoney.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

@Composable
expect fun EditableListItem(
    onDeleting: (() -> Boolean)? = null,
    onDelete: (() -> Boolean)? = null,
    onEdit: (() -> Boolean)? = null,
    content: @Composable () -> Unit
)

@Composable
internal fun DeletableItem(isDeleting: Boolean = false, onDelete: (() -> Boolean)? = null, content: @Composable () -> Unit) {
    LaunchedEffect(isDeleting) {
        if (isDeleting) {
            delay(500)
            onDelete!!.invoke()
        }
    }

    AnimatedVisibility(
        visible = !isDeleting,
        exit = shrinkVertically(
            animationSpec = tween(500),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        content()
    }
}

@Composable
internal fun BuildIcon(
    action: EditableAction,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current) {

    val icon = when(action) {
        EditableAction.Edit -> Icons.Default.Edit
        EditableAction.Delete -> Icons.Default.Delete
    }

    Icon(
        icon,
        icon::name.toString(),
        modifier,
        tint
    )
}

internal enum class EditableAction {
    Edit,

    Delete
}