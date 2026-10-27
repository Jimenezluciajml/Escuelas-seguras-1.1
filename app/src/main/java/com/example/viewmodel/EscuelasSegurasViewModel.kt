package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppScreen {
    LOGIN,
    REGISTER,
    PUBLIC_DASHBOARD,
    ADMIN_DASHBOARD
}

enum class StudentBottomTab {
    INICIO,
    ALERTAS,
    REPORTAR,
    PERFIL
}

enum class PublicModuleTab {
    EMERGENCIA,
    PROTOCOLOS,
    RECURSOS,
    CONTACTO
}

enum class AdminBottomTab {
    PANEL,
    ALERTAS,
    PUBLICAR,
    CONFIG
}

enum class AdminTopTab {
    INICIO,
    DIRECTIVOS,
    INSPECTORES,
    PLANOS,
    CONTACTOS,
    PROTOCOLOS,
    RECURSOS
}

data class EmergencyAlertPayload(
    val userId: String,
    val userName: String,
    val userRole: String,
    val schoolName: String,
    val schoolAddress: String,
    val lat: Double,
    val lng: Double,
    val timestamp: String,
    val mapsUrl: String,
    val messageText: String,
    val whatsappUrl: String
)

data class UiState(
    val currentScreen: AppScreen = AppScreen.LOGIN,
    val currentUser: UserProfile? = null,
    val studentBottomTab: StudentBottomTab = StudentBottomTab.INICIO,
    val publicModuleTab: PublicModuleTab = PublicModuleTab.EMERGENCIA,
    val adminBottomTab: AdminBottomTab = AdminBottomTab.PANEL,
    val adminTopTab: AdminTopTab = AdminTopTab.INICIO,
    val publishedPlan: String = "plano-evacuacion-2024.pdf",
    val publishedPlanDesc: String = "Plano oficial de evacuación del establecimiento educativo. Detalla salidas de emergencia, extintores, zonas seguras y puntos de encuentro.",
    val publishedPlanSize: String = "2.4 MB",
    val contacts: List<EmergencyContact> = SampleData.initialContacts,
    val alerts: List<SafetyAlert> = SampleData.initialAlerts,
    val reports: List<IncidentReport> = SampleData.initialReports,
    val guidelines: List<ProtocolGuideline> = SampleData.initialGuidelines,
    val resources: List<ResourceItem> = SampleData.initialResources,
    val sharedDocs: List<SharedDocument> = SampleData.initialSharedDocs,
    val directivoMessages: List<DirectivoChatMessage> = SampleData.initialMessages,
    val recentActivities: List<RecentActivity> = listOf(
        RecentActivity("act1", "Protocolo de Incendio actualizado", "Hace 2 h", "blue"),
        RecentActivity("act2", "Nueva solicitud: M. Torres", "Hace 5 h", "green"),
        RecentActivity("act3", "Recurso de RCP publicado", "Ayer", "blue"),
        RecentActivity("act4", "Plano de evacuación reemplazado", "Hace 3 días", "orange")
    ),
    val pendingRequests: List<PendingRequest> = listOf(
        PendingRequest("req1", "M. Torres", "m.torres@sanmiguel.edu.ar", UserRole.DIRECTIVO, "Hace 5 h"),
        PendingRequest("req2", "Prof. Alejandro Silva", "a.silva@sanmiguel.edu.ar", UserRole.DOCENTE, "Hace 1 día")
    ),
    val readProtocols: Set<String> = emptySet(),
    val activeCall: Pair<String, String>? = null, // Pair(name, number)
    val showFloorPlan: Boolean = false,
    val showQuiz: Boolean = false,
    val showForgot: Boolean = false,
    val showProfileEdit: Boolean = false,
    val emergencyAlertPayload: EmergencyAlertPayload? = null,
    val pushAlertBanner: SafetyAlert? = null,
    val toastMessage: String? = null
)

class EscuelasSegurasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun showToast(msg: String) {
        _uiState.update { it.copy(toastMessage = msg) }
    }

    fun dismissToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun navigateTo(screen: AppScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun setStudentBottomTab(tab: StudentBottomTab) {
        _uiState.update { it.copy(studentBottomTab = tab) }
    }

    fun setPublicModuleTab(tab: PublicModuleTab) {
        _uiState.update { it.copy(publicModuleTab = tab) }
    }

    fun setAdminBottomTab(tab: AdminBottomTab) {
        _uiState.update { it.copy(adminBottomTab = tab) }
    }

    fun setAdminTopTab(tab: AdminTopTab) {
        _uiState.update { it.copy(adminTopTab = tab) }
    }

    fun quickLogin(role: UserRole) {
        val profile = SampleData.sampleUsers[role] ?: SampleData.sampleUsers[UserRole.ALUMNO]!!
        _uiState.update {
            it.copy(
                currentUser = profile,
                currentScreen = if (role == UserRole.INSPECTOR || role == UserRole.DIRECTIVO) {
                    AppScreen.ADMIN_DASHBOARD
                } else {
                    AppScreen.PUBLIC_DASHBOARD
                },
                studentBottomTab = StudentBottomTab.INICIO,
                publicModuleTab = PublicModuleTab.EMERGENCIA,
                toastMessage = "Sesión iniciada como ${profile.name} (${profile.role.displayName})"
            )
        }
    }

    fun login(email: String, pass: String): Boolean {
        val emailLower = email.lowercase().trim()
        val passLower = pass.lowercase().trim()

        val role = when {
            emailLower.contains("inspector") || emailLower.contains("admin") || passLower == "admin1234" -> UserRole.INSPECTOR
            emailLower.contains("director") || emailLower.contains("directivo") || passLower == "directivo123" -> UserRole.DIRECTIVO
            emailLower.contains("alumno") || passLower == "alumno123" -> UserRole.ALUMNO
            emailLower.contains("auxiliar") || passLower == "auxiliar123" -> UserRole.AUXILIAR
            else -> UserRole.DOCENTE
        }

        val profile = SampleData.sampleUsers[role] ?: SampleData.sampleUsers[UserRole.DOCENTE]!!
        _uiState.update {
            it.copy(
                currentUser = profile,
                currentScreen = if (role == UserRole.INSPECTOR || role == UserRole.DIRECTIVO) {
                    AppScreen.ADMIN_DASHBOARD
                } else {
                    AppScreen.PUBLIC_DASHBOARD
                },
                toastMessage = "Bienvenido/a, ${profile.name}!"
            )
        }
        return true
    }

    fun register(name: String, surname: String, email: String, role: UserRole) {
        val fullName = "$name $surname"
        val newReq = PendingRequest(
            id = "req_${System.currentTimeMillis()}",
            name = fullName,
            email = email,
            role = role,
            date = "Hace unos instantes"
        )
        val newAct = RecentActivity(
            id = "act_${System.currentTimeMillis()}",
            text = "Nueva solicitud: $fullName",
            time = "Hace 1 min",
            color = "green"
        )
        _uiState.update {
            it.copy(
                pendingRequests = listOf(newReq) + it.pendingRequests,
                recentActivities = listOf(newAct) + it.recentActivities,
                toastMessage = "Solicitud enviada. Pendiente de aprobación del Inspector."
            )
        }
    }

    fun logout() {
        _uiState.update {
            it.copy(
                currentUser = null,
                currentScreen = AppScreen.LOGIN,
                toastMessage = "Sesión cerrada correctamente."
            )
        }
    }

    fun approveRequest(requestId: String, name: String) {
        _uiState.update { state ->
            val updated = state.pendingRequests.filterNot { it.id == requestId }
            val act = RecentActivity(
                id = "act_${System.currentTimeMillis()}",
                text = "Solicitud aprobada: $name",
                time = "Ahora mismo",
                color = "green"
            )
            state.copy(
                pendingRequests = updated,
                recentActivities = listOf(act) + state.recentActivities,
                toastMessage = "Solicitud de $name aprobada"
            )
        }
    }

    fun rejectRequest(requestId: String, name: String) {
        _uiState.update { state ->
            val updated = state.pendingRequests.filterNot { it.id == requestId }
            val act = RecentActivity(
                id = "act_${System.currentTimeMillis()}",
                text = "Solicitud rechazada: $name",
                time = "Ahora mismo",
                color = "red"
            )
            state.copy(
                pendingRequests = updated,
                recentActivities = listOf(act) + state.recentActivities,
                toastMessage = "Solicitud de $name rechazada"
            )
        }
    }

    fun broadcastAlert(title: String, desc: String, category: String) {
        val newAlert = SafetyAlert(
            id = "alert_${System.currentTimeMillis()}",
            title = title,
            description = desc,
            date = "Ahora",
            category = category
        )
        val newAct = RecentActivity(
            id = "act_${System.currentTimeMillis()}",
            text = "Alerta emitida: $title",
            time = "Ahora",
            color = if (category == "Critica") "red" else "blue"
        )
        _uiState.update {
            it.copy(
                alerts = listOf(newAlert) + it.alerts,
                recentActivities = listOf(newAct) + it.recentActivities,
                pushAlertBanner = newAlert,
                toastMessage = "Alerta masiva difundida con éxito!"
            )
        }
        viewModelScope.launch {
            delay(5000)
            _uiState.update { it.copy(pushAlertBanner = null) }
        }
    }

    fun submitReport(
        school: String,
        category: IncidentCategory,
        desc: String,
        isAnonymous: Boolean,
        photoUrl: String?
    ) {
        val newReport = IncidentReport(
            id = "r_usr_${System.currentTimeMillis()}",
            category = category,
            schoolName = school,
            description = desc,
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            status = IncidentStatus.ENVIADO,
            priority = "Alta",
            isAnonymous = isAnonymous,
            photoUrl = photoUrl
        )
        _uiState.update {
            it.copy(
                reports = listOf(newReport) + it.reports,
                toastMessage = "Reporte de incidente enviado con éxito."
            )
        }
    }

    fun markProtocolRead(protocolId: String) {
        _uiState.update { state ->
            val updatedSet = state.readProtocols + protocolId
            val currentUser = state.currentUser
            val updatedUser = if (currentUser != null && !currentUser.badges.contains("Protocolos Certificados")) {
                currentUser.copy(badges = currentUser.badges + "Protocolos Certificados")
            } else currentUser

            state.copy(
                readProtocols = updatedSet,
                currentUser = updatedUser,
                toastMessage = "Protocolo certificado y guardado en tu perfil!"
            )
        }
    }

    fun addOrUpdateContact(id: String?, name: String, phone: String) {
        _uiState.update { state ->
            val updatedList = if (id != null) {
                state.contacts.map { if (it.id == id) it.copy(name = name, phone = phone) else it }
            } else {
                state.contacts + EmergencyContact("c_${System.currentTimeMillis()}", name, phone)
            }
            state.copy(
                contacts = updatedList,
                toastMessage = if (id != null) "Contacto actualizado" else "Contacto creado exitosamente"
            )
        }
    }

    fun deleteContact(id: String) {
        _uiState.update { state ->
            state.copy(
                contacts = state.contacts.filterNot { it.id == id },
                toastMessage = "Contacto eliminado"
            )
        }
    }

    fun addOrUpdateProtocol(id: String?, title: String, category: String, desc: String, steps: List<String>, pdfUrl: String?) {
        _uiState.update { state ->
            val updated = if (id != null) {
                state.guidelines.map {
                    if (it.id == id) it.copy(title = title, category = category, description = desc, steps = steps, pdfUrl = pdfUrl) else it
                }
            } else {
                state.guidelines + ProtocolGuideline("p_${System.currentTimeMillis()}", title, category, desc, steps, pdfUrl)
            }
            state.copy(
                guidelines = updated,
                toastMessage = "Protocolo guardado exitosamente"
            )
        }
    }

    fun deleteProtocol(id: String) {
        _uiState.update { state ->
            state.copy(
                guidelines = state.guidelines.filterNot { it.id == id },
                toastMessage = "Protocolo eliminado"
            )
        }
    }

    fun addOrUpdateResource(id: String?, title: String, desc: String, type: String, linkOrFile: String, roles: List<UserRole>?) {
        _uiState.update { state ->
            val updated = if (id != null) {
                state.resources.map {
                    if (it.id == id) it.copy(title = title, description = desc, type = type, linkOrFile = linkOrFile, allowedRoles = roles) else it
                }
            } else {
                state.resources + ResourceItem("res_${System.currentTimeMillis()}", title, desc, type, linkOrFile, roles)
            }
            state.copy(
                resources = updated,
                toastMessage = "Recurso guardado exitosamente"
            )
        }
    }

    fun deleteResource(id: String) {
        _uiState.update { state ->
            state.copy(
                resources = state.resources.filterNot { it.id == id },
                toastMessage = "Recurso eliminado"
            )
        }
    }

    fun uploadSharedDocument(
        title: String,
        desc: String,
        fileName: String,
        fileType: SharedFileType,
        fileSize: String,
        targetAudience: String
    ) {
        val user = _uiState.value.currentUser
        val newDoc = SharedDocument(
            id = "doc_${System.currentTimeMillis()}",
            title = title,
            description = desc,
            fileName = fileName,
            fileType = fileType,
            fileSize = fileSize,
            uploadedByName = user?.name ?: "Personal Autorizado",
            uploadedByRole = user?.role?.displayName ?: "Directivo",
            targetAudience = targetAudience,
            createdAt = "Hoy, " + SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            downloadsCount = 0
        )
        _uiState.update {
            it.copy(
                sharedDocs = listOf(newDoc) + it.sharedDocs,
                toastMessage = "Archivo \"$fileName\" publicado en canal oficial"
            )
        }
    }

    fun deleteSharedDocument(docId: String) {
        _uiState.update { state ->
            state.copy(
                sharedDocs = state.sharedDocs.filterNot { it.id == docId },
                toastMessage = "Documento eliminado de la biblioteca"
            )
        }
    }

    fun sendDirectivoChatMessage(text: String, attachmentName: String? = null, attachmentType: SharedFileType? = null, attachmentSize: String? = null) {
        val user = _uiState.value.currentUser
        val isInspector = user?.role == UserRole.INSPECTOR
        val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        val newMsg = DirectivoChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderName = user?.name ?: if (isInspector) "Ing. Juan Carlos Romero" else "Prof. Alberto Distéfano",
            senderRole = if (isInspector) "Inspector" else "Directivo",
            avatarUrl = user?.avatarUrl ?: "",
            school = user?.school ?: if (isInspector) "Jefatura Distrital San Miguel" else "Colegio Nacional Simón Bolívar",
            message = text,
            timestamp = timeNow,
            attachmentName = attachmentName,
            attachmentType = attachmentType,
            attachmentSize = attachmentSize
        )

        _uiState.update {
            it.copy(directivoMessages = it.directivoMessages + newMsg)
        }

        // Automated counterpart simulation
        viewModelScope.launch {
            delay(1500)
            val counterpartRole = if (isInspector) "Directivo" else "Inspector"
            val counterpartName = if (isInspector) "Prof. Alberto Distéfano" else "Ing. Juan Carlos Romero"
            val counterpartSchool = if (isInspector) "Colegio Nacional Simón Bolívar" else "Jefatura Distrital San Miguel"
            val replyTexts = if (isInspector) {
                listOf(
                    "Mensaje recibido Inspector. Ya informamos al equipo docente y preceptores.",
                    "Comprendido. Verificaremos el estado de las instalaciones hoy mismo.",
                    "Excelente directiva. Ya descargamos la resolución para archivarlo en la dirección."
                )
            } else {
                listOf(
                    "Recibido desde Jefatura Distrital. Tomamos nota de las novedades.",
                    "Perfecto, agradecemos la pronta respuesta del equipo directivo.",
                    "Queda registrado en el libro de actas distrital de infraestructura."
                )
            }
            val autoReply = DirectivoChatMessage(
                id = "msg_rep_${System.currentTimeMillis()}",
                senderName = counterpartName,
                senderRole = counterpartRole,
                avatarUrl = "",
                school = counterpartSchool,
                message = replyTexts.random(),
                timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            )
            _uiState.update {
                it.copy(
                    directivoMessages = it.directivoMessages + autoReply,
                    toastMessage = "Nuevo mensaje de $counterpartName ($counterpartRole)"
                )
            }
        }
    }

    fun startCall(name: String, number: String) {
        _uiState.update { it.copy(activeCall = Pair(name, number)) }
    }

    fun endCall() {
        _uiState.update { it.copy(activeCall = null) }
    }

    fun setFloorPlanOpen(open: Boolean) {
        _uiState.update { it.copy(showFloorPlan = open) }
    }

    fun setQuizOpen(open: Boolean) {
        _uiState.update { it.copy(showQuiz = open) }
    }

    fun setForgotOpen(open: Boolean) {
        _uiState.update { it.copy(showForgot = open) }
    }

    fun setProfileEditOpen(open: Boolean) {
        _uiState.update { it.copy(showProfileEdit = open) }
    }

    fun updateProfile(phone: String, grade: String) {
        _uiState.update { state ->
            val u = state.currentUser
            state.copy(
                currentUser = u?.copy(phone = phone, grade = grade),
                toastMessage = "Perfil actualizado correctamente."
            )
        }
    }

    fun switchRole(newRole: UserRole) {
        val sample = SampleData.sampleUsers[newRole]
        _uiState.update { state ->
            state.copy(
                currentUser = sample ?: state.currentUser?.copy(role = newRole),
                currentScreen = if (newRole == UserRole.INSPECTOR || newRole == UserRole.DIRECTIVO) {
                    AppScreen.ADMIN_DASHBOARD
                } else {
                    AppScreen.PUBLIC_DASHBOARD
                },
                toastMessage = "Rol cambiado a ${newRole.displayName}"
            )
        }
    }

    fun triggerEmergencySOS(customSchoolName: String? = null) {
        val user = _uiState.value.currentUser
        val schoolName = customSchoolName ?: user?.school?.takeIf { it != "Jefatura Distrital San Miguel" } ?: "Colegio Nacional Simón Bolívar"
        val coords = SampleData.schoolCoords[schoolName] ?: Triple(-34.543055, -58.711944, "Av. de la Juventud 450, Centro, San Miguel")
        val lat = coords.first
        val lng = coords.second
        val address = coords.third
        val userId = user?.id ?: "USR-EMERGENCIA-SM"
        val userName = user?.name ?: "Comunidad Educativa"
        val userRole = user?.role?.displayName ?: "Estudiante"
        val time = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())

        val mapsUrl = "https://www.google.com/maps/search/?api=1&query=$lat,$lng"
        val messageText = """
            🚨 *ALERTA DE EMERGENCIA* 🚨
            *Ojos en Alerta - Municipio de San Miguel*

            📍 *Colegio:* $schoolName
            📌 *Dirección:* $address
            👤 *Usuario:* $userName ($userRole) - ID: $userId
            🌐 *Coordenadas:* Lat: $lat, Lng: $lng
            🗺️ *Ubicación:* $mapsUrl
            ⏰ *Hora:* $time
        """.trimIndent()

        val whatsappUrl = "https://wa.me/5491150910000?text=${java.net.URLEncoder.encode(messageText, "UTF-8")}"

        val payload = EmergencyAlertPayload(
            userId = userId,
            userName = userName,
            userRole = userRole,
            schoolName = schoolName,
            schoolAddress = address,
            lat = lat,
            lng = lng,
            timestamp = time,
            mapsUrl = mapsUrl,
            messageText = messageText,
            whatsappUrl = whatsappUrl
        )

        _uiState.update {
            it.copy(
                emergencyAlertPayload = payload,
                toastMessage = "Alerta de emergencia emitida a Ojos en Alerta San Miguel"
            )
        }
    }

    fun dismissEmergencyAlert() {
        _uiState.update { it.copy(emergencyAlertPayload = null) }
    }
}
