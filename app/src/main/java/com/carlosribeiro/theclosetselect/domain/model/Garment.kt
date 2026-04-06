package com.carlosribeiro.theclosetselect.domain.model

data class Garment(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val type: GarmentType = GarmentType.BLUSA,
    val category: GarmentCategory = GarmentCategory.ALL,
    val photoUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

enum class GarmentType(val displayName: String) {
    BLUSA("Blusa"),
    CAMISETA("Camiseta"),
    CAMISETA_CROPPED("Camiseta Cropped"),
    REGATA("Regata"),
    CAMISA("Camisa"),
    TRICOT("Tricot"),
    BODY("Body"),
    VESTIDO("Vestido"),
    VESTIDO_LONGO("Vestido Longo"),
    MACACAO("Macacão"),
    MACACAO_CURTO("Macacão Curto"),
    CONJUNTO("Conjunto"),
    CALCA_JEANS("Calça Jeans"),
    CALCA_SOCIAL("Calça Social"),
    CALCA_ALFAIATARIA("Calça Alfaiataria"),
    CALCA_LEGGING("Legging"),
    SAIA_CURTA("Saia Curta"),
    SAIA_MIDI("Saia Midi"),
    SAIA_LONGA("Saia Longa"),
    SHORTS("Shorts"),
    BERMUDA("Bermuda"),
    BLAZER("Blazer"),
    JAQUETA("Jaqueta"),
    CASACO("Casaco"),
    TRENCH_COAT("Trench Coat"),
    CARDIGAN("Cardigan"),
    MOLETOM("Moletom"),
    CINTO("Cinto"),
    BOLSA("Bolsa"),
    CLUTCH("Clutch"),
    MOCHILA("Mochila"),
    LENCO("Lenço"),
    CHAPEU("Chapéu"),
    OCULOS("Óculos"),
    SCARPIN("Scarpin"),
    SANDALIA("Sandália"),
    SANDALIA_PLATAFORMA("Sandália Plataforma"),
    BOTA("Bota"),
    BOTA_CANO_LONGO("Bota Cano Longo"),
    TENIS("Tênis"),
    SAPATILHA("Sapatilha"),
    MULE("Mule")
}

enum class GarmentCategory(val displayName: String) {
    ALL("All Items"),
    OUTERWEAR("Outerwear"),
    ESSENTIALS("Essentials"),
    EVENING("Evening"),
    CASUAL("Casual"),
    FORMAL("Formal")
}