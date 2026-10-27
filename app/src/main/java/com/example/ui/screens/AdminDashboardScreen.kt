package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.*
import com.example.ui.components.DirectivosInspectoresChannel
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.viewmodel.AdminBottomTab
import com.example.viewmodel.AdminTopTab
import com.example.viewmodel.UiState

@Composable
fun AdminDashboardScreen(
    uiState: UiState,
    onAdminBottomTabSelected: (AdminBottomTab) -> Unit,
    onAdminTopTabSelected: (AdminTopTab) -> Unit,
    onOpenPublicView: () -> Unit,
    onLogout: () -> Unit,
    onBroadcastAlert: (title: String, desc: String, cat: String) -> Unit,
    onPublishProtocol: (title: String, cat: String, desc: String, steps: List<String>) -> Unit,
    onApproveRequest: (id: String, name: String) -> Unit,
    onRejectRequest: (id: String, name: String) -> Unit,
    onDeleteContact: (String) -> Unit,
    onSaveContact: (id: String?, name: String, phone: String) -> Unit,
    onDeleteProtocol: (String) -> Unit,
    onSaveProtocol: (id: String?, title: String, cat: String, desc: String, steps: List<String>, pdfUrl: String?) -> Unit,
    onDeleteResource: (String) -> Unit,
    onSaveResource: (id: String?, title: String, desc: String, type: String, linkOrFile: String, roles: List<UserRole>?) -> Unit,
    onUploadDocument: (title: String, desc: String, fileName: String, fileType: SharedFileType, fileSize: String, targetAudience: String) -> Unit,
    onDeleteDocument: (String) -> Unit,
    onSendMessage: (text: String, attachName: String?, attachType: SharedFileType?, attachSize: String?) -> Unit,
    onOpenFloorPlan: () -> Unit,
    onToast: (String) -> Unit
) {
    val user = uiState.currentUser
    val isInspector = user?.role == UserRole.INSPECTOR

    // Form states for Contact
    var showContactDialog by remember { mutableStateOf(false) }
    var editingContactId by remember { mutableStateOf<String?>(null) }
    var contactNameInput by remember { mutableStateOf("") }
    var contactPhoneInput by remember { mutableStateOf("") }

    // Form states for Protocol
    var showProtocolDialog by remember { mutableStateOf(false) }
    var editingProtoId by remember { mutableStateOf<String?>(null) }
    var protoTitleInput by remember { mutableStateOf("") }
    var protoCatInput by remember { mutableStateOf("Infraestructura / Fuego") }
    var protoDescInput by remember { mutableStateOf("") }
    var protoStepsInput by remember { mutableStateOf("") }

    // Broadcast Alert states
    var bcTitle by remember { mutableStateOf("") }
    var bcMsg by remember { mutableStateOf("") }
    var bcCat by remember { mutableStateOf("Informativa") }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D1C44))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Main Header
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
                            color = Color.White,
                            modifier = Modifier.size(34.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                            shadowElevation = 3.dp
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_escuelas_seguras_logo),
                                contentDescription = "Escudo",
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column {
                            Text("PCIA. DE BUENOS AIRES", color = Color.White.copy(alpha = 0.8f), fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Text("Jefatura Distrital San Miguel", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Button(
                            onClick = onOpenPublicView,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("public_view_button")
                        ) {
                            Icon(imageVector = Icons.Default.Visibility, contentDescription = null, tint = Color(0xFF93C5FD), modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Vista Pública", color = Color(0xFFBFDBFE), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFEF3C7)
                        ) {
                            Text(
                                text = user?.role?.displayName ?: "ADMIN",
                                color = Color(0xFF92400E),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }

                        IconButton(onClick = onLogout, modifier = Modifier.size(32.dp)) {
                            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Salir", tint = Color(0xFFEF4444))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Slidable Horizontal Navigation Pills
                val profileWindows = listOf(
                    Triple(AdminTopTab.INICIO, "Inicio", Icons.Default.Home),
                    if (isInspector) {
                        Triple(AdminTopTab.DIRECTIVOS, "Directivos", Icons.Default.Work)
                    } else {
                        Triple(AdminTopTab.INSPECTORES, "Inspectores", Icons.Default.Security)
                    },
                    Triple(AdminTopTab.PLANOS, "Planos", Icons.Default.Map),
                    Triple(AdminTopTab.CONTACTOS, "Contactos", Icons.Default.Phone),
                    Triple(AdminTopTab.PROTOCOLOS, "Protocolos", Icons.Default.Description),
                    Triple(AdminTopTab.RECURSOS, "Archivos/Recursos", Icons.Default.Folder)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(profileWindows) { (tab, label, icon) ->
                        val isSelected = uiState.adminTopTab == tab
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.12f),
                            modifier = Modifier.clickable {
                                onAdminBottomTabSelected(AdminBottomTab.PANEL)
                                onAdminTopTabSelected(tab)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) Color(0xFF0F172A) else Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                    color = if (isSelected) Color(0xFF0F172A) else Color.White
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                listOf(
                    Triple(AdminBottomTab.PANEL, "Panel", Icons.Default.Dashboard),
                    Triple(AdminBottomTab.ALERTAS, "Alertas", Icons.Default.Notifications),
                    Triple(AdminBottomTab.PUBLICAR, "Publicar", Icons.Default.AddCircle),
                    Triple(AdminBottomTab.CONFIG, "Config.", Icons.Default.Settings)
                ).forEach { (tab, label, icon) ->
                    val isSelected = uiState.adminBottomTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onAdminBottomTabSelected(tab) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (tab == AdminBottomTab.CONFIG && uiState.pendingRequests.isNotEmpty()) {
                                        Badge(containerColor = Color(0xFFF59E0B)) {
                                            Text("${uiState.pendingRequests.size}", fontSize = 9.sp)
                                        }
                                    }
                                }
                            ) {
                                Icon(imageVector = icon, contentDescription = label)
                            }
                        },
                        label = { Text(label, fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NavyPrimary,
                            selectedTextColor = NavyPrimary,
                            indicatorColor = Color(0xFFE2E8F0)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF0D1C44))
        ) {
            when (uiState.adminBottomTab) {
                AdminBottomTab.PANEL -> {
                    when (uiState.adminTopTab) {
                        AdminTopTab.INICIO -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                item {
                                    // Panel Banner Card
                                    Card(
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(2.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text("Panel de Control", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF0F172A))
                                            Text("Resumen del contenido publicado en la plataforma distrital.", fontSize = 11.sp, color = Color(0xFF64748B))
                                        }
                                    }
                                }

                                item {
                                    // Bento Grid (2x2)
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            // Card Planos
                                            Card(
                                                shape = RoundedCornerShape(18.dp),
                                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clickable { onAdminTopTabSelected(AdminTopTab.PLANOS) }
                                            ) {
                                                Column(modifier = Modifier.padding(14.dp)) {
                                                    Surface(shape = CircleShape, color = Color(0xFFEFF6FF), modifier = Modifier.size(32.dp)) {
                                                        Box(contentAlignment = Alignment.Center) {
                                                            Icon(imageVector = Icons.Default.Map, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(18.dp))
                                                        }
                                                    }
                                                    Spacer(modifier = Modifier.height(10.dp))
                                                    Text("✓ Cargado", color = Color(0xFF059669), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                                    Text("Plano de evacuación", color = Color(0xFF64748B), fontSize = 10.sp)
                                                }
                                            }

                                            // Card Protocolos
                                            Card(
                                                shape = RoundedCornerShape(18.dp),
                                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clickable { onAdminTopTabSelected(AdminTopTab.PROTOCOLOS) }
                                            ) {
                                                Column(modifier = Modifier.padding(14.dp)) {
                                                    Surface(shape = CircleShape, color = Color(0xFFEFF6FF), modifier = Modifier.size(32.dp)) {
                                                        Box(contentAlignment = Alignment.Center) {
                                                            Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(18.dp))
                                                        }
                                                    }
                                                    Spacer(modifier = Modifier.height(6.dp))
                                                    Text("${uiState.guidelines.size}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF0F172A))
                                                    Text("Protocolos", color = Color(0xFF64748B), fontSize = 10.sp)
                                                }
                                            }
                                        }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            // Card Recursos
                                            Card(
                                                shape = RoundedCornerShape(18.dp),
                                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clickable { onAdminTopTabSelected(AdminTopTab.RECURSOS) }
                                            ) {
                                                Column(modifier = Modifier.padding(14.dp)) {
                                                    Surface(shape = CircleShape, color = Color(0xFFEFF6FF), modifier = Modifier.size(32.dp)) {
                                                        Box(contentAlignment = Alignment.Center) {
                                                            Icon(imageVector = Icons.Default.Folder, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(18.dp))
                                                        }
                                                    }
                                                    Spacer(modifier = Modifier.height(6.dp))
                                                    Text("${uiState.resources.size}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF0F172A))
                                                    Text("Recursos", color = Color(0xFF64748B), fontSize = 10.sp)
                                                }
                                            }

                                            // Card Contactos
                                            Card(
                                                shape = RoundedCornerShape(18.dp),
                                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clickable { onAdminTopTabSelected(AdminTopTab.CONTACTOS) }
                                            ) {
                                                Column(modifier = Modifier.padding(14.dp)) {
                                                    Surface(shape = CircleShape, color = Color(0xFFEFF6FF), modifier = Modifier.size(32.dp)) {
                                                        Box(contentAlignment = Alignment.Center) {
                                                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(18.dp))
                                                        }
                                                    }
                                                    Spacer(modifier = Modifier.height(6.dp))
                                                    Text("${uiState.contacts.size}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF0F172A))
                                                    Text("Contactos", color = Color(0xFF64748B), fontSize = 10.sp)
                                                }
                                            }
                                        }
                                    }
                                }

                                item {
                                    // Direct Access Banner to Canal Directivos / Inspectores
                                    Card(
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(2.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                onAdminTopTabSelected(if (isInspector) AdminTopTab.DIRECTIVOS else AdminTopTab.INSPECTORES)
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(14.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                Surface(
                                                    shape = RoundedCornerShape(12.dp),
                                                    color = if (isInspector) Color(0xFFFEF3C7) else Color(0xFFEEF2FF),
                                                    modifier = Modifier.size(42.dp)
                                                ) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        Icon(
                                                            imageVector = if (isInspector) Icons.Default.Work else Icons.Default.Security,
                                                            contentDescription = null,
                                                            tint = if (isInspector) Color(0xFFD97706) else Color(0xFF4F46E5)
                                                        )
                                                    }
                                                }
                                                Column {
                                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                        Text(if (isInspector) "Canal Directivos" else "Canal Inspectores", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                                                        Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFDBEAFE)) {
                                                            Text("${uiState.sharedDocs.size} archivos", fontSize = 8.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E40AF), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                                        }
                                                    }
                                                    Text(if (isInspector) "Subí archivos y chateá con Directivos" else "Archivos oficiales y chat con Inspector", fontSize = 11.sp, color = Color(0xFF64748B))
                                                }
                                            }
                                            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFF2563EB))
                                        }
                                    }
                                }

                                item {
                                    // Recent Activities Feed
                                    Card(
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(2.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                            Text("ACTIVIDAD RECIENTE", fontWeight = FontWeight.ExtraBold, fontSize = 10.sp, color = Color(0xFF94A3B8), letterSpacing = 1.sp)
                                            uiState.recentActivities.forEach { act ->
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
                                                    Box(
                                                        modifier = Modifier
                                                            .size(8.dp)
                                                            .background(dotColor, CircleShape)
                                                            .offset(y = 4.dp)
                                                    )
                                                    Column {
                                                        Text(act.text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                                                        Text(act.time, fontSize = 10.sp, color = Color(0xFF94A3B8))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        AdminTopTab.DIRECTIVOS, AdminTopTab.INSPECTORES -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                item {
                                    DirectivosInspectoresChannel(
                                        currentUser = user,
                                        documents = uiState.sharedDocs,
                                        messages = uiState.directivoMessages,
                                        onUploadDocument = onUploadDocument,
                                        onDeleteDocument = onDeleteDocument,
                                        onSendMessage = onSendMessage,
                                        onToast = onToast
                                    )
                                }
                            }
                        }

                        AdminTopTab.PLANOS -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                item {
                                    Card(
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(2.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                            Text("Planos de Evacuación", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
                                            Text("El plano guardado se muestra en la vista del estudiante y docente.", fontSize = 11.sp, color = Color(0xFF64748B))

                                            Surface(
                                                shape = RoundedCornerShape(12.dp),
                                                color = Color(0xFFF1F5F9),
                                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                            ) {
                                                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = Color(0xFF475569), modifier = Modifier.size(32.dp))
                                                    Text(uiState.publishedPlan, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF0F172A))
                                                    Text(uiState.publishedPlanSize, fontSize = 10.sp, color = Color(0xFF64748B))
                                                }
                                            }

                                            Text(uiState.publishedPlanDesc, fontSize = 11.sp, color = Color(0xFF475569))

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Button(
                                                    onClick = onOpenFloorPlan,
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(10.dp),
                                                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                                                ) {
                                                    Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Previsualizar", fontSize = 11.sp)
                                                }

                                                OutlinedButton(
                                                    onClick = { onToast("Plano reemplazado por defecto") },
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(10.dp)
                                                ) {
                                                    Text("Reemplazar", fontSize = 11.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        AdminTopTab.CONTACTOS -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                item {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Contactos de Emergencia", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
                                        Button(
                                            onClick = {
                                                editingContactId = null
                                                contactNameInput = ""
                                                contactPhoneInput = ""
                                                showContactDialog = true
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                                        ) {
                                            Text("+ Agregar", fontSize = 11.sp)
                                        }
                                    }
                                }

                                items(uiState.contacts) { c ->
                                    Card(
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(2.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(14.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text(c.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                                                Text(c.phone, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color(0xFF2563EB))
                                            }
                                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                IconButton(
                                                    onClick = {
                                                        editingContactId = c.id
                                                        contactNameInput = c.name
                                                        contactPhoneInput = c.phone
                                                        showContactDialog = true
                                                    }
                                                ) {
                                                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF475569))
                                                }
                                                IconButton(onClick = { onDeleteContact(c.id) }) {
                                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFEF4444))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        AdminTopTab.PROTOCOLOS -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                item {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Protocolos de Emergencia", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
                                        Button(
                                            onClick = {
                                                editingProtoId = null
                                                protoTitleInput = ""
                                                protoCatInput = "Infraestructura / Fuego"
                                                protoDescInput = ""
                                                protoStepsInput = ""
                                                showProtocolDialog = true
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                                        ) {
                                            Text("+ Agregar", fontSize = 11.sp)
                                        }
                                    }
                                }

                                items(uiState.guidelines) { g ->
                                    Card(
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(2.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFEFF6FF)) {
                                                    Text(g.category, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E40AF), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                                }
                                                Row {
                                                    IconButton(
                                                        onClick = {
                                                            editingProtoId = g.id
                                                            protoTitleInput = g.title
                                                            protoCatInput = g.category
                                                            protoDescInput = g.description
                                                            protoStepsInput = g.steps.joinToString("\n")
                                                            showProtocolDialog = true
                                                        },
                                                        modifier = Modifier.size(30.dp)
                                                    ) {
                                                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF475569), modifier = Modifier.size(16.dp))
                                                    }
                                                    IconButton(onClick = { onDeleteProtocol(g.id) }, modifier = Modifier.size(30.dp)) {
                                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                                                    }
                                                }
                                            }
                                            Text(g.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                                            Text(g.description, fontSize = 11.sp, color = Color(0xFF64748B))
                                        }
                                    }
                                }
                            }
                        }

                        AdminTopTab.RECURSOS -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                item {
                                    Text("Gestión de Archivos y Recursos", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
                                    Text("Administrá guías, videos y documentos con permisos por rol.", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                                }

                                items(uiState.resources) { r ->
                                    Card(
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(2.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(r.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                                                IconButton(onClick = { onDeleteResource(r.id) }, modifier = Modifier.size(24.dp)) {
                                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                                                }
                                            }
                                            Text(r.description, fontSize = 11.sp, color = Color(0xFF64748B))
                                            Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFF1F5F9)) {
                                                Text("Tipo: ${r.type} • ${r.linkOrFile}", fontSize = 10.sp, color = Color(0xFF475569), modifier = Modifier.padding(4.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                AdminBottomTab.ALERTAS -> {
                    // Broadcast Alert Screen
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("Nueva Emisión Masiva", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0F172A))
                                    Text("Las alertas emitidas se enviarán de forma inmediata a toda la comunidad escolar.", fontSize = 11.sp, color = Color(0xFF64748B))

                                    OutlinedTextField(
                                        value = bcTitle,
                                        onValueChange = { bcTitle = it },
                                        label = { Text("Título de Alerta") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp)
                                    )

                                    OutlinedTextField(
                                        value = bcMsg,
                                        onValueChange = { bcMsg = it },
                                        label = { Text("Mensaje o Indicación") },
                                        modifier = Modifier.fillMaxWidth().height(100.dp),
                                        shape = RoundedCornerShape(12.dp)
                                    )

                                    Text("NIVEL / CATEGORÍA", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        listOf("Critica", "Informativa", "Clima").forEach { cat ->
                                            FilterChip(
                                                selected = bcCat == cat,
                                                onClick = { bcCat = cat },
                                                label = { Text(cat, fontSize = 11.sp) }
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            if (bcTitle.isNotBlank() && bcMsg.isNotBlank()) {
                                                onBroadcastAlert(bcTitle, bcMsg, bcCat)
                                                bcTitle = ""
                                                bcMsg = ""
                                            } else {
                                                onToast("Completá título y mensaje")
                                            }
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                        modifier = Modifier.fillMaxWidth().height(48.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Enviar Alerta Masiva", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                AdminBottomTab.PUBLICAR -> {
                    // Quick Publish Protocol
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("Publicar Nuevo Protocolo", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF0F172A))
                                    Text("Cargá directrices paso a paso para docentes y alumnos.", fontSize = 11.sp, color = Color(0xFF64748B))

                                    var pTitle by remember { mutableStateOf("") }
                                    var pCat by remember { mutableStateOf("Infraestructura") }
                                    var pDesc by remember { mutableStateOf("") }
                                    var pSteps by remember { mutableStateOf("") }

                                    OutlinedTextField(
                                        value = pTitle,
                                        onValueChange = { pTitle = it },
                                        label = { Text("Título del Protocolo") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp)
                                    )

                                    OutlinedTextField(
                                        value = pDesc,
                                        onValueChange = { pDesc = it },
                                        label = { Text("Descripción General") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp)
                                    )

                                    OutlinedTextField(
                                        value = pSteps,
                                        onValueChange = { pSteps = it },
                                        label = { Text("Pasos a Seguir (uno por renglón)") },
                                        modifier = Modifier.fillMaxWidth().height(100.dp),
                                        shape = RoundedCornerShape(12.dp)
                                    )

                                    Button(
                                        onClick = {
                                            if (pTitle.isNotBlank()) {
                                                val stepsList = pSteps.lines().filter { it.isNotBlank() }
                                                onPublishProtocol(pTitle, pCat, pDesc, stepsList)
                                                pTitle = ""
                                                pDesc = ""
                                                pSteps = ""
                                            } else {
                                                onToast("Ingresá un título")
                                            }
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Publicar Protocolo", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                AdminBottomTab.CONFIG -> {
                    // Access requests & settings
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Solicitudes de Acceso", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
                                        Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFFEF3C7)) {
                                            Text("${uiState.pendingRequests.size} pendientes", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }

                                    if (uiState.pendingRequests.isEmpty()) {
                                        Text("No hay solicitudes pendientes.", color = Color(0xFF94A3B8), fontSize = 11.sp)
                                    } else {
                                        uiState.pendingRequests.forEach { req ->
                                            Surface(
                                                shape = RoundedCornerShape(12.dp),
                                                color = Color(0xFFF8FAFC),
                                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Column {
                                                            Text(req.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF1E293B))
                                                            Text(req.email, fontSize = 10.sp, color = Color(0xFF64748B))
                                                        }
                                                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFDBEAFE)) {
                                                            Text(req.role.displayName, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E40AF), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                                        }
                                                    }

                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.End,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        TextButton(onClick = { onRejectRequest(req.id, req.name) }) {
                                                            Text("Rechazar", color = Color(0xFFEF4444), fontSize = 11.sp)
                                                        }
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Button(
                                                            onClick = { onApproveRequest(req.id, req.name) },
                                                            shape = RoundedCornerShape(8.dp),
                                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                                                        ) {
                                                            Text("Aprobar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text("MANTENIMIENTO Y AUDITORÍA", fontWeight = FontWeight.ExtraBold, fontSize = 10.sp, color = Color(0xFF64748B))
                                    Button(
                                        onClick = { onToast("Auditoría de seguridad exportada en JSON") },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Descargar Auditoría de Seguridad (JSON)")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add / Edit Contact Dialog
    if (showContactDialog) {
        AlertDialog(
            onDismissRequest = { showContactDialog = false },
            title = { Text(if (editingContactId != null) "Editar Contacto" else "Nuevo Contacto") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = contactNameInput,
                        onValueChange = { contactNameInput = it },
                        label = { Text("Nombre") }
                    )
                    OutlinedTextField(
                        value = contactPhoneInput,
                        onValueChange = { contactPhoneInput = it },
                        label = { Text("Teléfono") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (contactNameInput.isNotBlank() && contactPhoneInput.isNotBlank()) {
                        onSaveContact(editingContactId, contactNameInput, contactPhoneInput)
                        showContactDialog = false
                    }
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showContactDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Add / Edit Protocol Dialog
    if (showProtocolDialog) {
        AlertDialog(
            onDismissRequest = { showProtocolDialog = false },
            title = { Text(if (editingProtoId != null) "Editar Protocolo" else "Nuevo Protocolo") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = protoTitleInput,
                        onValueChange = { protoTitleInput = it },
                        label = { Text("Título") }
                    )
                    OutlinedTextField(
                        value = protoDescInput,
                        onValueChange = { protoDescInput = it },
                        label = { Text("Descripción") }
                    )
                    OutlinedTextField(
                        value = protoStepsInput,
                        onValueChange = { protoStepsInput = it },
                        label = { Text("Pasos (uno por línea)") },
                        modifier = Modifier.height(90.dp)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (protoTitleInput.isNotBlank()) {
                        val steps = protoStepsInput.lines().filter { it.isNotBlank() }
                        onSaveProtocol(editingProtoId, protoTitleInput, protoCatInput, protoDescInput, steps, null)
                        showProtocolDialog = false
                    }
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showProtocolDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
