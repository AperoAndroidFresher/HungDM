package com.example.hungdm.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.hungdm.UserInfo
import com.example.hungdm.screen.BottomItem
import com.example.hungdm.screen.HomeScreen
import com.example.hungdm.screen.LoginScreen
import com.example.hungdm.screen.PlaylistScreen
import com.example.hungdm.screen.ProfileScreen
import com.example.hungdm.screen.SignupScreen

data class BottomItem(
    var label: String,
    var icon: ImageVector
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val backStack = rememberNavBackStack(Destination.Login(UserInfo()))
    var userInfoState by remember { mutableStateOf(UserInfo()) }
    var isEditUser by remember { mutableStateOf(false) }
    var darkTheme by remember { mutableStateOf(false) }

    val bottomItem = listOf(
        BottomItem("Home", Icons.Default.Home),
        BottomItem("Library", Icons.Default.DateRange),
        BottomItem("Playlist", Icons.Default.PlayArrow)
    )
    var selected by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {

            val showTopNav = backStack.last() !is Destination.Login && backStack.last() !is Destination.Signup
            if(showTopNav){
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Row(Modifier.fillMaxWidth()) {
                            Spacer(Modifier.weight(1f))
                            IconButton(onClick = { backStack.add(Destination.Profile(userInfoState)) }) {
                                Icon(
                                    Icons.Default.AccountCircle,
                                    null
                                )
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            val showBottomNav = backStack.last() !is Destination.Login && backStack.last() !is Destination.Signup

            if(showBottomNav) {
                NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                    bottomItem.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selected == index,
                            onClick = {
                                selected = index
                                if(selected==2) backStack.add(Destination.Playlist)
                            },
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = null
                                )
                            },
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
                            backStack.add(Destination.Login(key))
                        }
                    )
                }

                entry<Destination.Home> { key ->
                    HomeScreen(
                        userInfo = key.user,
                        onBack = {
                            backStack.clear()
                        },
                        onClickProfile = {
                            backStack.add(Destination.Profile(key.user))
                        },
                        onClickPlaylist = {
                            backStack.add(Destination.Playlist)
                        }
                    )
                }

                entry<Destination.Profile> { key ->
                    ProfileScreen(
                        userInfo = key.user,
                        isEditUser = isEditUser,
                        onBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                entry<Destination.Playlist> {
                    PlaylistScreen(
                        onBack = {
                            backStack.clear()
                        }
                    )
                }

            }
        )
    }
}