package com.example.weagri.Model

class MyPlan {
    var id: String? = null
    var products: String? = null
    var price: String? = null
    var daily_income: String? = null
    var monthly_income: String? = null
    var invite_bonus: String? = null
    var validity: String? = null
    var image: String? = null


    constructor(
        id: String?,
        products: String?,
        price: String?,
        daily_income: String?,
        monthly_income: String?,
        invite_bonus: String?,
        validity: String?,
        image: String?

    ) {
        this.id = id
        this.products = products
        this.price = price
        this.daily_income = daily_income
        this.monthly_income = monthly_income
        this.invite_bonus = invite_bonus
        this.validity = validity
        this.image = image
    }


}