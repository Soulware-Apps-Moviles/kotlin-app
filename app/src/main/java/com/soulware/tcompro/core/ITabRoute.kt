package com.soulware.tcompro.core

import androidx.annotation.StringRes

interface ITabRoute {
    val route: String
    @get:StringRes
    val labelResId: Int
}