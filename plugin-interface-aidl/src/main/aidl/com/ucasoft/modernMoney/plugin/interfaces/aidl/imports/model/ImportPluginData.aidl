package com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model;

import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportAccount;
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportAccountCurrency;
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportBank;
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportCard;
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportCategory;
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportPayee;
import com.ucasoft.modernMoney.plugin.interfaces.aidl.imports.model.ImportTransaction;

parcelable ImportPluginData {
    int sequenceNumber;
    List<ImportBank> banks;
    List<ImportAccount> accounts;
    List<ImportAccountCurrency> accountCurrencies;
    List<ImportCard> cards;
    List<ImportCategory> categories;
    List<ImportPayee> payees;
    List<ImportTransaction> transactions;
}
