package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.IncidentCategory
import com.example.model.UserRole
import com.example.ui.components.*
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.PublicDashboardScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.EscuelasSegurasViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                EscuelasSegurasApp()
            }
        }
    }
}

@Composable
fun EscuelasSegurasApp(
    viewModel: EscuelasSegurasViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showSimulatorModal by remember { mutableStateOf(false) }

    // Show toast notifications when state changes
    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.dismissToast()
        }
    }

    // Back button handling
    BackHandler(enabled = uiState.currentScreen != AppScreen.LOGIN) {
        when (uiState.currentScreen) {
            AppScreen.REGISTER -> viewModel.navigateTo(AppScreen.LOGIN)
            AppScreen.ADMIN_DASHBOARD -> viewModel.navigateTo(AppScreen.PUBLIC_DASHBOARD)
            AppScreen.PUBLIC_DASHBOARD -> {
                if (uiState.currentUser?.role == UserRole.INSPECTOR || uiState.currentUser?.role == UserRole.DIRECTIVO) {
                    viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD)
                } else {
                    viewModel.logout()
                }
            }
            AppScreen.LOGIN -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main Screen Routing
        when (uiState.currentScreen) {
            AppScreen.LOGIN -> {
                LoginScreen(
                    onLogin = { email, pass -> viewModel.login(email, pass) },
                    onQuickLogin = { role -> viewModel.quickLogin(role) },
                    onNavigateToRegister = { viewModel.navigateTo(AppScreen.REGISTER) },
                    onForgotPassword = { viewModel.setForgotOpen(true) }
                )
            }

            AppScreen.REGISTER -> {
                RegisterScreen(
                    onRegisterSubmit = { name, surname, email, role ->
                        viewModel.register(name, surname, email, role)
                    },
                    onNavigateToLogin = { viewModel.navigateTo(AppScreen.LOGIN) }
                )
            }

            AppScreen.PUBLIC_DASHBOARD -> {
                PublicDashboardScreen(
                    uiState = uiState,
                    onTabSelected = { tab -> viewModel.setStudentBottomTab(tab) },
                    onModuleSelected = { mod -> viewModel.setPublicModuleTab(mod) },
                    onOpenAdminPanel = { viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD) },
                    onLogout = { viewModel.logout() },
                    onStartCall = { name, number -> viewModel.startCall(name, number) },
                    onOpenFloorPlan = { viewModel.setFloorPlanOpen(true) },
                    onOpenQuiz = { viewModel.setQuizOpen(true) },
                    onOpenProfileEdit = { viewModel.setProfileEditOpen(true) },
                    onMarkProtocolRead = { id -> viewModel.markProtocolRead(id) },
                    onSubmitReport = { school, cat, desc, anon, photo ->
                        viewModel.submitReport(school, cat, desc, anon, photo)
                    },
                    onToast = { msg -> viewModel.showToast(msg) }
                )
            }

            AppScreen.ADMIN_DASHBOARD -> {
                AdminDashboardScreen(
                    uiState = uiState,
                    onAdminBottomTabSelected = { tab -> viewModel.setAdminBottomTab(tab) },
                    onAdminTopTabSelected = { tab -> viewModel.setAdminTopTab(tab) },
                    onOpenPublicView = { viewModel.navigateTo(AppScreen.PUBLIC_DASHBOARD) },
                    onLogout = { viewModel.logout() },
                    onBroadcastAlert = { title, desc, cat -> viewModel.broadcastAlert(title, desc, cat) },
                    onPublishProtocol = { title, cat, desc, steps ->
                        viewModel.addOrUpdateProtocol(null, title, cat, desc, steps, null)
                    },
                    onApproveRequest = { id, name -> viewModel.approveRequest(id, name) },
                    onRejectRequest = { id, name -> viewModel.rejectRequest(id, name) },
                    onDeleteContact = { id -> viewModel.deleteContact(id) },
                    onSaveContact = { id, name, phone -> viewModel.addOrUpdateContact(id, name, phone) },
                    onDeleteProtocol = { id -> viewModel.deleteProtocol(id) },
                    onSaveProtocol = { id, title, cat, desc, steps, pdf ->
                        viewModel.addOrUpdateProtocol(id, title, cat, desc, steps, pdf)
                    },
                    onDeleteResource = { id -> viewModel.deleteResource(id) },
                    onSaveResource = { id, title, desc, type, link, roles ->
                        viewModel.addOrUpdateResource(id, title, desc, type, link, roles)
                    },
                    onUploadDocument = { title, desc, fileName, fileType, fileSize, targetAudience ->
                        viewModel.uploadSharedDocument(title, desc, fileName, fileType, fileSize, targetAudience)
                    },
                    onDeleteDocument = { id -> viewModel.deleteSharedDocument(id) },
                    onSendMessage = { text, attachName, attachType, attachSize ->
                        viewModel.sendDirectivoChatMessage(text, attachName, attachType, attachSize)
                    },
                    onOpenFloorPlan = { viewModel.setFloorPlanOpen(true) },
                    onToast = { msg -> viewModel.showToast(msg) }
                )
            }
        }

        // Global Floating Emergency SOS Button (visible on dashboard screens)
        if (uiState.currentScreen == AppScreen.PUBLIC_DASHBOARD || uiState.currentScreen == AppScreen.ADMIN_DASHBOARD) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 72.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                GlobalEmergencyFloatingButton(
                    onClick = { viewModel.triggerEmergencySOS() }
                )
            }
        }

        // Top-Start Simulator Access Pill
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF0F172A).copy(alpha = 0.9f),
            border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.6f)),
            shadowElevation = 6.dp,
            modifier = Modifier
                .padding(top = 40.dp, start = 12.dp)
                .align(Alignment.TopStart)
                .clickable { showSimulatorModal = true }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color(0xFF34D399), CircleShape)
                )
                Text(
                    text = "SIMULADOR",
                    color = Color(0xFF34D399),
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Top Push Notification Alert Banner Animation
        AnimatedVisibility(
            visible = uiState.pushAlertBanner != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            uiState.pushAlertBanner?.let { alert ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF0F172A),
                    border = BorderStroke(1.dp, Color(0xFFEF4444)),
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(top = 44.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFDC2626),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color.White)
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ALERTA DISTRITAL (${alert.category})",
                                color = Color(0xFFF87171),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = alert.title,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = alert.description,
                                color = Color(0xFFCBD5E1),
                                fontSize = 11.sp,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }

        // Interactive Modals and Dialogs
        EmergencyAlertModal(
            payload = uiState.emergencyAlertPayload,
            onDismiss = { viewModel.dismissEmergencyAlert() }
        )

        FloorPlanDialog(
            isOpen = uiState.showFloorPlan,
            planName = uiState.publishedPlan,
            planDesc = uiState.publishedPlanDesc,
            onClose = { viewModel.setFloorPlanOpen(false) },
            onDownload = { viewModel.showToast("Descarga completada: ${uiState.publishedPlan}") }
        )

        QuizDialog(
            isOpen = uiState.showQuiz,
            onClose = { viewModel.setQuizOpen(false) },
            onComplete = { score ->
                viewModel.showToast("¡Trivia completada! Puntaje: $score/5")
            }
        )

        CallSimulatorDialog(
            callInfo = uiState.activeCall,
            onEndCall = { viewModel.endCall() }
        )

        ForgotPasswordDialog(
            isOpen = uiState.showForgot,
            initialEmail = "",
            onClose = { viewModel.setForgotOpen(false) },
            onSubmit = { email -> viewModel.showToast("Instrucciones enviadas a $email") }
        )

        ProfileEditDialog(
            isOpen = uiState.showProfileEdit,
            currentUser = uiState.currentUser,
            onClose = { viewModel.setProfileEditOpen(false) },
            onSave = { phone, grade -> viewModel.updateProfile(phone, grade) },
            onSwitchRole = { newRole -> viewModel.switchRole(newRole) }
        )

        SimulatorModal(
            isOpen = showSimulatorModal,
            onClose = { showSimulatorModal = false },
            onQuickLogin = { role -> viewModel.quickLogin(role) },
            onTriggerPushAlert = {
                viewModel.broadcastAlert(
                    "Simulación de Escape de Gas",
                    "Se reportó olor a gas en el sector de cocina del Colegio Simón Bolívar. Evacuar preventivamente.",
                    "Critica"
                )
            },
            onSimulateApplicant = {
                viewModel.register(
                    "Prof. Carolina",
                    "López",
                    "c.lopez@sanmiguel.edu.ar",
                    UserRole.DOCENTE
                )
            },
            onSimulateReport = {
                viewModel.submitReport(
                    "Colegio Nacional Simón Bolívar",
                    IncidentCategory.INFRAESTRUCTURA,
                    "Vidrio fisurado en ventana lateral del Aula 102 que requiere mantenimiento preventivo.",
                    false,
                    "https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=500"
                )
            },
            onReset = { viewModel.logout() },
            recentActivities = uiState.recentActivities
        )
    }
}
