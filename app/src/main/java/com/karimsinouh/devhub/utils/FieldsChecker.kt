package com.karimsinouh.devhub.utils

fun isValidEmail(email:String):Boolean =
    email.length> 5 && email.contains("@") && email.contains(".")

fun isValidPassword(password:String):Boolean = password.length>=6

fun isValidName(name:String):Boolean = name.length>=3

fun isValidTitle(title:String):Boolean = title.length>=10

fun isValidContent(content:String):Boolean = content.length>=20