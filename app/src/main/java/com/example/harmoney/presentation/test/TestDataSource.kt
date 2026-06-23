package com.example.harmoney.presentation.test

import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.SortOption
import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.OneDayTransactions
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.models.Transaction
import com.example.harmoney.domain.models.TransactionsFilter
import com.example.harmoney.presentation.converters.DateFormatter
import com.example.harmoney.presentation.models.DatePattern
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
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_SHOP_CART,
                color = CategoryColors.RED_T53
            ),
            createdAt = 1L,
            userOrder = 100.0,
        ),
        Category(
            id = 2,
            name = "Gifts",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_GIFT,
                color = CategoryColors.PINK_T75
            ),
            createdAt = 2L,
            userOrder = 200.0,
        ),
        Category(
            id = 3,
            name = "Vacation",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_VACATION_1,
                color = CategoryColors.ORANGE_T55
            ),
            createdAt = 3L,
            userOrder = 300.0,
        ),
        Category(
            id = 4,
            name = "Education",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_EDUCATION,
                color = CategoryColors.ORANGE_T70
            ),
            createdAt = 4L,
            userOrder = 400.0,
        ),
        Category(
            id = 5,
            name = "Internet and mobile",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_PHONE_INTERNET,
                color = CategoryColors.ORANGE_T47
            ),
            createdAt = 5L,
            userOrder = 500.0,
        ),
        Category(
            id = 6,
            name = "Psychology",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_BALANCE,
                color = CategoryColors.YELLOW_T58
            ),
            createdAt = 6L,
            userOrder = 600.0,
        ),
        Category(
            id = 7,
            name = "Transport",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_TRANSPORT_1,
                color = CategoryColors.OLIVE_T52
            ),
            createdAt = 7L,
            userOrder = 700.0,
        ),
        Category(
            id = 8,
            name = "Gadgets",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_COMPUTER,
                color = CategoryColors.TEAL_T75
            ),
            createdAt = 8L,
            userOrder = 800.0,
        ),
        Category(
            id = 9,
            name = "Eating on a walk",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_DINNER,
                color = CategoryColors.COBALT_T56
            ),
            createdAt = 9L,
            userOrder = 900.0,
        ),
        Category(
            id = 10,
            name = "For home",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_CLEANING,
                color = CategoryColors.BLUE_T80
            ),
            createdAt = 10L,
            userOrder = 1000.0,
        ),
        Category(
            id = 11,
            name = "Entertainment",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_GAMES_1,
                color = CategoryColors.PURPLE_T61
            ),
            createdAt = 11L,
            userOrder = 1100.0,
        ),
        Category(
            id = 12,
            name = "Fitness",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_FITNESS_1,
                color = CategoryColors.PURPLE_T72
            ),
            createdAt = 12L,
            userOrder = 1200.0,
        ),
        Category(
            id = 13,
            name = "Health",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_HEALTH,
                color = CategoryColors.BROWN_T51
            ),
            createdAt = 13L,
            userOrder = 1300.0,
        ),
        Category(
            id = 14,
            name = "Clothes",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_CLOTHES,
                color = CategoryColors.SLATE_T55
            ),
            createdAt = 14L,
            userOrder = 1400.0,
        ),
        Category(
            id = 15,
            name = "Other",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_FROG,
                color = CategoryColors.ROSE_T62
            ),
            createdAt = 15L,
            userOrder = 1500.0,
        ),
        Category(
            id = 16,
            name = "Beauty",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_HAIRCUT,
                color = CategoryColors.ORANGE_T60
            ),
            createdAt = 16L,
            userOrder = 1600.0,
        ),
        Category(
            id = 17,
            name = "Pet",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_CAT,
                color = CategoryColors.AMBER_T56
            ),
            createdAt = 17L,
            userOrder = 1700.0,
        ),
        Category(
            id = 18,
            name = "Children",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_BABY,
                color = CategoryColors.CYAN_T55
            ),
            createdAt = 18L,
            userOrder = 1800.0,
        ),
        Category(
            id = 19,
            name = "Car",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_GAS_STATION,
                color = CategoryColors.LIME_T62
            ),
            createdAt = 19L,
            userOrder = 1900.0,
        ),
        Category(
            id = 20,
            name = "Salary",
            type = CategoryType.INCOME,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_MONEY_1,
                color = CategoryColors.GREEN_T70
            ),
            createdAt = 20L,
            userOrder = 2000.0,
        ),
        Category(
            id = 21,
            name = "Gifts",
            type = CategoryType.INCOME,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_MONEY_3,
                color = CategoryColors.GREEN_T53
            ),
            createdAt = 21L,
            userOrder = 2100.0,
        ),
        Category(
            id = 22,
            name = "Other",
            type = CategoryType.INCOME,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_MONEY_2,
                color = CategoryColors.GREEN_T60
            ),
            createdAt = 22L,
            userOrder = 2200.0,
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
    )

    private var categorySortOption: SortOption = SortOption.ALPHABET

    private val curMonthFirstDay: Long =
        dateFormatter.dateToMillis(parseDateFromString("25.05.2026"))
    private val curMonthLastDay: Long =
        dateFormatter.dateToMillis(parseDateFromString("24.06.2026"))
    private val pastMonthFirstDay: Long =
        dateFormatter.dateToMillis(parseDateFromString("25.01.2026"))
    private val pastMonthLastDay: Long =
        dateFormatter.dateToMillis(parseDateFromString("24.02.2026"))

    // для корректной работы updateCategoryUserOrder
    private val userOrder = USER_ORDER_IN_ASCENDING_ORDER

    fun getBalance(): Double {
        var totalExpenses: Double = 0.0
        transactions.filter { it.category.type == CategoryType.EXPENSES }
            .forEach { totalExpenses += it.amount }

        var totalIncome: Double = 0.0
        transactions.filter { it.category.type == CategoryType.INCOME }
            .forEach { totalIncome += it.amount }

        return totalIncome - totalExpenses
    }

    private fun parseDateFromString(date: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern(DatePattern.TITLE_FORMAL)
        return LocalDate.parse(date, formatter)
    }

    private fun parseStringFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern(DatePattern.TITLE_FORMAL)
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

        return when (categorySortOption) {
            SortOption.ALPHABET -> categories.sortedBy { it.name }
            SortOption.TIME_CREATED -> categories.sortedBy { it.createdAt }
            SortOption.USER_ORDER -> {
                // для корректной работы updateCategoryUserOrder
                if (userOrder == USER_ORDER_IN_ASCENDING_ORDER) {
                    categories.sortedBy { it.userOrder }
                } else {
                    categories.sortedByDescending { it.userOrder }
                }
            }
        }
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
        val categories = getCategories(categoryType)

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

    fun createTransaction(transaction: Transaction) {
        transactions.add(
            transaction.copy(
                id = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
        )
    }

    fun updateTransaction(transaction: Transaction) {
        val oldTransaction = transactions.find { it.id == transaction.id }
        val oldTransactionIndex = transactions.indexOf(oldTransaction)
        if (oldTransactionIndex < 0) return

        transactions[oldTransactionIndex] = transaction
    }

    fun deleteTransaction(transaction: Transaction) {
        transactions.remove(transaction)
    }

    fun createCategory(category: Category) {
        val maxUserOrder = categories.maxOf { it.userOrder }
        val timeAdded =
            LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        categories.add(
            category.copy(
                id = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                createdAt = timeAdded,
                userOrder = maxUserOrder + USER_ORDER_STEP
            )
        )
    }

    fun updateCategory(category: Category) {
        val oldCategory = categories.find { it.id == category.id }
        val oldCategoryIndex = categories.indexOf(oldCategory)
        if (oldCategoryIndex < 0) return

        categories[oldCategoryIndex] = category
    }

    fun deleteCategory(category: Category) {
        categories.remove(category)
    }

    fun isCategoryAlreadyExists(categoryName: String, categoryType: CategoryType): Boolean {
        val list = categories.filter { it.type == categoryType }
        val category = list.find { it.name == categoryName }
        return category != null
    }

    fun getSortOption(): SortOption {
        return categorySortOption
    }

    fun updateCategorySortOption(newSortOption: SortOption) {
        categorySortOption = newSortOption
    }

    fun updateCategoryUserOrder(from: Int, to: Int, categoryType: CategoryType) {
        val categoryList: MutableList<Category> = getCategories(categoryType).toMutableList()

        if (from == to) return

        val item = categoryList.removeAt(from)
        categoryList.add(to, item)

        val prev = categoryList.getOrNull(to - 1)
        val next = categoryList.getOrNull(to + 1)

        val newUserOrder = when {
            prev == null && next == null -> USER_ORDER_STEP
            prev == null -> next!!.userOrder - userOrder * USER_ORDER_STEP
            next == null -> prev.userOrder + userOrder * USER_ORDER_STEP
            else -> (prev.userOrder + next.userOrder) / 2.0
        }

        updateCategory(item.copy(userOrder = newUserOrder))
    }

    private companion object {
        const val COUNT_RUB_IN_ONE_EUR = 80.0
        const val COUNT_RUB_IN_ONE_USD = 70.0
        const val COUNT_EUR_IN_ONE_USD = 0.86
        const val USER_ORDER_STEP = 100.0
        const val USER_ORDER_IN_ASCENDING_ORDER = 1 // значение не менять
        const val USER_ORDER_IN_DESCENDING_ORDER = -1 // значение не менять
    }
}
