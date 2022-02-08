package dev.sijanrijal.popularmovies.screens.search.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.MotionLayout

private const val searchIconId = "searchIcon"
private const val textFieldId = "textField"

@Composable
internal fun SearchBar(
    modifier: Modifier = Modifier,
    onSearchTextChange: (String) -> Unit
) {
    var isClicked by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (isClicked) 1f else 0f,
        animationSpec = tween(500)
    )
    var searchText by remember { mutableStateOf("") }
    val constraintSetStart = ConstraintSet(
        """{
        $searchIconId: {
            end: ['parent', 'end', 16],
            top: ['$textFieldId', 'top', 0],
            bottom: ['$textFieldId', 'bottom', 0],
            width: 'wrap',
            height: 'wrap'
        },
        $textFieldId: {
            start: ['$searchIconId', 'end', 16],
            end: ['parent', 'end', 16],
            top: ['parent', 'top', 4],
            alpha: 0.0,
            width: 'spread',
            height: 'wrap'
        }
    }"""
    )
    val constraintSetEnd = ConstraintSet(
        """ {
        $searchIconId: {
            start: ['parent', 'end', 16],
            top: ['$textFieldId', 'top', 0],
            bottom: ['$textFieldId', 'bottom', 0],
            width: 'wrap',
            height: 'wrap',
            alpha: 0.0
        },
        $textFieldId: {
            start: ['parent', 'start', 16],
            end: ['parent', 'end', 16],
            top: ['parent', 'top', 4],
            alpha: 1.0,
            width: 'spread',
            height: 'wrap'
        }
    }"""
    )
    MotionLayout(
        start = constraintSetStart,
        end = constraintSetEnd,
        progress = progress,
        modifier = modifier
    ) {
        Icon(imageVector = Icons.Default.Search, contentDescription = "Search", modifier = Modifier
            .layoutId(searchIconId)
            .clickable { isClicked = !isClicked })
        SearchTextField(
            searchText = searchText,
            onSearchTextChange = { text ->
                searchText = text
                onSearchTextChange(searchText)
            }, onClearTextIconClicked = {
                searchText = ""
                onSearchTextChange("")
                isClicked = !isClicked
            })

    }
}

@Composable
private fun SearchTextField(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onClearTextIconClicked: () -> Unit
) {
    BasicTextField(
        modifier = Modifier.layoutId(textFieldId),
        maxLines = 1,
        value = searchText,
        onValueChange = {
            onSearchTextChange(it)
        }, decorationBox = { innerTextField ->
            Column {
                Row {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f)
                    ) {
                        innerTextField()
                    }
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        modifier = Modifier.clickable { onClearTextIconClicked() })
                }
                Divider(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(color = Color.Blue, shape = RoundedCornerShape(40))
                )
            }
        })
}