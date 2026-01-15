package com.example.product.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.product.repo.model.ProductResponse
import com.example.product.screen.home.views.CategoryRow
import com.example.product.screen.home.views.TwoColumnGrid

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(
    viewModel: HomeViewModel = hiltViewModel(),
    padding: PaddingValues = PaddingValues(15.dp),
    onCardClicked: (ProductResponse) -> Unit = {}
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()


    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = { viewModel.trySendAction(HomeAction.OnRefreshProduct) }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {

        // Screen content (respects Scaffold padding)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 10.dp)
        ) {
            CategoryRow(
                categories = uiState.categories,
                selectedCategory = uiState.selectedCategory,
                onCategoryClick = {
                    viewModel.trySendAction(
                        HomeAction.OnCategoriesClicked(it)
                    )
                }
            )

            TwoColumnGrid(uiState.filteredList, onCardClicked = onCardClicked)
        }

        // Pull-to-refresh indicator (top)
        PullRefreshIndicator(
            refreshing = uiState.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Full-screen loading spinner (true center)
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.Gray,
                strokeWidth = 3.dp
            )
        }
    }

}
