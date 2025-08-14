package com.example.hungdm.navigation

import android.app.Activity
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.hungdm.R
import com.example.hungdm.component.Splash
import com.example.hungdm.screen.SettingScreen
import com.example.hungdm.screen.component.PlayerBottomBar
import com.example.hungdm.mvi.MviEvent
import com.example.hungdm.mvi.MviViewModel
import com.example.hungdm.screen.home.HomeScreen
import com.example.hungdm.screen.library.LibraryScreen
import com.example.hungdm.screen.login.LoginScreen
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.screen.player.PlayerScreen
import com.example.hungdm.screen.playlistdetail.PlaylistDetailScreen
import com.example.hungdm.screen.playlist.PlaylistScreen
import com.example.hungdm.screen.profile.ProfileScreen
import com.example.hungdm.screen.signup.SignupScreen
import com.example.hungdm.screen.TopAlbumsScreen
import com.example.hungdm.screen.TopArtistsScreen
import com.example.hungdm.screen.TopTracksScreen
import com.example.hungdm.ui.theme.AppTheme
import com.example.hungdm.utils.AppUtils
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val viewModel: MviViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val backStack = remember { mutableStateListOf<Destination>(Destination.Splash) }
    val context = LocalContext.current
    val activity = context as? Activity

    var selectedBottomBar by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        val userId = AppUtils.getUser(context)

        if (userId != null) {
            viewModel.processIntent(MviIntent.GetUser(userId))
            delay(2000)
            backStack.clear()
            backStack.add(Destination.Home)
        } else{
            delay(2000)
            backStack.clear()
            backStack.add(Destination.Login)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { e ->
            when (e) {
                is MviEvent.GotoLogin -> {
                    backStack.clear()
                    backStack.add(Destination.Login)
                }

                is MviEvent.GotoSignup -> {
                    backStack.add(Destination.Signup)
                }

                is MviEvent.GotoHome -> {
                    backStack.add(Destination.Home)
                }

                is MviEvent.GotoProfile -> {
                    backStack.add(Destination.Profile)
                }

                is MviEvent.GotoPlaylistDetail -> {
                    backStack.add(Destination.PlaylistDetail(e.playlistId))
                }

                is MviEvent.GotoTopAlbums -> {
                    backStack.add(Destination.TopAlbums)
                }

                is MviEvent.GotoTopTracks -> {
                    backStack.add(Destination.TopTracks)
                }

                is MviEvent.GotoTopArtists -> {
                    backStack.add(Destination.TopArtists)
                }

                is MviEvent.GotoSettings -> {
                    backStack.add(Destination.Settings)
                }

                is MviEvent.ShowToast -> {
                    Toast.makeText(context, e.mess, LENGTH_SHORT).show()
                }
            }
        }
    }

    AppTheme(
        darkTheme = state.darkTheme,
        dynamicColor = false
    ) {
        Scaffold(
            bottomBar = {
                val showBottomBar = backStack.lastOrNull()?.let {
                    it is Destination.Home || it is Destination.Playlist ||
                            it is Destination.Library || it is Destination.PlaylistDetail
                } ?: false

                if (showBottomBar) {
                    val bottomItem = listOf(
                        BottomItem(stringResource(R.string.home), R.drawable.outline_home_24, Destination.Home),
                        BottomItem(stringResource(R.string.library), R.drawable.outline_library_music_24, Destination.Library),
                        BottomItem(stringResource(R.string.playlist), R.drawable.outline_playlist_play_24, Destination.Playlist)
                    )
                    Column {
                        if(state.playerPlaylist!=null || state.playerListSong!=null){
                            PlayerBottomBar(
                                song = state.playerSong!!,
                                playerTime = state.playerTime,
                                isPlay = state.isPlay,
                                onClick = {
                                    backStack.add(Destination.Player)
                                },
                                onCLickPause = {
                                    viewModel.processIntent(MviIntent.OnChangeSongPlayState)
                                },
                                onClickClose = {
                                    viewModel.processIntent(MviIntent.OnClickClosePlayer)
                                }
                            )
                        }
                        NavigationBar(
                            windowInsets = NavigationBarDefaults.windowInsets,
                            containerColor = colorScheme.background
                        ) {
                            bottomItem.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = selectedBottomBar == index,
                                    onClick = {
                                        selectedBottomBar = index
                                        backStack.clear()
                                        backStack.add(item.destination)
                                    },
                                    icon = { Icon(painterResource(item.icon), null) },
                                    label = { Text(item.label, color = colorScheme.primary) }
                                )
                            }
                        }
                    }
                }
            }
        ) { p ->
            NavDisplay(
                modifier = Modifier.padding(p),
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = entryProvider {
                    entry<Destination.Splash> {
                        Splash()
                    }
                    entry<Destination.Login> {
                        LoginScreen(
                            viewModel = viewModel
                        )
                    }
                    entry<Destination.Signup> {
                        SignupScreen(
                            viewModel = viewModel,
                            onBack = { backStack.removeLastOrNull() }
                        )
                    }
                    entry<Destination.Home> {
                        HomeScreen(
                            viewModel = viewModel,
                            onBack = { activity?.finish() }
                        )
                    }
                    entry<Destination.Profile> {
                        ProfileScreen(
                            viewModel = viewModel,
                            onBack = { backStack.removeLastOrNull() }
                        )
                    }
                    entry<Destination.Library> {
                        LibraryScreen(
                            viewModel = viewModel,
                            onClickNewPlaylist = {
                                backStack.clear()
                                backStack.add(Destination.Playlist)
                                selectedBottomBar = 2
                            },
                            onBack = { activity?.finish() }
                        )
                    }
                    entry<Destination.Playlist> {
                        PlaylistScreen(
                            viewModel = viewModel,
                            onBack = { activity?.finish() }
                        )
                    }
                    entry<Destination.PlaylistDetail> { destination ->
                        PlaylistDetailScreen(
                            viewModel = viewModel,
                            playlistId = destination.playlistID,
                            onBack = { backStack.removeLastOrNull() }
                        )
                    }
                    entry<Destination.Player> {
                        PlayerScreen(
                            viewModel = viewModel,
                            onBack = { backStack.removeLastOrNull() }
                        )
                    }
                    entry<Destination.TopAlbums> {
                        TopAlbumsScreen(
                            onBack = { backStack.removeLastOrNull() },
                            topAlbums = state.topAlbums!!
                        )
                    }
                    entry<Destination.TopTracks> {
                        TopTracksScreen(
                            onBack = { backStack.removeLastOrNull() },
                            topTracks = state.topTracks!!
                        )
                    }
                    entry<Destination.TopArtists> {
                        TopArtistsScreen(
                            onBack = { backStack.removeLastOrNull() },
                            topArtists = state.topArtists!!
                        )
                    }
                    entry<Destination.Settings> {
                        SettingScreen(
                            onBack = { backStack.removeLastOrNull() },
                        )
                    }
                }
            )
        }
    }
}

data class BottomItem(
    var label: String,
    var icon: Int,
    val destination: Destination
)