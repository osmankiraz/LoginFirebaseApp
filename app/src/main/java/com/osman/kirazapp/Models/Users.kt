package com.osman.kirazapp.Models

class Users {
    var email :String? =null
    var password :String? =null
    var name_lastname:String?=null
    var age:String?=null
    var gender:String?=null
    var merital_status:String?=null
    var user_id:String?=null
    var verified:String="false"

    constructor(){}
    constructor(
        email: String?,
        password: String?,
        name_lastname: String?,
        age: String?,
        gender: String?,
        merital_status: String?,
        user_id: String?,
        verified: String
    ) {
        this.email = email
        this.password = password
        this.name_lastname = name_lastname
        this.age = age
        this.gender = gender
        this.merital_status = merital_status
        this.user_id = user_id
        this.verified = verified
    }


}