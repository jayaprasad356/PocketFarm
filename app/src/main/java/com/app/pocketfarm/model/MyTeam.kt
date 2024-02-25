package com.app.pocketfarm.model

class MyTeam {
    var id: String? = null
    var name: String? = null
    var mobile: String? = null
    var registered_datetime: String? = null
    var team_size: String? = null
    var total_assets: String? = null


    constructor(id: String?, name: String?, mobile: String?, registered_datetime: String?, team_size: String?, total_assets: String?) {
        this.id = id
        this.name = name
        this.mobile = mobile
        this.registered_datetime = registered_datetime
        this.team_size = team_size
        this.total_assets = total_assets
    }


}