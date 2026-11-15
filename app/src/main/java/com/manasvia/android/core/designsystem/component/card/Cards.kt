package com.manasvia.android.core.designsystem.component.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Standard Surface Card with customizable padding and shape.
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(16.dp),
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    contentPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            colors = colors,
            elevation = elevation,
            border = border
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                content = content
            )
        }
    } else {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            colors = colors,
            elevation = elevation,
            border = border
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                content = content
            )
        }
    }
}

/**
 * Standard Outlined Surface Card.
 */
@Composable
fun OutlinedAppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(16.dp),
    contentPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick != null) {
        OutlinedCard(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            shape = shape
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                content = content
            )
        }
    } else {
        OutlinedCard(
            modifier = modifier.fillMaxWidth(),
            shape = shape
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                content = content
            )
        }
    }
}

/**
 * Standard Elevated Surface Card with shadow.
 */
@Composable
fun ElevatedAppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(16.dp),
    elevation: Dp = 4.dp,
    contentPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick != null) {
        ElevatedCard(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = elevation)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                content = content
            )
        }
    } else {
        ElevatedCard(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = elevation)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                content = content
            )
        }
    }
}
