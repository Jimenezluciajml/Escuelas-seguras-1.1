package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.SampleData
import com.example.model.*
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.viewmodel.PublicModuleTab
import com.example.viewmodel.StudentBottomTab
import com.example.viewmodel.UiState

@Composable
fun PublicDashboardScreen(
    uiState: UiState,
    onTabSelected: (StudentBottomTab) -> Unit,
    onModuleSelected: (PublicModuleTab) -> Unit,
    onOpenAdminPanel: () -> Unit,
    onLogout: () -> Unit,
    onStartCall: (name: String, number: String) -> Unit,
    onOpenFloorPlan: () -> Unit,
    onOpenQuiz: () -> Unit,
    onOpenProfileEdit: () -> Unit,
    onMarkProtocolRead: (String) -> Unit,
    onSubmitReport: (school: String, cat: IncidentCategory, desc: String, anon: Boolean, photo: String?) -> Unit,
    onToast: (String) -> Unit
) {
    var selectedProtocol by remember { mutableStateOf<ProtocolGuideline?>(null) }
    var inquirySubject by remember { mutableStateOf("Consulta General") }
    var inquiryMessage by remember { mutableStateOf("") }
    var inquirySuccess by remember { mutableStateOf(false) }

    // Report Incident Form States
    var repSchool by remember { mutableStateOf(SampleData.schools.first().name) }
    var repCat by remember { mutableStateOf(IncidentCategory.INFRAESTRUCTURA) }
    var repDesc by remember { mutableStateOf("") }
    var repAnon by remember { mutableStateOf(false) }
    var repPhoto by remember { mutableStateOf<String?>(null) }
    var repSuccess by remember { mutableStateOf(false) }

    val user = uiState.currentUser
    val isPrivileged = user?.role == UserRole.DIRECTIVO || user?.role == UserRole.INSPECTOR

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyPrimary)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
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
                                contentDescription = "Escuelas Seguras Shield",
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column {
                            Text(
                                text = "PCIA. DE BUENOS AIRES",
                                color = Color.White.copy(alpha = 0.75f),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Jefatura Distrital San Miguel",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (isPrivileged) {
                            Button(
                                onClick = onOpenAdminPanel,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("admin_panel_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("PANEL", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFDEF7EC)
                            ) {
                                Text(
                                    text = "VISTA PÚBLICA",
                                    color = Color(0xFF03543F),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        IconButton(onClick = onLogout, modifier = Modifier.size(32.dp)) {
                            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Salir", tint = Color.White)
                        }
                    }
                }

                // Capsule Sub-Tabs (Emergencia, Protocolos, Recursos, Contacto) - Only in INICIO
                if (uiState.studentBottomTab == StudentBottomTab.INICIO) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(NavyLight, RoundedCornerShape(24.dp))
                            .padding(3.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        PublicModuleTab.values().forEach { tab ->
                            val isSelected = uiState.publicModuleTab == tab
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) Color.White else Color.Transparent,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        onModuleSelected(tab)
                                        selectedProtocol = null
                                    }
                            ) {
                                Text(
                                    text = tab.name.lowercase().replaceFirstChar { it.uppercase() },
                                    color = if (isSelected) NavyPrimary else Color(0xFFCBD5E1),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 7.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(Color(0xFFEA580C))
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                listOf(
                    Triple(StudentBottomTab.INICIO, "Inicio", Icons.Default.Home),
                    Triple(StudentBottomTab.ALERTAS, "Alertas", Icons.Default.Notifications),
                    Triple(StudentBottomTab.REPORTAR, "Reportar", Icons.Default.Assignment),
                    Triple(StudentBottomTab.PERFIL, "Perfil", Icons.Default.Person)
                ).forEach { (tab, label, icon) ->
                    val isSelected = uiState.studentBottomTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onTabSelected(tab) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (tab == StudentBottomTab.ALERTAS && uiState.alerts.isNotEmpty()) {
                                        Badge(containerColor = Color(0xFFEF4444)) {
                                            Text("${uiState.alerts.size}", fontSize = 9.sp)
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
                .background(Color(0xFFF8FAFC))
        ) {
            when (uiState.studentBottomTab) {
                StudentBottomTab.INICIO -> {
                    when (uiState.publicModuleTab) {
                        PublicModuleTab.EMERGENCIA -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                item {
                                    // Warning Banner
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = Color(0xFFFFFBEB),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(14.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Surface(
                                                shape = CircleShape,
                                                color = Color(0xFFFEF3C7),
                                                modifier = Modifier.size(36.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color(0xFFD97706))
                                                }
                                            }
                                            Column {
                                                Text("Ante una emergencia", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF92400E))
                                                Text("Consultá estos recursos inmediatamente ante cualquier situación de riesgo.", fontSize = 11.sp, color = Color(0xFFB45309), lineHeight = 15.sp)
                                            }
                                        }
                                    }
                                }

                                item {
                                    // Evacuation Plan Card
                                    Card(
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(2.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Surface(
                                                shape = CircleShape,
                                                color = Color(0xFFF1F5F9),
                                                modifier = Modifier.size(52.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Icon(imageVector = Icons.Default.Place, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(28.dp))
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text("Plano de Evacuación Oficial", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
                                            Text("${uiState.publishedPlan} (${uiState.publishedPlanSize})", fontSize = 11.sp, color = Color(0xFF64748B))

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Surface(
                                                shape = RoundedCornerShape(12.dp),
                                                color = Color(0xFFF8FAFC),
                                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = uiState.publishedPlanDesc,
                                                    fontSize = 11.sp,
                                                    color = Color(0xFF475569),
                                                    modifier = Modifier.padding(12.dp),
                                                    lineHeight = 15.sp
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(14.dp))

                                            Button(
                                                onClick = onOpenFloorPlan,
                                                shape = RoundedCornerShape(14.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD94600)),
                                                modifier = Modifier.fillMaxWidth().testTag("view_floor_plan_button")
                                            ) {
                                                Icon(imageVector = Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text("Ver y Descargar Plano (PDF)", fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }

                                item {
                                    // Emergency Contacts Card
                                    Card(
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(2.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column {
                                            Surface(
                                                color = Color(0xFFFFF7ED),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "Contactos de Emergencia",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp,
                                                    color = Color(0xFFC2410C),
                                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                                                )
                                            }

                                            Column {
                                                uiState.contacts.forEach { contact ->
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .clickable { onStartCall(contact.name, contact.phone) }
                                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                        ) {
                                                            Surface(
                                                                shape = CircleShape,
                                                                color = Color(0xFFFFEDD5),
                                                                modifier = Modifier.size(36.dp)
                                                            ) {
                                                                Box(contentAlignment = Alignment.Center) {
                                                                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = Color(0xFFEA580C), modifier = Modifier.size(18.dp))
                                                                }
                                                            }
                                                            Text(contact.name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                                                        }

                                                        Text(
                                                            text = contact.phone,
                                                            fontSize = 15.sp,
                                                            fontWeight = FontWeight.ExtraBold,
                                                            color = Color(0xFFEA580C)
                                                        )
                                                    }
                                                    Divider(color = Color(0xFFF1F5F9))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        PublicModuleTab.PROTOCOLOS -> {
                            if (selectedProtocol == null) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    item {
                                        Surface(
                                            shape = RoundedCornerShape(16.dp),
                                            color = Color(0xFFEFF6FF),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBEAFE)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                Surface(shape = CircleShape, color = Color(0xFFDBEAFE), modifier = Modifier.size(36.dp)) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = Color(0xFF2563EB))
                                                    }
                                                }
                                                Column {
                                                    Text("Protocolos Vigentes", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF1E40AF))
                                                    Text("Actuaciones reglamentarias obligatorias para el personal y alumnado.", fontSize = 11.sp, color = Color(0xFF3B82F6))
                                                }
                                            }
                                        }
                                    }

                                    items(uiState.guidelines) { proto ->
                                        Card(
                                            shape = RoundedCornerShape(18.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color.White),
                                            elevation = CardDefaults.cardElevation(2.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = Color(0xFFF1F5F9)
                                                ) {
                                                    Text(
                                                        text = proto.category.uppercase(),
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = Color(0xFF475569),
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                    )
                                                }

                                                Text(proto.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                                                Text(proto.description, fontSize = 11.sp, color = Color(0xFF64748B), lineHeight = 15.sp)

                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    OutlinedButton(
                                                        onClick = { selectedProtocol = proto },
                                                        modifier = Modifier.weight(1f),
                                                        shape = RoundedCornerShape(10.dp)
                                                    ) {
                                                        Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text("Previsualizar", fontSize = 11.sp)
                                                    }

                                                    Button(
                                                        onClick = { onToast("Descargando ${proto.title}.pdf...") },
                                                        modifier = Modifier.weight(1f),
                                                        shape = RoundedCornerShape(10.dp),
                                                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                                                    ) {
                                                        Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text("Descargar PDF", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                // Detailed View of Selected Protocol
                                val proto = selectedProtocol!!
                                val isRead = uiState.readProtocols.contains(proto.id)
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    item {
                                        TextButton(onClick = { selectedProtocol = null }) {
                                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Volver a protocolos", fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    item {
                                        Card(
                                            shape = RoundedCornerShape(20.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color.White),
                                            elevation = CardDefaults.cardElevation(2.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFDBEAFE)) {
                                                    Text(
                                                        proto.category,
                                                        color = Color(0xFF1E40AF),
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                    )
                                                }

                                                Text(proto.title, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF0F172A))
                                                Text(proto.description, fontSize = 12.sp, color = Color(0xFF475569), lineHeight = 16.sp)

                                                Divider(color = Color(0xFFE2E8F0))

                                                Text("PASOS A SEGUIR:", fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = Color(0xFF64748B))

                                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                                    proto.steps.forEachIndexed { idx, step ->
                                                        Row(
                                                            verticalAlignment = Alignment.Top,
                                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                        ) {
                                                            Surface(
                                                                shape = CircleShape,
                                                                color = NavyPrimary,
                                                                modifier = Modifier.size(22.dp)
                                                            ) {
                                                                Box(contentAlignment = Alignment.Center) {
                                                                    Text("${idx + 1}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                                }
                                                            }
                                                            Text(step, fontSize = 12.sp, color = Color(0xFF334155), modifier = Modifier.weight(1f), lineHeight = 16.sp)
                                                        }
                                                    }
                                                }

                                                Spacer(modifier = Modifier.height(8.dp))

                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    Button(
                                                        onClick = { onMarkProtocolRead(proto.id) },
                                                        modifier = Modifier.weight(1f),
                                                        shape = RoundedCornerShape(12.dp),
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = if (isRead) Color(0xFF059669) else Color(0xFF10B981)
                                                        )
                                                    ) {
                                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text(if (isRead) "Leído ✓" else "Marcar Leído", fontWeight = FontWeight.Bold)
                                                    }

                                                    OutlinedButton(
                                                        onClick = { onToast("Descargando ${proto.title}...") },
                                                        modifier = Modifier.weight(1f),
                                                        shape = RoundedCornerShape(12.dp)
                                                    ) {
                                                        Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text("Descargar Guía", fontWeight = FontWeight.Bold)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        PublicModuleTab.RECURSOS -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                item {
                                    // Trivia Launcher Banner
                                    Surface(
                                        shape = RoundedCornerShape(18.dp),
                                        color = NavyDark,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFFBBF24))
                                                Text("Trivia de Seguridad Escolar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            }
                                            Text("Pon a prueba tus conocimientos sobre sismos, incendios y evacuación.", color = Color(0xFFCBD5E1), fontSize = 11.sp)
                                            Button(
                                                onClick = onOpenQuiz,
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
                                                shape = RoundedCornerShape(12.dp),
                                                modifier = Modifier.fillMaxWidth().testTag("start_quiz_button")
                                            ) {
                                                Text("Comenzar Trivia de Prevención", color = Color.Black, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }

                                item {
                                    // Resources List
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        uiState.resources.forEach { res ->
                                            Card(
                                                shape = RoundedCornerShape(18.dp),
                                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                                elevation = CardDefaults.cardElevation(2.dp),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                    ) {
                                                        Surface(
                                                            shape = CircleShape,
                                                            color = if (res.type == "PDF") Color(0xFFFEE2E2) else if (res.type == "Video") Color(0xFFF3E8FF) else Color(0xFFDBEAFE),
                                                            modifier = Modifier.size(36.dp)
                                                        ) {
                                                            Box(contentAlignment = Alignment.Center) {
                                                                Icon(
                                                                    imageVector = if (res.type == "PDF") Icons.Default.Description else if (res.type == "Video") Icons.Default.PlayArrow else Icons.Default.Article,
                                                                    contentDescription = null,
                                                                    tint = if (res.type == "PDF") Color(0xFFDC2626) else if (res.type == "Video") Color(0xFF9333EA) else Color(0xFF2563EB),
                                                                    modifier = Modifier.size(18.dp)
                                                                )
                                                            }
                                                        }
                                                        Column {
                                                            Text(res.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                                                            Text(res.description, fontSize = 11.sp, color = Color(0xFF64748B))
                                                        }
                                                    }

                                                    Button(
                                                        onClick = { onToast("Abriendo ${res.title}...") },
                                                        shape = RoundedCornerShape(10.dp),
                                                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        Text("Abrir Recurso (${res.type})", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                item {
                                    // Cronograma de Simulacros Card
                                    Card(
                                        shape = RoundedCornerShape(18.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(2.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFFD97706))
                                                Text("Cronograma de Simulacros 2024", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0F172A))
                                            }

                                            listOf(
                                                Triple("Simulacro de Evacuación General (Incendios)", "14 DE MAYO", "Primer Semestre"),
                                                Triple("Simulacro de Contingencia de Gas", "22 DE JULIO", "Invierno"),
                                                Triple("Simulacro de Primeros Auxilios y Siniestros", "10 DE OCTUBRE", "Segundo Semestre")
                                            ).forEach { (event, date, period) ->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(event, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                                        Text(period, fontSize = 10.sp, color = Color(0xFF94A3B8))
                                                    }
                                                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFFEF3C7)) {
                                                        Text(date, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF92400E), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                                    }
                                                }
                                                Divider(color = Color(0xFFF1F5F9))
                                            }

                                            Button(
                                                onClick = { onToast("Alerta de simulacro agendada en calendario!") },
                                                shape = RoundedCornerShape(12.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text("Agendar Recordatorio", color = Color.Black, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        PublicModuleTab.CONTACTO -> {
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
                                            Text("Contacto Técnico de Turno", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
                                            Text("Enviá consultas o reportá anomalías de seguridad al Inspector Distrital a cargo.", fontSize = 11.sp, color = Color(0xFF64748B))

                                            if (inquirySuccess) {
                                                Surface(
                                                    shape = RoundedCornerShape(12.dp),
                                                    color = Color(0xFFD1FAE5),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Text(
                                                        "¡Mensaje enviado con éxito! Tu consulta ha sido canalizada a la Jefatura Distrital.",
                                                        color = Color(0xFF065F46),
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(12.dp)
                                                    )
                                                }
                                            } else {
                                                OutlinedTextField(
                                                    value = inquirySubject,
                                                    onValueChange = { inquirySubject = it },
                                                    label = { Text("Asunto de Consulta") },
                                                    modifier = Modifier.fillMaxWidth(),
                                                    shape = RoundedCornerShape(12.dp)
                                                )

                                                OutlinedTextField(
                                                    value = inquiryMessage,
                                                    onValueChange = { inquiryMessage = it },
                                                    label = { Text("Mensaje Detallado") },
                                                    modifier = Modifier.fillMaxWidth().height(100.dp),
                                                    shape = RoundedCornerShape(12.dp)
                                                )

                                                Button(
                                                    onClick = {
                                                        if (inquiryMessage.isNotBlank()) {
                                                            inquirySuccess = true
                                                            onToast("Mensaje enviado a Jefatura Distrital")
                                                        }
                                                    },
                                                    shape = RoundedCornerShape(12.dp),
                                                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Text("Enviar Mensaje", fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }
                                    }
                                }

                                item {
                                    Card(
                                        shape = RoundedCornerShape(18.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(1.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Text("OFICINA DISTRITAL SAN MIGUEL", fontWeight = FontWeight.ExtraBold, fontSize = 10.sp, color = Color(0xFF64748B))
                                            Text("📍 Av. Balbín 1032, San Miguel, Buenos Aires", fontSize = 12.sp, color = Color(0xFF1E293B))
                                            Text("📞 +54 11 4451-2290", fontSize = 12.sp, color = Color(0xFF1E293B))
                                            Text("✉️ inspectores.sm@sanmiguel.gob.ar", fontSize = 12.sp, color = Color(0xFF1E293B))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                StudentBottomTab.ALERTAS -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text("Alertas de Seguridad Distritales", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF0F172A))
                            Text("Notificaciones y advertencias emitidas en tiempo real por el Inspector.", fontSize = 11.sp, color = Color(0xFF64748B))
                        }

                        items(uiState.alerts) { alert ->
                            val isCritical = alert.category == "Critica"
                            val isWeather = alert.category == "Clima"

                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = androidx.compose.foundation.BorderStroke(
                                    2.dp,
                                    if (isCritical) Color(0xFFEF4444) else if (isWeather) Color(0xFFF59E0B) else Color(0xFF3B82F6)
                                ),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(alert.date, fontSize = 10.sp, color = Color(0xFF94A3B8))
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isCritical) Color(0xFFFEE2E2) else if (isWeather) Color(0xFFFEF3C7) else Color(0xFFDBEAFE)
                                        ) {
                                            Text(
                                                alert.category,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isCritical) Color(0xFF991B1B) else if (isWeather) Color(0xFF92400E) else Color(0xFF1E40AF),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Text(alert.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F172A))
                                    Text(alert.description, fontSize = 12.sp, color = Color(0xFF475569), lineHeight = 16.sp)

                                    if (alert.schoolName != null) {
                                        Text("🏫 ${alert.schoolName}", fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                        }
                    }
                }

                StudentBottomTab.REPORTAR -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text("Reportar Incidente Escolar", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF0F172A))
                            Text("Informa anomalías o situaciones de riesgo escolar con o sin anonimato.", fontSize = 11.sp, color = Color(0xFF64748B))
                        }

                        if (repSuccess) {
                            item {
                                Card(
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(20.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(48.dp))
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text("¡Reporte Enviado con Éxito!", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF065F46))
                                        Text("Tu reporte ha sido recibido por la Jefatura Distrital. Podés seguirlo en tu Perfil.", fontSize = 11.sp, color = Color(0xFF475569), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Button(
                                            onClick = { repSuccess = false },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                                        ) {
                                            Text("Enviar otro reporte")
                                        }
                                    }
                                }
                            }
                        } else {
                            item {
                                Card(
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(2.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        // School
                                        Text("ESTABLECIMIENTO EDUCATIVO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                                        var schoolExpanded by remember { mutableStateOf(false) }
                                        Box(modifier = Modifier.fillMaxWidth()) {
                                            OutlinedButton(onClick = { schoolExpanded = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                    Text(repSchool, fontSize = 12.sp, color = Color(0xFF1E293B))
                                                    Text("▼", fontSize = 10.sp)
                                                }
                                            }
                                            DropdownMenu(expanded = schoolExpanded, onDismissRequest = { schoolExpanded = false }) {
                                                SampleData.schools.forEach { s ->
                                                    DropdownMenuItem(text = { Text(s.name) }, onClick = { repSchool = s.name; schoolExpanded = false })
                                                }
                                            }
                                        }

                                        // Category
                                        Text("CATEGORÍA DEL INCIDENTE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                                        var catExpanded by remember { mutableStateOf(false) }
                                        Box(modifier = Modifier.fillMaxWidth()) {
                                            OutlinedButton(onClick = { catExpanded = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                    Text(repCat.label, fontSize = 12.sp, color = Color(0xFF1E293B))
                                                    Text("▼", fontSize = 10.sp)
                                                }
                                            }
                                            DropdownMenu(expanded = catExpanded, onDismissRequest = { catExpanded = false }) {
                                                IncidentCategory.values().forEach { c ->
                                                    DropdownMenuItem(text = { Text(c.label) }, onClick = { repCat = c; catExpanded = false })
                                                }
                                            }
                                        }

                                        // Anonymous Toggle
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text("Reporte Anónimo", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF1E293B))
                                                Text("Tu identidad se mantendrá resguardada.", fontSize = 10.sp, color = Color(0xFF64748B))
                                            }
                                            Switch(checked = repAnon, onCheckedChange = { repAnon = it })
                                        }

                                        // Description
                                        OutlinedTextField(
                                            value = repDesc,
                                            onValueChange = { repDesc = it },
                                            label = { Text("Descripción de la Situación") },
                                            modifier = Modifier.fillMaxWidth().height(100.dp),
                                            shape = RoundedCornerShape(12.dp)
                                        )

                                        // Photo Attachment Simulation
                                        OutlinedButton(
                                            onClick = {
                                                repPhoto = "https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=500"
                                                onToast("Foto de evidencia simulada adjuntada")
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(if (repPhoto != null) "✓ Foto adjuntada (foto_evidencia.jpg)" else "Adjuntar Foto de Evidencia")
                                        }

                                        Button(
                                            onClick = {
                                                if (repDesc.isNotBlank()) {
                                                    onSubmitReport(repSchool, repCat, repDesc, repAnon, repPhoto)
                                                    repSuccess = true
                                                    repDesc = ""
                                                    repPhoto = null
                                                } else {
                                                    onToast("Ingresá una descripción para el reporte")
                                                }
                                            },
                                            shape = RoundedCornerShape(14.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                                            modifier = Modifier.fillMaxWidth().height(48.dp)
                                        ) {
                                            Text("Enviar Reporte", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                StudentBottomTab.PERFIL -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            // User Profile Card
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = NavyPrimary,
                                        modifier = Modifier.size(56.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = (user?.name ?: "U").take(1),
                                                color = Color.White,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 22.sp
                                            )
                                        }
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(user?.name ?: "Usuario", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF0F172A))
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFFDBEAFE),
                                            modifier = Modifier.padding(vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = user?.role?.displayName ?: "Alumno",
                                                color = Color(0xFF1E40AF),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Text("🏫 ${user?.school}", fontSize = 11.sp, color = Color(0xFF64748B))
                                        if (!user?.grade.isNullOrBlank()) {
                                            Text("Cargo / Año: ${user?.grade}", fontSize = 10.sp, color = Color(0xFF94A3B8))
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            // Badges & Credentials
                            Text("CREDENCIALES Y LOGROS DIGITALES", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF64748B))
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFFD1FAE5),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = Color(0xFF059669))
                                        Text("Brigadista", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF065F46))
                                        Text("Prevención Activa", fontSize = 9.sp, color = Color(0xFF047857))
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFFDBEAFE),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFF2563EB))
                                        Text("Simulacros 2024", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF1E40AF))
                                        Text("Al Día", fontSize = 9.sp, color = Color(0xFF1D4ED8))
                                    }
                                }
                            }
                        }

                        item {
                            // Incident reports history
                            Text("MIS REPORTES ENVIADOS (${uiState.reports.size})", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF64748B))
                        }

                        items(uiState.reports) { rep ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(1.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(rep.category.label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (rep.status == IncidentStatus.RESUELTO) Color(0xFFD1FAE5) else Color(0xFFFEF3C7)
                                        ) {
                                            Text(
                                                rep.status.label,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (rep.status == IncidentStatus.RESUELTO) Color(0xFF065F46) else Color(0xFF92400E),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Text(rep.description, fontSize = 11.sp, color = Color(0xFF475569))
                                    Text("Fecha: ${rep.date} • ${if (rep.isAnonymous) "Anónimo" else user?.name}", fontSize = 9.sp, color = Color(0xFF94A3B8))
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = onOpenProfileEdit,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Configurar Perfil / Cambiar Rol")
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedButton(
                                onClick = onLogout,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Cerrar Sesión")
                            }
                        }
                    }
                }
            }
        }
    }
}
