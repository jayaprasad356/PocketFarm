package com.example.weagri.Model

class MyPlan {
    var id: String? = null
    var crop: String? = null
    var price: String? = null
    var daily_income: String? = null
    var total_income: String? = null
    var invite_bonus: String? = null
    var validity: String? = null
    var image: String? = null




    constructor(
        id: String?,
        crop: String?,
        price: String?,
        daily_income: String?,
        total_income: String?,
        invite_bonus: String?,
        validity: String?,
        image : String?
    ) {
        this.id = id
        this.crop = crop
        this.price = price
        this.daily_income = daily_income
        this.total_income = total_income
        this.invite_bonus = invite_bonus
        this.validity = validity
        this.image = image
    }









}