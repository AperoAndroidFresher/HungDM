package com.example.hungdm.navigation

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.hungdm.R
import com.example.hungdm.UserInfo
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

    var userInfoState by remember { mutableStateOf(UserInfo()) }
    val backStack = remember { mutableStateListOf<Destination>(Destination.Login(userInfoState)) }
    var selected by remember { mutableStateOf(0) }
    var darkTheme by remember { mutableStateOf(true) }
    var linearListMusic by remember { mutableStateOf(true) }


    val bottomItem = listOf(
        BottomItem("Home", Icons.Default.Home, Destination.Home(userInfoState)),
        BottomItem("Library", Icons.Default.DateRange, Destination.Library),
        BottomItem("Playlist", Icons.Default.PlayArrow, Destination.Playlist)
    )

    AppTheme (
        darkTheme = darkTheme,
        dynamicColor = false
    ){
        Scaffold(
            topBar = {
                val showTopNav = backStack.lastOrNull()?.let {
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
                                    Text("Home")
                                }

                                1 -> {
                                    Text("Library")
                                }

                                2 -> {
                                    Text("Playlist")
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
                                onClick = { darkTheme = !darkTheme }
                            ) {
                                Icon(
                                    painter = painterResource(if(darkTheme) R.drawable.light else R.drawable.dark),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                )
                            }
                            IconButton(
                                onClick = { backStack.add(Destination.Profile(userInfoState)) }
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
                val showBottomNav = backStack.lastOrNull()?.let {
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
                                    backStack.add(item.destination)
                                },
                                icon = { Icon(item.icon,null) },
                                label = { Text(item.label) }
                            )
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
                    entry<Destination.Login> {
                        LoginScreen(
                            userInfo = userInfoState,
                            onClickSignup = {
                                backStack.add(Destination.Signup)
                            },
                            onClickLogin = {
                                backStack.clear()
                                backStack.add(Destination.Home(userInfoState))
                            },
                            onValueChangeUsername = {
                                userInfoState = userInfoState.copy(username = it)
                            },
                            onValueChangePassword = {
                                userInfoState = userInfoState.copy(password = it)
                            }
                        )
                    }

                    entry<Destination.Signup> {
                        SignupScreen(
                            onBack = { backStack.removeLastOrNull() },
                            onSigupClick = { key ->
                                userInfoState = key
                                backStack.removeLastOrNull()
                                backStack.add(Destination.Login(userInfoState))
                            }
                        )
                    }

                    entry<Destination.Home> { key ->
                        val context = LocalContext.current
                        val activity = context as? Activity
                        HomeScreen(
                            userInfo = key.user,
                            onBack = {
                                activity?.finish()
                            }
                        )
                    }

                    entry<Destination.Profile> { key ->
                        ProfileScreen(
                            userInfo = key.user,
                            onBack = {
                                backStack.removeLastOrNull()
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