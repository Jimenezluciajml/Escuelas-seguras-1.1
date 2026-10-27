package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.RecentActivity
import com.example.model.UserRole

@Composable
fun SimulatorModal(
    isOpen: Boolean,
    onClose: () -> Unit,
    onQuickLogin: (UserRole) -> Unit,
    onTriggerPushAlert: () -> Unit,
    onSimulateApplicant: () -> Unit,
    onSimulateReport: () -> Unit,
    onReset: () -> Unit,
    recentActivities: List<RecentActivity>
) {
    if (!isOpen) return

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.88f)
                .testTag("simulator_modal"),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF0F172A),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF059669).copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "● SIMULADOR ACTIVO",
                                color = Color(0xFF34D399),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = "Control de Pruebas",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    IconButton(onClick = onClose) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar", tint = Color.LightGray)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Role quick switchers
                    item {
                        Text(
                            text = "CAMBIAR DE PERFIL ESCOLAR:",
                            color = Color(0xFF94A3B8),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        listOf(
                            Triple(UserRole.DIRECTIVO, "Directivo (Director)", "Control de Planes y Solicitudes"),
                            Triple(UserRole.INSPECTOR, "Inspector Distrital (Admin)", "Difusión masiva y Auditoría"),
                            Triple(UserRole.DOCENTE, "Docente / Profesor", "Protocolos, simulacros y reportes"),
                            Triple(UserRole.ALUMNO, "Alumno / Estudiante", "Trivia, reportes anónimos y mapas"),
                            Triple(UserRole.AUXILIAR, "Auxiliar (Mantenimiento)", "Válvulas, llaves y extintores")
                        ).forEach { (role, title, subtitle) ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF1E293B),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable {
                                        onQuickLogin(role)
                                        onClose()
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = when (role) {
                                            UserRole.DIRECTIVO -> Color(0xFFFEF3C7)
                                            UserRole.INSPECTOR -> Color(0xFFEEF2FF)
                                            UserRole.DOCENTE -> Color(0xFFEFF6FF)
                                            UserRole.ALUMNO -> Color(0xFFD1FAE5)
                                            UserRole.AUXILIAR -> Color(0xFFCCFBF1)
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = when (role) {
                                                    UserRole.DIRECTIVO -> Icons.Default.Work
                                                    UserRole.INSPECTOR -> Icons.Default.Security
                                                    UserRole.DOCENTE -> Icons.Default.Person
                                                    UserRole.ALUMNO -> Icons.Default.School
                                                    UserRole.AUXILIAR -> Icons.Default.Build
                                                },
                                                contentDescription = null,
                                                tint = when (role) {
                                                    UserRole.DIRECTIVO -> Color(0xFFD97706)
                                                    UserRole.INSPECTOR -> Color(0xFF4F46E5)
                                                    UserRole.DOCENTE -> Color(0xFF2563EB)
                                                    UserRole.ALUMNO -> Color(0xFF059669)
                                                    UserRole.AUXILIAR -> Color(0xFF0D9488)
                                                },
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Column {
                                        Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text(subtitle, color = Color(0xFF94A3B8), fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }

                    // Live Event Simulation Buttons
                    item {
                        Text(
                            text = "DISPARADORES DE EVENTOS EN VIVO:",
                            color = Color(0xFF94A3B8),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            // Trigger Push Alert
                            Button(
                                onClick = {
                                    onTriggerPushAlert()
                                    onClose()
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF991B1B)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Forzar Alerta Push Masiva", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            // Simulate Docente Applicant
                            OutlinedButton(
                                onClick = {
                                    onSimulateApplicant()
                                    onClose()
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF93C5FD))
                            ) {
                                Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Simular Solicitante Docente", fontSize = 12.sp)
                            }

                            // Simulate Incident Report
                            OutlinedButton(
                                onClick = {
                                    onSimulateReport()
                                    onClose()
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFDE68A))
                            ) {
                                Icon(imageVector = Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Simular Reporte de Incidente", fontSize = 12.sp)
                            }
                        }
                    }

                    // Console of Live Events
                    item {
                        Text(
                            text = "CONSOLA DE EVENTOS:",
                            color = Color(0xFF94A3B8),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF020617),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B)),
                            modifier = Modifier.fillMaxWidth().heightIn(max = 160.dp)
                        ) {
                            LazyColumn(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(recentActivities) { act ->
                                    val dotColor = when (act.color) {
                                        "green" -> Color(0xFF10B981)
                                        "orange" -> Color(0xFFF59E0B)
                                        "red" -> Color(0xFFEF4444)
                                        else -> Color(0xFF3B82F6)
                                    }
                                    Row(
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(modifier = Modifier.size(6.dp).background(dotColor, CircleShape).offset(y = 4.dp))
                                        Column {
                                            Text(act.text, color = Color(0xFFE2E8F0), fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                            Text(act.time, color = Color(0xFF64748B), fontSize = 9.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Reset button
                Button(
                    onClick = {
                        onReset()
                        onClose()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reiniciar Simulador", color = Color.White, fontSize = 11.sp)
                }
            }
        }
    }
}
