package com.example.hungdm.navigation

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.hungdm.mvi.MviEvent
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviViewModel
import com.example.hungdm.screen.HomeScreen
import com.example.hungdm.screen.LibraryScreen
import com.example.hungdm.screen.LoginScreen
import com.example.hungdm.screen.PlaylistDetailScreen
import com.example.hungdm.screen.PlaylistScreen
import com.example.hungdm.screen.ProfileScreen
import com.example.hungdm.screen.SignupScreen
import com.example.hungdm.ui.theme.AppTheme

data class BottomItem(
    var label: String,
    var icon: ImageVector,
    val destination: Destination
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val viewModel: MviViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val bottomItem = listOf(
        BottomItem("Home", Icons.Default.Home, Destination.Home),
        BottomItem("Library", Icons.Default.DateRange, Destination.Library),
        BottomItem("Playlist", Icons.Default.PlayArrow, Destination.Playlist)
    )

    LaunchedEffect(Unit) {
        if(!state.isLoadSong){
            viewModel.processIntent(MviIntent.LoadSong(context))
        }
    }

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
                    viewModel.add(Destination.PlaylistDetail)
                }
            }
        }
    }


    AppTheme (
        darkTheme = state.darkTheme,
        dynamicColor = false
    ){
        Scaffold(
//            topBar = {
//                val showTopNav = state.backStack.lastOrNull()?.let {
//                    it !is Destination.Login && it !is Destination.Signup && it !is Destination.Profile && it !is Destination.PlaylistDetail
//                } ?: false
//
//                if (showTopNav) {
//                    TopAppBar(
//                        colors = TopAppBarDefaults.topAppBarColors(
//                            containerColor = colorScheme.background
//                        ),
//                        title = {
//                            when (state.selectedBottomBar) {
//                                0 -> {
//                                    Text("Home", color = colorScheme.primary)
//                                }
//
//                                1 -> {
//                                    Text("Library", color = colorScheme.primary)
//                                }
//
//                                2 -> {
//                                    Text("Playlist", color = colorScheme.primary)
//                                }
//                            }
//                        },
//                        actions = {
//                            if(state.selectedBottomBar ==2){
//                                IconButton(
//                                    onClick = { viewModel.processIntent(MviIntent.ShowCreatePlaylistDialod) }
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.Default.Add,
//                                        contentDescription = null,
//                                        modifier = Modifier.size(20.dp),
//                                    )
//                                }
//                            }
//
//                            IconButton(
//                                onClick = { viewModel.processIntent(MviIntent.ChangeTheme) }
//                            ) {
//                                Icon(
//                                    painter = painterResource(if(state.darkTheme) R.drawable.light else R.drawable.dark),
//                                    contentDescription = null,
//                                    modifier = Modifier.size(20.dp),
//                                )
//                            }
//                            IconButton(
//                                onClick = { viewModel.processIntent(MviIntent.OnClickProfile) }
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.AccountCircle,
//                                    contentDescription = null,
//                                    modifier = Modifier.size(20.dp),
//                                )
//                            }
//                        }
//                    )
//                }
//            },
            bottomBar = {
                val showBottomNav = state.backStack.lastOrNull()?.let {
                    it !is Destination.Login && it !is Destination.Signup && it !is Destination.Profile && it !is Destination.PlaylistDetail
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

                    entry<Destination.PlaylistDetail>{
                        PlaylistDetailScreen(
                            viewModel = viewModel,
                            onBack = {
                                viewModel.removeLast()
                            }
                        )
                    }
                }
            )
        }
    }
}