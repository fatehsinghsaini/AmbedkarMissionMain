package com.info.work.ambedkarmission.model

class UploadMedia {
    var type = ""
    var path = ""
    var userName = ""
    var date = ""
    var key =""



    constructor()
    constructor(type: String, path: String, userName: String, date: String) {
        this.type = type
        this.path = path
        this.userName = userName
        this.date = date
    }


}
