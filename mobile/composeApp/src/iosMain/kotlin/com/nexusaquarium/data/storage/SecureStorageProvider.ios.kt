package com.nexusaquarium.data.storage

actual fun createSecureStorage(): SecureStorage {
    return IosSecureStorage()
}
