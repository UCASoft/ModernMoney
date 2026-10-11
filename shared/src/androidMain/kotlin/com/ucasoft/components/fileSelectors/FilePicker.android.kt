package com.ucasoft.components.fileSelectors

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

@Composable
fun <I> rememberFilePicker(
    contract: ActivityResultContract<I, Uri?>,
    mime: I,
    onFileSelected: (SelectedFile<*>?) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val currentOnFileSelected by rememberUpdatedState(onFileSelected)
    val launcher = rememberLauncherForActivityResult(
        contract = contract
    ) { uri ->
        currentOnFileSelected(uri?.let { AndroidSelectedFile(context, it) })
    }
    return remember(launcher, mime) {
        {
            launcher.launch(mime)
        }
    }
}