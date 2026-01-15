package com.example.product.screen.home.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun CategoryRow(
    categories: List<String>,
    selectedCategory: String?,
    onCategoryClick: (String?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {
            CategoryChip(
                text = "All",
                isSelected = selectedCategory == null
            ) { onCategoryClick(null) }
        }

        items(categories) { category ->
            CategoryChip(
                text = category,
                isSelected = selectedCategory == category
            ) {
                onCategoryClick(category)
            }
        }
    }
}
