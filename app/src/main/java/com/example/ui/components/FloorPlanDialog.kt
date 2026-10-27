package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.NavyPrimary

@Composable
fun FloorPlanDialog(
    isOpen: Boolean,
    planName: String,
    planDesc: String,
    onClose: () -> Unit,
    onDownload: () -> Unit
) {
    if (!isOpen) return

    var showRoutes by remember { mutableStateOf(true) }
    var showExtinguishers by remember { mutableStateOf(true) }
    var showValves by remember { mutableStateOf(true) }

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f)
                .testTag("floor_plan_dialog"),
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF0F172A),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = NavyPrimary.copy(alpha = 0.3f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Map,
                                    contentDescription = "Plano",
                                    tint = Color(0xFF60A5FA)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Plano Oficial de Evacuación",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = planName,
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Layer Toggles
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E293B), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Capas:",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilterChip(
                            selected = showRoutes,
                            onClick = { showRoutes = !showRoutes },
                            label = { Text("Vías Escape", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFEA580C).copy(alpha = 0.3f),
                                selectedLabelColor = Color(0xFFFDBA74),
                                containerColor = Color(0xFF0F172A),
                                labelColor = Color.LightGray
                            )
                        )
                        FilterChip(
                            selected = showExtinguishers,
                            onClick = { showExtinguishers = !showExtinguishers },
                            label = { Text("Extintores", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFDC2626).copy(alpha = 0.3f),
                                selectedLabelColor = Color(0xFFFCA5A5),
                                containerColor = Color(0xFF0F172A),
                                labelColor = Color.LightGray
                            )
                        )
                        FilterChip(
                            selected = showValves,
                            onClick = { showValves = !showValves },
                            label = { Text("Válvulas", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF059669).copy(alpha = 0.3f),
                                selectedLabelColor = Color(0xFF6EE7B7),
                                containerColor = Color(0xFF0F172A),
                                labelColor = Color.LightGray
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Canvas Floor Plan Drawing
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFF020617), RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(16.dp))
                        .padding(8.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // Rooms background
                        val roomColor = Color(0xFF1E293B)
                        val strokeColor = Color(0xFF475569)

                        // Aula 101
                        drawRoundRect(
                            color = roomColor,
                            topLeft = Offset(w * 0.05f, h * 0.05f),
                            size = Size(w * 0.26f, h * 0.28f),
                            cornerRadius = CornerRadius(12f, 12f)
                        )
                        drawRoundRect(
                            color = strokeColor,
                            topLeft = Offset(w * 0.05f, h * 0.05f),
                            size = Size(w * 0.26f, h * 0.28f),
                            cornerRadius = CornerRadius(12f, 12f),
                            style = Stroke(width = 3f)
                        )

                        // Aula 102
                        drawRoundRect(
                            color = roomColor,
                            topLeft = Offset(w * 0.35f, h * 0.05f),
                            size = Size(w * 0.26f, h * 0.28f),
                            cornerRadius = CornerRadius(12f, 12f)
                        )
                        drawRoundRect(
                            color = strokeColor,
                            topLeft = Offset(w * 0.35f, h * 0.05f),
                            size = Size(w * 0.26f, h * 0.28f),
                            cornerRadius = CornerRadius(12f, 12f),
                            style = Stroke(width = 3f)
                        )

                        // Laboratorio
                        drawRoundRect(
                            color = roomColor,
                            topLeft = Offset(w * 0.65f, h * 0.05f),
                            size = Size(w * 0.30f, h * 0.28f),
                            cornerRadius = CornerRadius(12f, 12f)
                        )
                        drawRoundRect(
                            color = strokeColor,
                            topLeft = Offset(w * 0.65f, h * 0.05f),
                            size = Size(w * 0.30f, h * 0.28f),
                            cornerRadius = CornerRadius(12f, 12f),
                            style = Stroke(width = 3f)
                        )

                        // Dirección
                        drawRoundRect(
                            color = roomColor,
                            topLeft = Offset(w * 0.05f, h * 0.55f),
                            size = Size(w * 0.38f, h * 0.38f),
                            cornerRadius = CornerRadius(12f, 12f)
                        )
                        drawRoundRect(
                            color = strokeColor,
                            topLeft = Offset(w * 0.05f, h * 0.55f),
                            size = Size(w * 0.38f, h * 0.38f),
                            cornerRadius = CornerRadius(12f, 12f),
                            style = Stroke(width = 3f)
                        )

                        // Patio Central Seguro (Punto de Encuentro)
                        val patioColor = Color(0xFF0369A1).copy(alpha = 0.35f)
                        val patioBorder = Color(0xFF0284C7)
                        drawRoundRect(
                            color = patioColor,
                            topLeft = Offset(w * 0.48f, h * 0.55f),
                            size = Size(w * 0.47f, h * 0.38f),
                            cornerRadius = CornerRadius(12f, 12f)
                        )
                        drawRoundRect(
                            color = patioBorder,
                            topLeft = Offset(w * 0.48f, h * 0.55f),
                            size = Size(w * 0.47f, h * 0.38f),
                            cornerRadius = CornerRadius(12f, 12f),
                            style = Stroke(width = 4f)
                        )

                        // Safe Point Circle
                        drawCircle(
                            color = Color(0xFF10B981).copy(alpha = 0.3f),
                            radius = w * 0.12f,
                            center = Offset(w * 0.71f, h * 0.74f)
                        )
                        drawCircle(
                            color = Color(0xFF10B981),
                            radius = w * 0.12f,
                            center = Offset(w * 0.71f, h * 0.74f),
                            style = Stroke(width = 4f)
                        )

                        // Evacuation Corridor & Routes
                        if (showRoutes) {
                            val routeColor = Color(0xFFF97316)
                            // Main corridor line
                            drawLine(
                                color = routeColor,
                                start = Offset(w * 0.05f, h * 0.42f),
                                end = Offset(w * 0.95f, h * 0.42f),
                                strokeWidth = 8f,
                                cap = StrokeCap.Round
                            )
                            // Path into patio
                            drawLine(
                                color = routeColor,
                                start = Offset(w * 0.71f, h * 0.42f),
                                end = Offset(w * 0.71f, h * 0.62f),
                                strokeWidth = 6f,
                                cap = StrokeCap.Round
                            )
                        }

                        // Extinguishers Layer
                        if (showExtinguishers) {
                            val extColor = Color(0xFFEF4444)
                            drawRect(
                                color = extColor,
                                topLeft = Offset(w * 0.32f, h * 0.40f),
                                size = Size(28f, 28f)
                            )
                            drawRect(
                                color = extColor,
                                topLeft = Offset(w * 0.90f, h * 0.40f),
                                size = Size(28f, 28f)
                            )
                        }

                        // Valves Layer
                        if (showValves) {
                            // Gas Valve
                            drawRect(
                                color = Color(0xFFDC2626),
                                topLeft = Offset(w * 0.10f, h * 0.88f),
                                size = Size(32f, 32f)
                            )
                            // Electrical Disyuntor
                            drawRect(
                                color = Color(0xFFEAB308),
                                topLeft = Offset(w * 0.22f, h * 0.88f),
                                size = Size(32f, 32f)
                            )
                        }
                    }

                    // Room Labels overlay
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Text("Aula 101", color = Color(0xFFE2E8F0), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("Aula 102", color = Color(0xFFE2E8F0), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("Laboratorio", color = Color(0xFFE2E8F0), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Dirección / Sala Docentes", color = Color(0xFFCBD5E1), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Punto de Encuentro", color = Color(0xFF6EE7B7), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                                Text("(P.E. Seguro)", color = Color(0xFF34D399), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Plan Description
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1E293B),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = planDesc,
                        color = Color(0xFFCBD5E1),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(10.dp),
                        lineHeight = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onDownload,
                        modifier = Modifier.weight(1f).testTag("download_plan_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                    ) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Descargar PDF", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onClose,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }
}
