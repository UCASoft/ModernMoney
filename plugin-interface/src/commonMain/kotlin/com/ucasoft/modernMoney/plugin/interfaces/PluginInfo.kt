package com.ucasoft.modernMoney.plugin.interfaces

data class PluginInfo(
    val author: String,
    val packageName: String,
    val serviceClassName: String,
    val icon: ByteArray,
    val title: String,
    val description: String,
    val type: PluginType
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as PluginInfo

        if (author != other.author) return false
        if (packageName != other.packageName) return false
        if (serviceClassName != other.serviceClassName) return false
        if (!icon.contentEquals(other.icon)) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = author.hashCode()
        result = 31 * result + packageName.hashCode()
        result = 31 * result + serviceClassName.hashCode()
        result = 31 * result + icon.contentHashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}

