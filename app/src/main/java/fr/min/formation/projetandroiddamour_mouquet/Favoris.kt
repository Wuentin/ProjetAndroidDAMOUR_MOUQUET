package fr.min.formation.projetandroiddamour_mouquet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.min.formation.projetandroiddamour_mouquet.model.StationModel

class Favoris : AppCompatActivity() {

    val stationList: MutableList<StationModel> = mutableListOf()
    private var stationAdapter : StationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favoris)

        val recyclerView =
            findViewById<RecyclerView>(R.id.liste_stations_fav)


        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


       // var stationList = listOf(StationModel("esef","ih",5.5,5.5,5,5,5))
        val stationList = StationModel.bdd(40)
        stationAdapter = StationAdapter(stationList)
        recyclerView.adapter = stationAdapter

    }

}