package com.mielechm.pyrkontechnicaltask.features.guests.guests_list

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.mielechm.pyrkontechnicaltask.R
import com.mielechm.pyrkontechnicaltask.data.model.model.GuestItem
import com.mielechm.pyrkontechnicaltask.utils.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestsListScreen(navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.shadow(4.dp),
                title = {
                    Icon(
                        modifier = Modifier.padding(8.dp),
                        painter = painterResource(R.drawable.pyrkon_logo),
                        contentDescription = stringResource(R.string.pyrkon_logo)
                    )
                }
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) { GuestsList(navController) }
    }
}

@Composable
fun GuestsList(navController: NavController, viewModel: GuestsListViewModel = hiltViewModel()) {

    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    val error = viewModel.error.collectAsStateWithLifecycle()
    val guests = viewModel.guestItems.collectAsStateWithLifecycle()
    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val context = LocalContext.current.applicationContext

    LaunchedEffect(true) {
        viewModel.getGuestDataFromLocalFile(context as Context, "guests.json")
    }

    TextField(
        value = searchText,
        onValueChange = viewModel::onSearchTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp),
        placeholder = { Text(text = stringResource(R.string.info_search_guest)) }
    )

    if (isSearching) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    if (guests.value.isNotEmpty()) {
        viewModel.upsertGuests(guests.value)

        LazyColumn(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(16.dp)) {
            val itemCount = if (guests.value.size % 2 == 0) {
                guests.value.size / 2
            } else {
                guests.value.size / 2 + 1
            }
            items(itemCount) {
                GuestsRow(navController = navController, rowIndex = it, guests = guests.value)
            }
        }

    } else if (guests.value.isEmpty() && !isLoading.value && error.value.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp),
                text = stringResource(R.string.info_nothing_to_show)
            )
        }
    }

    if (isLoading.value) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    if (error.value.isNotEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp), text = error.value
            )
        }
    }

}

@Composable
fun GuestsRow(navController: NavController, rowIndex: Int, guests: List<GuestItem>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            GuestEntry(navController = navController, guest = guests[rowIndex * 2], modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))
            if (guests.size >= rowIndex * 2 + 2) {
                GuestEntry(navController = navController, guest = guests[rowIndex * 2 + 1], modifier = Modifier.weight(1f))
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun GuestEntry(navController: NavController, guest: GuestItem, modifier: Modifier) {
    Card(modifier = modifier.shadow(4.dp), onClick = {
        navController.navigate(Destination.GuestDetailsView(guest.name))
    }) {
        Column {
            SubcomposeAsyncImage(
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                    )
                },
                model = guest.imageUrl,
                contentDescription = guest.name,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally),
                text = guest.name
            )
        }
    }
}