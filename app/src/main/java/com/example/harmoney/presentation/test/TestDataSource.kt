package com.example.harmoney.presentation.test

import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.OneDayTransactions
import com.example.harmoney.domain.models.Transaction
import com.example.harmoney.domain.models.StatisticsPeriod
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/** A temporary class to simulate a data source until the domain part is implemented */
class TestDataSource {

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
            date = parseDateFromString("01.03.2026"),
            amount = 1500.0,
            note = "Вкусняшки"
        ),
        Transaction(
            id = 2,
            category = categories.first { it.id == 2L },
            date = parseDateFromString("01.03.2026"),
            amount = 500.0,
            note = "8 марта"
        ),
        Transaction(
            id = 3,
            category = categories.first { it.id == 3L },
            date = parseDateFromString("05.03.2026"),
            amount = 15000.0,
            note = "Билеты"
        ),
        Transaction(
            id = 4,
            category = categories.first { it.id == 1L },
            date = parseDateFromString("10.03.2026"),
            amount = 2000.0,
        ),
        Transaction(
            id = 5,
            category = categories.first { it.id == 2L },
            date = parseDateFromString("10.03.2026"),
            amount = 3000.0,
            note = "Сумку себе любимой"
        ),
        Transaction(
            id = 6,
            category = categories.first { it.id == 2L },
            date = parseDateFromString("12.03.2026"),
            amount = 4000.0,
        ),
        //-------------------------------------------------------------
        Transaction(
            id = 7,
            category = categories.first { it.id == 1L },
            date = parseDateFromString("05.02.2026"),
            amount = 2000.0,
            note = "Продукты к ужину"
        ),
        Transaction(
            id = 8,
            category = categories.first { it.id == 3L },
            date = parseDateFromString("05.02.2026"),
            amount = 15000.0,
            note = "Хата"
        ),
        Transaction(
            id = 9,
            category = categories.first { it.id == 2L },
            date = parseDateFromString("07.02.2026"),
            amount = 1500.0,
            note = "Валентинка"
        ),
        Transaction(
            id = 10,
            category = categories.first { it.id == 1L },
            date = parseDateFromString("16.02.2026"),
            amount = 4000.0,
        ),
        Transaction(
            id = 11,
            category = categories.first { it.id == 2L },
            date = parseDateFromString("16.02.2026"),
            amount = 1000.0,
            note = "Носки"
        ),
        //---------------------------------------------------------------------
        Transaction(
            id = 12,
            category = categories.first { it.id == 6L },
            date = parseDateFromString("08.03.2026"),
            amount = 2000.0,
            note = "От мамы"
        ),
        Transaction(
            id = 13,
            category = categories.first { it.id == 6L },
            date = parseDateFromString("08.03.2026"),
            amount = 1500.0,
            note = "От бабушки"
        ),
        Transaction(
            id = 14,
            category = categories.first { it.id == 5L },
            date = parseDateFromString("25.02.2026"),
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

    private val curMonthFirstDay: LocalDate = parseDateFromString("25.02.2026")
    private val curMonthLastDay: LocalDate = parseDateFromString("24.03.2026")
    private val pastMonthFirstDay: LocalDate = parseDateFromString("25.01.2026")
    private val pastMonthLastDay: LocalDate = parseDateFromString("24.02.2026")

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
            parseStringFromDate(curMonthFirstDay) + " - " +
                    parseStringFromDate(curMonthLastDay)
        } else {
            parseStringFromDate(pastMonthFirstDay) + " - " +
                    parseStringFromDate(pastMonthLastDay)
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

    private fun getTransactions(
        statisticsPeriod: StatisticsPeriod,
        categoryType: CategoryType,
        categoryId: Long? = null
    ): List<Transaction> {
        var firstDay: LocalDate
        var lastDay: LocalDate

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
            .filter { ((it.date >= firstDay) && (it.date <= lastDay)) }
            .filter { it.category.type == categoryType }

        return if (categoryId != null) {
            filteredTransactions.filter { it.category.id == categoryId }
        } else {
            filteredTransactions
        }

    }

    fun getTransactionList(
        statisticsPeriod: StatisticsPeriod,
        categoryType: CategoryType,
        categoryId: Long?
    ) : List<OneDayTransactions> {
        val filteredTransactions = if (categoryId != null) {
            getTransactions(statisticsPeriod, categoryType).filter { it.category.id == categoryId }
        } else {
            getTransactions(statisticsPeriod, categoryType)
        }
        val total = filteredTransactions.sumOf { it.amount }

        val days = filteredTransactions.map { it.date }.distinct().sortedDescending()

        return days.map { day ->
            val transactions = filteredTransactions.filter { it.date.isEqual(day) }
            OneDayTransactions(
                date = day,
                transactions = transactions,
                totalAmount = transactions.sumOf { it.amount }
            )
        }
    }

    private companion object {
        const val DATE_PATTERN = "dd.MM.yyyy"
    }
}