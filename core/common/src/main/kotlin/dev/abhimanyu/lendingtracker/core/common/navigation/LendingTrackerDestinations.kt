package dev.abhimanyu.lendingtracker.core.common.navigation

object LendingTrackerDestinations {
    const val DASHBOARD = "dashboard"
    const val LEND_MONEY = "lend_money"
    const val RECORD_REPAYMENT = "record_repayment"
    const val TRANSACTION_HISTORY = "transaction_history"
    const val PERSON_LIST = "person_list"
    const val TRANSACTION_DETAILS = "transaction_details/{transactionId}"
    
    fun transactionDetails(transactionId: String) = "transaction_details/$transactionId"
}