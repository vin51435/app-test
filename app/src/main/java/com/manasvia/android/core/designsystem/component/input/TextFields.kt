package com.manasvia.android.core.designsystem.component.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Standard Form Text Field.
 * Standardizes rounded corners, supporting error text, clear button, and label placement.
 */
@Composable
fun PrimaryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    errorMessage: String? = null,
    isError: Boolean = errorMessage != null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    shape: Shape = RoundedCornerShape(12.dp)
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it) } },
        isError = isError,
        supportingText = errorMessage?.let {
            { Text(text = it, color = MaterialTheme.colorScheme.error) }
        },
        enabled = enabled,
        readOnly = readOnly,
        singleLine = singleLine,
        maxLines = maxLines,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        shape = shape,
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * Standard Search Bar Input.
 * Uses pill shape, search icon, and clear action.
 */
@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    onClearClick: () -> Unit = { onValueChange("") },
    leadingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        leadingIcon = leadingIcon ?: { Text("🔍") },
        trailingIcon = if (value.isNotEmpty()) {
            {
                IconButton(onClick = onClearClick) {
                    Text("✕")
                }
            }
        } else null,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier.fillMaxWidth()
    )
}
