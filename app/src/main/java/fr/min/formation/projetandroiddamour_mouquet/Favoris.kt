package fr.min.formation.projetandroiddamour_mouquet

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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


        var db = DataBaseSQLite(this)
        var stationList = db.recupFavoris()


        //var stationList = listOf(StationModel("esef","ih",5,5,5))
       // val stationList = StationModel.bdd(40)
        stationAdapter = StationAdapter(stationList)
        recyclerView.adapter = stationAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nav,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.menu_ajout -> {
                val intent = Intent(this, Ajout_Favoris::class.java)
                startActivity(intent)
            }

            R.id.menu_accueil -> {val intent = Intent(this, ProjetVelib::class.java)
                startActivity(intent)}
        }
        return super.onOptionsItemSelected(item)
    }

}