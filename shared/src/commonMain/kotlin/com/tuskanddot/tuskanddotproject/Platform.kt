package com.tuskanddot.tuskanddotproject

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform