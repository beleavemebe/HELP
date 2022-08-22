package company.vk.education.siriusapp.data.mapper

import company.vk.education.siriusapp.core.BiMapper
import company.vk.education.siriusapp.data.model.UserDto
import company.vk.education.siriusapp.domain.model.User
import company.vk.education.siriusapp.domain.model.UserContacts
import javax.inject.Inject

class UserDtoMapper @Inject constructor() : BiMapper<User, UserDto> {
    override fun mapTo(arg: User): UserDto {
        return UserDto(
            id = arg.id,
            name = arg.name,
            imageUrl = arg.imageUrl,
            phone = arg.contact.phoneNumber,
            vkLink = arg.contact.vkLink,
            tgLink = arg.contact.tgLink,
            rating = arg.rating,
        )
    }

    override fun mapFrom(arg: UserDto): User {
        return User(
            id = arg.id!!,
            name = arg.name!!,
            imageUrl = arg.imageUrl,
            contact = UserContacts(
                phoneNumber = arg.phone!!,
                vkLink = arg.vkLink,
                tgLink = arg.tgLink
            ),
            rating = arg.rating!!
        )
    }
}