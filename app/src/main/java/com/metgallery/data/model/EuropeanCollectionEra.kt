package com.metgallery.data.model

enum class EuropeanCollectionEra(val displayValue: String, val dateBegin: Int, val dateEnd: Int) {
    None("Date/Era...", 0, 0),
    AD1000_1400("A.D.+1000-1400", 1000, 1400),
    AD1400_1600("A.D.+1400-1600", 1400, 1600),
    AD1600_1800("A.D.+1600-1800", 1600, 1800),
    AD1800_1900("A.D.+1800-1900", 1800, 1900),
    AD1900_2000("A.D.+1900-2000", 1900, 2000);

    override fun toString(): String {
        return displayValue
    }

    fun displayNameOrEmpty() = if (this != None) {
        displayValue
    } else ""
}