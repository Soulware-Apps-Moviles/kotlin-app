package com.soulware.tcompro.features.shop.data

import com.soulware.tcompro.features.shop.domain.Employee
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    private val api: ShopApiService
) {

    suspend fun getShopkeepers(shopId: String): List<Employee> {
        try {
            val response = api.getShopkeepers(shopId)
            return response.map { shopkeeper ->
                Employee(
                    id = shopkeeper.id,
                    name = "${shopkeeper.firstName} ${shopkeeper.lastName}",
                    role = "Shopkeeper",
                    imageUrl = ""
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    suspend fun hireShopkeeper(shopId: String, authId: String): Employee? {
        try {
            val request = HireShopkeeperRequest(authId = authId)

            val response = api.hireShopkeeper(shopId, request)

            return Employee(
                id = response.id,
                name = "${response.firstName} ${response.lastName}",
                role = "Shopkeeper",
                imageUrl = ""
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
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