package com.chatcore.smack.utilities

const val BASE_URL = "http://192.168.1.60:3005/v1/"
const val SOCKET_URL = "http://192.168.1.60:3005/"

const val URL_REGISTER = "${BASE_URL}account/register"
const val URL_LOGIN = "${BASE_URL}account/login"

const val URL_CREATE_USER = "${BASE_URL}user/add"
const val URL_GET_USER = "${BASE_URL}user/byEmail/"


//Broadcast Contants
const val BROADCAST_USER_DATA_CHANGE = "BROADCAST_USER_DATA_CHANGE"
