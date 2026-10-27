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
import com.example.model.*
import com.example.ui.theme.NavyPrimary

@Composable
fun DirectivosInspectoresChannel(
    currentUser: UserProfile?,
    documents: List<SharedDocument>,
    messages: List<DirectivoChatMessage>,
    onUploadDocument: (title: String, desc: String, fileName: String, fileType: SharedFileType, fileSize: String, targetAudience: String) -> Unit,
    onDeleteDocument: (String) -> Unit,
    onSendMessage: (text: String, attachName: String?, attachType: SharedFileType?, attachSize: String?) -> Unit,
    onToast: (String) -> Unit
) {
    val isInspector = currentUser?.role == UserRole.INSPECTOR
    var activeSubTab by remember { mutableStateOf("archivos") } // "archivos" or "chat"
    var fileFilter by remember { mutableStateOf("all") } // "all", "pdf", "photo", "doc"
    var showUploadForm by remember { mutableStateOf(false) }

    // Upload Form State
    var newTitle by remember { mutableStateOf("") }
    var newDesc by remember { mutableStateOf("") }
    var newFileType by remember { mutableStateOf(SharedFileType.PDF) }
    var newFileName by remember { mutableStateOf("") }
    var newFileSize by remember { mutableStateOf("1.5 MB") }

    // Chat Form State
    var chatText by remember { mutableStateOf("") }
    var attachedDoc by remember { mutableStateOf<SharedDocument?>(null) }
    var showAttachmentPicker by remember { mutableStateOf(false) }

    // Preview
    var previewDocId by remember { mutableStateOf<String?>(null) }

    val filteredDocs = documents.filter { doc ->
        when (fileFilter) {
            "pdf" -> doc.fileType == SharedFileType.PDF
            "photo" -> doc.fileType == SharedFileType.PHOTO
            "doc" -> doc.fileType == SharedFileType.DOC
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("directivos_inspectores_channel"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Channel Header Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isInspector) Color(0xFFFEF3C7) else Color(0xFFEEF2FF),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isInspector) Icons.Default.Work else Icons.Default.Shield,
                                contentDescription = null,
                                tint = if (isInspector) Color(0xFFD97706) else Color(0xFF4F46E5)
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = if (isInspector) "Ventana de Directivos" else "Ventana de Inspectores",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = Color(0xFF0F172A)
                            )
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFD1FAE5)
                            ) {
                                Text(
                                    text = "CANAL OFICIAL",
                                    color = Color(0xFF065F46),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = if (isInspector) "Compartí resoluciones, fotos y documentos con los Directivos." else "Consultá archivos oficiales de Inspección y chateá con el Inspector.",
                            color = Color(0xFF64748B),
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Sub-tabs row (Archivos vs Chat)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (activeSubTab == "archivos") NavyPrimary else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { activeSubTab = "archivos" }
                    ) {
                        Text(
                            text = "Archivos (${documents.size})",
                            color = if (activeSubTab == "archivos") Color.White else Color(0xFF475569),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (activeSubTab == "chat") NavyPrimary else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { activeSubTab = "chat" }
                    ) {
                        Text(
                            text = "Chat (${messages.size})",
                            color = if (activeSubTab == "chat") Color.White else Color(0xFF475569),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // TAB 1: ARCHIVOS
        if (activeSubTab == "archivos") {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Biblioteca de Documentos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF0F172A)
                        )
                        Button(
                            onClick = { showUploadForm = !showUploadForm },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (showUploadForm) "Cancelar" else "+ Subir Archivo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Filter chips
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(
                            "all" to "Todos",
                            "pdf" to "PDFs",
                            "photo" to "Fotos",
                            "doc" to "Word (Doc)"
                        ).forEach { (key, label) ->
                            FilterChip(
                                selected = fileFilter == key,
                                onClick = { fileFilter = key },
                                label = { Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF0F172A),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Upload Form
            if (showUploadForm) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF93C5FD)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Subir Nuevo Archivo para ${if (isInspector) "Directivos" else "Inspectores"}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = NavyPrimary
                        )

                        // Quick Presets
                        Text("Ejemplos Rápidos:", fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    newTitle = "Circular Técnica de Prevención 2026"
                                    newFileName = "Circular_Prevencion_2026.pdf"
                                    newFileType = SharedFileType.PDF
                                    newFileSize = "1.8 MB"
                                    newDesc = "Pautas obligatorias de seguridad en calderas y tableros eléctricos."
                                },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Ej. PDF", fontSize = 10.sp)
                            }
                            OutlinedButton(
                                onClick = {
                                    newTitle = "Foto Relevamiento Portón y Patio"
                                    newFileName = "Relevamiento_Patio.jpg"
                                    newFileType = SharedFileType.PHOTO
                                    newFileSize = "3.2 MB"
                                    newDesc = "Inspección visual de cerramiento perimetral de seguridad."
                                },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Ej. Foto", fontSize = 10.sp)
                            }
                            OutlinedButton(
                                onClick = {
                                    newTitle = "Acta de Inspección Trimestral"
                                    newFileName = "Acta_Inspeccion_Trimestral.docx"
                                    newFileType = SharedFileType.DOC
                                    newFileSize = "520 KB"
                                    newDesc = "Planilla editable con rúbricas de verificación edilicia."
                                },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Ej. Word", fontSize = 10.sp)
                            }
                        }

                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Título del Documento") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = newFileName,
                            onValueChange = { newFileName = it },
                            label = { Text("Nombre de Archivo") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = newDesc,
                            onValueChange = { newDesc = it },
                            label = { Text("Descripción u Observaciones") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Button(
                            onClick = {
                                if (newTitle.isNotBlank()) {
                                    val fName = if (newFileName.isNotBlank()) newFileName else "documento_oficial.pdf"
                                    onUploadDocument(
                                        newTitle,
                                        newDesc.ifBlank { "Documento oficial distrital." },
                                        fName,
                                        newFileType,
                                        newFileSize,
                                        if (isInspector) "Todos los Directivos del Distrito" else "Jefatura Distrital e Inspectores"
                                    )
                                    showUploadForm = false
                                    newTitle = ""
                                    newDesc = ""
                                    newFileName = ""
                                } else {
                                    onToast("Ingresá un título para el documento")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                        ) {
                            Text("Publicar Archivo en Canal", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Documents List
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                filteredDocs.forEach { doc ->
                    val isPdf = doc.fileType == SharedFileType.PDF
                    val isPhoto = doc.fileType == SharedFileType.PHOTO
                    val isPreviewOpen = previewDocId == doc.id

                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isPdf) Color(0xFFFEE2E2) else if (isPhoto) Color(0xFFD1FAE5) else Color(0xFFDBEAFE)
                                ) {
                                    Text(
                                        text = if (isPdf) "PDF OFICIAL" else if (isPhoto) "FOTOGRAFÍA" else "WORD (DOC)",
                                        color = if (isPdf) Color(0xFF991B1B) else if (isPhoto) Color(0xFF065F46) else Color(0xFF1E40AF),
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                                Text(doc.createdAt, fontSize = 10.sp, color = Color(0xFF94A3B8))
                            }

                            Text(
                                text = doc.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF0F172A)
                            )

                            Text(
                                text = doc.description,
                                fontSize = 11.sp,
                                color = Color(0xFF475569),
                                lineHeight = 15.sp
                            )

                            // Attachment Box
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFF8FAFC),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Icon(
                                            imageVector = if (isPdf) Icons.Default.Description else if (isPhoto) Icons.Default.Image else Icons.Default.Article,
                                            contentDescription = null,
                                            tint = if (isPdf) Color(0xFFDC2626) else if (isPhoto) Color(0xFF059669) else Color(0xFF2563EB),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Column {
                                            Text(doc.fileName, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                            Text("Tamaño: ${doc.fileSize} • Por: ${doc.uploadedByName}", fontSize = 9.sp, color = Color(0xFF64748B))
                                        }
                                    }
                                }
                            }

                            // Inline preview
                            if (isPreviewOpen) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFFF1F5F9),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text("Previsualización de Documento", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                                        Text("Destinatario: ${doc.targetAudience}", fontSize = 10.sp, color = Color(0xFF475569))
                                        Text("Verificación distrital completada. Archivo listo para inspección.", fontSize = 10.sp, color = Color(0xFF059669))
                                    }
                                }
                            }

                            // Actions Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { previewDocId = if (isPreviewOpen) null else doc.id },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isPreviewOpen) "Ocultar" else "Ver", fontSize = 11.sp)
                                }

                                Button(
                                    onClick = { onToast("Descargando ${doc.fileName}...") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                                ) {
                                    Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Descargar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                if (isInspector || doc.uploadedByName == currentUser?.name) {
                                    IconButton(
                                        onClick = { onDeleteDocument(doc.id) },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // TAB 2: CHAT OFICIAL
        if (activeSubTab == "chat") {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Chat header info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color(0xFF10B981), CircleShape)
                            )
                            Text(
                                text = if (isInspector) "Chat con Directivos" else "Chat con Inspección Distrital",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF0F172A)
                            )
                        }
                        Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFEFF6FF)) {
                            Text(
                                text = "${messages.size} msjs",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2563EB),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Message list
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 280.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        messages.forEach { msg ->
                            val isMe = msg.senderRole == currentUser?.role?.displayName ||
                                    (isInspector && msg.senderRole == "Inspector") ||
                                    (!isInspector && msg.senderRole == "Directivo")

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(
                                        topStart = 14.dp,
                                        topEnd = 14.dp,
                                        bottomStart = if (isMe) 14.dp else 2.dp,
                                        bottomEnd = if (isMe) 2.dp else 14.dp
                                    ),
                                    color = if (isMe) NavyPrimary else Color(0xFFF1F5F9),
                                    modifier = Modifier.widthIn(max = 280.dp)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = msg.senderName,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp,
                                                color = if (isMe) Color(0xFF93C5FD) else Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = msg.timestamp,
                                                fontSize = 9.sp,
                                                color = if (isMe) Color(0xFFCBD5E1) else Color(0xFF64748B)
                                            )
                                        }

                                        Text(
                                            text = msg.message,
                                            fontSize = 11.sp,
                                            color = if (isMe) Color.White else Color(0xFF1E293B),
                                            lineHeight = 15.sp,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )

                                        if (msg.attachmentName != null) {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = if (isMe) Color(0xFF1E3A8A) else Color(0xFFE2E8F0),
                                                modifier = Modifier.padding(top = 6.dp).fillMaxWidth()
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(6.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.AttachFile,
                                                        contentDescription = null,
                                                        tint = if (isMe) Color(0xFF93C5FD) else Color(0xFF2563EB),
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                    Text(
                                                        text = msg.attachmentName,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isMe) Color.White else Color(0xFF0F172A)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Quick replies
                    Text("Respuestas Rápidas:", fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val quick = if (isInspector) {
                            listOf("Confirmar recepción", "Enviar acta firmada", "Aprobado")
                        } else {
                            listOf("Recibido y notificado", "Adjuntamos acta", "Pedimos revisión")
                        }
                        quick.forEach { r ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF1F5F9),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { chatText = r }
                            ) {
                                Text(
                                    text = r,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF334155),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                            }
                        }
                    }

                    // Attached preview
                    if (attachedDoc != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFDBEAFE),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Adjunto: ${attachedDoc?.fileName}", fontSize = 10.sp, color = Color(0xFF1E40AF), fontWeight = FontWeight.Bold)
                                IconButton(onClick = { attachedDoc = null }, modifier = Modifier.size(20.dp)) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Color(0xFF1E40AF), modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }

                    // Input Form
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = {
                                if (documents.isNotEmpty()) {
                                    attachedDoc = documents.first()
                                    onToast("Archivo adjuntado: ${documents.first().fileName}")
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.AttachFile, contentDescription = "Adjuntar", tint = Color(0xFF475569))
                        }

                        OutlinedTextField(
                            value = chatText,
                            onValueChange = { chatText = it },
                            placeholder = { Text("Escribir mensaje oficial...", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true
                        )

                        IconButton(
                            onClick = {
                                if (chatText.isNotBlank() || attachedDoc != null) {
                                    onSendMessage(
                                        chatText.ifBlank { "Adjunto archivo oficial: ${attachedDoc?.fileName}" },
                                        attachedDoc?.fileName,
                                        attachedDoc?.fileType,
                                        attachedDoc?.fileSize
                                    )
                                    chatText = ""
                                    attachedDoc = null
                                }
                            },
                            modifier = Modifier
                                .background(NavyPrimary, CircleShape)
                                .size(42.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Send, contentDescription = "Enviar", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}
