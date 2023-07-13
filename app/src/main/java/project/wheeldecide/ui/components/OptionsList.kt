package project.wheeldecide.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import project.wheeldecide.R
import project.wheeldecide.model.DecideOption
import project.wheeldecide.ui.theme.WheelDecideTheme

@Composable
fun OptionsList(
    modifier: Modifier,
    options: List<DecideOption>,
    deletable: Boolean,
    onDelete: (index: Int) -> Unit,
) {
    if (options.isEmpty()) {
        Text("no options :(", modifier.padding(all = 20.dp))
    } else {
        LazyColumn(modifier) {
            items(options.size) {
                OptionListItem(options[it], deletable) {
                    onDelete(it)
                }
                if (it == options.lastIndex) {
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun OptionListItem(option: DecideOption, deletable: Boolean, onDelete: () -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
    ) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text(
                option.title,
                Modifier
                    .padding(vertical = 12.dp)
                    .padding(start = 12.dp)
                    .weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (deletable) {
                Icon(
                    painterResource(id = R.drawable.outline_delete_outline_24),
                    "",
                    Modifier
                        .padding(all = 12.dp)
                        .size(ButtonDefaults.IconSize)
                        .clickable { onDelete() },
                    Color.Red
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OptionsListPreview() {
    WheelDecideTheme {
        OptionsList(Modifier, options = listOf(), true, onDelete = {})
    }
}