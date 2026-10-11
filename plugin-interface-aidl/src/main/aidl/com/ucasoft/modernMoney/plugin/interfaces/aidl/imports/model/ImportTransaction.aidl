package com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model;

import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportAccountAmount;
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportPayeeAmount;

parcelable ImportTransaction {
    long date;
    @nullable ImportAccountAmount expenseAmount;
    @nullable ImportAccountAmount incomeAmount;
    @nullable String payeeId;
    @nullable ImportPayeeAmount payeeAmount;
    @nullable String categoryId;
    @nullable String comment;
}
