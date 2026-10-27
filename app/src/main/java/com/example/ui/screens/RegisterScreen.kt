package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.UserRole

@Composable
fun RegisterScreen(
    onRegisterSubmit: (name: String, surname: String, email: String, role: UserRole) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(UserRole.DOCENTE) }
    var roleDropdownExpanded by remember { mutableStateOf(false) }
    var isSubmitted by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF132A68),
            Color(0xFF0D1C44),
            Color(0xFF122453)
        )
    )

    val buttonGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF2563EB),
            Color(0xFF4F46E5)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .testTag("register_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header Emblem
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(38.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                    shadowElevation = 4.dp
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_escuelas_seguras_logo),
                        contentDescription = "Escudo",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Column {
                    Text(
                        text = "PCIA. DE BUENOS AIRES",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Jefatura Distrital San Miguel",
                        color = Color.White,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isSubmitted) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFD1FAE5),
                            modifier = Modifier.size(60.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF059669),
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "¡Solicitud enviada con éxito!",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = Color(0xFF065F46)
                        )

                        Text(
                            text = "Tu cuenta se guardó como solicitud pendiente en el Panel de Control del Inspector Distrital.",
                            fontSize = 12.sp,
                            color = Color(0xFF475569),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(top = 6.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .background(buttonGradient, RoundedCornerShape(12.dp))
                                .clickable { onNavigateToLogin() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Ir a Iniciar Sesión", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            modifier = Modifier
                                .width(95.dp)
                                .height(138.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_escuelas_seguras_logo),
                                contentDescription = "Logo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Solicitud de Acceso",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Seguridad e Higiene Escolar",
                            fontSize = 11.5.sp,
                            color = Color(0xFF64748B)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("NOMBRE/S", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF475569))
                                OutlinedTextField(
                                    value = name,
                                    onValueChange = { name = it },
                                    placeholder = { Text("Ej: María", fontSize = 11.sp, color = Color(0xFF94A3B8)) },
                                    modifier = Modifier.fillMaxWidth().testTag("reg_name_input"),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("APELLIDO/S", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF475569))
                                OutlinedTextField(
                                    value = surname,
                                    onValueChange = { surname = it },
                                    placeholder = { Text("Ej: González", fontSize = 11.sp, color = Color(0xFF94A3B8)) },
                                    modifier = Modifier.fillMaxWidth().testTag("reg_surname_input"),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text("CORREO ELECTRÓNICO", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF475569))
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                placeholder = { Text("nombre@ejemplo.com", fontSize = 11.sp, color = Color(0xFF94A3B8)) },
                                modifier = Modifier.fillMaxWidth().testTag("reg_email_input"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text("CONTRASEÑA", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF475569))
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                placeholder = { Text("••••••••", fontSize = 11.sp, color = Color(0xFF94A3B8)) },
                                modifier = Modifier.fillMaxWidth().testTag("reg_password_input"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { showPassword = !showPassword }) {
                                        Icon(
                                            imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = null,
                                            tint = Color(0xFF94A3B8),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Role Selector
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text("ROL", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF475569))
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedButton(
                                    onClick = { roleDropdownExpanded = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(selectedRole.displayName, color = Color(0xFF1E293B), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                        Text("▼", fontSize = 10.sp, color = Color(0xFF64748B))
                                    }
                                }

                                DropdownMenu(
                                    expanded = roleDropdownExpanded,
                                    onDismissRequest = { roleDropdownExpanded = false }
                                ) {
                                    UserRole.values().forEach { r ->
                                        DropdownMenuItem(
                                            text = { Text(r.displayName) },
                                            onClick = {
                                                selectedRole = r
                                                roleDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .background(buttonGradient, RoundedCornerShape(14.dp))
                                .clickable {
                                    if (name.isNotBlank() && surname.isNotBlank() && email.isNotBlank()) {
                                        onRegisterSubmit(name, surname, email, selectedRole)
                                        isSubmitted = true
                                    }
                                }
                                .testTag("reg_submit_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Enviar solicitud", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("¿Ya tenés cuenta? ", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
            TextButton(onClick = onNavigateToLogin) {
                Text("Iniciá sesión", color = Color(0xFF60A5FA), fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
            }
        }
    }
}
