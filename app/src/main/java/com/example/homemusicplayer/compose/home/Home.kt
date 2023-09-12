package com.example.homemusicplayer.compose.home

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.homemusicplayer.R
import com.example.homemusicplayer.compose.library.Library
import com.example.homemusicplayer.compose.search.SearchPage
import kotlinx.coroutines.launch


enum class HomePage(
    @StringRes val titleResId: Int,
    val icon: ImageVector
) {

    LIBRARY(R.string.library, Icons.Filled.LibraryMusic),
    SEARCH(R.string.search, Icons.Filled.Search),

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Home(
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = 1,
        initialPageOffsetFraction = 0.0f
    ) {
        2
    }

    Scaffold(bottomBar = { TabBar(pagerState = pagerState) }) {
        HomeNavBar(pagerState = pagerState, modifier.padding(top = it.calculateTopPadding()))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TabBar(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    pages: Array<HomePage> = HomePage.values()
) {

    val coroutineScope = rememberCoroutineScope()

    TabRow(
        modifier = Modifier.height(50.dp),
        selectedTabIndex = pagerState.currentPage
    ) {
        pages.forEachIndexed { index, page ->
            val title = stringResource(id = page.titleResId)
            Tab(
                selected = pagerState.currentPage == index,
                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                text = { Text(text = title) },
                icon = {
                    Icon(
                        page.icon,
                        contentDescription = title
                    )
                },
                unselectedContentColor = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeNavBar(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {


    Column(modifier) {
        HorizontalPager(

            state = pagerState,
            pageSpacing = 0.dp,
            userScrollEnabled = true,
            reverseLayout = false,
            contentPadding = PaddingValues(0.dp),
            beyondBoundsPageCount = 0,
            pageSize = PageSize.Fill,
            flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
            key = null,
            pageNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(Orientation.Horizontal)
        ) { index ->


            Scaffold(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background),
                topBar = {
                    TopAppBar(
                        backgroundColor = androidx.compose.material.MaterialTheme.colors.background,
                        elevation = 0.dp,
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Text(
                                text = when (index) {
                                    0 -> stringResource(id = R.string.library)
                                    1 -> stringResource(id = R.string.search)
                                    else -> stringResource(id = R.string.search)
                                },
                                color = androidx.compose.material.MaterialTheme.colors.onBackground,
                                style = androidx.compose.material.MaterialTheme.typography.h4
                            )
                        }
                    }
                }
            ) {

                when (index) {
                    0 -> {
                        Library(
                            modifier = Modifier
                                .padding(it)
                                .fillMaxSize()
                        )
                    }

                    1 -> {
                        SearchPage(
                            modifier = Modifier
                                .padding(it)
                                .fillMaxSize()
                        )
                    }
                }
            }
        }
    }

}