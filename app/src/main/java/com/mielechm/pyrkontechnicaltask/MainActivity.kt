package com.mielechm.pyrkontechnicaltask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mielechm.pyrkontechnicaltask.features.guests.guests_details.GuestDetailsScreen
import com.mielechm.pyrkontechnicaltask.features.guests.guests_list.GuestsListScreen
import com.mielechm.pyrkontechnicaltask.ui.theme.PyrkonTechnicalTaskTheme
import com.mielechm.pyrkontechnicaltask.utils.Destination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PyrkonTechnicalTaskTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Destination.GuestsListView
                ) {
                    composable<Destination.GuestsListView> {
                        GuestsListScreen(navController)
                    }
                    composable<Destination.GuestDetailsView> {
                        val arguments = it.toRoute<Destination.GuestDetailsView>()
                        GuestDetailsScreen(arguments.name, navController)
                    }
                }
            }
        }
    }
}

