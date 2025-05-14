package com.mielechm.pyrkontechnicaltask.features.guests.guests_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.mielechm.pyrkontechnicaltask.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestDetailsScreen(name: String, navController: NavController) {
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
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.info_go_back)
                        )
                    }
                }
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            GuestDetails(name)
        }
    }
}

@Composable
fun GuestDetails(name: String, viewModel: GuestDetailsViewModel = hiltViewModel()) {

    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    val guest = viewModel.guest.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.getGuestDetails(name)
    }

    if (isLoading.value) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                )
            },
            model = guest.value.imageUrl,
            contentDescription = guest.value.name
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = guest.value.name,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            text = guest.value.summary
        )
    }
}