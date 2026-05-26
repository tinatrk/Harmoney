package com.example.harmoney.presentation.test

import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.OneDayTransactions
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.models.Transaction
import com.example.harmoney.domain.models.TransactionsFilter
import com.example.harmoney.presentation.converters.DateFormatter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/** A temporary class to simulate a data source until the domain part is implemented */
class TestDataSource(private val dateFormatter: DateFormatter) {

    private val categories: MutableList<Category> = mutableListOf(
        Category(
            id = 1,
            name = "Products",
            type = CategoryType.Expenses,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_SHOP_CART,
                color = CategoryColors.VIOLET_T68
            ),
            createdAt = 1L,
            userOrder = 100.0,
        ),
        Category(
            id = 2,
            name = "Gifts",
            type = CategoryType.Expenses,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_GIFT,
                color = CategoryColors.ORANGE_T70
            ),
            createdAt = 2L,
            userOrder = 200.0,
        ),
        Category(
            id = 3,
            name = "Vacation",
            type = CategoryType.Expenses,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_VACATION_1,
                color = CategoryColors.BLUE_T80
            ),
            createdAt = 3L,
            userOrder = 300.0,
        ),
        Category(
            id = 4,
            name = "Education",
            type = CategoryType.Expenses,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_EDUCATION,
                color = CategoryColors.ROSE_T62
            ),
            createdAt = 4L,
            userOrder = 400.0,
        ),
        Category(
            id = 5,
            name = "Salary",
            type = CategoryType.Income,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_MONEY_1,
                color = CategoryColors.GREEN_T70
            ),
            createdAt = 5L,
            userOrder = 500.0,
        ),
        Category(
            id = 6,
            name = "Gifts",
            type = CategoryType.Income,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_MONEY_3,
                color = CategoryColors.GREEN_T53
            ),
            createdAt = 6L,
            userOrder = 600.0,
        ),
        Category(
            id = 7,
            name = "Other",
            type = CategoryType.Income,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_MONEY_2,
                color = CategoryColors.GREEN_T60
            ),
            createdAt = 7L,
            userOrder = 700.0,
        )
    )

    private val transactions: MutableList<Transaction> = mutableListOf(
        Transaction(
            id = 1,
            category = categories.first { it.id == 1L },
            dateMillis = dateFormatter.dateToMillis(parseDateFromString("01.03.2026")),
            amount = 1500.0,
            note = "Вкусняшки"
        ),
        Transaction(
            id = 2,
            category = categories.first { it.id == 2L },
            dateMillis = dateFormatter.dateToMillis(parseDateFromString("01.03.2026")),
            amount = 500.0,
            note = "8 марта"
        ),
        Transaction(
            id = 3,
            category = categories.first { it.id == 3L },
            dateMillis = dateFormatter.dateToMillis(parseDateFromString("05.03.2026")),
            amount = 15000.0,
            note = "Билеты"
        ),
        Transaction(
            id = 4,
            category = categories.first { it.id == 1L },
            dateMillis = dateFormatter.dateToMillis(parseDateFromString("10.03.2026")),
            amount = 2000.0,
        ),
        Transaction(
            id = 5,
            category = categories.first { it.id == 2L },
            dateMillis = dateFormatter.dateToMillis(parseDateFromString("10.03.2026")),
            amount = 3000.0,
            note = "Сумку себе любимой"
        ),
        Transaction(
            id = 6,
            category = categories.first { it.id == 2L },
            dateMillis = dateFormatter.dateToMillis(parseDateFromString("12.03.2026")),
            amount = 4000.0,
        ),
        //-------------------------------------------------------------
        Transaction(
            id = 7,
            category = categories.first { it.id == 1L },
            dateMillis = dateFormatter.dateToMillis(parseDateFromString("05.02.2026")),
            amount = 2000.0,
            note = "Продукты к ужину"
        ),
        Transaction(
            id = 8,
            category = categories.first { it.id == 3L },
            dateMillis = dateFormatter.dateToMillis(parseDateFromString("05.02.2026")),
            amount = 15000.0,
            note = "Хата"
        ),
        Transaction(
            id = 9,
            category = categories.first { it.id == 2L },
            dateMillis = dateFormatter.dateToMillis(parseDateFromString("07.02.2026")),
            amount = 1500.0,
            note = "Валентинка"
        ),
        Transaction(
            id = 10,
            category = categories.first { it.id == 1L },
            dateMillis = dateFormatter.dateToMillis(parseDateFromString("16.02.2026")),
            amount = 4000.0,
        ),
        Transaction(
            id = 11,
            category = categories.first { it.id == 2L },
            dateMillis = dateFormatter.dateToMillis(parseDateFromString("16.02.2026")),
            amount = 1000.0,
            note = "Носки"
        ),
        //---------------------------------------------------------------------
        Transaction(
            id = 12,
            category = categories.first { it.id == 6L },
            dateMillis = dateFormatter.dateToMillis(parseDateFromString("08.03.2026")),
            amount = 2000.0,
            note = "От мамы"
        ),
        Transaction(
            id = 13,
            category = categories.first { it.id == 6L },
            dateMillis = dateFormatter.dateToMillis(parseDateFromString("08.03.2026")),
            amount = 1500.0,
            note = "От бабушки"
        ),
        Transaction(
            id = 14,
            category = categories.first { it.id == 5L },
            dateMillis = dateFormatter.dateToMillis(parseDateFromString("25.02.2026")),
            amount = 25000.0,
            note = "Зарплата"
        ),
        //----------------------------------------------------------------------------------
        /*Transaction(
            id = 15,
            category = categories.first { it.id == 5L },
            date = parseDateFromString("25.01.2026"),
            amount = 30000.0,
            note = "Зарплата"
        ),*/
    )

    private val curMonthFirstDay: Long =
        dateFormatter.dateToMillis(parseDateFromString("25.02.2026"))
    private val curMonthLastDay: Long =
        dateFormatter.dateToMillis(parseDateFromString("24.03.2026"))
    private val pastMonthFirstDay: Long =
        dateFormatter.dateToMillis(parseDateFromString("25.01.2026"))
    private val pastMonthLastDay: Long =
        dateFormatter.dateToMillis(parseDateFromString("24.02.2026"))

    fun getBalance(): Double {
        var totalExpenses: Double = 0.0
        transactions.filter { it.category.type == CategoryType.Expenses }
            .forEach { totalExpenses += it.amount }

        var totalIncome: Double = 0.0
        transactions.filter { it.category.type == CategoryType.Income }
            .forEach { totalIncome += it.amount }

        return totalIncome - totalExpenses
    }

    private fun parseDateFromString(date: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        return LocalDate.parse(date, formatter)
    }

    private fun parseStringFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        return date.format(formatter)

    }

    fun getStatisticsDate(statisticsPeriod: StatisticsPeriod): String {
        return if (statisticsPeriod == StatisticsPeriod.CURRENT_MONTH) {
            parseStringFromDate(dateFormatter.millisToDate(curMonthFirstDay)) + " - " +
                    parseStringFromDate(dateFormatter.millisToDate(curMonthLastDay))
        } else {
            parseStringFromDate(dateFormatter.millisToDate(pastMonthFirstDay)) + " - " +
                    parseStringFromDate(dateFormatter.millisToDate(pastMonthLastDay))
        }
    }

    fun getCategoriesForStatistics(
        statisticsPeriod: StatisticsPeriod,
        categoryType: CategoryType
    ): List<CategoryStatistics> {
        val filteredTransactions = getTransactions(statisticsPeriod, categoryType)
        val total = filteredTransactions.sumOf { it.amount }

        val categories = filteredTransactions.map { it.category }.distinct()

        return categories.map { category ->
            val categoryTotal =
                filteredTransactions.filter { it.category.id == category.id }.sumOf { it.amount }
            CategoryStatistics(
                category = category,
                totalAmount = categoryTotal,
                percentage = (categoryTotal / total * 100).toFloat()
            )
        }.sortedByDescending { it.percentage }
    }

    fun getCategories(
        categoryType: CategoryType
    ): List<Category> {
        val categories = categories.filter { it.type == categoryType }
        // Если категорий много, то берем 10 первых
        return if (categories.size > 10) {
            categories.take(10)
        } else categories
    }

    fun getCategory(categoryId: Long?): Category? {
        return categoryId?.let { categories.find { it.id == categoryId } }
    }

    private fun getTransactions(
        statisticsPeriod: StatisticsPeriod,
        categoryType: CategoryType,
        categoryId: Long? = null
    ): List<Transaction> {
        var firstDay: Long
        var lastDay: Long

        when (statisticsPeriod) {
            StatisticsPeriod.CURRENT_MONTH -> {
                firstDay = curMonthFirstDay
                lastDay = curMonthLastDay
            }

            StatisticsPeriod.LAST_MONTH -> {
                firstDay = pastMonthFirstDay
                lastDay = pastMonthLastDay
            }
        }

        val filteredTransactions = transactions
            .filter { ((it.dateMillis >= firstDay) && (it.dateMillis <= lastDay)) }
            .filter { it.category.type == categoryType }

        return if (categoryId != null) {
            filteredTransactions.filter { it.category.id == categoryId }
        } else {
            filteredTransactions
        }
    }

    fun getTransaction(transactionId: Long?): Transaction? {
        return transactionId?.let {
            transactions.find { it.id == transactionId }
        }
    }

    fun getTransactionList(
        statisticsPeriod: StatisticsPeriod,
        categoryType: CategoryType,
        filterId: Long?
    ): List<OneDayTransactions> {
        val filteredTransactions = if (filterId != null && filterId > 0) {
            getTransactions(statisticsPeriod, categoryType).filter { it.category.id == filterId }
        } else {
            getTransactions(statisticsPeriod, categoryType)
        }
        val total = filteredTransactions.sumOf { it.amount }

        val days = filteredTransactions.map { it.dateMillis }.distinct().sortedDescending()

        return days.map { day ->
            val transactions = filteredTransactions.filter { it.dateMillis == day }
            OneDayTransactions(
                dateMillis = day,
                transactions = transactions,
                totalAmount = transactions.sumOf { it.amount }
            )
        }
    }

    fun getTransactionFilters(
        categoryType: CategoryType,
    ): List<TransactionsFilter> {
        val categories = categories.filter { it.type == categoryType }
        val filters = categories.map {
            TransactionsFilter(
                id = it.id,
                name = it.name
            )
        }

        return listOf(
            TransactionsFilter(
                id = 0,
                name = "Все категории",
            )
        ) + filters
    }

    fun getFirstDay(): Long {
        return curMonthFirstDay
    }

    fun getLastDay(): Long {
        return curMonthLastDay
    }

    fun isDateInCorrectRange(dateMillis: Long?): Boolean {
        return dateMillis in curMonthFirstDay..curMonthLastDay
    }

    // Добавить логику?
    fun getAmountAfterCurrencyExchanged(
        localAmount: Double,
        localCurrency: Currency,
        targetCurrency: Currency
    ): Double {
        val index = localCurrency.ordinal + targetCurrency.ordinal
        return when (index) {
            // EUR и RUB
            1 -> {
                if (targetCurrency == Currency.RUB) {
                    localAmount * COUNT_RUB_IN_ONE_EUR
                } else {
                    localAmount / COUNT_RUB_IN_ONE_EUR
                }
            }

            // EUR и USD
            2 -> {
                if (targetCurrency == Currency.USD) {
                    localAmount * COUNT_EUR_IN_ONE_USD
                } else {
                    localAmount / COUNT_EUR_IN_ONE_USD
                }
            }

            // USD и RUB
            3 -> {
                if (targetCurrency == Currency.RUB) {
                    localAmount * COUNT_RUB_IN_ONE_USD
                } else {
                    localAmount / COUNT_RUB_IN_ONE_USD
                }
            }

            else -> localAmount
        }
    }

    fun saveTransaction(transaction: Transaction) {
        transactions.add(
            transaction.copy(
                id = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
        )
    }

    private companion object {
        const val DATE_PATTERN = "dd.MM.yyyy"
        const val COUNT_RUB_IN_ONE_EUR = 80.0
        const val COUNT_RUB_IN_ONE_USD = 70.0
        const val COUNT_EUR_IN_ONE_USD = 0.86
    }
}
