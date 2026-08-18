package com.realtouchapp.admitted.network.entity

sealed class CountryCode(val code: Int) {
    object USA : CountryCode(1)
    object Canada : CountryCode(11)
    object UK  : CountryCode(44)
    // ... 其他國家代碼
}
