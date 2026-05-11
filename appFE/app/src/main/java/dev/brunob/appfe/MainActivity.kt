package dev.brunob.appfe

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.brunob.appfe.ui.AppViewModel
import dev.brunob.appfe.ui.auth.LoginScreen
import dev.brunob.appfe.ui.auth.RegisterScreen
import dev.brunob.appfe.ui.calendar.CalendarScreen
import dev.brunob.appfe.ui.home.HomeScreen
import dev.brunob.appfe.ui.navigation.Routes
import dev.brunob.appfe.ui.profile.ProfileScreen
import dev.brunob.appfe.ui.theme.AppFETheme

class MainActivity : ComponentActivity() {

    private val notifPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* no-op */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notifPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        setContent {
            AppFETheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    AppNavHost()
                }
            }
        }
    }
}

@Composable
private fun AppNavHost(vm: AppViewModel = viewModel()) {
    val nav: NavHostController = rememberNavController()
    val student by vm.currentStudent.collectAsState()

    val startRoute = if (student != null) Routes.HOME else Routes.LOGIN

    NavHost(navController = nav, startDestination = startRoute) {
        composable(Routes.LOGIN) {
            LoginScreen(
                vm = vm,
                onLoggedIn = {
                    nav.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateRegister = { nav.navigate(Routes.REGISTER) }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                vm = vm,
                onRegistered = {
                    nav.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onBack = { nav.popBackStack() }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                vm = vm,
                onOpenProfile = { nav.navigate(Routes.PROFILE) },
                onOpenCalendar = { nav.navigate(Routes.CALENDAR) }
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                vm = vm,
                onBack = { nav.popBackStack() },
                onLogout = {
                    vm.logout()
                    nav.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.CALENDAR) {
            CalendarScreen(vm = vm, onBack = { nav.popBackStack() })
        }
    }
}
