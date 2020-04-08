package `in`.bitspilani.eon.login.data

//will be changed acc to actual contract

data class LoginResponse(
    var data: Data
)

data class Data(
    var access: String,
    var refresh: String,
    var user: User
)

data class User(
    var active_status: Boolean,
    var address: String,
    var contact_number: String,
    var created_on: String,
    var email: String,
    var name: Any,
    var organization: String,
    var role: Role,
    var updated_on: String,
    var user_id: Int
)

data class Role(
    var id: Int,
    var role: String

)

data class ResetPasswordResponse(
    var message: String
)
data class GenerateCodeResponse(
    var message: String
)




