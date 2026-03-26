package com.example.harmoney.presentation.category.models

import androidx.annotation.StringRes
import com.example.harmoney.R
import com.example.harmoney.domain.models.CategoryIcons

enum class CategoryIconSubType(
    val id: Long,
    @StringRes val resId: Int,
    val iconIds: List<Long>
) {
    FINANCE(
        id = 1,
        resId = R.string.category_icon_sub_type_finance,
        iconIds = listOf(
            CategoryIcons.IC_BANK.id,
            CategoryIcons.IC_CARD.id,
            CategoryIcons.IC_EXPENSE.id,
            CategoryIcons.IC_INCOME.id,
            CategoryIcons.IC_MONEY_1.id,
            CategoryIcons.IC_MONEY_2.id,
            CategoryIcons.IC_MONEY_3.id,
            CategoryIcons.IC_PURSE.id,
            CategoryIcons.IC_WALLET.id,
            CategoryIcons.IC_PIGGY_CAPITAL.id,
            CategoryIcons.IC_DEBT.id,
            CategoryIcons.IC_OVERSPEND.id,
            CategoryIcons.IC_BILL.id,
            CategoryIcons.IC_MORTGAGE.id,
            CategoryIcons.IC_RENTAL.id,
            CategoryIcons.IC_WELFARE.id,
        )
    ),

    HOME(
        id = 2,
        resId = R.string.category_icon_sub_type_home,
        iconIds = listOf(
            CategoryIcons.IC_HOME_1.id,
            CategoryIcons.IC_HOME_2.id,
            CategoryIcons.IC_FAMILY.id,
            CategoryIcons.IC_BABY.id,
            CategoryIcons.IC_BABY_WHEELCHAIR.id,
            CategoryIcons.IC_BASKET.id,
            CategoryIcons.IC_CLEANING.id,
            CategoryIcons.IC_SERVICE.id,
            CategoryIcons.IC_SHOP_CART.id,
        )
    ),

    HEALTH(
        id = 3,
        resId = R.string.category_icon_sub_type_health,
        iconIds = listOf(
            CategoryIcons.IC_COSMETICS.id,
            CategoryIcons.IC_HAIRCUT.id,
            CategoryIcons.IC_CHARITY.id,
            CategoryIcons.IC_CURE.id,
            CategoryIcons.IC_HEALTH.id,
            CategoryIcons.IC_MONEY_2.id,
        )
    ),

    TRANSPORT(
        id = 4,
        resId = R.string.category_icon_sub_type_transport,
        iconIds = listOf(
            CategoryIcons.IC_CAR.id,
            CategoryIcons.IC_TRANSPORT_1.id,
            CategoryIcons.IC_TRANSPORT_2.id,
            CategoryIcons.IC_TRANSPORT_3.id,
            CategoryIcons.IC_GAS_STATION.id,
            CategoryIcons.IC_BICYCLE.id,
        )
    ),

    SPORT(
        id = 5,
        resId = R.string.category_icon_sub_type_sport,
        iconIds = listOf(
            CategoryIcons.IC_SPORT_1.id,
            CategoryIcons.IC_SPORT_2.id,
            CategoryIcons.IC_SPORT_3.id,
            CategoryIcons.IC_YOGA_1.id,
            CategoryIcons.IC_YOGA_2.id,
            CategoryIcons.IC_YOGA_3.id,
            CategoryIcons.IC_FITNESS_1.id,
            CategoryIcons.IC_FITNESS_2.id,
            CategoryIcons.IC_FITNESS_3.id,
            CategoryIcons.IC_FITNESS_4.id,
        )
    ),

    ANIMALS(
        id = 6,
        resId = R.string.category_icon_sub_type_animals,
        iconIds = listOf(
            CategoryIcons.IC_PET.id,
            CategoryIcons.IC_CAT.id,
            CategoryIcons.IC_DOG.id,
            CategoryIcons.IC_PAW.id,
            CategoryIcons.IC_BALANCE.id,
            CategoryIcons.IC_DINOSAUR.id,
            CategoryIcons.IC_DUCK.id,
            CategoryIcons.IC_FROG.id,
            CategoryIcons.IC_OCTOPUS.id,
            CategoryIcons.IC_SQUIRREL.id,
        )
    ),

    HOBBY(
        id = 7,
        resId = R.string.category_icon_sub_type_hobby,
        iconIds = listOf(
            CategoryIcons.IC_ART_1.id,
            CategoryIcons.IC_ART_2.id,
            CategoryIcons.IC_GAMES_1.id,
            CategoryIcons.IC_GAMES_2.id,
            CategoryIcons.IC_BOOKS.id,
            CategoryIcons.IC_GUITAR.id,
        )
    ),

    EVENTS(
        id = 8,
        resId = R.string.category_icon_sub_type_events,
        iconIds = listOf(
            CategoryIcons.IC_EVENT.id,
            CategoryIcons.IC_DINNER.id,
            CategoryIcons.IC_CONCERT.id,
            CategoryIcons.IC_TICKETS.id,
            CategoryIcons.IC_MOVIE.id,
            CategoryIcons.IC_PARTY.id,
        )
    ),

    COMMON(
        id = 9,
        resId = R.string.category_icon_sub_type_common,
        iconIds = listOf(
            CategoryIcons.IC_HEALTHY_FOOD.id,
            CategoryIcons.IC_CAKE.id,
            CategoryIcons.IC_COFFEE.id,
            CategoryIcons.IC_CLOTHES.id,
            CategoryIcons.IC_SUITCASE.id,
            CategoryIcons.IC_COMPUTER.id,
            CategoryIcons.IC_LAUNDRY.id,
            CategoryIcons.IC_PHONE_INTERNET.id,
            CategoryIcons.IC_GIFT.id,
            CategoryIcons.IC_EDUCATION.id,
            CategoryIcons.IC_TRAVEL.id,
            CategoryIcons.IC_VACATION_1.id,
            CategoryIcons.IC_VACATION_2.id,
        )
    ),

    OTHER(
        id = 10,
        resId = R.string.category_icon_sub_type_other,
        iconIds = listOf(
            CategoryIcons.IC_GOAL.id,
            CategoryIcons.IC_EULER.id,
        )
    )
}
