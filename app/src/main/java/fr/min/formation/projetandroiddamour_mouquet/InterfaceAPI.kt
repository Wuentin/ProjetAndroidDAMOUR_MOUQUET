package fr.min.formation.projetandroiddamour_mouquet


import retrofit2.http.GET
import retrofit2.http.Query

interface InterfaceAPI {

    @GET("/opendata/Velib_Metropole/station_information.json")
    suspend fun getStations(@Query("stations")size:Int=20): GetStationsResult

}

//data class GetStationsResult(val results : MutableList<DataJSONMODEL>)
data class GetStationsResult(val stations : List<Station>)
data class Station ( val station_id: String, val name: String,val lat: Double,val lon: Double,val capacity: Double)