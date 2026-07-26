package com.example.mob3000

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Text
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.example.mob3000.database.UserViewModel
    // Route identifiers that represents each screen
object PageHome : NavKey
object PageSettings : NavKey
object PageAffirmation : NavKey
object PageCBT : NavKey
object PageSession : NavKey
object PageBreathing : NavKey


class NavigationViewModel : ViewModel() {
    val backStack = mutableStateListOf<NavKey>(PageHome)
}

@Composable
fun Navigation(
    vm: UserViewModel
) {
    val navViewModel: NavigationViewModel = viewModel()
    val backStack = navViewModel.backStack

    val user = vm.user
    val username = user?.username ?: "User"
    val homeDefaultAffirmationsEnabled = user?.defaultAffirmationsHomeEnabled ?: true

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {

                PageHome -> NavEntry(PageHome) {
                    HomeScreen(
                        username = username,
                        onOpenSettings = { backStack.add(PageSettings) },
                        onGoToAffirmations = { backStack.add(PageAffirmation) },
                        onGoToCBT = { backStack.add(PageCBT) },
                        onGoToBreathing = { backStack.add(PageBreathing) },
                        homeDefaultAffirmationsEnabled = homeDefaultAffirmationsEnabled
                    )
                }

                PageSettings -> NavEntry(PageSettings) {
                    UserSettingsScreen(
                        userViewModel = vm,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }

                PageAffirmation -> NavEntry(PageAffirmation) {
                    AffirmationScreen(
                        onGoHome = { backStack.removeLastOrNull() }
                    )
                }

                PageCBT -> NavEntry(PageCBT) {
                    CbtScreen(
                        onGoHome = {
                            backStack.clear()
                            backStack.add(PageHome)
                        },
                        onStartSession = {
                            backStack.add(PageSession)
                        }
                    )
                }

                PageSession -> NavEntry(PageSession) {
                    SessionScreen(
                        onGoBackToCBT = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                PageBreathing -> NavEntry(PageBreathing) {
                    BreathingScreen(
                        onGoHome = {
                            backStack.clear()
                            backStack.add(PageHome)
                        }
                    )
                }

                else -> NavEntry(PageHome) {
                    Text("Unknown route")
                }
            }
        }
    )
}
