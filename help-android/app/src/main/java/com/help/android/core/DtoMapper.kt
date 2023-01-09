package com.help.android.core

interface DtoMapper<Entity, Dto> {
    fun mapToDto(arg: Entity): Dto
    fun mapToEntity(arg: Dto): Entity
}