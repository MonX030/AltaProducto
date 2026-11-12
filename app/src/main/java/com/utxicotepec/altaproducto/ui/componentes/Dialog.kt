package com.utxicotepec.altaproducto.ui.componentes

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.utxicotepec.altaproducto.R

@Composable
fun AntDialogInfo(
    message: String,
    onClean: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClean,
        title = { Text(stringResource(R.string.dialog_title)) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onClean) {
                Text(stringResource(R.string.dialog_clean))
            }
        }
    )
}
