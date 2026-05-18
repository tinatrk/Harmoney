package com.example.harmoney.domain.models

import com.example.harmoney.base.IdEnum
import com.example.harmoney.base.IdEnumRegistry

enum class CategoryIcons(
    override val id: Long
) : IdEnum {
    // финансы
    IC_BANK(id = 1),
    IC_CARD(id = 2),
    IC_EXPENSE(id = 3),
    IC_INCOME(id = 4),
    IC_MONEY_1(id = 5),
    IC_MONEY_2(id = 6),
    IC_MONEY_3(id = 7),
    IC_PURSE(id = 8),
    IC_WALLET(id = 9),
    IC_PIGGY_CAPITAL(id = 10),
    IC_DEBT(id = 11),
    IC_OVERSPEND(id = 12),
    IC_BILL(id = 13),
    IC_MORTGAGE(id = 14),
    IC_RENTAL(id = 15),
    IC_WELFARE(id = 16),

    // дом
    IC_HOME_1(id = 17),
    IC_HOME_2(id = 18),
    IC_FAMILY(id = 19),
    IC_BABY(id = 20),
    IC_BABY_WHEELCHAIR(id = 21),
    IC_BASKET(id = 22),
    IC_CLEANING(id = 23),
    IC_SERVICE(id = 24),
    IC_SHOP_CART(id = 25),

    //красота и здоровье
    IC_COSMETICS(id = 26),
    IC_HAIRCUT(id = 27),
    IC_CHARITY(id = 28),
    IC_CURE(id = 29),
    IC_HEALTH(id = 30),

    // транспорт
    IC_CAR(id = 31),
    IC_TRANSPORT_1(id = 32),
    IC_TRANSPORT_2(id = 33),
    IC_TRANSPORT_3(id = 34),
    IC_GAS_STATION(id = 35),
    IC_BICYCLE(id = 36),

    // спорт
    IC_SPORT_1(id = 37),
    IC_SPORT_2(id = 38),
    IC_SPORT_3(id = 39),
    IC_YOGA_1(id = 40),
    IC_YOGA_2(id = 41),
    IC_YOGA_3(id = 42),
    IC_FITNESS_1(id = 43),
    IC_FITNESS_2(id = 46),
    IC_FITNESS_3(id = 44),
    IC_FITNESS_4(id = 45),

    //животные
    IC_PET(id = 47),
    IC_CAT(id = 48),
    IC_DOG(id = 49),
    IC_PAW(id = 50),
    IC_BALANCE(id = 51),
    IC_DINOSAUR(id = 52),
    IC_DUCK(id = 53),
    IC_FROG(id = 54),
    IC_OCTOPUS(id = 55),
    IC_SQUIRREL(id = 56),

    //хобби
    IC_ART_1(id = 57),
    IC_ART_2(id = 58),
    IC_GAMES_1(id = 59),
    IC_GAMES_2(id = 60),
    IC_BOOKS(id = 61),
    IC_GUITAR(id = 62),

    // события
    IC_EVENT(id = 63),
    IC_DINNER(id = 64),
    IC_CONCERT(id = 65),
    IC_TICKETS(id = 66),
    IC_MOVIE(id = 67),
    IC_PARTY(id = 68),

    // общее
    IC_HEALTHY_FOOD(id = 69),
    IC_CAKE(id = 70),
    IC_COFFEE(id = 71),
    IC_CLOTHES(id = 72),
    IC_SUITCASE(id = 73),
    IC_COMPUTER(id = 74),
    IC_LAUNDRY(id = 75),
    IC_PHONE_INTERNET(id = 76),
    IC_GIFT(id = 77),
    IC_EDUCATION(id = 78),
    IC_TRAVEL(id = 79),
    IC_VACATION_1(id = 80),
    IC_VACATION_2(id = 81),

    // другое
    IC_GOAL(id = 82),
    IC_EULER(id = 83);

    companion object {
        private val registry = IdEnumRegistry(CategoryIcons::class.java) { it.id }
        fun fromId(id: Long) = registry.fromId(id)
    }
}
