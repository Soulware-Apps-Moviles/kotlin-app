package com.soulware.tcompro.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.soulware.tcompro.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContextualHeader(
    @StringRes titleResId: Int,
    @StringRes backContentDescResId: Int = R.string.desc_go_back,
    @StringRes actionContentDescResId: Int = R.string.desc_more_options,
    onBackClick: () -> Unit,
    onActionClick: () -> Unit = {}
) {
    val headerColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val contentColor = MaterialTheme.colorScheme.onPrimary

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = headerColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor,
            navigationIconContentColor = contentColor
        ),

        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(backContentDescResId)
                )
            }
        },

        title = {
            Text(
                text = stringResource(titleResId),
                style = MaterialTheme.typography.titleLarge
            )
        },

        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(actionContentDescResId)
                )
            }
        }
    )
}