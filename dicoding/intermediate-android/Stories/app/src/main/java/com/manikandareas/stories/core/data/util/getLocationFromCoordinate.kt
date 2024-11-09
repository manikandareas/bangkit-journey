package com.manikandareas.stories.core.data.util

import android.content.Context
import android.location.Geocoder
import com.manikandareas.stories.core.domain.util.Address
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

suspend fun getLocationFromCoordinate(context: Context, lat: Double, long: Double): Address? {
    return withContext(Dispatchers.IO) {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, long, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses.first()
                Address(
                    countryName = address.countryName,
                    countryCode = address.countryCode,
                    latitude = address.latitude,
                    longitude = address.longitude
                )
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}