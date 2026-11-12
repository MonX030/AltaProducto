package com.utxicotepec.altaproducto

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.utxicotepec.altaproducto.ui.componentes.AntDialogInfo
import com.utxicotepec.altaproducto.ui.componentes.AntDropdown
import com.utxicotepec.altaproducto.ui.componentes.AntTextField
import com.utxicotepec.altaproducto.ui.componentes.AntTfDate
import com.utxicotepec.altaproducto.ui.componentes.DatePickerModal
import kotlinx.coroutines.launch

@Composable
fun MainView(
    onSave: (Producto) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val profiles = stringArrayResource(R.array.profiles).toList()
    val nameMax = integerResource(R.integer.name_max_length)
    val priceMin = integerResource(R.integer.height_min_value)
    val priceMaxLength = integerResource(R.integer.height_max_length)
    val notesMax = integerResource(R.integer.notes_max_length)

    var formKey by remember { mutableIntStateOf(0) }
    var nombreValue by remember { mutableStateOf("") }
    var marcaValue by remember { mutableStateOf("") }
    var precioValue by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var dateValue by remember { mutableStateOf<Long?>(null) }
    var selectedOption by remember { mutableStateOf("") }
    var descripcionValue by remember { mutableStateOf("") }
    var isAgree by remember { mutableStateOf(false) }
    var inProgress by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var savedProducto by remember { mutableStateOf<Producto?>(null) }

    val saveFun: () -> Unit = {
        if (foundErrors(nombreValue, marcaValue, precioValue, dateValue, selectedOption, priceMin)) {
            submitted = true
            Toast.makeText(context, R.string.form_invalid, Toast.LENGTH_LONG).show()
        } else {
            inProgress = true
            scope.launch {
                fakeWait()
                val producto = Producto(
                    nombreValue.trim(),
                    marcaValue.trim(),
                    precioValue.toIntOrNull() ?: 0,
                    dateValue ?: 0,
                    selectedOption,
                    descripcionValue.trim()
                )
                inProgress = false
                savedProducto = producto
                onSave(producto)
                showDialog = true
            }
        }
    }

    val cleanForm: () -> Unit = {
        nombreValue = ""
        marcaValue = ""
        precioValue = ""
        dateValue = null
        selectedOption = ""
        descripcionValue = ""
        isAgree = false
        submitted = false
        showDialog = false
        savedProducto = null
        formKey++
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(dimensionResource(R.dimen.common_padding_default)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.common_padding_micro))
        ) {
            Text(
                text = stringResource(R.string.form_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.common_padding_min))
            )

            key(formKey) {
                AntTextField(
                    value = nombreValue,
                    onValueChange = { nombreValue = it },
                    label = stringResource(R.string.hint_name),
                    iconRes = R.drawable.ic_person,
                    maxLength = nameMax,
                    forceValidation = submitted
                )

                AntTextField(
                    value = marcaValue,
                    onValueChange = { marcaValue = it },
                    label = stringResource(R.string.hint_surname),
                    iconRes = R.drawable.ic_person,
                    maxLength = nameMax,
                    forceValidation = submitted
                )

                val priceError: String? =
                    if (precioValue.isNotEmpty() && (precioValue.toIntOrNull() ?: 0) < priceMin)
                        stringResource(R.string.error_min_height_valid)
                    else null

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.common_padding_min))
                ) {
                    AntTextField(
                        value = precioValue,
                        onValueChange = { precioValue = it },
                        label = stringResource(R.string.hint_height),
                        iconRes = R.drawable.ic_height,
                        maxLength = priceMaxLength,
                        keyboardType = KeyboardType.Number,
                        errorMessage = priceError,
                        forceValidation = submitted,
                        modifier = Modifier.weight(1f)
                    )

                    AntTfDate(
                        value = dateValue?.let { convertMillisToDate(it) } ?: "",
                        label = stringResource(R.string.hint_birth_date),
                        iconRes = R.drawable.ic_calendar_today,
                        onClick = { showDatePicker = true },
                        forceValidation = submitted,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (showDatePicker) {
                    DatePickerModal(
                        onDateSelected = { dateValue = it },
                        onDismiss = { showDatePicker = false }
                    )
                }

                AntDropdown(
                    selected = selectedOption,
                    options = profiles,
                    onSelected = { selectedOption = it },
                    label = stringResource(R.string.section_occupation),
                    forceValidation = submitted
                )

                AntTextField(
                    value = descripcionValue,
                    onValueChange = { descripcionValue = it },
                    label = stringResource(R.string.hint_notes),
                    iconRes = R.drawable.ic_notes,
                    maxLength = notesMax,
                    singleLine = false,
                    minLines = 3,
                    isRequired = false
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = dimensionResource(R.dimen.rb_row_height)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isAgree,
                    onCheckedChange = { isAgree = it }
                )
                Text(text = stringResource(R.string.cb_agree))
            }

            Spacer(Modifier.height(dimensionResource(R.dimen.common_padding_min)))

            Button(
                onClick = saveFun,
                enabled = isAgree && !inProgress,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(painterResource(R.drawable.ic_check), contentDescription = null)
                Text(
                    text = stringResource(R.string.btn_register),
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.common_padding_min))
                )
            }
        }

        if (inProgress) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.progress_background))
                    .pointerInput(Unit) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (showDialog) {
            savedProducto?.let {
                AntDialogInfo(
                    message = productoFormatter(it),
                    onClean = cleanForm
                )
            }
        }
    }
}
