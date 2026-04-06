package com.carlosribeiro.theclosetselect.data.model

import com.carlosribeiro.theclosetselect.domain.model.Garment
import com.carlosribeiro.theclosetselect.domain.model.GarmentCategory
import com.carlosribeiro.theclosetselect.domain.model.GarmentType

data class GarmentDto(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val type: String = GarmentType.BLUSA.name,
    val category: String = GarmentCategory.ALL.name,
    val photoUrl: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val materialCharacters: List<String> = emptyList() // ← adiciona essa linha
) {
    fun toDomain(): Garment = Garment(
        id = id,
        userId = userId,
        name = name,
        type = GarmentType.entries.firstOrNull { it.name == type } ?: GarmentType.BLUSA,
        category = GarmentCategory.entries.firstOrNull { it.name == category } ?: GarmentCategory.ALL,
        photoUrl = photoUrl,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(garment: Garment): GarmentDto = GarmentDto(
            id = garment.id,
            userId = garment.userId,
            name = garment.name,
            type = garment.type.name,
            category = garment.category.name,
            photoUrl = garment.photoUrl,
            createdAt = garment.createdAt
        )
    }
}