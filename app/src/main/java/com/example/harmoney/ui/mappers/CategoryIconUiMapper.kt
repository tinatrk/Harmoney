package com.example.harmoney.ui.mappers

import com.example.harmoney.R
import com.example.harmoney.domain.models.CategoryIcons

@Suppress("detekt:CyclomaticComplexMethod", "detekt:LongMethod")
object CategoryIconUiMapper {
    fun CategoryIcons.toDrawableRes(): Int =
        when (this) {
            // финансы
            CategoryIcons.IC_BANK -> R.drawable.ic_bank_24px
            CategoryIcons.IC_CARD -> R.drawable.ic_card_24px
            CategoryIcons.IC_EXPENSE -> R.drawable.ic_expense_24px
            CategoryIcons.IC_INCOME -> R.drawable.ic_income_24px
            CategoryIcons.IC_MONEY_1 -> R.drawable.ic_money_1_24px
            CategoryIcons.IC_MONEY_2 -> R.drawable.ic_money_2_24px
            CategoryIcons.IC_MONEY_3 -> R.drawable.ic_money_3_24px
            CategoryIcons.IC_PURSE -> R.drawable.ic_purse_24px
            CategoryIcons.IC_WALLET -> R.drawable.ic_wallet_24px
            CategoryIcons.IC_PIGGY_CAPITAL -> R.drawable.ic_piggy_capital_24px
            CategoryIcons.IC_DEBT -> R.drawable.ic_debt_24px
            CategoryIcons.IC_OVERSPEND -> R.drawable.ic_overspend_24px
            CategoryIcons.IC_BILL -> R.drawable.ic_bill_24px
            CategoryIcons.IC_MORTGAGE -> R.drawable.ic_mortgage_24px
            CategoryIcons.IC_RENTAL -> R.drawable.ic_rental_24px
            CategoryIcons.IC_WELFARE -> R.drawable.ic_welfare_24px

            // дом
            CategoryIcons.IC_HOME_1 -> R.drawable.ic_home_1_24px
            CategoryIcons.IC_HOME_2 -> R.drawable.ic_home_2_24px
            CategoryIcons.IC_FAMILY -> R.drawable.ic_family_24px
            CategoryIcons.IC_BABY -> R.drawable.ic_baby_24px
            CategoryIcons.IC_BABY_WHEELCHAIR -> R.drawable.ic_baby_wheelchair_24px
            CategoryIcons.IC_BASKET -> R.drawable.ic_basket_24px
            CategoryIcons.IC_CLEANING -> R.drawable.ic_cleaning_24px
            CategoryIcons.IC_SERVICE -> R.drawable.ic_service_24px
            CategoryIcons.IC_SHOP_CART -> R.drawable.ic_shop_cart_24px

            //красота и здоровье
            CategoryIcons.IC_COSMETICS -> R.drawable.ic_cosmetics_24px
            CategoryIcons.IC_HAIRCUT -> R.drawable.ic_haircut_24px
            CategoryIcons.IC_CHARITY -> R.drawable.ic_charity_24px
            CategoryIcons.IC_CURE -> R.drawable.ic_cure_24px
            CategoryIcons.IC_HEALTH -> R.drawable.ic_health_24px

            // транспорт
            CategoryIcons.IC_CAR -> R.drawable.ic_car_24px
            CategoryIcons.IC_TRANSPORT_1 -> R.drawable.ic_transport_1_24px
            CategoryIcons.IC_TRANSPORT_2 -> R.drawable.ic_transport_2_24px
            CategoryIcons.IC_TRANSPORT_3 -> R.drawable.ic_transport_3_24px
            CategoryIcons.IC_GAS_STATION -> R.drawable.ic_gas_station_24px
            CategoryIcons.IC_BICYCLE -> R.drawable.ic_bicycle_24px

            // спорт
            CategoryIcons.IC_SPORT_1 -> R.drawable.ic_sport_1_24px
            CategoryIcons.IC_SPORT_2 -> R.drawable.ic_sport_2_24px
            CategoryIcons.IC_SPORT_3 -> R.drawable.ic_sport_3_24px
            CategoryIcons.IC_YOGA_1 -> R.drawable.ic_yoga_1_24px
            CategoryIcons.IC_YOGA_2 -> R.drawable.ic_yoga_2_24x
            CategoryIcons.IC_YOGA_3 -> R.drawable.ic_yoga_3_24px
            CategoryIcons.IC_FITNESS_1 -> R.drawable.ic_fitness_1_24px
            CategoryIcons.IC_FITNESS_2 -> R.drawable.ic_fitness_2_24px
            CategoryIcons.IC_FITNESS_3 -> R.drawable.ic_fitness_3_24px
            CategoryIcons.IC_FITNESS_4 -> R.drawable.ic_fitness_4_24px

            // животные
            CategoryIcons.IC_PET -> R.drawable.ic_pet_24px
            CategoryIcons.IC_CAT -> R.drawable.ic_cat_24px
            CategoryIcons.IC_DOG -> R.drawable.ic_dog_24px
            CategoryIcons.IC_PAW -> R.drawable.ic_paw_24px
            CategoryIcons.IC_BALANCE -> R.drawable.ic_balance_24px
            CategoryIcons.IC_DINOSAUR -> R.drawable.ic_dinosaur_24px
            CategoryIcons.IC_DUCK -> R.drawable.ic_duck_24px
            CategoryIcons.IC_FROG -> R.drawable.ic_frog_24px
            CategoryIcons.IC_OCTOPUS -> R.drawable.ic_octopus_24px
            CategoryIcons.IC_SQUIRREL -> R.drawable.ic_squirrel_24px

            // хобби
            CategoryIcons.IC_ART_1 -> R.drawable.ic_art_1_24px
            CategoryIcons.IC_ART_2 -> R.drawable.ic_art_2_24px
            CategoryIcons.IC_GAMES_1 -> R.drawable.ic_games_1_24px
            CategoryIcons.IC_GAMES_2 -> R.drawable.ic_games_2_24px
            CategoryIcons.IC_BOOKS -> R.drawable.ic_books_24px
            CategoryIcons.IC_GUITAR -> R.drawable.ic_guitar_24px

            // события
            CategoryIcons.IC_EVENT -> R.drawable.ic_event_24px
            CategoryIcons.IC_DINNER -> R.drawable.ic_dinner_24px
            CategoryIcons.IC_CONCERT -> R.drawable.ic_concert_24px
            CategoryIcons.IC_TICKETS -> R.drawable.ic_tickets_24px
            CategoryIcons.IC_MOVIE -> R.drawable.ic_movie_24px
            CategoryIcons.IC_PARTY -> R.drawable.ic_party_24px

            //общее
            CategoryIcons.IC_HEALTHY_FOOD -> R.drawable.ic_healthy_food_24px
            CategoryIcons.IC_CAKE -> R.drawable.ic_cake_24px
            CategoryIcons.IC_COFFEE -> R.drawable.ic_coffee_24px
            CategoryIcons.IC_CLOTHES -> R.drawable.ic_clothes_24px
            CategoryIcons.IC_SUITCASE -> R.drawable.ic_suitcase_24px
            CategoryIcons.IC_COMPUTER -> R.drawable.ic_computer_24px
            CategoryIcons.IC_LAUNDRY -> R.drawable.ic_laundry_24px
            CategoryIcons.IC_PHONE_INTERNET -> R.drawable.ic_phone_internet_24px
            CategoryIcons.IC_GIFT -> R.drawable.ic_gift_24px
            CategoryIcons.IC_EDUCATION -> R.drawable.ic_education_24px
            CategoryIcons.IC_TRAVEL -> R.drawable.ic_travel_24px
            CategoryIcons.IC_VACATION_1 -> R.drawable.ic_vacation_1_24px
            CategoryIcons.IC_VACATION_2 -> R.drawable.ic_vacation_2_24px

            // другое
            CategoryIcons.IC_GOAL -> R.drawable.ic_goal_24px
            CategoryIcons.IC_EULER -> R.drawable.ic_euler_24px
        }
}
