package com.example.harmoney.ui.other

import androidx.compose.material3.Text
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.presentation.categoryStatistics.models.AccountContent
import com.example.harmoney.presentation.models.AccountScopeUi
import com.example.harmoney.presentation.models.CategoryStatisticsUi
import com.example.harmoney.presentation.models.CategoryUi
import com.example.harmoney.presentation.models.CurrencyUi
import com.example.harmoney.presentation.models.MenuOption
import com.example.harmoney.presentation.models.PieChartItem
import com.example.harmoney.presentation.models.TransactionFilterUi
import com.example.harmoney.presentation.models.TransactionUi
import com.example.harmoney.presentation.models.TransactionsPerDayUi
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
            amount = "20 €",
            note = "Маме на 8 марта",
            date = "06 Марта"
        ),
        TransactionUi(
            id = 2,
            category = expensesCategories[1],
            amount = "15 €",
            note = "Бабушке на 8 марта",
            date = "06 Марта"
        ),
        TransactionUi(
            id = 3,
            category = expensesCategories[2],
            amount = "15 €",
            note = "Чемодан",
            date = "06 Марта"
        ),
        TransactionUi(
            id = 4,
            category = expensesCategories[0],
            amount = "20 €",
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

    private val currencies: ImmutableList<CurrencyUi> = persistentListOf(
        CurrencyUi(currency = Currency.AMD, name = "Армянский драм"),
        CurrencyUi(currency = Currency.AUD, name = "Австралийский доллар"),
        CurrencyUi(currency = Currency.BYN, name = "Белорусский рубль"),
        CurrencyUi(currency = Currency.GBP, name = "Британский фунт стерлингов"),
        CurrencyUi(currency = Currency.BTN, name = "Бутанский нгултрум"),
        CurrencyUi(currency = Currency.EUR, name = "Евро"),
        CurrencyUi(currency = Currency.EGP, name = "Египетский фунт"),
        CurrencyUi(currency = Currency.USD, name = "Доллар США"),
        CurrencyUi(currency = Currency.CNY, name = "Китайский юань"),
        CurrencyUi(currency = Currency.RUB, name = "Российский рубль"),
        CurrencyUi(currency = Currency.CHF, name = "Швейцарский франк"),
    )

    private val accountScopes: List<AccountScopeUi> = listOf(
        AccountScopeUi(
            id = 1L,
            name = "Основная карта",
            balance = "30 000 ₽",
            balanceOriginals = null,
            isArchivedMark = false
        ),
        AccountScopeUi(
            id = 2L,
            name = "Наличка",
            balance = "100 ₽",
            balanceOriginals = null,
            isArchivedMark = false
        ),
        AccountScopeUi(
            id = 3L,
            name = "Иностранная карта",
            balance = "1 000 $",
            balanceOriginals = null,
            isArchivedMark = true
        ),
        AccountScopeUi(
            id = 4L,
            name = "Активные счета",
            balance = "30 100 ₽",
            balanceOriginals = null,
            isArchivedMark = false
        ),
        AccountScopeUi(
            id = 5L,
            name = "Все счета",
            balance = "40 100 ₽",
            balanceOriginals = "30 100 ₽ = 30 100 ₽\n" + "1000 $ = 10 000 ₽",
            isArchivedMark = false
        ),
    )

    private val accountScopesMenu: List<MenuOption> = listOf(
        MenuOption(
            body = { Text("Основная карта") },
            expanded = true,
            onClick = {}
        ),
        MenuOption(
            body = { Text("Наличка") },
            expanded = false,
            onClick = {}
        ),
        MenuOption(
            body = { Text("Иностранная карта") },
            expanded = false,
            onClick = {}
        ),
        MenuOption(
            body = { Text("Все счета") },
            expanded = false,
            onClick = {}
        ),
    )

    private const val exchangeRateStatus =
        "Найден актуальный курс:\n" +
                "1 $ = 80 ₽\n" +
                "1 € = 100 ₽\n\n" +
        "Найден только старый курс:\n" +
                "1 ₸ = 1.5 ₽ [10.05.2026]\n\n" +
        "Курс не найден:\n" +
                "¥ -> ₽"

    fun getExpensesCategoryStatistics(): ImmutableList<CategoryStatisticsUi> {
        return persistentListOf(
            CategoryStatisticsUi(
                category = expensesCategories[2],
                totalAmount = "15 000 ₽",
                totalOrigins = "",
                percentage = "57.7%",
            ),
            CategoryStatisticsUi(
                category = expensesCategories[1],
                totalAmount = "7 500 ₽",
                totalOrigins = "",
                percentage = "28.8%",
            ),
            CategoryStatisticsUi(
                category = expensesCategories[0],
                totalAmount = "3 500 ₽",
                totalOrigins = "",
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

    fun getExpensesTransactions(): ImmutableList<TransactionUi> = expensesTransactions

    fun getExpensesTransactionsPerDay(): ImmutableList<TransactionsPerDayUi> {
        return persistentListOf(
            TransactionsPerDayUi(
                date = "06 Марта",
                totalAmount = "5 000 ₽",
                transactions = expensesTransactions.subList(0, 2)
            ),
            TransactionsPerDayUi(
                date = "15 Марта",
                totalAmount = "2 000 ₽",
                transactions = expensesTransactions.subList(3, 3)
            ),
            TransactionsPerDayUi(
                date = "20 Марта",
                totalAmount = "30 000 ₽",
                transactions = expensesTransactions.subList(4, 4)
            ),
            TransactionsPerDayUi(
                date = "25 Марта",
                totalAmount = "25 000 ₽",
                transactions = expensesTransactions.subList(5, 5)
            ),
        )
    }

    fun getExpensesFilters(): ImmutableList<TransactionFilterUi> {
        val filters = persistentListOf(TransactionFilterUi.All) +
                expensesCategories.map { TransactionFilterUi.CategoryUi(it.id, it.name) }
                    .sortedBy { it.name }

        return filters.toImmutableList()
    }

    fun getFilteredTransactions(): ImmutableList<TransactionsPerDayUi> {
        return persistentListOf(
            TransactionsPerDayUi(
                date = "06 Марта",
                totalAmount = "1 500 ₽",
                transactions = persistentListOf(expensesTransactions[2])
            ),
            TransactionsPerDayUi(
                date = "20 Марта",
                totalAmount = "30 000 ₽",
                transactions = persistentListOf(expensesTransactions[4])
            ),
            TransactionsPerDayUi(
                date = "25 Марта",
                totalAmount = "25 000 ₽",
                transactions = persistentListOf(expensesTransactions[5])
            ),
        )
    }

    fun getExpensesCategories(): ImmutableList<CategoryUi> {
        return expensesCategories.sortedBy { it.name }.toImmutableList()
    }

    fun getFilteredCurrencyList(query: String): ImmutableList<CurrencyUi> {
        return currencies.filter { item ->
            item.name.contains(query, ignoreCase = true) ||
                    item.currency.isoCode.contains(query, ignoreCase = true)
        }.toImmutableList()
    }

    fun getAccountScopeMenu(): List<MenuOption> = accountScopesMenu

    fun getAccountContent(): AccountContent = AccountContent(
        accountScopes = accountScopes.toImmutableList(),
        accountsExchangeRateStatus = "Найден актуальный курс:\n" + "1 $ = 100 ₽",
        selectedAccountScope = accountScopes.last()
    )

    fun getExchangeRateStatus(): String = exchangeRateStatus
}
