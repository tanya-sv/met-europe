package com.metgallery.data.model

enum class ArtistNationality(val displayValue: String) {
    None("Artist Nationality..."),
    American("American"),
    Austrian("Austrian"),
    Belgian("Belgian"),
    Byzantine("Byzantine"),
    British("British"),
    Continental("Continental"),
    Danish("Danish"),
    Dutch("Dutch"),
    European("European"),
    Flemish("Flemish"),
    French("French"),
    German("German"),
    Greek("Greek"),
    Hungarian("Hungarian"),
    Italian("Italian"),
    Irish("Irish"),
    Netherlandish("Netherlandish"),
    Northern_European("Northern European"),
    Norwegian("Norwegian"),
    Portuguese("Portuguese"),
    Russian("Russian"),
    Scottish("Scottish"),
    Spanish("Spanish"),
    Swedish("Swedish"),
    Swiss("Swiss"),
    Welsh("Welsh");

    override fun toString(): String {
        return displayValue
    }

    fun isNone(): Boolean = this == None

    fun displayNameOrEmpty() = if (this != None) {
        displayValue
    } else ""
}