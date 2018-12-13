package com.drt.quizapp.Controller

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.drt.quizapp.Model.DownloadingObject
import com.drt.quizapp.Model.ParsePlantUtility
import com.drt.quizapp.Model.Plant
import com.drt.quizapp.R
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var cameraButton: Button? = null
    private var galleryButton: Button? = null
    private var imageTaken: ImageView? = null
    /* private var button1: Button? = null
     private var button2: Button? = null
     private var button3: Button? = null
     private var button4: Button? = null*/
    val OPEN_CAMERA_BUTTON_REQUEST_ID = 1
    val OPEN_GALLERY_BUTTON_REQUEST_ID = 2

    //Instance Variables
    var correctAnswerIndex: Int = 0
    var correctPlant: Plant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // setSupportActionBar(toolbar)


        /*  val myPlant: Plant = Plant("","","","","",""
          ,0,  0)
x
          myPlant.plantName = "Golden Rain Tree"*/

        /*var flower = Plant()
        var tree = Plant()

        var collectionOfPlants: ArrayList<Plant> = ArrayList()

        collectionOfPlants.add(flower)
        collectionOfPlants.add(tree)*/



        cameraButton = findViewById<Button>(R.id.btnOpenCamera)
        galleryButton = findViewById<Button>(R.id.btnOpenGallery)
        imageTaken = findViewById<ImageView>(R.id.imgTaken)

        /*button1 = findViewById<Button>(R.id.button1)
        button2 = findViewById<Button>(R.id.button2)
        button3 = findViewById<Button>(R.id.button3)
        button4 = findViewById<Button>(R.id.button4)*/

        cameraButton?.setOnClickListener {
            Toast.makeText(this, "Button 2 is clicked", Toast.LENGTH_LONG).show()
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, OPEN_CAMERA_BUTTON_REQUEST_ID)
        }

        galleryButton?.setOnClickListener {
            Toast.makeText(this, "Photo Gallery Button is clicked", Toast.LENGTH_LONG).show()

            val galleryIntent =
                Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, OPEN_GALLERY_BUTTON_REQUEST_ID)
        }

//

        // see next plant
        btnNextPlant.setOnClickListener {
            if (checkForInternetConnection()) {
                try {
                    val innerClassObject = DownloadingPlantTask()
                    innerClassObject.execute()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


    }

    // check for internet connection
    private fun checkForInternetConnection(): Boolean {
        var flag: Boolean = false
        val connectivityManager: ConnectivityManager = this.getSystemService(
            android.content.Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager


        val networkInfo = connectivityManager.activeNetworkInfo
        val isDeviceConnectedToInternet = networkInfo != null &&
                networkInfo.isConnected

        if (isDeviceConnectedToInternet) {
            flag = true
        } else {
            createAlert()
            flag = false
        }
        return flag
    }

    override fun onResume() {
        super.onResume()
        checkForInternetConnection()
    }

    private fun createAlert() {
        val alertDailog: AlertDialog = AlertDialog.Builder(this@MainActivity).create()
        alertDailog.setTitle("Network Error")
        alertDailog.setMessage("Please check internet connection")
        alertDailog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", { dialog: DialogInterface?, which: Int ->

            startActivity(Intent(Settings.ACTION_SETTINGS))
        })

        alertDailog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", { dialog: DialogInterface?, which: Int ->
            Toast.makeText(this@MainActivity, "You must be connected to the internet", Toast.LENGTH_LONG).show()
            finish()
        })

        alertDailog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == OPEN_CAMERA_BUTTON_REQUEST_ID) {
            if (resultCode == Activity.RESULT_OK) {
                val imageData = data?.getExtras()?.get("data") as Bitmap
                imageTaken?.setImageBitmap(imageData)
            }
        }

        if (requestCode == OPEN_GALLERY_BUTTON_REQUEST_ID) {
            if (resultCode == Activity.RESULT_OK) {
                val contentURI = data?.getData()
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                imgTaken.setImageBitmap(bitmap)
            }
        }
    }

    fun imageClicked(view: View) {
        /* val randomNumber: Int = (Math.random() * 6).toInt() + 1
         Log.i("TAG","the random number is: $randomNumber")
          if(randomNumber == 1)
              btnOpenCamera.setBackgroundColor(Color.BLUE)
          else if(randomNumber == 2)
              btnOpenCamera.setBackgroundColor(Color.GREEN)
          else if(randomNumber == 3)
              button4.setBackgroundColor(Color.MAGENTA)
          else if(randomNumber == 4)
              button1.setBackgroundColor(Color.GREEN)
          else if(randomNumber == 5)
              button2.setBackgroundColor(Color.YELLOW)
          else if(randomNumber == 6)
              button3.setBackgroundColor(Color.DKGRAY)*/

    }

    fun button1IsClicked(buttonView: View) {
        specifyCorrectAnswer(userGuess = 0)
    }

    fun button2IsClicked(buttonView: View) {
        specifyCorrectAnswer(userGuess = 1)
    }

    fun button3IsClicked(buttonView: View) {
        specifyCorrectAnswer(userGuess = 2)
    }

    fun button4IsClicked(buttonView: View) {
        specifyCorrectAnswer(userGuess = 3)
    }


    inner class DownloadingPlantTask : AsyncTask<String, Int, List<Plant>>() {

        override fun doInBackground(vararg params: String?): List<Plant>? {
            //can access background interface but cannot access User Interface
            /* val downloadingObject: DownloadingObject = DownloadingObject()
             var jSONData = downloadingObject.downloadJSONDataFromLink("http://plantplaces.com/perl/mobile/flashcard.pl" )
             Log.i("JSON", jSONData)*/

            var parsePlant = ParsePlantUtility()

            return parsePlant.parsePlantObjectsFromJSONData()
        }


        override fun onPostExecute(result: List<Plant>?) {
            //cannot access background interface but can access User Interface
            var numberOfPlants = result?.size ?: 0
            if (numberOfPlants > 0) {
                var randonPlantIndexForButton1: Int = (Math.random() * result!!.size).toInt()
                var randonPlantIndexForButton2: Int = (Math.random() * result!!.size).toInt()
                var randonPlantIndexForButton3: Int = (Math.random() * result!!.size).toInt()
                var randonPlantIndexForButton4: Int = (Math.random() * result!!.size).toInt()

                var allRandomPlants = ArrayList<Plant>()
                allRandomPlants.add(result.get(randonPlantIndexForButton1))
                allRandomPlants.add(result.get(randonPlantIndexForButton2))
                allRandomPlants.add(result.get(randonPlantIndexForButton3))
                allRandomPlants.add(result.get(randonPlantIndexForButton4))

                button1.text = result.get(randonPlantIndexForButton1).toString()
                button2.text = result.get(randonPlantIndexForButton2).toString()
                button3.text = result.get(randonPlantIndexForButton3).toString()
                button4.text = result.get(randonPlantIndexForButton4).toString()

                correctAnswerIndex = (Math.random() * allRandomPlants.size).toInt()
                correctPlant = allRandomPlants.get(correctAnswerIndex)

                val downloadingPictureTask = DownloadingImageTask()
                downloadingPictureTask.execute(allRandomPlants.get(correctAnswerIndex).picture_name)
            }
            super.onPostExecute(result)
        }
    }

    private fun specifyCorrectAnswer(userGuess: Int) {

        when (correctAnswerIndex) {
            0 -> button1.setBackgroundColor(Color.CYAN)
            1 -> button1.setBackgroundColor(Color.CYAN)
            2 -> button1.setBackgroundColor(Color.CYAN)
            3 -> button1.setBackgroundColor(Color.CYAN)
        }

        if (userGuess == correctAnswerIndex)
            textView.setText("Right")
        else {
            var correctPlantName = correctPlant.toString()
            textView.setText("Wrong. Choose: $correctPlantName")
        }

        //Downloading Image Process


    }

    inner class DownloadingImageTask : AsyncTask<String, Int, Bitmap?>() {
        override fun doInBackground(vararg pictureName: String?): Bitmap? {

            try {
                val downloadingObject = DownloadingObject()
                val plantBitmap: Bitmap? = downloadingObject.downloadPlantPicture(pictureName[0])
                return plantBitmap
            }
            catch (e: Exception){
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)

            imgTaken.setImageBitmap(result)
        }


    }
}



