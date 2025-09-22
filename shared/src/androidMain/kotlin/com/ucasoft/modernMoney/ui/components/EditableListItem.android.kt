package com.ucasoft.modernMoney.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
actual fun EditableListItem(
    onDeleting: (() -> Boolean)?,
    onDelete: (() -> Boolean)?,
    onEdit: (() -> Boolean)?,
    content: @Composable (() -> Unit)
) {

    var isDeleting by remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.EndToStart -> { !onEdit!!.invoke() }
                SwipeToDismissBoxValue.StartToEnd -> {
                    isDeleting = onDeleting!!.invoke()
                    false
                }
                else -> false
            }
        }
    )

    DeletableItem(
        isDeleting,
        onDelete
    ) {
        SwipeToDismissBox(
            state = dismissState,
            enableDismissFromEndToStart = onEdit != null,
            enableDismissFromStartToEnd = onDeleting != null && onDelete != null,
            backgroundContent = { SwipeBackground(dismissState) }
        ) {
            ListItem(
                headlineContent = {
                    content()
                }
            )
        }
    }
}

@Composable
fun SwipeBackground(state: SwipeToDismissBoxState) {
    val direction = state.dismissDirection
    val isDelete = direction == SwipeToDismissBoxValue.StartToEnd
    val isEdit = direction == SwipeToDismissBoxValue.EndToStart

    val backgroundColor = when {
        isEdit -> MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        isDelete -> MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
        else -> Color.Transparent
    }

    val action = when {
        isEdit -> EditableAction.Edit
        isDelete -> EditableAction.Delete
        else -> null
    }

    val progress = state.progress
    val scale by animateFloatAsState(
        targetValue = if (progress > 0.1f) 1f else 0.5f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 16.dp),
        contentAlignment = when {
            isDelete -> Alignment.CenterStart
            isEdit -> Alignment.CenterEnd
            else -> Alignment.Center
        }
    ) {
        if (action != null) {
            BuildIcon(
                action,
                Modifier
                    .scale(scale)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}