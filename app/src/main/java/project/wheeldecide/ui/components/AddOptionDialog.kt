package project.wheeldecide.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddOptionDialog(
    shown: Boolean,
    onOptionAdded: (String) -> Unit,
    onCancel: () -> Unit,
) {
    if (!shown) {
        return
    }

    Dialog(onCancel, DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(Modifier.fillMaxSize(), color = Color.Black.copy(alpha = 0.5f)) {
            Column(verticalArrangement = Arrangement.Center) {
                AddOptionDialogContent(onOptionAdded, onCancel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOptionDialogContent(onConfirm: (String) -> Unit, onCancel: () -> Unit) {
    val optionName = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    ElevatedCard(Modifier.padding(horizontal = 40.dp)) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(all = 20.dp)
        ) {
            Text("name new option", Modifier.padding(all = 8.dp))
            OutlinedTextField(
                optionName.value, { optionName.value = it },
                Modifier.focusRequester(focusRequester)
            )
            Row {
                TextButton({ onConfirm(optionName.value) }) {
                    Text("confirm")
                }
                TextButton(onCancel) {
                    Text("cancel", color = Color.Red)
                }
            }
        }
    }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}
