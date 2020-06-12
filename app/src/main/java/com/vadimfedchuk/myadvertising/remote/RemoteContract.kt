package com.vadimfedchuk.myadvertising.remote



class RemoteContract {

    companion object {

        const val BASE_URL = "https://shopreklama.ru/api/"

        const val REGISTRATION_PART = "register"
        const val REGISTRATION_SUBSCRIBE = "subscribe"
        const val CONFIRM_CODE_PART = "register/confirm"
        const val CONFIRM_RECOVERY_CODE_PART = "recovery/confirm"
        const val LOGIN_PART = "login"
        const val RECOVERY_PASSWORD_PART = "recovery"
        const val GET_ALL_MARKERS_PART = "markers/getAll"
        const val CREATE_ORDER_PART = "orders/create"
        const val CANCEL_ORDER_PART = "orders/cancel"
        const val GET_HISTORY_ORDERS_PART = "orders/getAll"
        const val ADD_AVATAR_USER_PART = "users/avatar"
        const val UPDATE_INFO_USER_PART = "users/update"

    }
}