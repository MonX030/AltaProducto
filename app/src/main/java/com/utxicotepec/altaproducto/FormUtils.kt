package com.utxicotepec.altaproducto

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun convertMillisToDate(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate().format(formatter)
}

/** Devuelve true si el formulario tiene algún error de validación. */
fun foundErrors(
    nombre: String,
    marca: String,
    precio: String,
    fecha: Long?,
    categoria: String,
    precioMinimo: Int
): Boolean {
    val precioInt = precio.toIntOrNull() ?: 0
    return nombre.isBlank() ||
        marca.isBlank() ||
        precioInt < precioMinimo ||
        fecha == null ||
        categoria.isBlank()
}

fun productoFormatter(producto: Producto): String {
    val result = StringBuilder()
    result.append("Producto: ${producto.nombre}\n")
    result.append("Marca: ${producto.marca}\n")
    result.append("Precio: \$${producto.precio} MXN\n")
    result.append("Fecha de caducidad: ${convertMillisToDate(producto.fechaCaducidad)}\n")
    result.append("Categoría: ${producto.categoria}\n")
    result.append("Descripción: ${producto.descripcion}")
    return result.toString()
}
