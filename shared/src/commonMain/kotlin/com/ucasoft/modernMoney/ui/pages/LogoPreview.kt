package com.ucasoft.modernMoney.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.ucasoft.modernMoney.model.LogoEntity

@Composable
fun LogoPreview(entity: LogoEntity?, imagePicker: () -> Unit, onLogoDelete: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            entity?.logo?.let {
                BadgedBox(
                    badge = {
                        IconButton(
                            onClick = { onLogoDelete() },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Delete Logo",
                                Modifier.size(16.dp)
                            )
                        }
                    }
                ) {
                    Image(
                        bitmap = it,
                        contentDescription = "Logo",
                        modifier = Modifier.width(64.dp).height(64.dp).padding(start = 8.dp),
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.CenterStart
                    )
                }
            }
        }
        Button(
            modifier = Modifier.weight(1f).padding(end = 8.dp),
            onClick = { imagePicker() }
        ) {
            Text("Upload Logo")
        }
    }
}