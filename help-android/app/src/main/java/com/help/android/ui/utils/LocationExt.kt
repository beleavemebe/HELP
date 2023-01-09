package com.help.android.ui.utils

import com.yandex.mapkit.geometry.Point
import com.help.android.domain.model.Location

fun Location.toPoint() = Point(latitude, longitude)