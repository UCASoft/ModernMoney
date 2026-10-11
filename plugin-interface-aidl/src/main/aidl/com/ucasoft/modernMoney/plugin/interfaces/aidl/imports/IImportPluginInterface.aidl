package com.ucasoft.modernMoney.plugin.interfaces.aidl.imports;

import android.os.ParcelFileDescriptor;
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.IImportPluginCallback;

interface IImportPluginInterface {
    oneway void startImport(in ParcelFileDescriptor source, IImportPluginCallback callback);
    void cancelImport();
}
