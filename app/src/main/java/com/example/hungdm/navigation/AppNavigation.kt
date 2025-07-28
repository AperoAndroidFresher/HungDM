package com.example.hungdm.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.hungdm.UserInfo
import com.example.hungdm.screen.HomeScreen
import com.example.hungdm.screen.LoginScreen
import com.example.hungdm.screen.PlaylistScreen
import com.example.hungdm.screen.ProfileScreen
import com.example.hungdm.screen.SignupScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val backStack = rememberNavBackStack(Destination.Login(UserInfo()))
    var userInfoState by remember { mutableStateOf(UserInfo()) }
    var isEditUser by remember { mutableStateOf(false) }
    var darkTheme by remember { mutableStateOf(false) }

    NavDisplay(
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
                    onValueChangeUsername = { userInfoState = userInfoState.copy(username = it) },
                    onValueChangePassword = { userInfoState = userInfoState.copy(password = it) }
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

            entry<Destination.Playlist>{
                PlaylistScreen(
                    onBack = {
                        backStack.clear()
                    }
                )
            }

        }
    )
}