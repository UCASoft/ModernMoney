package com.ucasoft.modernMoney.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.ui.graphics.ImageBitmap
import com.ucasoft.components.treeview.TreeViewNode
import com.ucasoft.komm.abstractions.KOMMContextResolver
import com.ucasoft.komm.annotations.*
import com.ucasoft.modernMoney.ui.toImageBitmap
import com.ucasoft.modern_money.shared.generated.resources.Res
import com.ucasoft.modern_money.shared.generated.resources.allDrawableResources
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.jetbrains.compose.resources.getSystemResourceEnvironment
import com.ucasoft.modernMoney.db.model.Category as DbCategory

@KOMMMap(
    from = [DbCategory::class],
    to = [DbCategory::class],
    context = CategoryMapContext::class,
    config = MapConfiguration(
        allowNotNullAssertion = false,
        tryAutoCast = true,
        mapDefaultAsFallback = false,
        nullableContext = true,
        convertFunctionName = ""
    )
)
@MapTargetDefault(
    "parentId",
    MapDefault(CategoryParentIdResolver::class),
)
data class Category(
    override val name: String,
    val buildInLogoCode: String? = null,
    @MapFunction("com.ucasoft.modernMoney.ui", "")
    val uploadLogo: ImageBitmap? = null,
) : TreeViewNode<Long>, KeyEntity<Long>, LogoEntity {

    var id: Long = 0L
        internal set

    override val logo: ImageBitmap?
        get() = uploadLogo ?: Res.allDrawableResources[buildInLogoCode]?.let {
            runBlocking {
                getDrawableResourceBytes(getSystemResourceEnvironment(), it)
            }.toImageBitmap()
        }

    val icon: ImageBitmap
        get() = logo ?: Icons.Default.Category.toImageBitmap()

    override val children = mutableListOf<Category>()

    override val key : Long
        get() = id

    fun addChild(category: Category) {
        children.add(category)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Category

        if (name != other.name) return false
        if (icon != other.icon) return false
        if (id != other.id) return false
        if (children != other.children) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + icon.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + children.hashCode()
        return result
    }
}

class CategoryParentIdResolver(category: DbCategory?, context: CategoryMapContext) : KOMMContextResolver<CategoryMapContext, DbCategory?, Long?>(category, context) {
    override fun resolve() = context.parentId
}

data class CategoryMapContext(
    val parentId: Long?
)