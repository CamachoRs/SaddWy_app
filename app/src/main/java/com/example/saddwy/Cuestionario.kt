package com.example.saddwy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class Cuestionario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuestionario)


        var viewPager: ViewPager = findViewById(R.id.viewPager) as ViewPager
        var tableyout: TabLayout = findViewById(R.id.tablayout) as TabLayout

        val fragmentAdapter = FragmentAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(Explicacion_Fragment(), "Explicacion")
        fragmentAdapter.addFragment(Pregunta_Fragment(), "Pregunta")

        viewPager.adapter = fragmentAdapter
        tableyout.setupWithViewPager(viewPager)
    }
}