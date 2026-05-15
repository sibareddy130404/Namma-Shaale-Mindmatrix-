package com.nammashale.assetmanager.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nammashale.assetmanager.ui.screens.*

/**
 * Route constants for navigation destinations.
 */
object Routes {
    const val DASHBOARD = "dashboard"
    const val ASSET_REGISTRATION = "asset_registration"
    const val ASSET_LIST = "asset_list"
    const val ASSET_DETAIL = "asset_detail/{assetId}"
    const val ISSUE_LIST = "issue_list"
    const val CAMERA = "camera"
    const val REPORT = "report"

    fun assetDetail(assetId: Long) = "asset_detail/$assetId"
}

/**
 * Main navigation graph for the app.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD,
        modifier = modifier
    ) {
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onNavigateToRegister = { navController.navigate(Routes.ASSET_REGISTRATION) },
                onNavigateToAssetList = { navController.navigate(Routes.ASSET_LIST) },
                onNavigateToIssues = { navController.navigate(Routes.ISSUE_LIST) },
                onNavigateToReports = { navController.navigate(Routes.REPORT) }
            )
        }

        composable(Routes.ASSET_REGISTRATION) { backStackEntry ->
            val photoUri = backStackEntry.savedStateHandle.get<String>("photoUri")
            AssetRegistrationScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCamera = { navController.navigate(Routes.CAMERA) },
                photoUri = photoUri
            )
        }

        composable(Routes.ASSET_LIST) {
            AssetListScreen(
                onNavigateBack = { navController.popBackStack() },
                onAssetClick = { assetId -> navController.navigate(Routes.assetDetail(assetId)) }
            )
        }

        composable(
            route = Routes.ASSET_DETAIL,
            arguments = listOf(navArgument("assetId") { type = NavType.LongType })
        ) { backStackEntry ->
            val assetId = backStackEntry.arguments?.getLong("assetId") ?: return@composable
            AssetDetailScreen(
                assetId = assetId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ISSUE_LIST) {
            IssueListScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.CAMERA) {
            CameraScreen(
                onNavigateBack = { navController.popBackStack() },
                onPhotoCaptured = { uri ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("photoUri", uri)
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.REPORT) {
            ReportScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
