package mohamed.taha.moviestask.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import mohamed.taha.moviestask.R
import mohamed.taha.moviestask.local.MyListMovie
import mohamed.taha.moviestask.ui.sharedComposables.SearchResultItem
import mohamed.taha.moviestask.ui.theme.AppOnPrimaryColor
import mohamed.taha.moviestask.ui.theme.AppPrimaryColor
import mohamed.taha.moviestask.ui.theme.ButtonColor
import mohamed.taha.moviestask.util.Constants
import mohamed.taha.moviestask.viewmodel.SearchViewModel
import mohamed.taha.moviestask.viewmodel.WatchListViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun WatchList(
    
    watchListViewModel: WatchListViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    var totalDismissed by remember { mutableStateOf(0) }

    val myWatchList = watchListViewModel.watchList.value.collectAsState(initial = emptyList())

    LaunchedEffect(key1 = myWatchList.value.size) {

    }

    var currentList: State<List<MyListMovie>> by remember { mutableStateOf(myWatchList) }
    if (searchViewModel.searchParamState.value.isEmpty()) {
        currentList = myWatchList
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("FavoriteList")
            .background(AppPrimaryColor)
    ) {


        fun countItems(items: Int): String {
            return when (items) {
                1 -> "Found 1 item"
                0 -> "There's nothing here!"
                else -> "Found $items items"
            }
        }

        var showNumberIndicator by remember { mutableStateOf(true) }
        AnimatedVisibility(visible = showNumberIndicator) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .border(
                        width = 1.dp, color = ButtonColor,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .background(ButtonColor.copy(alpha = 0.25F))
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = countItems(currentList.value.size),
                        color = AppOnPrimaryColor
                    )
                    IconButton(onClick = { showNumberIndicator = false }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cancel),
                            tint = AppOnPrimaryColor,
                            contentDescription = "Cancel button"
                        )
                    }
                }
            }
        }

        LazyColumn() {
            items(myWatchList.value) { film ->
                SwipeToDismissItem(
                    modifier = Modifier.animateItemPlacement(),
                    onDismiss = {
                        watchListViewModel.removeFromWatchList(film.mediaId)
                        totalDismissed += 1
                        // FIXME: Find another way to fix swipe after searching
                        // searchViewModel.searchParam.value = ""
                        // searchViewModel.previousSearch.value = ""
                    }) {
                    SearchResultItem(
                        title = film.title,
                        mediaType = null,
                        posterImage = "${Constants.BASE_POSTER_IMAGE_URL}/${film.imagePath}",
                        genres = emptyList(),
                        rating = film.rating,
                        releaseYear = film.releaseDate
                    ) { }
                }
            }
        }

        var openDialog by remember { mutableStateOf(true) }
        if (totalDismissed == 3 && openDialog && currentList.value.size > 1) {
            AlertDialog(
                title = { Text(text = "Delete All") },
                text = { Text(text = "Would you like to clear your watch list?") },
                shape = RoundedCornerShape(8.dp),
                confirmButton = {
                    TextButton(onClick = {
                        watchListViewModel.deleteWatchList()
                        openDialog = openDialog.not()
                    }) {
                        Text(text = "YES")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { openDialog = openDialog.not() }) {
                        Text(text = "NO")
                    }
                },
                backgroundColor = ButtonColor,
                contentColor = AppOnPrimaryColor,
                onDismissRequest = {
                    openDialog = openDialog.not()
                })
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun SwipeToDismissItem(
    modifier: Modifier,
    onDismiss: () -> Unit,
    swippable: @Composable () -> Unit,
) {
    val dismissState = rememberDismissState(initialValue = DismissValue.Default,
        confirmStateChange = {
            if (it == DismissValue.DismissedToStart) {
                onDismiss()
            }
            true
        })

    SwipeToDismiss(
        state = dismissState,
        modifier = modifier,
        background = {
            if (dismissState.dismissDirection == DismissDirection.EndToStart) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, ButtonColor.copy(alpha = 0.25F))
                            )
                        )
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.align(Alignment.CenterEnd)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color(0xFFFF6F6F),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        },
        dismissContent = {
            swippable()
        },
        directions = setOf(DismissDirection.EndToStart)
    )
}

