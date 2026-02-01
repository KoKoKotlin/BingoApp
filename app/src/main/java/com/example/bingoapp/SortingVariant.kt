package com.example.bingoapp

enum class SortingVariant(val displayName: String, val value: String) {
    Id("ID", "id"),
    MostCrossed("Most crossed out", "most_crossed"),
    LeastCrossed("Least crossed out", "least_crossed");

    override fun toString() = displayName

    companion object {
        fun from(value: String): SortingVariant {
            return when (value) {
                Id.value, Id.displayName -> Id
                MostCrossed.value, MostCrossed.displayName -> MostCrossed
                LeastCrossed.value, LeastCrossed.displayName -> LeastCrossed
                else -> SortingVariant.MostCrossed
            }
        }
    }
}