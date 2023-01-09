package com.help.android.data.mapper

import com.help.android.core.DtoMapper
import com.help.android.data.model.TripDto
import com.help.android.domain.model.*
import com.help.android.data.model.UserDto
import java.util.*
import javax.inject.Inject

class TripDtoMapper @Inject constructor(
    private val userDtoMapper: DtoMapper<User, UserDto>
) : DtoMapper<Trip, TripDto> {
    override fun mapToDto(arg: Trip): TripDto {
        return TripDto(
            id = arg.id,
            startLatitude = arg.route.startLocation.latitude,
            startLongitude = arg.route.startLocation.longitude,
            endLatitude = arg.route.endLocation.latitude,
            endLongitude = arg.route.endLocation.longitude,
            dateMillis = arg.route.date.time,
            freePlaces = arg.freePlaces,
            host = userDtoMapper.mapToDto(arg.host),
            passengers = arg.passengers.map(userDtoMapper::mapToDto),
            taxiService = arg.taxiService.alias,
            taxiVehicleClass = arg.taxiVehicleClass.alias,
        )
    }

    override fun mapToEntity(arg: TripDto): Trip {
        val taxiService = findTaxiService(arg.taxiService!!)
        return Trip(
            id = arg.id!!,
            route = TripRoute(
                startLocation = Location(arg.startLatitude!!, arg.startLongitude!!),
                endLocation = Location(arg.endLatitude!!, arg.endLongitude!!),
                date = Date(arg.dateMillis!!),
            ),
            freePlaces = arg.freePlaces!!,
            host = userDtoMapper.mapToEntity(arg.host!!),
            passengers = arg.passengers!!.map(userDtoMapper::mapToEntity),
            taxiService = taxiService,
            taxiVehicleClass = findVehicleClass(taxiService, arg.taxiVehicleClass!!),
        )
    }
}