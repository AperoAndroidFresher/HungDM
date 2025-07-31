package com.example.hungdm.navigation

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.hungdm.R
import com.example.hungdm.model.InfoName
import com.example.hungdm.mvi.MviEvent
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviViewModel
import com.example.hungdm.screen.HomeScreen
import com.example.hungdm.screen.LibraryScreen
import com.example.hungdm.screen.LoginScreen
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
            }
        }
    }


    var selected by remember { mutableStateOf(0) }
    var linearListMusic by remember { mutableStateOf(true) }



    val bottomItem = listOf(
        BottomItem("Home", Icons.Default.Home, Destination.Home),
        BottomItem("Library", Icons.Default.DateRange, Destination.Library),
        BottomItem("Playlist", Icons.Default.PlayArrow, Destination.Playlist)
    )

    AppTheme (
        darkTheme = state.darkTheme,
        dynamicColor = false
    ){
        Scaffold(
            topBar = {
                val showTopNav = state.backStack.lastOrNull()?.let {
                    it !is Destination.Login && it !is Destination.Signup && it !is Destination.Profile
                } ?: false

                if (showTopNav) {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = colorScheme.background
                        ),
                        title = {
                            when (selected) {
                                0 -> {
                                    Text("Home", color = colorScheme.primary)
                                }

                                1 -> {
                                    Text("Library", color = colorScheme.primary)
                                }

                                2 -> {
                                    Text("Playlist", color = colorScheme.primary)
                                }
                            }
                        },
                        actions = {
                            if (selected == 2) {
                                IconButton(
                                    onClick = { linearListMusic = !linearListMusic }
                                ) {
                                    Icon(
                                        painter = if (linearListMusic) painterResource(R.drawable.type) else painterResource(R.drawable.type1),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                    )
                                }
                                IconButton(
                                    onClick = {}
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.sort),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                    )
                                }
                            }
                            IconButton(
                                onClick = { viewModel.processIntent(MviIntent.ChangeTheme) }
                            ) {
                                Icon(
                                    painter = painterResource(if(state.darkTheme) R.drawable.light else R.drawable.dark),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                )
                            }
                            IconButton(
                                onClick = { viewModel.processIntent(MviIntent.OnClickProfile) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                )
                            }
                        }
                    )
                }
            },
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
                                selected = selected == index,
                                onClick = {
                                    selected = index
//                                    backStack.clear()
//                                    backStack.add(item.destination)
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
                            state = state,
                            onClickSignup = {
                                viewModel.processIntent(MviIntent.OnSignupClicked)
                            },
                            onClickLogin = {
                                viewModel.processIntent(MviIntent.CheckLogin)
                            },
                            onValueChangeUsername = {
                                viewModel.processIntent(MviIntent.OnChangedInput(it, InfoName.USERNAME))
                            },
                            onValueChangePassword = {
                                viewModel.processIntent(MviIntent.OnChangedInput(it, InfoName.PASSWORD))
                            }
                        )
                    }

                    entry<Destination.Signup> {
                        SignupScreen(
                            state = state,
                            onBack = { viewModel.removeLast() },
                            onSigupClick = {
                                viewModel.processIntent(MviIntent.CheckSignup)
                            },
                            onValueChangeUsername = {
                                viewModel.processIntent(MviIntent.OnChangedInput(it, InfoName.USERNAME))
                            },
                            onValueChangePass = {
                                viewModel.processIntent(MviIntent.OnChangedInput(it, InfoName.PASSWORD))
                            },
                            onValueChangePass2 = {
                                viewModel.processIntent(MviIntent.OnChangedInput(it, InfoName.PASS2))
                            },
                            onValueChangeEmail = {
                                viewModel.processIntent(MviIntent.OnChangedInput(it, InfoName.EMAIL))
                            }
                        )
                    }

                    entry<Destination.Home> {
                        val context = LocalContext.current
                        val activity = context as? Activity
                        HomeScreen(
                            state = state,
                            onBack = {
                                activity?.finish()
                            }
                        )
                    }

                    entry<Destination.Profile> {
                        ProfileScreen(
                            state = state,
                            viewModel = viewModel,
                            onBack = {
                                viewModel.removeLast()
                            },
                            onValueChangeName = {
                                viewModel.processIntent(MviIntent.OnChangedInput(it, InfoName.NAME))
                            },
                            onValueChangePhone = {
                                viewModel.processIntent(MviIntent.OnChangedInput(it, InfoName.PHONE))
                            },
                            onValueChangeUni = {
                                viewModel.processIntent(MviIntent.OnChangedInput(it, InfoName.UNI))
                            },
                            onValueChangeEmail = {
                                viewModel.processIntent(MviIntent.OnChangedInput(it, InfoName.EMAIL))
                            },
                            onValueChangeDesc = {
                                viewModel.processIntent(MviIntent.OnChangedInput(it, InfoName.DESC))
                            },
                            onClickEdit = {
                                viewModel.processIntent(MviIntent.OnClickEditProfile)
                            },
                            onClickSubmit = {
                                viewModel.processIntent(MviIntent.CheckEditProfile)
                            }
                        )
                    }

                    entry<Destination.Library> {
                        val context = LocalContext.current
                        val activity = context as? Activity
                        LibraryScreen(
                            onBack = {
                                activity?.finish()
                            }
                        )
                    }

                    entry<Destination.Playlist> {
                        val context = LocalContext.current
                        val activity = context as? Activity
                        PlaylistScreen(
                            onBack = {
                                activity?.finish()
                            },
                            linearListMusic = linearListMusic
                        )
                    }

                }
            )
        }
    }
}