package fr.min.formation.projetandroiddamour_mouquet

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class DataJSONMODEL(
    @JsonProperty("station_id")
    var stationId: String?,

    @JsonProperty("name")
    var stationName: String?,

    @JsonProperty("lat")
    var latitude: Double?,

    @JsonProperty("lon")
    var longitude: Double?,

    @JsonProperty("capacity")
    var capacity: Double?
)
