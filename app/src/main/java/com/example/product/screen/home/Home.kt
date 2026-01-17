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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.product.repo.model.ProductResponse
import com.example.product.screen.home.views.CategoryRow
import com.example.product.screen.home.views.TwoColumnGrid

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(
    uiState: HomeState,
    padding: PaddingValues = PaddingValues(15.dp),
    onRefresh: () -> Unit,
    onCategoriesClicked: (String?) -> Unit,
    onCardClicked: (ProductResponse) -> Unit = {}
) {


    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = { onRefresh.invoke() }
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
                    onCategoriesClicked.invoke(it)
                }
            )

            TwoColumnGrid(uiState.filteredList, onCardClicked = onCardClicked)
        }

        // Pull-to-refresh indicator (top)
        PullRefreshIndicator(
            refreshing = uiState.isRefreshing,
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
