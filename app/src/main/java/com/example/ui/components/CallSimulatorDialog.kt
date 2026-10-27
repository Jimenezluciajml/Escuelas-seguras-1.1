package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VolumeUp
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

@Composable
fun CallSimulatorDialog(
    callInfo: Pair<String, String>?,
    onEndCall: () -> Unit
) {
    if (callInfo == null) return

    var isMuted by remember { mutableStateOf(false) }
    var isSpeaker by remember { mutableStateOf(true) }

    Dialog(
        onDismissRequest = onEndCall,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .wrapContentHeight()
                .testTag("call_simulator_dialog"),
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF0F172A),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "LLAMADA DE EMERGENCIA EN CURSO",
                    color = Color(0xFF34D399),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = callInfo.first,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    text = callInfo.second,
                    color = Color(0xFF60A5FA),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Pulsing Phone Circle
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFDC2626).copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFEF4444)),
                    modifier = Modifier.size(80.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Audio Controls (Mute / Speaker)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { isMuted = !isMuted },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isMuted) Color(0xFFF59E0B).copy(alpha = 0.2f) else Color(0xFF1E293B)
                        )
                    ) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = null,
                            tint = if (isMuted) Color(0xFFFBBF24) else Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isMuted) "Silenciado" else "Silenciar",
                            fontSize = 11.sp,
                            color = if (isMuted) Color(0xFFFBBF24) else Color.White
                        )
                    }

                    OutlinedButton(
                        onClick = { isSpeaker = !isSpeaker },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSpeaker) Color(0xFF3B82F6).copy(alpha = 0.2f) else Color(0xFF1E293B)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = if (isSpeaker) Color(0xFF60A5FA) else Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isSpeaker) "Altavoz ON" else "Altavoz OFF",
                            fontSize = 11.sp,
                            color = if (isSpeaker) Color(0xFF60A5FA) else Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Conexión directa simulada con la central distrital.",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onEndCall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("end_call_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Icon(imageVector = Icons.Default.CallEnd, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Finalizar Llamada", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
