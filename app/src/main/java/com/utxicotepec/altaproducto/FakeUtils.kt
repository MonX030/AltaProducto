package com.utxicotepec.altaproducto

import kotlinx.coroutines.delay

/** Simula la espera de una respuesta del servidor. */
suspend fun fakeWait(millis: Long = 2000L) {
    delay(millis)
}

/** Simula una llamada de red que siempre responde con éxito. */
suspend fun fakeNetworkSave(): Boolean {
    fakeWait()
    return true
}
