package com.example.quizdesarrollobasadoenplataformas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight


class menu {
    private val banda1y2Valores = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    private val banda3Valores = listOf(1.0, 10.0, 100.0, 1000.0, 10000.0)
    private val toleranciaValores = listOf(0.05, 0.10, 0.20)


    fun calcularValor(banda1Index: Int, banda2Index: Int, banda3Index: Int): Double {
        return (banda1y2Valores[banda1Index] * 10 + banda1y2Valores[banda2Index] + banda3Valores
            [banda3Index])
    }
}
class RegistrarCalculadora {
    private val banda1y2Valores = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    private val banda3Valores = listOf(1.0, 10.0, 100.0, 1000.0, 10000.0) // Multiplicadores
    private val toleranciaValores = listOf(0.05, 0.10, 0.20) // 5%, 10%, 20%

    fun calcularValor(banda1Index: Int, banda2Index: Int, banda3Index: Int): Double { //los banda index son la posición (índice) del color seleccionado en una lista de valores predefinidos.
        return (banda1y2Valores[banda1Index] * 10 + banda1y2Valores[banda2Index]) * banda3Valores[banda3Index]
    }
    fun formatearResultado(valor: Double, toleranciaIndex: Int): Pair<String, String> {
        val tolerancia = toleranciaValores[toleranciaIndex]

        var valorConvertido = valor
        var unidad = "Ω"

        if(valor >= 1000000){
            valorConvertido /= 1000000
            unidad = "MΩ"
        } else if (valor >= 1000 ){
            valorConvertido /= 1000
            unidad = "kΩ"
        }

        val valorFormateado = "%.2f %s".format(valorConvertido, unidad)
        val toleranciaFormateada = "+%.0f%%".format(tolerancia * 100)

        return Pair(valorFormateado, toleranciaFormateada)
    }
}
@Composable
fun calculadoraResistencia(){
    val context = LocalContext.current
    val calculadora = remember { RegistrarCalculadora() }

    val colorValores = listOf(
        Color.Black, Color(0xFF8B4513), Color.Red, Color(0xFFFFA500), Color.Yellow,
        Color.Green, Color.Blue, Color(0xFF8A2BE2), Color.Gray, Color.White,
        Color(0xFFFFD700), Color(0xFFC0C0C0)
    )
    val colorNombres = listOf(
        "Negro", "Marrón", "Rojo", "Naranja", "Amarillo",
        "Verde", "Azul", "Violeta", "Gris", "Blanco",
        "Dorado", "Plateado", "Ninguno"
    )
    var menuBanda1Abierto by remember { mutableStateOf(false) }
    var menuBanda2Abierto by remember { mutableStateOf(false) }
    var menuBanda3Abierto by remember { mutableStateOf(false) }
    var menuToleranciaAbierto by remember { mutableStateOf(false) }

//para los valores seleccionados
    var banda1Index by remember { mutableStateOf(0) }
    var banda2Index by remember { mutableStateOf(0) }
    var banda3Index by remember { mutableStateOf(0) }
    var toleranciaIndex by remember { mutableStateOf(0) }

    val resistorValue = calculadora.calcularValor(banda1Index, banda2Index, banda3Index)
    val (valorFormateado, toleranciaFormateada) = calculadora.formatearResultado(resistorValue, toleranciaIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Calculadora de Resistencias",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .width(40.dp)
                .height(6.dp)
                .background(Color.LightGray)
            )

            // Banda 1
            Box(modifier = Modifier
                .width(24.dp)
                .height(80.dp)
                .background(colorValores[banda1Index])
            )
            // Banda 2
            Box(modifier = Modifier
                .width(24.dp)
                .height(80.dp)
                .background(colorValores[banda2Index])
            )

            // Banda 3 (multiplicador)
            Box(modifier = Modifier
                .width(24.dp)
                .height(80.dp)
                .background(colorValores[banda3Index])
            )

            // Banda de tolerancia (si es dorado: 10, plateado: 11, ninguno: 0)
            val toleranciaColorIndex = when (toleranciaIndex) {
                0 -> 10  // Dorado
                1 -> 11  // Plateado
                else -> 0  // Ninguno (Negro)
            }

            Box(modifier = Modifier
                .width(24.dp)
                .height(80.dp)
                .background(colorValores[toleranciaColorIndex])
            )

            Box(modifier = Modifier
                .width(40.dp)
                .height(6.dp)
                .background(Color.LightGray)
            )
        }
        // Selección de Banda 1
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { menuBanda1Abierto = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Banda 1: ${colorNombres[banda1Index]}")
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Mostrar opciones")
            }

            DropdownMenu(
                expanded = menuBanda1Abierto,
                onDismissRequest = { menuBanda1Abierto = false },
                modifier = Modifier.width(280.dp)
            ) {
                // Mostramos solo los colores de 0 a 9 (Negro a Blanco)
                for (i in 0..9) {
                    DropdownMenuItem(
                        text = { Text(colorNombres[i]) },
                        onClick = {
                            banda1Index = i
                            menuBanda1Abierto = false
                        },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(colorValores[i])
                            )
                        }
                    )
                }
            }
        }

        // Selección de Banda 2
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { menuBanda2Abierto = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Banda 2: ${colorNombres[banda2Index]}")
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Mostrar opciones")
            }

            DropdownMenu(
                expanded = menuBanda2Abierto,
                onDismissRequest = { menuBanda2Abierto = false },
                modifier = Modifier.width(280.dp)
            ) {
                // Mostramos solo los colores de 0 a 9 (Negro a Blanco)
                for (i in 0..9) {
                    DropdownMenuItem(
                        text = { Text(colorNombres[i]) },
                        onClick = {
                            banda2Index = i
                            menuBanda2Abierto = false
                        },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(colorValores[i])
                            )
                        }
                    )
                }
            }
        }

        // Selección de Banda 3
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { menuBanda3Abierto = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Banda 3 (Multiplicador): ${colorNombres[banda3Index]}")
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Mostrar opciones")
            }

            DropdownMenu(
                expanded = menuBanda3Abierto,
                onDismissRequest = { menuBanda3Abierto = false },
                modifier = Modifier.width(280.dp)
            ) {
                // Mostramos solo los colores para multiplicadores (Negro a Amarillo)
                for (i in 0..4) {
                    DropdownMenuItem(
                        text = {
                            val valor = when (i) {
                                0 -> "×1"
                                1 -> "×10"
                                2 -> "×100"
                                3 -> "×1,000"
                                4 -> "×10,000"
                                else -> ""
                            }
                            Text("${colorNombres[i]} $valor")
                        },
                        onClick = {
                            banda3Index = i
                            menuBanda3Abierto = false
                        },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(colorValores[i])
                            )
                        }
                    )
                }
            }
        }
        // Selección de Tolerancia
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { menuToleranciaAbierto = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                val toleranciaNombre = when (toleranciaIndex) {
                    0 -> "Dorado (±5%)"
                    1 -> "Plateado (±10%)"
                    2 -> "Ninguno (±20%)"
                    else -> ""
                }
                Text("Tolerancia: $toleranciaNombre")
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Mostrar opciones")
            }

            DropdownMenu(
                expanded = menuToleranciaAbierto,
                onDismissRequest = { menuToleranciaAbierto = false },
                modifier = Modifier.width(280.dp)
            ) {
                val toleranciaOpciones = listOf(
                    Triple(0, "Dorado", "±5%"),
                    Triple(1, "Plateado", "±10%"),
                    Triple(2, "Ninguno", "±20%")
                )

                toleranciaOpciones.forEach { (index, nombre, valor) ->
                    DropdownMenuItem(
                        text = { Text("$nombre $valor") },
                        onClick = {
                            toleranciaIndex = index
                            menuToleranciaAbierto = false
                        },
                        leadingIcon = {
                            val colorIndex = when (index) {
                                0 -> 10  // Dorado
                                1 -> 11  // Plateado
                                else -> 0  // Ninguno (Negro)
                            }
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(colorValores[colorIndex])
                            )
                        }
                    )
                }
            }
        }
        // Resultado
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Valor de la resistencia:",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = valorFormateado,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = toleranciaFormateada,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

