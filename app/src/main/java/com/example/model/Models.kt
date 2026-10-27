package com.example.model

enum class UserRole(val displayName: String) {
    INSPECTOR("Inspector"),
    DIRECTIVO("Directivo"),
    DOCENTE("Docente"),
    ALUMNO("Alumno"),
    AUXILIAR("Auxiliar")
}

data class UserProfile(
    val id: String,
    val name: String,
    val role: UserRole,
    val school: String,
    val grade: String = "",
    val phone: String = "",
    val avatarUrl: String = "",
    val badges: List<String> = emptyList()
)

enum class IncidentCategory(val label: String) {
    INFRAESTRUCTURA("Infraestructura"),
    BULLYING("Acoso / Bullying"),
    SOSPECHOSO("Actividad Sospechosa"),
    ACCIDENTE("Accidente"),
    OTRO("Otro")
}

enum class IncidentStatus(val label: String) {
    ENVIADO("Enviado"),
    EN_REVISION("En revisión"),
    RESUELTO("Resuelto")
}

data class IncidentReport(
    val id: String,
    val category: IncidentCategory,
    val schoolName: String,
    val description: String,
    val date: String,
    val status: IncidentStatus,
    val priority: String = "Alta",
    val isAnonymous: Boolean = false,
    val photoUrl: String? = null,
    val lat: Double? = null,
    val lng: Double? = null
)

data class School(
    val id: String,
    val name: String,
    val address: String,
    val safetyRating: Double,
    val zone: String,
    val phone: String,
    val studentsCount: Int,
    val hasSecurityGuard: Boolean,
    val safeRoutesCount: Int
)

data class SafetyAlert(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val category: String, // "Critica", "Informativa", "Clima", "Tránsito"
    val schoolName: String? = null
)

data class EmergencyContact(
    val id: String,
    val name: String,
    val phone: String
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

enum class SharedFileType {
    PDF, PHOTO, DOC
}

data class SharedDocument(
    val id: String,
    val title: String,
    val description: String,
    val fileName: String,
    val fileType: SharedFileType,
    val fileSize: String,
    val uploadedByName: String,
    val uploadedByRole: String,
    val targetAudience: String,
    val createdAt: String,
    val downloadsCount: Int = 0
)

data class DirectivoChatMessage(
    val id: String,
    val senderName: String,
    val senderRole: String,
    val avatarUrl: String,
    val school: String,
    val message: String,
    val timestamp: String,
    val attachmentName: String? = null,
    val attachmentType: SharedFileType? = null,
    val attachmentSize: String? = null
)

data class ProtocolGuideline(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val steps: List<String>,
    val pdfUrl: String? = null
)

data class ResourceItem(
    val id: String,
    val title: String,
    val description: String,
    val type: String, // "PDF", "Video", "Archivo"
    val linkOrFile: String,
    val allowedRoles: List<UserRole>? = null
)

data class RecentActivity(
    val id: String,
    val text: String,
    val time: String,
    val color: String = "blue" // "green", "orange", "red", "blue"
)

data class PendingRequest(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val date: String
)
