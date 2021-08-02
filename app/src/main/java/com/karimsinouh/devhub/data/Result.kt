package com.karimsinouh.devhub.data

data class Result<T>(
    val isSuccessful:Boolean,
    val data:T?=null,
    val message:String?=null
)
