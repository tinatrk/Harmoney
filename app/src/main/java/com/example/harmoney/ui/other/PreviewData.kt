package com.example.harmoney.ui.other

import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.models.CategoryStatisticsUi
import com.example.harmoney.presentation.models.CategoryUi
import com.example.harmoney.presentation.models.OneDayTransactionsUi
import com.example.harmoney.presentation.models.PieChartItem
import com.example.harmoney.presentation.models.TransactionUi
import com.example.harmoney.presentation.models.TransactionsFilterUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Suppress("detekt:MagicNumber")
object PreviewData {
    private val expensesCategories: ImmutableList<CategoryUi> = persistentListOf(
        CategoryUi(
            id = 1,
            name = "Продукты",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_SHOP_CART,
                color = CategoryColors.RED_T53
            ),
            createdAt = 1L,
            userOrder = 100.0,
        ),
        CategoryUi(
            id = 2,
            name = "Подарки",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_GIFT,
                color = CategoryColors.PINK_T75
            ),
            createdAt = 2L,
            userOrder = 200.0,
        ),
        CategoryUi(
            id = 3,
            name = "Путешествия",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_VACATION_1,
                color = CategoryColors.VIOLET_T68
            ),
            createdAt = 3L,
            userOrder = 300.0,
        ),
        CategoryUi(
            id = 4,
            name = "Образование",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_EDUCATION,
                color = CategoryColors.BLUE_T60
            ),
            createdAt = 4L,
            userOrder = 400.0,
        ),
        CategoryUi(
            id = 5,
            name = "Интернет и связь",
            type = CategoryType.EXPENSES,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_PHONE_INTERNET,
                color = CategoryColors.ORANGE_T47
            ),
            createdAt = 5L,
            userOrder = 500.0,
        ),
    )

    private val expensesTransactions: ImmutableList<TransactionUi> = persistentListOf(
        TransactionUi(
            id = 1,
            category = expensesCategories[1],
            amount = "2 000 ₽",
            note = "Маме на 8 марта",
            date = "06 Марта"
        ),
        TransactionUi(
            id = 2,
            category = expensesCategories[1],
            amount = "1 500 ₽",
            note = "Бабушке на 8 марта",
            date = "06 Марта"
        ),
        TransactionUi(
            id = 3,
            category = expensesCategories[2],
            amount = "1 500 ₽",
            note = "Чемодан",
            date = "06 Марта"
        ),
        TransactionUi(
            id = 4,
            category = expensesCategories[0],
            amount = "2 000 ₽",
            date = "15 Марта"
        ),
        TransactionUi(
            id = 5,
            category = expensesCategories[2],
            amount = "30 000 ₽",
            note = "Билеты",
            date = "20 Марта"
        ),
        TransactionUi(
            id = 6,
            category = expensesCategories[2],
            amount = "25 000 ₽",
            note = "Жилье",
            date = "25 Марта"
        ),
    )

    fun getExpensesCategoryStatistics(): ImmutableList<CategoryStatisticsUi> {
        return persistentListOf(
            CategoryStatisticsUi(
                category = expensesCategories[2],
                totalAmount = "15 000 ₽",
                percentage = "57.7%",
            ),
            CategoryStatisticsUi(
                category = expensesCategories[1],
                totalAmount = "7 500 ₽",
                percentage = "28.8%",
            ),
            CategoryStatisticsUi(
                category = expensesCategories[0],
                totalAmount = "3 500 ₽",
                percentage = "13.5%",
            ),
        )
    }

    fun getExpensesPieChartCategories(
    ): ImmutableList<PieChartItem> {
        return persistentListOf(
            PieChartItem(
                value = "15 000 ₽",
                colorValue = expensesCategories[2].icon.color.background,
                startAngle = -90f,
                sweepAngle = 205.69f,
            ),
            PieChartItem(
                value = "7 500 ₽",
                colorValue = expensesCategories[1].icon.color.background,
                startAngle = 117.69f,
                sweepAngle = 101.84f,
            ),
            PieChartItem(
                value = "3 500 ₽",
                colorValue = expensesCategories[0].icon.color.background,
                startAngle = 221.53f,
                sweepAngle = 46.46f,
            )
        )
    }

    fun getExpensesTransactions(): ImmutableList<OneDayTransactionsUi> {
        return persistentListOf(
            OneDayTransactionsUi(
                date = "06 Марта",
                totalAmount = "5 000 ₽",
                transactions = expensesTransactions.subList(0, 2)
            ),
            OneDayTransactionsUi(
                date = "15 Марта",
                totalAmount = "2 000 ₽",
                transactions = expensesTransactions.subList(3, 3)
            ),
            OneDayTransactionsUi(
                date = "20 Марта",
                totalAmount = "30 000 ₽",
                transactions = expensesTransactions.subList(4, 4)
            ),
            OneDayTransactionsUi(
                date = "25 Марта",
                totalAmount = "25 000 ₽",
                transactions = expensesTransactions.subList(5, 5)
            ),
        )
    }

    fun getExpensesFilters(): ImmutableList<TransactionsFilterUi> {
        val filters = persistentListOf(TransactionsFilterUi(id = 0, name = "Все категории")) +
                expensesCategories.map { TransactionsFilterUi(it.id, it.name) }
                    .sortedBy { it.name }

        return filters.toImmutableList()
    }

    fun getFilteredTransactions(): ImmutableList<OneDayTransactionsUi> {
        return persistentListOf(
            OneDayTransactionsUi(
                date = "06 Марта",
                totalAmount = "1 500 ₽",
                transactions = persistentListOf(expensesTransactions[2])
            ),
            OneDayTransactionsUi(
                date = "20 Марта",
                totalAmount = "30 000 ₽",
                transactions = persistentListOf(expensesTransactions[4])
            ),
            OneDayTransactionsUi(
                date = "25 Марта",
                totalAmount = "25 000 ₽",
                transactions = persistentListOf(expensesTransactions[5])
            ),
        )
    }

    fun getExpensesCategories(): ImmutableList<CategoryUi> {
        return expensesCategories.sortedBy { it.name }.toImmutableList()
    }
}
