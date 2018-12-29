package com.info.work.ambedkarmission.model

class LikeCount {
    var userName = ""
    var count = ""

    constructor()
    constructor(userName: String, count: String) {
        this.userName = userName
        this.count = count
    }
}