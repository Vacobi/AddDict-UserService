package vstu.isd.userservice.dto

class FindUserRequestDto (
    val id: Long?,
    val login: String?
) {
    override fun toString(): String {
        return "FindUserRequestDto: id=$id, login=$login"
    }
}