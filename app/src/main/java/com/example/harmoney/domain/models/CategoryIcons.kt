package com.example.harmoney.domain.models

import androidx.annotation.DrawableRes
import com.example.harmoney.R
import com.example.harmoney.base.IdEnum
import com.example.harmoney.base.IdEnumRegistry

enum class CategoryIcons(
    override val id: Long, @DrawableRes val resIconId: Int
) : IdEnum {
    // финансы
    IC_BANK(id = 1, resIconId = R.drawable.ic_bank_24px),
    IC_CARD(id = 2, resIconId = R.drawable.ic_card_24px),
    IC_EXPENSE(id = 3, resIconId = R.drawable.ic_expense_24px),
    IC_INCOME(id = 4, resIconId = R.drawable.ic_income_24px),
    IC_MONEY_1(id = 5, resIconId = R.drawable.ic_money_1_24px),
    IC_MONEY_2(id = 6, resIconId = R.drawable.ic_money_2_24px),
    IC_MONEY_3(id = 7, resIconId = R.drawable.ic_money_3_24px),
    IC_PURSE(id = 8, resIconId = R.drawable.ic_purse_24px),
    IC_WALLET(id = 9, resIconId = R.drawable.ic_wallet_24px),
    IC_PIGGY_CAPITAL(id = 10, resIconId = R.drawable.ic_piggy_capital_24px),
    IC_DEBT(id = 11, resIconId = R.drawable.ic_debt_24px),
    IC_OVERSPEND(id = 12, resIconId = R.drawable.ic_overspend_24px),
    IC_BILL(id = 13, resIconId = R.drawable.ic_bill_24px),
    IC_MORTGAGE(id = 14, resIconId = R.drawable.ic_mortgage_24px),
    IC_RENTAL(id = 15, resIconId = R.drawable.ic_rental_24px),
    IC_WELFARE(id = 16, resIconId = R.drawable.ic_welfare_24px),

    // дом
    IC_HOME_1(id = 17, resIconId = R.drawable.ic_home_1_24px),
    IC_HOME_2(id = 18, resIconId = R.drawable.ic_home_2_24px),
    IC_FAMILY(id = 19, resIconId = R.drawable.ic_family_24px),
    IC_BABY(id = 20, resIconId = R.drawable.ic_baby_24px),
    IC_BABY_WHEELCHAIR(id = 21, resIconId = R.drawable.ic_baby_wheelchair_24px),
    IC_BASKET(id = 22, resIconId = R.drawable.ic_basket_24px),
    IC_CLEANING(id = 23, resIconId = R.drawable.ic_cleaning_24px),
    IC_SERVICE(id = 24, resIconId = R.drawable.ic_service_24px),
    IC_SHOP_CART(id = 25, resIconId = R.drawable.ic_shop_cart_24px),

    //красота и здоровье
    IC_COSMETICS(id = 26, resIconId = R.drawable.ic_cosmetics_24px),
    IC_HAIRCUT(id = 27, resIconId = R.drawable.ic_haircut_24px),
    IC_CHARITY(id = 28, resIconId = R.drawable.ic_charity_24px),
    IC_CURE(id = 29, resIconId = R.drawable.ic_cure_24px),
    IC_HEALTH(id = 30, resIconId = R.drawable.ic_health_24px),

    // транспорт
    IC_CAR(id = 31, resIconId = R.drawable.ic_car_24px),
    IC_TRANSPORT_1(id = 32, resIconId = R.drawable.ic_transport_1_24px),
    IC_TRANSPORT_2(id = 33, resIconId = R.drawable.ic_transport_2_24px),
    IC_TRANSPORT_3(id = 34, resIconId = R.drawable.ic_transport_3_24px),
    IC_GAS_STATION(id = 35, resIconId = R.drawable.ic_gas_station_24px),
    IC_BICYCLE(id = 36, resIconId = R.drawable.ic_bicycle_24px),

    // спорт
    IC_SPORT_1(id = 37, resIconId = R.drawable.ic_sport_1_24px),
    IC_SPORT_2(id = 38, resIconId = R.drawable.ic_sport_2_24px),
    IC_SPORT_3(id = 39, resIconId = R.drawable.ic_sport_3_24px),
    IC_YOGA_1(id = 40, resIconId = R.drawable.ic_yoga_1_24px),
    IC_YOGA_2(id = 41, resIconId = R.drawable.ic_yoga_2_24x),
    IC_YOGA_3(id = 42, resIconId = R.drawable.ic_yoga_3_24px),
    IC_FITNESS_1(id = 43, resIconId = R.drawable.ic_fitness_1_24px),
    IC_FITNESS_2(id = 46, resIconId = R.drawable.ic_fitness_2_24px),
    IC_FITNESS_3(id = 44, resIconId = R.drawable.ic_fitness_3_24px),
    IC_FITNESS_4(id = 45, resIconId = R.drawable.ic_fitness_4_24px),

    //животные
    IC_PET(id = 47, resIconId = R.drawable.ic_pet_24px),
    IC_CAT(id = 48, resIconId = R.drawable.ic_cat_24px),
    IC_DOG(id = 49, resIconId = R.drawable.ic_dog_24px),
    IC_PAW(id = 50, resIconId = R.drawable.ic_paw_24px),
    IC_BALANCE(id = 51, resIconId = R.drawable.ic_balance_24px),
    IC_DINOSAUR(id = 52, resIconId = R.drawable.ic_dinosaur_24px),
    IC_DUCK(id = 53, resIconId = R.drawable.ic_duck_24px),
    IC_FROG(id = 54, resIconId = R.drawable.ic_frog_24px),
    IC_OCTOPUS(id = 55, resIconId = R.drawable.ic_octopus_24px),
    IC_SQUIRREL(id = 56, resIconId = R.drawable.ic_squirrel_24px),

    //хобби
    IC_ART_1(id = 57, resIconId = R.drawable.ic_art_1_24px),
    IC_ART_2(id = 58, resIconId = R.drawable.ic_art_2_24px),
    IC_GAMES_1(id = 59, resIconId = R.drawable.ic_games_1_24px),
    IC_GAMES_2(id = 60, resIconId = R.drawable.ic_games_2_24px),
    IC_BOOKS(id = 61, resIconId = R.drawable.ic_books_24px),
    IC_GUITAR(id = 62, resIconId = R.drawable.ic_guitar_24px),

    // события
    IC_EVENT(id = 63, resIconId = R.drawable.ic_event_24px),
    IC_DINNER(id = 64, resIconId = R.drawable.ic_dinner_24px),
    IC_CONCERT(id = 65, resIconId = R.drawable.ic_concert_24px),
    IC_TICKETS(id = 66, resIconId = R.drawable.ic_tickets_24px),
    IC_MOVIE(id = 67, resIconId = R.drawable.ic_movie_24px),
    IC_PARTY(id = 68, resIconId = R.drawable.ic_party_24px),

    // общее
    IC_HEALTHY_FOOD(id = 69, resIconId = R.drawable.ic_healthy_food_24px),
    IC_CAKE(id = 70, resIconId = R.drawable.ic_cake_24px),
    IC_COFFEE(id = 71, resIconId = R.drawable.ic_coffee_24px),
    IC_CLOTHES(id = 72, resIconId = R.drawable.ic_clothes_24px),
    IC_SUITCASE(id = 73, resIconId = R.drawable.ic_suitcase_24px),
    IC_COMPUTER(id = 74, resIconId = R.drawable.ic_computer_24px),
    IC_LAUNDRY(id = 75, resIconId = R.drawable.ic_laundry_24px),
    IC_PHONE_INTERNET(id = 76, resIconId = R.drawable.ic_phone_internet_24px),
    IC_GIFT(id = 77, resIconId = R.drawable.ic_gift_24px),
    IC_EDUCATION(id = 78, resIconId = R.drawable.ic_education_24px),
    IC_TRAVEL(id = 79, resIconId = R.drawable.ic_travel_24px),
    IC_VACATION_1(id = 80, resIconId = R.drawable.ic_vacation_1_24px),
    IC_VACATION_2(id = 81, resIconId = R.drawable.ic_vacation_2_24px),

    // другое
    IC_GOAL(id = 82, resIconId = R.drawable.ic_goal_24px),
    IC_EULER(id = 83, resIconId = R.drawable.ic_euler_24px);

    companion object {
        private val registry = IdEnumRegistry(CategoryIcons::class.java) { it.id }
        fun fromId(id: Long) = registry.fromId(id)
    }
}
