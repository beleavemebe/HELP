package company.vk.education.siriusapp.core

interface DtoMapper<Entity, Dto> {
    fun mapToDto(arg: Entity): Dto
    fun mapToEntity(arg: Dto): Entity
}