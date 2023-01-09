package com.help.android.core

interface Mapper<A, R> {
    fun map(arg: A): R
}