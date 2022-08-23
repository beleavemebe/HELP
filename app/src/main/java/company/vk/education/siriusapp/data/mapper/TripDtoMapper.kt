package company.vk.education.siriusapp.data.mapper

import company.vk.education.siriusapp.core.BiMapper
import company.vk.education.siriusapp.data.model.TripDto
import company.vk.education.siriusapp.data.model.UserDto
import company.vk.education.siriusapp.domain.model.*
import java.util.*
import javax.inject.Inject

class TripDtoMapper @Inject constructor(
    private val userDtoMapper: BiMapper<User, UserDto>
) : BiMapper<Trip, TripDto> {
    override fun mapTo(arg: Trip): TripDto {
        return TripDto(
            id = arg.id,
            startLatitude = arg.route.startLocation.latitude,
            startLongitude = arg.route.startLocation.longitude,
            endLatitude = arg.route.endLocation.latitude,
            endLongitude = arg.route.endLocation.longitude,
            dateMillis = arg.route.date.time,
            freePlaces = arg.freePlaces,
            host = userDtoMapper.mapTo(arg.host),
            passengers = arg.passengers.map(userDtoMapper::mapTo),
            taxiService = arg.taxiService.alias,
            taxiVehicleClass = arg.taxiVehicleClass.alias,
        )
    }

    override fun mapFrom(arg: TripDto): Trip {
        val taxiService = findTaxiService(arg.taxiService!!)
        return Trip(
            id = arg.id!!,
            route = TripRoute(
                startLocation = Location(arg.startLatitude!!, arg.startLongitude!!),
                endLocation = Location(arg.endLatitude!!, arg.endLongitude!!),
                date = Date(arg.dateMillis!!),
            ),
            freePlaces = arg.freePlaces!!,
            host = userDtoMapper.mapFrom(arg.host!!),
            passengers = arg.passengers!!.map(userDtoMapper::mapFrom),
            taxiService = taxiService,
            taxiVehicleClass = findVehicleClass(taxiService, arg.taxiVehicleClass!!),
        )
    }
}