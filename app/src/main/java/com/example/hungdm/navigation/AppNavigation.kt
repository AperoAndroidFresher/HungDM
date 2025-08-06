package com.example.hungdm.navigation

import android.app.Activity
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.hungdm.mvi.MviEvent
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviViewModel
import com.example.hungdm.screen.home.HomeScreen
import com.example.hungdm.screen.library.LibraryScreen
import com.example.hungdm.screen.login.LoginScreen
import com.example.hungdm.screen.playlistdetail.PlaylistDetailScreen
import com.example.hungdm.screen.playlist.PlaylistScreen
import com.example.hungdm.screen.profile.ProfileScreen
import com.example.hungdm.screen.signup.SignupScreen
import com.example.hungdm.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val viewModel: MviViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val bottomItem = listOf(
        BottomItem("Home", Icons.Default.Home, Destination.Home),
        BottomItem("Library", Icons.Default.DateRange, Destination.Library),
        BottomItem("Playlist", Icons.Default.PlayArrow, Destination.Playlist)
    )

    LaunchedEffect(Unit) {
        viewModel.event.collect{e->
            when(e){
                is MviEvent.GotoLogin ->{
                    viewModel.add(Destination.Login)
                }
                is MviEvent.GotoSignup ->{
                    viewModel.add(Destination.Signup)
                }
                is MviEvent.GotoHome->{
                    viewModel.add(Destination.Home)
                }
                is MviEvent.GotoProfile ->{
                    viewModel.add(Destination.Profile)
                }
                is MviEvent.GotoPlaylist ->{
                    viewModel.replace(Destination.Playlist)
                }
                is MviEvent.GotoPlaylistDetail->{
                    viewModel.add(Destination.PlaylistDetail(e.playlistId))
                }
                is MviEvent.ShowToast -> {
                    Toast.makeText(context, e.mess, LENGTH_SHORT).show()
                }
            }
        }
    }

    AppTheme (
        darkTheme = state.darkTheme,
        dynamicColor = false
    ){
        Scaffold(
            bottomBar = {
                val showBottomNav = state.backStack.lastOrNull()?.let {
                    it !is Destination.Login && it !is Destination.Signup && it !is Destination.Profile
                } ?: false

                if (showBottomNav) {
                    NavigationBar(
                        windowInsets = NavigationBarDefaults.windowInsets,
                        containerColor = colorScheme.background
                    ) {
                        bottomItem.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = state.selectedBottomBar==index,
                                onClick = {
                                    viewModel.processIntent(MviIntent.OnClickItemBottomBar(index))
                                    viewModel.replace(item.destination)
                                },
                                icon = { Icon(item.icon,null) },
                                label = { Text(item.label, color = colorScheme.primary) }
                            )
                        }
                    }
                }
            }
        ) { p ->
            NavDisplay(
                modifier = Modifier.padding(p),
                backStack = state.backStack,
                onBack = { viewModel.removeLast() },
                entryProvider = entryProvider {
                    entry<Destination.Login> {
                        LoginScreen(
                            viewModel = viewModel
                        )
                    }
                    entry<Destination.Signup> {
                        SignupScreen(
                            viewModel = viewModel
                        )
                    }
                    entry<Destination.Home> {
                        val activity = context as? Activity
                        HomeScreen(
                            viewModel = viewModel,
                            onBack = {
                                activity?.finish()
                            }
                        )
                    }
                    entry<Destination.Profile> {
                        ProfileScreen(
                            viewModel = viewModel,
                        )
                    }
                    entry<Destination.Library> {
                        val activity = context as? Activity
                        LibraryScreen(
                            viewModel = viewModel,
                            onBack = {
                                activity?.finish()
                            }
                        )
                    }
                    entry<Destination.Playlist> {
                        val activity = context as? Activity
                        PlaylistScreen(
                            viewModel = viewModel,
                            onBack = {
                                activity?.finish()
                            }
                        )
                    }
                    entry<Destination.PlaylistDetail>{ destination ->
                        PlaylistDetailScreen(
                            viewModel = viewModel,
                            destination = destination
                        )
                    }
                }
            )
        }
    }
}

data class BottomItem(
    var label: String,
    var icon: ImageVector,
    val destination: Destination
)