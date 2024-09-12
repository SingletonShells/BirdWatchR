package com.example.AllAroundTheBirds.DataClasses

data class Challenges(
    val type: String? = null,
    val medal: String? = null,
    val challenge: String? = null,
    val birdname: String? = null,
    val amount: Int? = null
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Challenges

        if (type != other.type) return false
        if (medal != other.medal) return false
        if (challenge != other.challenge) return false
        if (birdname != other.birdname) return false
        if (amount != other.amount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type?.hashCode() ?: 0
        result = 31 * result + (medal?.hashCode() ?: 0)
        result = 31 * result + (challenge?.hashCode() ?: 0)
        result = 31 * result + (birdname?.hashCode() ?: 0)
        result = 31 * result + (amount ?: 0)
        return result
    }
}
