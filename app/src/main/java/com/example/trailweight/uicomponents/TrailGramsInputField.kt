package com.auroralabs.trailweight.uicomponents

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType

/**
 * Composable function that displays a waymark input field.
 * @param value The current value of the input field.
 * @param onValueChange The action to perform when the value changes.
 * @param label The label to display above the input field.
 * @param modifier The modifier to apply to the input field.
 * @param imeAction The IME action to use for the input field.
 * @param keyboardActions The keyboard actions to use for the input field.
 * @param isPassword Whether the input field is a password field.
 * @param visualTransformation The visual transformation to apply to the input field.
 * @param trailingIcon The trailing icon to display with the input field.
 * @param minLines The minimum number of lines to display in the input field.
 * @param keyboardType The keyboard type to use for the input field.
 * @param textStyle The text style to apply to the input field.
 */
@Composable
fun TrailWeightInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isPassword: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    minLines: Int = 1,
    textStyle: TextStyle = TextStyle(fontSize = 16.sp),
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = false
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
        keyboardActions = keyboardActions,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = trailingIcon,
        minLines = minLines,
        textStyle = textStyle,
        singleLine = singleLine,
    )
}