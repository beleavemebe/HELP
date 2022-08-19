package company.vk.education.siriusapp.core

interface BiMapper<T, V> {
    fun mapTo(arg: T): V
    fun mapFrom(arg: V): T
}