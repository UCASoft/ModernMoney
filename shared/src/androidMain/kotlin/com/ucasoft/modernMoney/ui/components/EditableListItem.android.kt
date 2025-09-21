package com.ucasoft.modernMoney.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
actual fun EditableListItem(
    onDelete: (() -> Boolean)?,
    onEdit: (() -> Boolean)?,
    content: @Composable (() -> Unit)
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.EndToStart -> onEdit!!.invoke()
                SwipeToDismissBoxValue.StartToEnd -> onDelete!!.invoke()
                else -> false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromEndToStart = onEdit != null,
        enableDismissFromStartToEnd = onDelete != null,
        backgroundContent = { SwipeBackground(dismissState) }
    ) {
        ListItem(
            headlineContent = {
                content()
            }
        )
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

    val icon = when {
        isEdit -> Icons.Default.Edit
        isDelete -> Icons.Default.Delete
        else -> null
    }

    val contentDescription = when {
        isEdit -> "Edit item"
        isDelete -> "Delete item"
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
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier
                    .scale(scale)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}