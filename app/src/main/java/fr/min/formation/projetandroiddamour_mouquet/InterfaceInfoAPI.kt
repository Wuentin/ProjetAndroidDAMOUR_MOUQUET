package fr.min.formation.projetandroiddamour_mouquet

import retrofit2.http.GET
import retrofit2.http.Query

interface InterfaceInfoAPI {



    @GET("/opendata/Velib_Metropole/station_status.json")
    suspend fun getStationsStatus(@Query("lastUpdatedOther") lastUpdatedOther: Int): LastUpdate2


}

//data class GetStationsResult(val results : MutableList<DataJSONMODEL>)
data class LastUpdate2 (val lastUpdatedOther : Int,val ttl:Int, val data:GetStationsStatusResults)
data class GetStationsStatusResults(val stations : List<StationStatus>)
data class StationStatus ( val station_id: String,val num_bikes_available: Int,val num_docks_available: Int)