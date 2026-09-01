package com.example.diseodeinterfaz

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.diseodeinterfaz.ui.theme.DiseñoDeInterfazTheme
import kotlinx.coroutines.delay

// 1. Solo dos rutas: Splash y Registro
enum class AppScreen {
    Splash,
    Register
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiseñoDeInterfazTheme {
                AppNavigation()
            }
        }
    }
}

// 2. Orquestador de Rutas
@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf(AppScreen.Splash) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (currentScreen) {
            AppScreen.Splash -> SplashScreen(
                modifier = Modifier.padding(innerPadding),
                onNavigateToRegister = { currentScreen = AppScreen.Register }
            )
            AppScreen.Register -> RegisterScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

// 3. Pantalla de Bienvenida (Carga automática)
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onNavigateToRegister: () -> Unit
) {
    // TEMPORIZADOR: Espera 3 segundos (3000 milisegundos) y navega
    LaunchedEffect(Unit) {
        delay(3000)
        onNavigateToRegister()
    }

    // CONFIGURACIÓN DEL CARGADOR DE GIF (Coil)
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.backgroup_loguin),
            contentDescription = "background_login",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Texto LOGIN libre arriba
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 600.dp)
        ) {
            Text(
                text = "LOGIN",
                fontSize = 32.sp,
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.Cursive,
                color = Color(0xFF1E1E1E)
            )
        }

        // Indicador de Carga anclado abajo
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = R.drawable.loading,
                contentDescription = "GIF Cargando",
                imageLoader = imageLoader,
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cargando...",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// 4. Pantalla de Registro
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier
) {
    // Variable para controlar el cierre de la app (Ya no dará error)
    val activity = LocalContext.current as? Activity

    var nombreCompleto by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var aceptoTerminos by remember { mutableStateOf(false) }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF1C1C1E),
        unfocusedBorderColor = Color(0xFFE5E5EA),
        focusedLabelColor = Color(0xFF1C1C1E),
        unfocusedLabelColor = Color(0xFF8E8E93),
        cursorColor = Color(0xFF1C1C1E)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFB))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crear Cuenta",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1C1C1E)
        )
        Text(
            text = "Ingresa tus credenciales para continuar",
            fontSize = 14.sp,
            color = Color(0xFF8E8E93),
            modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
        )

        OutlinedTextField(
            value = nombreCompleto,
            onValueChange = { nombreCompleto = it },
            label = { Text("Nombre") },
            placeholder = { Text("Ej. Alex Morgan", color = Color(0xFFC7C7CC)) },
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null, tint = Color(0xFF8E8E93)) },
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo Electrónico") },
            placeholder = { Text("ejemplo@correo.com", color = Color(0xFFC7C7CC)) },
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = Color(0xFF8E8E93)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            placeholder = { Text("+51 987 654 321", color = Color(0xFFC7C7CC)) },
            leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null, tint = Color(0xFF8E8E93)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            placeholder = { Text("Mínimo 8 caracteres", color = Color(0xFFC7C7CC)) },
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = Color(0xFF8E8E93)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = aceptoTerminos,
                onCheckedChange = { aceptoTerminos = it },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF1C1C1E))
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Acepto los términos y condiciones de uso",
                fontSize = 12.sp,
                color = Color(0xFF636366)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Acción Registro */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1C1E))
        ) {
            Text(
                text = "Registrarse",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para Salir de la App
        OutlinedButton(
            onClick = { activity?.finish() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
        ) {
            Text(
                text = "Salir",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    DiseñoDeInterfazTheme {
        AppNavigation()
    }
}