package com.nexusaquarium.data.storage

import com.nexusaquarium.NexusApplication

actual fun createSecureStorage(): SecureStorage {
    return AndroidSecureStorage(NexusApplication.context)
}
