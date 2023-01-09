package com.help.android.data.mapper

import com.help.android.core.DtoMapper
import com.help.android.data.model.UserDto
import com.help.android.domain.model.User
import com.help.android.domain.model.UserContacts
import javax.inject.Inject

class UserDtoMapper @Inject constructor() : DtoMapper<User, UserDto> {
    override fun mapToDto(arg: User): UserDto {
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

    override fun mapToEntity(arg: UserDto): User {
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