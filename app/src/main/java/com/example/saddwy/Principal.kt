package com.example.saddwy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
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
                val manager = supportFragmentManager
                var transaction = manager.beginTransaction()
                //var contenedorFragment=findViewById<>(R.id.fragment_layout_main)
                transaction.replace(
                    R.id.fragment_layout_main,
                    Fragment_Manual()
                ).commit()
                transaction.addToBackStack((null))

            }

           

            R.id.nav_item_Perfil -> {
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
}