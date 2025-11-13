package com.soulware.tcompro.features.inventory.domain.model

import androidx.annotation.StringRes
import com.soulware.tcompro.R

enum class ProductCategory(@StringRes val categoryName: Int) {
    PRODUCE(R.string.category_produce),
    DAIRY(R.string.category_dairy),
    MEAT(R.string.category_meat),
    BEVERAGES(R.string.category_beverages),
    HOUSEHOLD(R.string.category_house),
    GROCERY(R.string.category_grocery),
    SNACKS(R.string.category_snacks),
    PET(R.string.category_pet),
    OTHER(R.string.category_other)
}

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val category: ProductCategory,
    val stock: Int,
    val imageUrl: String? = null
)