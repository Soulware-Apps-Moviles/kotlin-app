package com.soulware.tcompro.features.shop.data

import com.soulware.tcompro.core.data.SessionManager
import com.soulware.tcompro.features.auth.data.ProfileApiService
import com.soulware.tcompro.features.shop.domain.Employee
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    private val api: ShopApiService,
    private val profileApi: ProfileApiService,
    private val sessionManager: SessionManager
) {



    suspend fun createShopForOwner(
        name: String,
        latitude: Double,
        longitude: Double,
        ownerEmail: String
    ): Boolean {
        return try {
            val token = sessionManager.userSessionFlow.first().accessToken ?: return false
            val formattedToken = "Bearer $token"

            val owner = profileApi.getOwnerByEmail(formattedToken, ownerEmail)

            val request = CreateShopRequest(
                ownerId = owner.id,
                paymentMethods = listOf("CASH"),
                pickupMethods = listOf("SHOP_PICK_UP"),
                maxCreditPerCustomer = 500.0,
                latitude = latitude,
                longitude = longitude,
                name = name
            )

            val newShop = api.createShop(formattedToken, request)
            sessionManager.saveShopId(newShop.id)

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getShopkeepers(shopId: String): List<Employee> {
        return try {
            // 1. Obtenemos el token de sesión (igual que en hireShopkeeper)
            val token = sessionManager.userSessionFlow.first().accessToken ?: return emptyList()
            val formattedToken = "Bearer $token"

            // 2. Convertimos el ID a Long
            val shopIdLong = shopId.toLongOrNull() ?: return emptyList()

            // 3. Llamamos a la API con el token y el ID correcto
            val response = api.getShopkeepers(formattedToken, shopIdLong)

            // 4. Mapeamos la respuesta para la pantalla
            response
                .filter { it.isHired == true }
                .map {
                Employee(
                    id = it.id,
                    name = "${it.firstName} ${it.lastName}",
                    role = "Shopkeeper",
                    imageUrl = ""
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Si falla, devuelve lista vacía (por eso veías "No staff members")
        }
    }

    suspend fun hireShopkeeperByEmail(shopId: String, email: String): Employee? {
        return try {
            // Obtener el token
            val token = sessionManager.userSessionFlow.first().accessToken ?: return null
            val formattedToken = "Bearer $token"

            // Preparar el request con los datos que pide el Java: HireShopkeeperResource(Long shopId, String email)
            // Asegúrate de convertir el shopId a Long
            val shopIdLong = shopId.toLongOrNull() ?: return null
            val request = HireShopkeeperRequest(shopId = shopIdLong, email = email)

            // Llamar al endpoint corregido
            val response = api.hireShopkeeper(formattedToken, request)

            // Mapear la respuesta a tu objeto de dominio Employee
            Employee(response.id, "${response.firstName} ${response.lastName}", "Shopkeeper", "")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun deleteEmployee(shopId: String, shopkeeperId: Long): Boolean {
        return try {
            api.fireShopkeeper(shopId, shopkeeperId)
            true
        } catch (e: Exception) { false }
    }
}