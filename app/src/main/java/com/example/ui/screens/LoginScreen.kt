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
import androidx.compose.material.icons.filled.*
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
fun LoginScreen(
    onLogin: (email: String, pass: String) -> Unit,
    onQuickLogin: (UserRole) -> Unit,
    onNavigateToRegister: () -> Unit,
    onForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("director.bolivar@sanmiguel.edu.ar") }
    var password by remember { mutableStateOf("directivo123") }
    var showPassword by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }

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
            .testTag("login_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Logo Header (Official Header)
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
                        contentDescription = "Escudo Escuelas Seguras",
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

            // Main White Login Card
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
                    // Official Escuelas Seguras Logo Image
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        modifier = Modifier
                            .width(135.dp)
                            .height(195.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_escuelas_seguras_logo),
                            contentDescription = "Escuelas Seguras Logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Acceso a la plataforma",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A),
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Seguridad e Higiene Escolar",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // CORREO ELECTRÓNICO
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "CORREO ELECTRÓNICO",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF475569),
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("nombre@ejemplo.com", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                            modifier = Modifier.fillMaxWidth().testTag("login_email_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color(0xFFF8FAFC),
                                focusedBorderColor = Color(0xFF3B82F6),
                                unfocusedBorderColor = Color(0xFFE2E8F0),
                                focusedTextColor = Color(0xFF0F172A),
                                unfocusedTextColor = Color(0xFF0F172A)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // CONTRASEÑA
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "CONTRASEÑA",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF475569),
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("••••••••", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                            modifier = Modifier.fillMaxWidth().testTag("login_password_input"),
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
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color(0xFFF8FAFC),
                                focusedBorderColor = Color(0xFF3B82F6),
                                unfocusedBorderColor = Color(0xFFE2E8F0),
                                focusedTextColor = Color(0xFF0F172A),
                                unfocusedTextColor = Color(0xFF0F172A)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Remember Me & Forgot Password
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { rememberMe = !rememberMe }
                        ) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF2563EB))
                            )
                            Text("Recordarme", fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                        }

                        TextButton(
                            onClick = onForgotPassword,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("¿Olvidaste tu contraseña?", fontSize = 11.sp, color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Gradient Log-In Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(buttonGradient, RoundedCornerShape(14.dp))
                            .clickable { onLogin(email, password) }
                            .testTag("login_submit_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ingresar",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Quick Role Switcher Buttons
                    Text(
                        text = "PERFILES DEL SISTEMA ESCOLAR (SIMULADOR):",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFFEF3C7),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        email = "director.bolivar@sanmiguel.edu.ar"
                                        password = "directivo123"
                                        onQuickLogin(UserRole.DIRECTIVO)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(imageVector = Icons.Default.Work, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Directivo", color = Color(0xFF92400E), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFEEF2FF),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC7D2FE)),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        email = "admin.inspector@sanmiguel.gob.ar"
                                        password = "admin1234"
                                        onQuickLogin(UserRole.INSPECTOR)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = Color(0xFF4F46E5), modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Inspector", color = Color(0xFF3730A3), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFEFF6FF),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        email = "juan.docente@sanmiguel.edu.ar"
                                        password = "docente123"
                                        onQuickLogin(UserRole.DOCENTE)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Docente", color = Color(0xFF1E40AF), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFD1FAE5),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA7F3D0)),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        email = "juan.alumno@sanmiguel.edu.ar"
                                        password = "alumno123"
                                        onQuickLogin(UserRole.ALUMNO)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(imageVector = Icons.Default.School, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Alumno", color = Color(0xFF065F46), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFCCFBF1),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF99F6E4)),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        email = "roberto.auxiliar@sanmiguel.edu.ar"
                                        password = "auxiliar123"
                                        onQuickLogin(UserRole.AUXILIAR)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(imageVector = Icons.Default.Build, contentDescription = null, tint = Color(0xFF0D9488), modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Auxiliar", color = Color(0xFF115E59), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Bottom link to register
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿No tenés cuenta? ", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                TextButton(onClick = onNavigateToRegister) {
                    Text("Solicitá acceso", color = Color(0xFF60A5FA), fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                }
            }
            Text(
                text = "Sistema de Gestión • Higiene y Seguridad Escolar",
                color = Color.White.copy(alpha = 0.45f),
                fontSize = 10.sp
            )
        }
    }
}
