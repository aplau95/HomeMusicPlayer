package com.example.homemusicplayer.compose.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    textSearch: String,
    updateSearchQuery: (String) -> Unit
) {

    val focusManager = LocalFocusManager.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        BasicTextField(
            value = textSearch,
            onValueChange = updateSearchQuery,
            modifier = Modifier
                .height(40.dp)
                .weight(1f),
            keyboardActions = KeyboardActions(
                onSearch = { focusManager.clearFocus() }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            textStyle = TextStyle(
                fontSize = 20.sp
            )
        ) {
            TextFieldDefaults.DecorationBox(
                value = textSearch,
                innerTextField = it,
                singleLine = true,
                enabled = true,
                visualTransformation = VisualTransformation.None,
                leadingIcon = {
                    Image(
                        Icons.Filled.Search,
                        "",
                        modifier = Modifier.size(24.dp),
                    )
                },
                trailingIcon = {
                    Image(
                        Icons.Filled.Cancel,
                        "",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { updateSearchQuery("") },
                        )
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = PaddingValues(0.dp)
            )
        }

        Text(
            text = "Cancel",
            modifier = Modifier.padding(12.dp),
            fontSize = 20.sp,
            color = Color.Red

        )
    }
}
//    }

@Preview
@Composable
fun PreviewSearchBar() {

    fun updateSearch(str: String) {

    }
    Box(modifier = Modifier.background(Color.White)) {
        SearchBar(
            "Test",
            ::updateSearch
        )
    }


}