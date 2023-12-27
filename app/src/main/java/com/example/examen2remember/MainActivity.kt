package com.example.examen2remember

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.examen2remember.data.DataSource
import com.example.examen2remember.data.Producto
import com.example.examen2remember.ui.theme.Examen2RememberTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Examen2RememberTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Pantallas()
                }
            }
        }
    }
}

@Composable
fun Pantallas(
    modifier: Modifier = Modifier, productosTotales: ArrayList<Producto> = DataSource.productos
) {

    var nombreProductoTextEditor by remember { mutableStateOf("") }
    var precioProductoTextEditor by remember { mutableStateOf("0") }

    var textoAbajo by remember { mutableStateOf("Todavía no han añadido ningun valor") }
    Column() {
        Text(
            text = "Hola soy alumno Remember",
            modifier = modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .weight(0.25f)
                .padding(start = 20.dp, top = 50.dp)
        )
        ProductosYTextEditBoton(modifier = modifier.weight(1f),
            productosTotales = productosTotales,
            valorPrimerTextEditor = nombreProductoTextEditor,
            valorSegundoTextEditor = precioProductoTextEditor,
            primerTextEditor = { nombreProductoTextEditor = it },
            segundoTextEditor = { precioProductoTextEditor = it },
            clickAction = {
                val precioNuevo: Int
                try{
                 precioNuevo = precioProductoTextEditor.toInt()
                }
                catch(e: Exception){
                    textoAbajo =
                        "El precio introducido no es un número entero"
                    return@ProductosYTextEditBoton
                }
                for (producto in productosTotales) {
                    if (producto.nombre.equals(nombreProductoTextEditor) && producto.precio == precioNuevo) {

                        textoAbajo =
                            "NO se ha modificado nada del producto ${producto.nombre}, el precio es el mismo"
                        return@ProductosYTextEditBoton
                    }
                    if (producto.nombre.equals(nombreProductoTextEditor) && producto.precio != precioNuevo) {

                        textoAbajo =
                            "Del producto ${producto.nombre} se ha modificado el precio de: ${producto.precio} euros a ${precioNuevo} euros"
                        producto.precio = precioNuevo
                        return@ProductosYTextEditBoton
                    }
                }
                //Solo llega aquí si no hay ningun producto con el mismo nombre, si no, entra por uno de los returns anteriores
                val nuevoProducto = Producto(nombreProductoTextEditor, precioNuevo)
                productosTotales.add(nuevoProducto)
                textoAbajo =
                    "Se ha añadido el producto ${nuevoProducto.nombre} con precio ${nuevoProducto.precio}"

            })
        Text(
            text = textoAbajo,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .weight(0.25f)

        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductosYTextEditBoton(
    modifier: Modifier,
    productosTotales: ArrayList<Producto>,
    clickAction: () -> Unit,
    valorPrimerTextEditor: String,
    valorSegundoTextEditor: String,
    primerTextEditor: (String) -> Unit,
    segundoTextEditor: (String) -> Unit,
) {
    Row(modifier = modifier) {
        Column(modifier = modifier.weight(1f)) {
            TextField(
                value = valorPrimerTextEditor,
                singleLine = true,
                modifier = Modifier.padding(16.dp),
                onValueChange = primerTextEditor,
                label = { Text("Nombre") },

                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                )
            )
            TextField(
                value = valorSegundoTextEditor,
                singleLine = true,
                modifier = Modifier.padding(16.dp),
                onValueChange = segundoTextEditor,
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                )
            )
            Button(
                onClick = {
                    clickAction.invoke() //Lo hago con invoke en vez de ponerlo directamente
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Add/Update producto")
            }
        }

        LazyColumn(
            modifier = modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(productosTotales) { producto ->
                Card(
                    modifier = modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Nombre: ${producto.nombre}",
                        modifier = Modifier
                            .background(Color.Yellow)
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                    Text(
                        text = "Precio: ${producto.precio.toString()}",
                        modifier = Modifier
                            .background(Color.Cyan)
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                }
            }
        }
    }
}