package com.drt.quizapp.Model

import org.json.JSONArray
import org.json.JSONObject

class ParsePlantUtility {

    fun parsePlantObjectsFromJSONData() : List<Plant>?{


        var allPlantObjects : ArrayList<Plant> = ArrayList()
        var downloadingObject = DownloadingObject()
        var topLevelPlantJSONData = downloadingObject.
            downloadJSONDataFromLink("http://plantplaces.com/perl/mobile/flashcard.pl")

        var topLevelJSONDataObject: JSONObject = JSONObject(topLevelPlantJSONData)

        var plantObjectArray: JSONArray = topLevelJSONDataObject.getJSONArray("values")
        var count: Int = 0
        while (count < plantObjectArray.length()){
            var plantObject: Plant = Plant()
            var jsonObject = plantObjectArray.getJSONObject(count)

            with(jsonObject){
                plantObject.genus = getString("genus")
                plantObject.species = getString("species")
                plantObject.cultivar = getString("cultivar")
                plantObject.common = getString("common")
                plantObject.picture_name = getString("picture_name")
                plantObject.description = getString("description")
                plantObject.difficulty = getInt("difficulty")
                plantObject.id = getInt("id")

            }
            allPlantObjects.add(plantObject)

            count++

        }

        return allPlantObjects

    }
}