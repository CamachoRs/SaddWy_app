package com.example.saddwy

import Config.Config
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Principal : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        //se crea una variable que contiene la header main
       val tolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(tolbar)

        //sesion

        //-----Finis session
        var toggle: ActionBarDrawerToggle

        drawer = findViewById<DrawerLayout>(R.id.drawer_layout)

        toggle=ActionBarDrawerToggle(
            this,
            drawer,
            tolbar,
            R.string.app_close,
            R.string.app_open
        )

        drawer.addDrawerListener(toggle)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //añadir funcionalida
        supportFragmentManager.popBackStack()
        var navigationView: NavigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)


        val manager = supportFragmentManager
        var transaction = manager.beginTransaction()
        //var contenedorFragment=findViewById<>(R.id.fragment_layout_main)
        transaction.add(
            R.id.fragment_layout_main,
            Fragmen_Inicio()
        ).commit()
        transaction.addToBackStack((null))


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_Home -> {
                supportFragmentManager.popBackStack()
                val manager = supportFragmentManager
                var transaction = manager.beginTransaction()
                //var contenedorFragment=findViewById<>(R.id.fragment_layout_main)
                transaction.replace(
                    R.id.fragment_layout_main,
                    Fragmen_Inicio()
                ).commit()
                transaction.addToBackStack((null))
            }

            R.id.nav_item_Manual -> {
                val request = DownloadManager.Request(Uri.parse("https://drive.google.com/file/d/1y9Uocy55qlZtpMg-gO7kDVnu27ulaGBn/view?usp=sharing"))
                    .setTitle("Manual de usuario: SaddWy")
                    .setDescription("Descarga tu guía para dominar la App")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Manual_de_usuario.pdf")

                val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                manager.enqueue(request)

            }

           

            R.id.nav_item_Perfil -> {
                supportFragmentManager.popBackStack()
                val manager = supportFragmentManager
                var transaction = manager.beginTransaction()
                //var contenedorFragment=findViewById<>(R.id.fragment_layout_main)
                transaction.replace(
                    R.id.fragment_layout_main,
                    Fragment_Perfil()
                ).commit()
                transaction.addToBackStack((null))
            }

            R.id.nav_item_Ranking -> {
                supportFragmentManager.popBackStack()
                val manager = supportFragmentManager
                var transaction = manager.beginTransaction()
                //var contenedorFragment=findViewById<>(R.id.fragment_layout_main)
                transaction.replace(
                    R.id.fragment_layout_main,
                    Frangment_Ranking()
                ).commit()
                transaction.addToBackStack((null))
            }

            R.id.nav_item_Session -> {
                borrarSesion()
               CerrarSesion()
            }

        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


    private fun CerrarSesion(){
    startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    private fun borrarSesion() {
        val prefs = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // Verificar si se presionó la tecla de retroceso
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Exit")
            builder.setMessage("¿Está seguro de cerrar la aplicación?")

            builder.setNegativeButton("No") { dialog, id ->
                dialog.cancel() // Cancelar el diálogo
            }
            builder.setPositiveButton("Aceptar") { dialog, id ->
                CerrarSesion()
                borrarSesion()
                finish() // Cerrar la actividad
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
            return true // Indicar que se ha manejado el evento de la tecla de retroceso
        }
        return super.onKeyDown(keyCode, event)
    }



}