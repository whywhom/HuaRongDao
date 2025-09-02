package com.mammoth.huarongdao

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform