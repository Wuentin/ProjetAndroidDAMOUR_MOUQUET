package fr.min.formation.projetandroiddamour_mouquet
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import fr.min.formation.projetandroiddamour_mouquet.databinding.AjoutFavorisBinding
import fr.min.formation.projetandroiddamour_mouquet.model.StationModel

class Ajout_Favoris : AppCompatActivity() {

    lateinit var binding : AjoutFavorisBinding
    val stationListRecherche: MutableList<StationModel> = mutableListOf()
    val listeStationGlobale: MutableList<StationModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AjoutFavorisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var db = DataBaseSQLite(this)
        var stationListRecherche = db.recupNomStation()
        var listeStationGlobale = db.recuperationStations()


        val stationAdapter : ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            stationListRecherche
        )

        binding.listViewStation.adapter = stationAdapter;
        binding.listViewStation.isClickable = true

        binding.listViewStation.setOnItemClickListener{adapterview, view, position, id  ->
            val valFavClick = stationAdapter.getItem(position).toString()

            listeStationGlobale.map {
                if (valFavClick == it.name) {
                    db.ajoutFavoris(it)
                }
            }
        }


        binding.barreRecherche.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {

                binding.barreRecherche.clearFocus()

                if (stationListRecherche.contains(query)){
                    stationAdapter.filter.filter(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                stationAdapter.filter.filter(newText)
                return false
            }


        })


    }





    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nav,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menu_favori -> {val intent = Intent(this, Favoris::class.java)
                startActivity(intent)}

            R.id.menu_accueil -> {val intent = Intent(this, ProjetVelib::class.java)
                startActivity(intent)}
        }
        return super.onOptionsItemSelected(item)
    }
}
