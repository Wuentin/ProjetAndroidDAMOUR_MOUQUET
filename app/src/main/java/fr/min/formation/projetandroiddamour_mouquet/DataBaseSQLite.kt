package fr.min.formation.projetandroiddamour_mouquet

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getStringOrNull
import fr.min.formation.projetandroiddamour_mouquet.model.StationModel

val table_principale = "Stations"
val table_favoris = "Favoriss"
val col_id = "Identifiant"
val col_nom = "Nom"
val col_capacite = "Capacite"
val col_veloDispo = "VeloDispo"
val col_emplacement = "Emplacement"
val col_latitude = "Latitude"
val col_longitude = "Longitude"


class DataBaseSQLite(context: Context) : SQLiteOpenHelper(context, "DBStation", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {


    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


    fun insertionDonnee(station: StationModel){
        val db = this.writableDatabase
        var valeurs = ContentValues()
        valeurs.put(col_id, station.station_id)
        valeurs.put(col_nom, station.name)
        valeurs.put(col_capacite, station.capacity)
        valeurs.put(col_veloDispo, station.num_bikes_available)
        valeurs.put(col_emplacement, station.num_docks_available)
        valeurs.put(col_latitude, station.latitude)
        valeurs.put(col_longitude, station.longitude)

        var envoie = db.insert(table_principale, null, valeurs)
    }


        fun recuperationStations(): MutableList<StationModel> {

            var liste_Noms : MutableList<StationModel> = ArrayList()
            val db = this.readableDatabase
            val query =  "Select * from " + table_principale

            val result = db.rawQuery(query,null)
            if (result.moveToFirst()){
                do {
                    var ajoutStation = StationModel(
                        result.getStringOrNull(0).toString(),
                        result.getStringOrNull(1).toString(),
                        result.getStringOrNull(2)!!.toInt(),
                        result.getStringOrNull(3)!!.toInt(),
                        result.getStringOrNull(4)!!.toInt(),
                        result.getStringOrNull(5)!!.toDouble(),
                        result.getStringOrNull(6)!!.toDouble()
                    )

                    liste_Noms.add(ajoutStation)
                } while (result.moveToNext())
            }

            result.close()
            db.close()
            return liste_Noms
        }


    fun recupFavoris(): MutableList<StationModel> {

        var liste_Noms : MutableList<StationModel> = ArrayList()
        val db = this.readableDatabase
        val query =  "Select * from " + table_favoris

        val result = db.rawQuery(query,null)
        if (result.moveToFirst()){
            do {
                var ajoutStationFav = StationModel(
                    result.getStringOrNull(0).toString(),
                    result.getStringOrNull(1).toString(),
                    result.getStringOrNull(2)!!.toInt(),
                    result.getStringOrNull(3)!!.toInt(),
                    result.getStringOrNull(4)!!.toInt(),
                    result.getStringOrNull(5)!!.toDouble(),
                    result.getStringOrNull(6)!!.toDouble()
                )

                liste_Noms.add(ajoutStationFav)
            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return liste_Noms
    }

    fun recupNomStation(): MutableList<String> {


        var liste_Noms : MutableList<String> = ArrayList()
        val db = this.readableDatabase
        val query =  "Select Nom from " + table_principale

        val result = db.rawQuery(query,null)
        if (result.moveToFirst()){
            do {
                var nomRecup = result.getString(0)
                liste_Noms.add(nomRecup)
            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return liste_Noms
    }



    fun ajoutFavoris(station: StationModel){
        val db = this.writableDatabase
        val create_favoris = "CREATE TABLE IF NOT EXISTS " + table_favoris + " (" +
                col_id + " VARCHAR(256)," +
                col_nom + " VARCHAR(256)," +
                col_capacite + " INTEGER," +
                col_veloDispo + " INTEGER," +
                col_emplacement + " INTEGER," +
                col_latitude + " DOUBLE," +
                col_longitude + " DOUBLE)"
        db?.execSQL(create_favoris)


        var valeurs = ContentValues()
        valeurs.put(col_id, station.station_id)
        valeurs.put(col_nom, station.name)
        valeurs.put(col_capacite, station.capacity)
        valeurs.put(col_veloDispo, station.num_bikes_available)
        valeurs.put(col_emplacement, station.num_docks_available)
        valeurs.put(col_latitude, station.latitude)
        valeurs.put(col_longitude, station.longitude)

        var envoie = db.insert(table_favoris, null, valeurs)
    }

   fun creation_table(){
       val db = this.writableDatabase

       val create_table = "CREATE TABLE IF NOT EXISTS " + table_principale + " (" +
               col_id + " VARCHAR(256) NOT NULL PRIMARY KEY," +
               col_nom + " VARCHAR(256)," +
               col_capacite + " INTEGER," +
               col_veloDispo + " INTEGER," +
               col_emplacement + " INTEGER," +
               col_latitude + " DOUBLE," +
               col_longitude + " DOUBLE)"


       db?.execSQL(create_table)
   }
    fun delete(){
        val db = this.writableDatabase

        val query= "DELETE FROM $table_principale"
        db?.execSQL(query)
    }


    fun recupNomStationFavoris(): MutableList<String> {


        var liste_Noms : MutableList<String> = ArrayList()
        val db = this.readableDatabase
        val query =  "Select Nom from " + table_favoris

        val result = db.rawQuery(query,null)
        if (result.moveToFirst()){
            do {
                var nomRecup = result.getString(0)
                liste_Noms.add(nomRecup)
            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return liste_Noms
    }



    fun update(station: StationModel) {

        val db = this.writableDatabase


        val valeur_id = station.station_id
        val valeur_name = station.name
        val valeur_capacite = station.capacity
        val valeur_veloDispo = station.num_bikes_available
        val valeur_emplacement = station.num_docks_available
        val valeur_latitude = station.latitude
        val valeur_longitude = station.longitude

        // .contains ne détecte pas le '
        if (valeur_name.contains("'")){valeur_name.replace("'"," ")}
        val query =
            " UPDATE " + table_favoris + " SET "+ col_nom + " = " + "'$valeur_name'" + "," +
                    col_capacite + " = " +"$valeur_capacite" + "," +
                    col_veloDispo + " = " + "$valeur_veloDispo" + "," +
                    col_emplacement + " = " + "$valeur_emplacement" + "," +
                    col_latitude + " = " + "$valeur_latitude" + "," +
                    col_longitude + " = " + "$valeur_longitude"+
                    " WHERE " + col_nom + " = " + "'$valeur_name'"

                    db?.execSQL(query)


    }

}