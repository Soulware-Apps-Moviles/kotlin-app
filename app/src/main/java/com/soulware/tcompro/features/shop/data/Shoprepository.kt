package com.soulware.tcompro.features.shop.data

import com.soulware.tcompro.core.data.SessionManager
import com.soulware.tcompro.features.shop.domain.Employee
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    private val api: ShopApiService,
    private val sessionManager: SessionManager // Inyectado para obtener el token
) {

    suspend fun getShopkeepers(shopId: String): List<Employee> {
        return try {
            val response = api.getShopkeepers(shopId)
            response.map { shopkeeper ->
                Employee(
                    id = shopkeeper.id,
                    name = "${shopkeeper.firstName} ${shopkeeper.lastName}",
                    role = "Shopkeeper",
                    imageUrl = ""
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Función auxiliar para contratar por Auth ID (Usada internamente o por QR)
    suspend fun hireShopkeeper(shopId: String, authId: String): Employee? {
        return try {
            // CORRECCIÓN: Ya no usamos ShopApiService.HireShopkeeperRequest
            // Usamos HireShopkeeperRequest directamente porque la sacamos fuera de la interfaz
            val request = HireShopkeeperRequest(authId = authId)

            val response = api.hireShopkeeper(shopId, request)

            Employee(
                id = response.id,
                name = "${response.firstName} ${response.lastName}",
                role = "Shopkeeper",
                imageUrl = ""
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // --- NUEVA FUNCIÓN: Contratar por Email ---
    // Esta es la que usa tu AddEmployeeViewModel
    suspend fun hireShopkeeperByEmail(shopId: String, email: String): Employee? {
        return try {
            // 1. Obtener Token para la búsqueda
            val token = sessionManager.userSessionFlow.first().accessToken
            val formattedToken = "Bearer $token"

            // 2. Buscar al empleado por email
            // Esto llama al endpoint GET /shopkeepers/v1/?email=...
            val resource = api.getShopkeeperByEmail(formattedToken, email)

            // 3. Obtener su ID (profileId es el authId en este contexto)
            val authId = resource.profileId

            // 4. Reutilizar la función de contratar
            hireShopkeeper(shopId, authId)

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun deleteEmployee(shopId: String, shopkeeperId: Long): Boolean {
        return try {
            api.fireShopkeeper(shopId, shopkeeperId)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}