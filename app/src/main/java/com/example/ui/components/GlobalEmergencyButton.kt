package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.viewmodel.EmergencyAlertPayload

@Composable
fun GlobalEmergencyFloatingButton(
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        modifier = Modifier.padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer pulsing ring
        Box(
            modifier = Modifier
                .size(68.dp)
                .scale(pulseScale)
                .background(Color(0xFFEF4444).copy(alpha = 0.35f), CircleShape)
        )

        // Main SOS Button
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier
                .size(56.dp)
                .testTag("global_sos_button"),
            shape = CircleShape,
            containerColor = Color(0xFFDC2626),
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "Botón de Alerta SOS San Miguel",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun EmergencyAlertModal(
    payload: EmergencyAlertPayload?,
    onDismiss: () -> Unit
) {
    if (payload == null) return
    val context = LocalContext.current
    var showWhatsAppText by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .testTag("emergency_alert_dialog"),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
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
                            shape = CircleShape,
                            color = Color(0xFFD1FAE5),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF059669),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "AVISO GENERADO - OJOS EN ALERTA",
                                color = Color(0xFF065F46),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 9.sp
                            )
                            Text(
                                text = "¡Alerta emitida con éxito!",
                                color = Color(0xFF0F172A),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Alert details card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("ID Usuario:", color = Color(0xFF64748B), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = payload.userId,
                                color = Color(0xFFDC2626),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        Divider(color = Color(0xFFE2E8F0))

                        Text(
                            text = "Establecimiento Escolar:",
                            color = Color(0xFF64748B),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = payload.schoolName,
                            color = Color(0xFF0F172A),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = payload.schoolAddress,
                            color = Color(0xFF475569),
                            fontSize = 11.sp
                        )

                        Divider(color = Color(0xFFE2E8F0))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Latitud GPS:", color = Color(0xFF64748B), fontSize = 10.sp)
                                Text("${payload.lat}", color = Color(0xFF0F172A), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Longitud GPS:", color = Color(0xFF64748B), fontSize = 10.sp)
                                Text("${payload.lng}", color = Color(0xFF0F172A), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Open Google Maps button
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(payload.mapsUrl))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth().testTag("open_maps_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2563EB))
                ) {
                    Icon(imageVector = Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Abrir Ubicación en Google Maps", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // WhatsApp Ojos en Alerta trigger
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(payload.whatsappUrl))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth().testTag("whatsapp_alert_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Abrir en WhatsApp (Ojos en Alerta)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = { showWhatsAppText = !showWhatsAppText },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (showWhatsAppText) "Ocultar mensaje armado" else "Ver mensaje para Ojos en Alerta",
                        fontSize = 11.sp,
                        color = Color(0xFF475569)
                    )
                }

                if (showWhatsAppText) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF064E3B).copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    ) {
                        Text(
                            text = payload.messageText,
                            fontSize = 10.sp,
                            color = Color(0xFF065F46),
                            modifier = Modifier.padding(10.dp),
                            lineHeight = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A))
                ) {
                    Text("Entendido, Cerrar Aviso")
                }
            }
        }
    }
}
