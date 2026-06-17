package com.example.harmoney.presentation.category.models

import com.example.harmoney.domain.models.CategoryIcons

enum class CategoryIconSubType(
    val id: Long,
    val icons: List<CategoryIcons>
) {
    COMMON(
        id = 1,
        icons = listOf(
            CategoryIcons.IC_HEALTHY_FOOD,
            CategoryIcons.IC_CAKE,
            CategoryIcons.IC_COFFEE,
            CategoryIcons.IC_CLOTHES,
            CategoryIcons.IC_SUITCASE,
            CategoryIcons.IC_COMPUTER,
            CategoryIcons.IC_LAUNDRY,
            CategoryIcons.IC_PHONE_INTERNET,
            CategoryIcons.IC_GIFT,
            CategoryIcons.IC_EDUCATION,
            CategoryIcons.IC_TRAVEL,
            CategoryIcons.IC_VACATION_1,
            CategoryIcons.IC_VACATION_2,
        )
    ),

    FINANCE(
        id = 2,
        icons = listOf(
            CategoryIcons.IC_BANK,
            CategoryIcons.IC_CARD,
            CategoryIcons.IC_EXPENSE,
            CategoryIcons.IC_INCOME,
            CategoryIcons.IC_MONEY_1,
            CategoryIcons.IC_MONEY_2,
            CategoryIcons.IC_MONEY_3,
            CategoryIcons.IC_PURSE,
            CategoryIcons.IC_WALLET,
            CategoryIcons.IC_PIGGY_CAPITAL,
            CategoryIcons.IC_DEBT,
            CategoryIcons.IC_OVERSPEND,
            CategoryIcons.IC_BILL,
            CategoryIcons.IC_MORTGAGE,
            CategoryIcons.IC_RENTAL,
            CategoryIcons.IC_WELFARE,
        )
    ),

    HOME(
        id = 3,
        icons = listOf(
            CategoryIcons.IC_HOME_1,
            CategoryIcons.IC_HOME_2,
            CategoryIcons.IC_FAMILY,
            CategoryIcons.IC_BABY,
            CategoryIcons.IC_BABY_WHEELCHAIR,
            CategoryIcons.IC_BASKET,
            CategoryIcons.IC_CLEANING,
            CategoryIcons.IC_SERVICE,
            CategoryIcons.IC_SHOP_CART,
        )
    ),

    HEALTH(
        id = 4,
        icons = listOf(
            CategoryIcons.IC_COSMETICS,
            CategoryIcons.IC_HAIRCUT,
            CategoryIcons.IC_CHARITY,
            CategoryIcons.IC_CURE,
            CategoryIcons.IC_HEALTH,
            CategoryIcons.IC_MONEY_2,
        )
    ),

    TRANSPORT(
        id = 5,
        icons = listOf(
            CategoryIcons.IC_CAR,
            CategoryIcons.IC_TRANSPORT_1,
            CategoryIcons.IC_TRANSPORT_2,
            CategoryIcons.IC_TRANSPORT_3,
            CategoryIcons.IC_GAS_STATION,
            CategoryIcons.IC_BICYCLE,
        )
    ),

    SPORT(
        id = 6,
        icons = listOf(
            CategoryIcons.IC_SPORT_1,
            CategoryIcons.IC_SPORT_2,
            CategoryIcons.IC_SPORT_3,
            CategoryIcons.IC_YOGA_1,
            CategoryIcons.IC_YOGA_2,
            CategoryIcons.IC_YOGA_3,
            CategoryIcons.IC_FITNESS_1,
            CategoryIcons.IC_FITNESS_2,
            CategoryIcons.IC_FITNESS_3,
            CategoryIcons.IC_FITNESS_4,
        )
    ),

    ANIMALS(
        id = 7,
        icons = listOf(
            CategoryIcons.IC_PET,
            CategoryIcons.IC_CAT,
            CategoryIcons.IC_DOG,
            CategoryIcons.IC_PAW,
            CategoryIcons.IC_BALANCE,
            CategoryIcons.IC_DINOSAUR,
            CategoryIcons.IC_DUCK,
            CategoryIcons.IC_FROG,
            CategoryIcons.IC_OCTOPUS,
            CategoryIcons.IC_SQUIRREL,
        )
    ),

    HOBBY(
        id = 8,
        icons = listOf(
            CategoryIcons.IC_ART_1,
            CategoryIcons.IC_ART_2,
            CategoryIcons.IC_GAMES_1,
            CategoryIcons.IC_GAMES_2,
            CategoryIcons.IC_BOOKS,
            CategoryIcons.IC_GUITAR,
        )
    ),

    EVENTS(
        id = 9,
        icons = listOf(
            CategoryIcons.IC_EVENT,
            CategoryIcons.IC_DINNER,
            CategoryIcons.IC_CONCERT,
            CategoryIcons.IC_TICKETS,
            CategoryIcons.IC_MOVIE,
            CategoryIcons.IC_PARTY,
        )
    ),

    OTHER(
        id = 10,
        icons = listOf(
            CategoryIcons.IC_GOAL,
            CategoryIcons.IC_EULER,
        )
    )
}
