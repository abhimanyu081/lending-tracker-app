package dev.abhimanyu.lendingtracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.abhimanyu.lendingtracker.core.common.navigation.LendingTrackerDestinations
import dev.abhimanyu.lendingtracker.feature.dashboard.DashboardScreen
import dev.abhimanyu.lendingtracker.feature.person.AddPersonScreen
import dev.abhimanyu.lendingtracker.feature.transaction.BorrowMoneyScreen
import dev.abhimanyu.lendingtracker.feature.transaction.LendMoneyScreen
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
                onBorrowMoneyClick = {
                    navController.navigate(LendingTrackerDestinations.BORROW_MONEY)
                },
                onAddPersonClick = {
                    navController.navigate(LendingTrackerDestinations.ADD_PERSON)
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
        
        composable(LendingTrackerDestinations.BORROW_MONEY) {
            BorrowMoneyScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(LendingTrackerDestinations.ADD_PERSON) {
            AddPersonScreen(
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