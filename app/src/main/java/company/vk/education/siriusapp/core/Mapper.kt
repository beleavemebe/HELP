package company.vk.education.siriusapp.core

interface Mapper<A, R> {
    fun map(arg: A): R
}