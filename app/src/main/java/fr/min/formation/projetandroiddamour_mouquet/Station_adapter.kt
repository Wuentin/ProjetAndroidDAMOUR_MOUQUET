package fr.min.formation.projetandroiddamour_mouquet
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.min.formation.projetandroiddamour_mouquet.model.StationModel


class StationAdapter(val stationList: List<StationModel>) :
    RecyclerView.Adapter<StationAdapter.StationViewHolder>() {

    class StationViewHolder(val view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val stationView = inflater.inflate(R.layout.model_favoris, parent, false)
        return StationViewHolder(stationView)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val station = stationList[position]



        val station_nom = holder.view.findViewById<TextView>(R.id.adapter_nom)
            station_nom.text = station.nom

        val station_capacite = holder.view.findViewById<TextView>(R.id.adapter_capacité)
        station_capacite.text = "Capacité : " + station.capacite

        val station_veloDispo = holder.view.findViewById<TextView>(R.id.adapter_velo_dispo)
        station_veloDispo.text = "Vélos disponibles : " + station.disponibilite

        val station_emplacement = holder.view.findViewById<TextView>(R.id.adapter_emplacement_dispo)
        station_emplacement.text = "Emplacements disponibles : " + station.emplacement





    }

    override fun getItemCount() =  stationList.size

}



val StationModel.nom
    get() = "${this.name}"

val StationModel.capacite
    get() = "${this.capacity}"

val StationModel.disponibilite
    get() = "${this.num_bikes_available}"

val StationModel.emplacement
    get() = "${this.num_docks_available}"

