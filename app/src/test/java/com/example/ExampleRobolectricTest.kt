package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.IncidentCategory
import com.example.model.UserRole
import com.example.viewmodel.AppScreen
import com.example.viewmodel.EscuelasSegurasViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Escuelas Seguras", appName)
    }

    @Test
    fun `viewModel login as directivo navigates to admin dashboard`() {
        val vm = EscuelasSegurasViewModel()
        vm.quickLogin(UserRole.DIRECTIVO)
        val state = vm.uiState.value
        assertEquals(AppScreen.ADMIN_DASHBOARD, state.currentScreen)
        assertEquals(UserRole.DIRECTIVO, state.currentUser?.role)
    }

    @Test
    fun `viewModel login as alumno navigates to public dashboard`() {
        val vm = EscuelasSegurasViewModel()
        vm.quickLogin(UserRole.ALUMNO)
        val state = vm.uiState.value
        assertEquals(AppScreen.PUBLIC_DASHBOARD, state.currentScreen)
        assertEquals(UserRole.ALUMNO, state.currentUser?.role)
    }

    @Test
    fun `viewModel submit report adds report to state`() {
        val vm = EscuelasSegurasViewModel()
        val initialCount = vm.uiState.value.reports.size
        vm.submitReport(
            school = "Colegio Nacional Simón Bolívar",
            category = IncidentCategory.INFRAESTRUCTURA,
            desc = "Vidrio roto en aula",
            isAnonymous = false,
            photoUrl = null
        )
        val afterCount = vm.uiState.value.reports.size
        assertEquals(initialCount + 1, afterCount)
    }

    @Test
    fun `viewModel trigger emergency SOS generates GPS payload`() {
        val vm = EscuelasSegurasViewModel()
        vm.triggerEmergencySOS("Colegio Nacional Simón Bolívar")
        val payload = vm.uiState.value.emergencyAlertPayload
        assertNotNull(payload)
        assertEquals("Colegio Nacional Simón Bolívar", payload?.schoolName)
        assertTrue(payload?.whatsappUrl?.contains("5491150910000") == true)
    }
}
