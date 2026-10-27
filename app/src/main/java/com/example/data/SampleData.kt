package com.example.data

import com.example.model.*

object SampleData {

    val schools = listOf(
        School(
            id = "s1",
            name = "Colegio Nacional Simón Bolívar",
            address = "Av. de la Juventud 450, Centro",
            safetyRating = 4.8,
            zone = "Centro",
            phone = "+54 11 4321-9876",
            studentsCount = 1200,
            hasSecurityGuard = true,
            safeRoutesCount = 3
        ),
        School(
            id = "s2",
            name = "Escuela Primaria Benito Juárez",
            address = "Calle del Sol 102, Zona Norte",
            safetyRating = 4.2,
            zone = "Norte",
            phone = "+54 11 5555-1234",
            studentsCount = 650,
            hasSecurityGuard = true,
            safeRoutesCount = 2
        ),
        School(
            id = "s3",
            name = "Liceo de Señoritas María Auxiliadora",
            address = "Paseo de las Acacias 89, Zona Sur",
            safetyRating = 4.5,
            zone = "Sur",
            phone = "+54 11 2345-6789",
            studentsCount = 800,
            hasSecurityGuard = false,
            safeRoutesCount = 4
        ),
        School(
            id = "s4",
            name = "Instituto Tecnológico San Martín",
            address = "Av. Libertador 2300, Zona Este",
            safetyRating = 3.9,
            zone = "Este",
            phone = "+54 11 8765-4321",
            studentsCount = 1500,
            hasSecurityGuard = true,
            safeRoutesCount = 1
        ),
        School(
            id = "s5",
            name = "Colegio Público República de Colombia",
            address = "Calle 14 nro. 780, Zona Oeste",
            safetyRating = 3.5,
            zone = "Oeste",
            phone = "+54 11 9898-0011",
            studentsCount = 950,
            hasSecurityGuard = false,
            safeRoutesCount = 2
        )
    )

    val initialContacts = listOf(
        EmergencyContact("c1", "Policía (Comisaría 1ra)", "911"),
        EmergencyContact("c2", "Bomberos Voluntarios", "100"),
        EmergencyContact("c3", "Emergencias Médicas (SAME)", "107"),
        EmergencyContact("c4", "Defensa Civil San Miguel", "103")
    )

    val initialAlerts = listOf(
        SafetyAlert(
            id = "a1",
            title = "Simulacro de Evacuación",
            description = "Hoy a las 10:30 AM se realizará un simulacro de sismo programado en el Colegio Simón Bolívar. Mantener la calma.",
            date = "Hoy, 09:00 AM",
            category = "Informativa",
            schoolName = "Colegio Nacional Simón Bolívar"
        ),
        SafetyAlert(
            id = "a2",
            title = "Presencia Policial Preventiva",
            description = "Patrullaje reforzado en los ingresos y salidas escolares de la Zona Sur durante el turno de la tarde.",
            date = "Ayer, 03:30 PM",
            category = "Informativa"
        ),
        SafetyAlert(
            id = "a3",
            title = "Lluvia Fuerte e Inundación Temporal",
            description = "Evitar el cruce peatonal de la Calle del Sol por acumulación de agua. Utilizar ruta alterna por Av. de la Juventud.",
            date = "Ayer, 12:15 PM",
            category = "Clima",
            schoolName = "Escuela Primaria Benito Juárez"
        ),
        SafetyAlert(
            id = "a4",
            title = "Reporte de Alerta SOS Activado",
            description = "Se reportó actividad sospechosa cerca del ingreso trasero del Instituto San Martín. Personal de seguridad acudió al lugar.",
            date = "Hace 2 días",
            category = "Critica",
            schoolName = "Instituto Tecnológico San Martín"
        )
    )

    val initialReports = listOf(
        IncidentReport(
            id = "r1",
            category = IncidentCategory.INFRAESTRUCTURA,
            schoolName = "Escuela Primaria Benito Juárez",
            description = "La reja perimetral del patio trasero está rota y permite el ingreso de personas ajenas a la institución.",
            date = "2026-06-24",
            status = IncidentStatus.EN_REVISION,
            priority = "Alta",
            isAnonymous = false,
            photoUrl = "https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=500"
        ),
        IncidentReport(
            id = "r2",
            category = IncidentCategory.BULLYING,
            schoolName = "Colegio Nacional Simón Bolívar",
            description = "Grupo de alumnos molestando constantemente a los de primer ingreso en la zona de casilleros.",
            date = "2026-06-23",
            status = IncidentStatus.RESUELTO,
            priority = "Media",
            isAnonymous = true
        ),
        IncidentReport(
            id = "r3",
            category = IncidentCategory.SOSPECHOSO,
            schoolName = "Instituto Tecnológico San Martín",
            description = "Vehículo sin patentes estacionado de manera sospechosa frente a la parada escolar a la salida.",
            date = "2026-06-25",
            status = IncidentStatus.ENVIADO,
            priority = "Alta",
            isAnonymous = false
        )
    )

    val initialGuidelines = listOf(
        ProtocolGuideline(
            id = "p1",
            title = "Escape de Gas Detectado",
            category = "Gas y Calefacción",
            description = "Protocolo oficial ante la detección de olor a gas u otras fugas de combustibles en recintos escolares.",
            steps = listOf(
                "No accionar interruptores eléctricos, luces, timbres ni utilizar teléfonos móviles dentro del aula.",
                "Abrir inmediatamente puertas y ventanas para ventilar por completo el espacio.",
                "Evacuar el aula de forma ordenada hacia el punto de encuentro exterior sin correr ni gritar.",
                "Cerrar la llave de paso general del gas si se encuentra al alcance de forma segura.",
                "Dar aviso inmediato a los Bomberos (100) y a la Jefatura Distrital."
            ),
            pdfUrl = "escape-de-gas-oficial.pdf"
        ),
        ProtocolGuideline(
            id = "p2",
            title = "Protocolo de Evacuación por Incendio",
            category = "Infraestructura / Fuego",
            description = "Directrices para la evacuación inmediata en presencia de humo o fuego activo en el establecimiento.",
            steps = listOf(
                "Activar la alarma de incendios del pasillo o alertar a viva voz en todas las aulas.",
                "Interrumpir de inmediato todas las actividades. No recoger pertenencias personales.",
                "Formar fila y seguir al docente guía por la ruta de evacuación indicada en el plano.",
                "Si hay humo espeso, desplazarse agachado o a gatas cubriéndose la boca con un paño.",
                "Concentrarse en el punto de encuentro designado y realizar el conteo de alumnos de inmediato."
            ),
            pdfUrl = "evacuacion-incendios.pdf"
        ),
        ProtocolGuideline(
            id = "p3",
            title = "Prevención ante Inundación o Tormenta Fuerte",
            category = "Clima / Siniestros",
            description = "Instrucciones ante lluvias intensas con peligro de inundaciones perimetrales en los accesos.",
            steps = listOf(
                "Desconectar interruptores generales de electricidad si se detecta filtración grave de agua.",
                "Mantener a los alumnos dentro de las aulas en pisos elevados si el patio se inunda.",
                "Suspender el cruce peatonal de zonas inundadas cercanas a la escuela.",
                "Monitorear las alertas emitidas por Defensa Civil (103) mediante la aplicación."
            )
        ),
        ProtocolGuideline(
            id = "p4",
            title = "Procedimiento Ante Sospechas o Intrusión",
            category = "Seguridad Ciudadana",
            description = "Acciones recomendadas en presencia de personas sospechosas en el perímetro o intrusos en el patio.",
            steps = listOf(
                "Cerrar con llave las puertas de acceso a las aulas y mantener a los alumnos en silencio.",
                "Dar aviso silencioso a la dirección escolar y llamar de inmediato al 911 (Policía).",
                "No enfrentar de manera directa al intruso bajo ninguna circunstancia.",
                "Aguardar las indicaciones de las fuerzas de seguridad antes de reanudar el tránsito."
            )
        )
    )

    val initialResources = listOf(
        ResourceItem(
            id = "r1",
            title = "Guía de Seguridad Escolar",
            description = "Manual completo de normas de seguridad distrital.",
            type = "PDF",
            linkOrFile = "guia-seguridad-escolar.pdf",
            allowedRoles = listOf(UserRole.DOCENTE, UserRole.DIRECTIVO, UserRole.INSPECTOR, UserRole.AUXILIAR, UserRole.ALUMNO)
        ),
        ResourceItem(
            id = "r2",
            title = "Video Instructivo de RCP",
            description = "Tutorial paso a paso sobre reanimación cardiopulmonar en escuelas.",
            type = "Video",
            linkOrFile = "https://www.youtube.com/watch?v=A8vN9vX78j0"
        ),
        ResourceItem(
            id = "r3",
            title = "Cronograma de Simulacros 2024",
            description = "Fechas y detalles de los simulacros del ciclo lectivo.",
            type = "Archivo",
            linkOrFile = "cronograma-simulacros-2024.docx",
            allowedRoles = listOf(UserRole.DOCENTE, UserRole.DIRECTIVO, UserRole.INSPECTOR, UserRole.AUXILIAR)
        )
    )

    val initialSharedDocs = listOf(
        SharedDocument(
            id = "doc1",
            title = "Resolución Distrital de Prevención y Simulacros 2026",
            description = "Circular obligatoria con cronograma de simulacros de evacuación e inspección técnica para todas las instituciones.",
            fileName = "Resolucion_Distrital_2026_Simulacros.pdf",
            fileType = SharedFileType.PDF,
            fileSize = "2.4 MB",
            uploadedByName = "Ing. Juan Carlos Romero",
            uploadedByRole = "Inspector",
            targetAudience = "Todos los Directivos del Distrito",
            createdAt = "Hoy, 09:15",
            downloadsCount = 14
        ),
        SharedDocument(
            id = "doc2",
            title = "Planilla de Control de Instalaciones y Extintores",
            description = "Formulario editable para asentar control de extintores, calderas, disyuntores y salidas de emergencia.",
            fileName = "Planilla_Control_Instalaciones_v3.docx",
            fileType = SharedFileType.DOC,
            fileSize = "540 KB",
            uploadedByName = "Ing. Juan Carlos Romero",
            uploadedByRole = "Inspector",
            targetAudience = "Todos los Directivos del Distrito",
            createdAt = "Ayer, 16:30",
            downloadsCount = 22
        ),
        SharedDocument(
            id = "doc3",
            title = "Relevamiento Fotográfico - Reparación Techo Patio Central",
            description = "Fotografías del avance de obra y cerramiento perimetral de seguridad realizado durante el fin de semana.",
            fileName = "Relevamiento_Obra_Patio_Bolivar.jpg",
            fileType = SharedFileType.PHOTO,
            fileSize = "3.8 MB",
            uploadedByName = "Prof. Alberto Distéfano",
            uploadedByRole = "Directivo",
            targetAudience = "Jefatura Distrital e Inspectores",
            createdAt = "Ayer, 18:40",
            downloadsCount = 5
        ),
        SharedDocument(
            id = "doc4",
            title = "Acta de Conformidad - Simulacro Sísmico e Incendio",
            description = "Acta firmada por bomberos voluntarios y directivos con tiempos de evacuación registrados.",
            fileName = "Acta_Simulacro_Bolivar_Firmada.pdf",
            fileType = SharedFileType.PDF,
            fileSize = "1.9 MB",
            uploadedByName = "Prof. Alberto Distéfano",
            uploadedByRole = "Directivo",
            targetAudience = "Jefatura Distrital e Inspectores",
            createdAt = "Hace 2 días",
            downloadsCount = 8
        )
    )

    val initialMessages = listOf(
        DirectivoChatMessage(
            id = "msg1",
            senderName = "Ing. Juan Carlos Romero",
            senderRole = "Inspector",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
            school = "Jefatura Distrital San Miguel",
            message = "Estimados directivos: acabamos de publicar la Resolución Distrital 2026 de Simulacros en la pestaña de archivos. Por favor descarguen la planilla de control de instalaciones.",
            timestamp = "09:20",
            attachmentName = "Resolucion_Distrital_2026_Simulacros.pdf",
            attachmentType = SharedFileType.PDF,
            attachmentSize = "2.4 MB"
        ),
        DirectivoChatMessage(
            id = "msg2",
            senderName = "Prof. Alberto Distéfano",
            senderRole = "Directivo",
            avatarUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150",
            school = "Colegio Nacional Simón Bolívar",
            message = "Buenos días Ingeniero. Ya descargamos la resolución y adjuntamos el relevamiento fotográfico de la obra del patio que nos habían solicitado.",
            timestamp = "09:45",
            attachmentName = "Relevamiento_Obra_Patio_Bolivar.jpg",
            attachmentType = SharedFileType.PHOTO,
            attachmentSize = "3.8 MB"
        ),
        DirectivoChatMessage(
            id = "msg3",
            senderName = "Ing. Juan Carlos Romero",
            senderRole = "Inspector",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
            school = "Jefatura Distrital San Miguel",
            message = "Excelente director Distéfano. Vemos las fotos del cerramiento correctamente colocado. La semana entrante pasaremos a firmar el libro de actas.",
            timestamp = "10:02"
        )
    )

    val quizQuestions = listOf(
        QuizQuestion(
            id = 1,
            question = "Si presencias una situación de acoso (bullying) escolar hacia un compañero, ¿cuál es la mejor acción?",
            options = listOf(
                "Ignorarlo para que no te molesten a ti también.",
                "Grabar un video y subirlo a redes sociales.",
                "Informar discretamente a un docente, directivo o usar el reporte anónimo de la app.",
                "Enfrentar físicamente a los agresores."
            ),
            correctIndex = 2,
            explanation = "Reportar la situación de manera segura y confiable a las autoridades escolares ayuda a detener el acoso sin poner en riesgo tu integridad física."
        ),
        QuizQuestion(
            id = 2,
            question = "¿Qué debes hacer inmediatamente si se activa la Alerta Sísmica o de Incendio en tu escuela?",
            options = listOf(
                "Correr rápidamente hacia la salida gritando.",
                "Mantener la calma, alejarte de ventanas y seguir las instrucciones de evacuación hacia la zona segura.",
                "Buscar tus pertenencias en el casillero antes de salir.",
                "Esconderte debajo de un escritorio y quedarte allí hasta el día siguiente."
            ),
            correctIndex = 1,
            explanation = "La calma es fundamental. Alejarte de vidrios y dirigirte de forma ordenada al punto de encuentro señalado salvará vidas."
        ),
        QuizQuestion(
            id = 3,
            question = "Al regresar de la escuela a casa caminando, la ruta más segura es:",
            options = listOf(
                "Cualquier atajo oscuro o callejón desierto si te ahorra 5 minutos de camino.",
                "Ir solo escuchando música con auriculares con volumen máximo.",
                "Caminar por las 'Rutas Seguras' señaladas por la app, con vigilancia, iluminación y comercios adheridos.",
                "Ir detrás de un desconocido que parece amigable."
            ),
            correctIndex = 2,
            explanation = "Las Rutas Seguras son senderos monitoreados con comercios que sirven como refugio y tienen presencia de personal de seguridad."
        ),
        QuizQuestion(
            id = 4,
            question = "¿Qué información NUNCA debes compartir con desconocidos en internet o redes sociales?",
            options = listOf(
                "Tu nombre completo, dirección de tu casa, escuela a la que asistes y tus horarios.",
                "Tus géneros musicales favoritos.",
                "Tus recomendaciones de videojuegos.",
                "Tus dibujos o proyectos escolares."
            ),
            correctIndex = 0,
            explanation = "Tus datos personales y de ubicación deben mantenerse privados para evitar situaciones de ciberacoso o riesgos físicos."
        ),
        QuizQuestion(
            id = 5,
            question = "Si te encuentras en una situación de emergencia real o peligro inminente dentro de la escuela, ¿cómo te asiste la app?",
            options = listOf(
                "Te permite jugar un minijuego para distraerte.",
                "No tiene opciones para emergencias de respuesta rápida.",
                "Presionando el Botón de Alerta SOS para emitir coordenadas GPS a Ojos en Alerta y llamar al servicio de emergencia.",
                "Enviando un correo esperando respuesta la próxima semana."
            ),
            correctIndex = 2,
            explanation = "El Botón de Emergencia de la app emite geolocalización satelital precisa de tu escuela directamente a Ojos en Alerta San Miguel."
        )
    )

    val sampleUsers = mapOf(
        UserRole.INSPECTOR to UserProfile(
            id = "USR-INSP-001",
            name = "Ing. Juan Carlos Romero",
            role = UserRole.INSPECTOR,
            school = "Jefatura Distrital San Miguel",
            grade = "Infraestructura y Seguridad",
            phone = "+54 9 11 5500-1122",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
            badges = listOf("Auditor Líder", "Prevención de Riesgos")
        ),
        UserRole.DIRECTIVO to UserProfile(
            id = "USR-DIR-104",
            name = "Prof. Alberto Distéfano",
            role = UserRole.DIRECTIVO,
            school = "Colegio Nacional Simón Bolívar",
            grade = "Director General",
            phone = "+54 9 11 4455-8899",
            avatarUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150",
            badges = listOf("Guardián Escolar", "Plan Evacuación Certificado")
        ),
        UserRole.DOCENTE to UserProfile(
            id = "USR-DOC-215",
            name = "Prof. Carolina López",
            role = UserRole.DOCENTE,
            school = "Colegio Nacional Simón Bolívar",
            grade = "Docente Titular - Turno Tarde",
            phone = "+54 9 11 3344-5566",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150",
            badges = listOf("Auxiliar Certificado", "Brigada de Evacuación")
        ),
        UserRole.ALUMNO to UserProfile(
            id = "USR-ALU-502",
            name = "Juan P.",
            role = UserRole.ALUMNO,
            school = "Colegio Nacional Simón Bolívar",
            grade = "5to Año, Turno Mañana",
            phone = "+54 9 11 5566-7788",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
            badges = listOf("Brigadista Escolar", "Trivia Certificada")
        ),
        UserRole.AUXILIAR to UserProfile(
            id = "USR-AUX-309",
            name = "Roberto Méndez",
            role = UserRole.AUXILIAR,
            school = "Colegio Nacional Simón Bolívar",
            grade = "Mantenimiento e Instalaciones",
            phone = "+54 9 11 4987-6543",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150",
            badges = listOf("Mantenimiento Certificado", "Primeros Auxilios")
        )
    )

    val schoolCoords = mapOf(
        "Colegio Nacional Simón Bolívar" to Triple(-34.543055, -58.711944, "Av. de la Juventud 450, Centro, San Miguel"),
        "Escuela Primaria Benito Juárez" to Triple(-34.536820, -58.705410, "Calle del Sol 102, Zona Norte, San Miguel"),
        "Liceo de Señoritas María Auxiliadora" to Triple(-34.551240, -58.718900, "Paseo de las Acacias 89, Zona Sur, San Miguel"),
        "Instituto Tecnológico San Martín" to Triple(-34.540110, -58.698750, "Av. Libertador 2300, Zona Este, San Miguel"),
        "Colegio Público República de Colombia" to Triple(-34.548900, -58.726300, "Calle 14 nro. 780, Zona Oeste, San Miguel"),
        "Jefatura Distrital San Miguel" to Triple(-34.542510, -58.711200, "Av. Ricardo Balbín 1349, Centro, San Miguel")
    )
}
