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
            val response = api.getShopkeepers(shopId)
            response.map { Employee(it.id, "${it.firstName} ${it.lastName}", "Shopkeeper", "") }
        } catch (e: Exception) { emptyList() }
    }

    suspend fun hireShopkeeperByEmail(shopId: String, email: String): Employee? {
        return try {
            val token = sessionManager.userSessionFlow.first().accessToken ?: return null
            val formattedToken = "Bearer $token"
            val resource = api.getShopkeeperByEmail(formattedToken, email)
            val request = HireShopkeeperRequest(authId = resource.authId)
            val response = api.hireShopkeeper(shopId, request)
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