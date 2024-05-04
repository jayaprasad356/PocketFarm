package com.app.pocketfarm.model

class Review {




    var id: String? = null
    var user_id: String? = null
    var image: String? = null
    var status: String? = null
    var datetime: String? = null


    constructor(
        id: String?,
        user_id: String?,
        image: String?,
        status: String?,
        datetime: String?
    ) {
        this.id = id
        this.user_id = user_id
        this.image = image
        this.status = status
        this.datetime = datetime

    }


}