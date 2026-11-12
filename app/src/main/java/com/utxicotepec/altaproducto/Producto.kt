package com.utxicotepec.altaproducto

data class Producto(
    val nombre: String,
    val marca: String,
    val precio: Int,
    val fechaCaducidad: Long,
    val categoria: String,
    val descripcion: String
)
