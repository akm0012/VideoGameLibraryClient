package com.andrewkingmarshall.beachbuddy.extensions

fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { it.toLowerCase().capitalize() }