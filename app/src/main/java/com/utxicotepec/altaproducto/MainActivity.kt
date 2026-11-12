package com.utxicotepec.altaproducto

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.utxicotepec.altaproducto.ui.theme.AltaProductoTheme

class MainActivity : ComponentActivity() {

    private var productoFilled: Producto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AltaProductoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(
                        onSave = { producto ->
                            productoFilled = producto
                            Log.i("RMR", productoFormatter(producto))
                        }
                    )
                }
            }
        }
    }
}
