package com.example.product.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.product.theme.DarkGrey
import com.example.product.theme.LavenderBlush

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    title: String = "Products",
    showBackArrow: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    snackbarMessage: String? = null, // New parameter to handle Snackbar
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val scaffoldState_ = rememberScaffoldState()

    // Show the Snackbar when snackbarMessage is provided
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            scaffoldState_.snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short // Snackbar disappears after a short duration
            )
        }
    }

    Scaffold(
        scaffoldState = scaffoldState_,
        snackbarHost = { it ->
            SnackbarHost(
                modifier = Modifier.padding(bottom = 50.dp),
                hostState = scaffoldState_.snackbarHostState
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    if (showBackArrow && onBackClick != null) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LavenderBlush,
                    titleContentColor = DarkGrey
                ),
                actions = actions
            )
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}
