package com.ucasoft.modernMoney.plugin.interfaces

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.IBinder
import android.util.Log
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.IImportPluginCallback
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.IImportPluginInterface
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportPluginData
import com.ucasoft.modernMoney.plugin.interfaces.imports.ImportPluginCallback
import com.ucasoft.modernMoney.plugin.interfaces.imports.ImportSource
import com.ucasoft.modernMoney.plugin.interfaces.imports.model.toModel
import java.io.ByteArrayOutputStream

actual class PluginManager(private val context: Context) {
    actual fun loadPlugins() =
        context.packageManager.queryIntentServices(Intent("com.ucasoft.modernMoney.PLUGIN"), PackageManager.GET_META_DATA).map {
            it.toPluginInfo(context)
        }

    actual fun initImportPlugin(pluginInfo: PluginInfo, source: ImportSource?, callback: ImportPluginCallback) {
        initPlugin(pluginInfo, { IImportPluginInterface.Stub.asInterface(it) }) { plugin, finish ->
            plugin.startImport(source, object : IImportPluginCallback.Stub() {
                override fun onProgress(
                    phase: String?,
                    message: String?,
                    percent: Int
                ) {
                    callback.onProgress(phase, message, percent)
                }

                override fun onDataChunk(
                    chunk: ImportPluginData?
                ) {
                    if (chunk != null) {
                        callback.onDataChunk(chunk.toModel())
                    }
                }

                override fun onCompleted() {
                    try {
                        callback.onCompleted()
                    } finally {
                        finish()
                    }
                }

                override fun onError(message: String?) {
                    try {
                        callback.onError(message ?: "Unknown error")
                    } finally {
                        finish()
                    }
                }
            })
        }
    }

    private fun <PI> initPlugin(
        pluginInfo: PluginInfo,
        fetchPluginInterface: (IBinder?) -> PI,
        invokePlugin: (PI, () -> Unit) -> Unit
    ) {
        val intent = Intent().apply {
            setClassName(pluginInfo.packageName, pluginInfo.serviceClassName)
        }

        val pluginConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                val pluginInterface = fetchPluginInterface(binder)
                invokePlugin(pluginInterface) {
                    context.unbindService(this)
                }
                Log.d("PluginManager", "Import started")
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Log.d("PluginManager", "Service disconnected: ${pluginInfo.serviceClassName}")
            }

        }
        context.bindService(intent, pluginConnection, Context.BIND_AUTO_CREATE)
    }
}

fun ResolveInfo.toPluginInfo(context: Context): PluginInfo {
    val serviceInfo = this.serviceInfo
    val metaData = serviceInfo.metaData
    val pluginType = when (metaData.getString("com.ucasoft.modernMoney.PLUGIN_TYPE")) {
        "IMPORT" -> PluginType.IMPORT
        "PARSER" -> PluginType.PARSER
        else -> throw IllegalArgumentException("Unknown plugin type: ${metaData.getString("com.ucasoft.modernMoney.PLUGIN_TYPE")}")
    }
    val icon = context.packageManager.getApplicationIcon(serviceInfo.applicationInfo)
    return PluginInfo(
        author = metaData.getString("com.ucasoft.modernMoney.PLUGIN_AUTHOR") ?: serviceInfo.packageName,
        packageName = serviceInfo.packageName,
        serviceClassName = serviceInfo.name,
        icon = icon.toByteArray(),
        title = metaData.getString("com.ucasoft.modernMoney.PLUGIN_TITLE") ?: serviceInfo.name,
        description = metaData.getString("com.ucasoft.modernMoney.PLUGIN_DESCRIPTION")
            ?: metaData.getString("com.ucasoft.modernMoney.PLUGIN_TITLE")
            ?: serviceInfo.name,
        type = pluginType
    )
}

fun Drawable.toByteArray(): ByteArray {
    val bitmap = if (this is BitmapDrawable) {
        bitmap
    } else {
        val width = if (intrinsicWidth > 0) intrinsicWidth else 1
        val height = if (intrinsicHeight > 0) intrinsicHeight else 1

        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also { bmp ->
            val canvas = Canvas(bmp)
            setBounds(0, 0, canvas.width, canvas.height)
            draw(canvas)
        }
    }

    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
