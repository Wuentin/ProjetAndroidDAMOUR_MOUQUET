package fr.min.formation.projetandroiddamour_mouquet.model

import fr.min.formation.projetandroiddamour_mouquet.Station


data class StationModel (
    val station_id: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val capacity: Int,
    val num_bikes_available: Int,
    val num_docks_available: Int){

    companion object {
        fun bdd(nb : Int = 30) =
            (1..nb).map {
                StationModel(
                    "nom$it", "prenom$it",
                    5.5, 5.5, 5,5,5

                )
            }

    }
}


