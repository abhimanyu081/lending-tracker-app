package dev.abhimanyu.lendingtracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.abhimanyu.lendingtracker.core.common.navigation.LendingTrackerDestinations
import dev.abhimanyu.lendingtracker.feature.dashboard.DashboardScreen
import dev.abhimanyu.lendingtracker.feature.transaction.LendMoneyScreen
import dev.abhimanyu.lendingtracker.feature.transaction.RepaymentScreen
import dev.abhimanyu.lendingtracker.feature.person.PersonDetailScreen
import dev.abhimanyu.lendingtracker.feature.transaction.TransactionHistoryScreen

@Composable
fun LendingTrackerNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = LendingTrackerDestinations.DASHBOARD,
        modifier = modifier
    ) {
        composable(LendingTrackerDestinations.DASHBOARD) {
            DashboardScreen(
                onLendMoneyClick = {
                    navController.navigate(LendingTrackerDestinations.LEND_MONEY)
                },
                onRecordRepaymentClick = {
                    navController.navigate(LendingTrackerDestinations.RECORD_REPAYMENT)
                },
                onPersonClick = { personId, personName ->
                    navController.navigate("${LendingTrackerDestinations.PERSON_DETAIL}/$personId/$personName")
                },
                onViewHistoryClick = {
                    navController.navigate(LendingTrackerDestinations.TRANSACTION_HISTORY)
                }
            )
        }
        
        composable(LendingTrackerDestinations.LEND_MONEY) {
            LendMoneyScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(LendingTrackerDestinations.RECORD_REPAYMENT) {
            RepaymentScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("${LendingTrackerDestinations.PERSON_DETAIL}/{personId}/{personName}") { backStackEntry ->
            val personId = backStackEntry.arguments?.getString("personId")?.toLongOrNull() ?: 0L
            val personName = backStackEntry.arguments?.getString("personName") ?: ""
            PersonDetailScreen(
                personId = personId,
                personName = personName,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(LendingTrackerDestinations.TRANSACTION_HISTORY) {
            TransactionHistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}