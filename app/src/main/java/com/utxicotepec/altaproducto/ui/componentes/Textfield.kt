package com.utxicotepec.altaproducto.ui.componentes

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.utxicotepec.altaproducto.R

/**
 * Campo de texto reutilizable con icono, contador de caracteres,
 * validación de obligatoriedad y mensaje de error.
 */
@Composable
fun AntTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    @DrawableRes iconRes: Int,
    maxLength: Int,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1,
    isRequired: Boolean = true,
    errorMessage: String? = null,
    forceValidation: Boolean = false
) {
    var hadFocus by remember { mutableStateOf(false) }
    var touched by remember { mutableStateOf(false) }

    val requiredError = isRequired && (touched || forceValidation) && value.isBlank()
    val isError = requiredError || errorMessage != null
    val supporting = when {
        requiredError -> stringResource(R.string.supporting_required)
        errorMessage != null -> errorMessage
        else -> ""
    }

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            val filtered = if (keyboardType == KeyboardType.Number) {
                newValue.filter { it.isDigit() }
            } else newValue
            if (filtered.length <= maxLength) onValueChange(filtered)
            touched = true
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (it.isFocused) hadFocus = true
                else if (hadFocus) touched = true
            },
        label = { Text(label) },
        leadingIcon = { Icon(painterResource(iconRes), contentDescription = null) },
        isError = isError,
        singleLine = singleLine,
        minLines = minLines,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        supportingText = {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = supporting,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
                Text(text = "${value.length}/$maxLength")
            }
        }
    )
}

/** Campo que no se escribe: al tocarlo abre el calendario. */
@Composable
fun AntTfDate(
    value: String,
    label: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    forceValidation: Boolean = false
) {
    val isError = forceValidation && value.isBlank()
    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label) },
            leadingIcon = { Icon(painterResource(iconRes), contentDescription = null) },
            isError = isError,
            singleLine = true,
            supportingText = {
                Text(
                    text = if (isError) stringResource(R.string.supporting_required) else "",
                    color = MaterialTheme.colorScheme.error
                )
            }
        )
        // Capa transparente que captura el toque
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { onClick() }
        )
    }
}
