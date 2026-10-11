package com.ucasoft.modernMoney.plugin.interfaces.aidl.imports;

import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportPluginData;

interface IImportPluginCallback {
    void onProgress(String phase, String message, int percent);
    void onDataChunk(in ImportPluginData chunk);
    void onCompleted();
    void onError(String message);
}
