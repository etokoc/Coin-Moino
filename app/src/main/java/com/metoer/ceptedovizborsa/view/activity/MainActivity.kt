package com.metoer.ceptedovizborsa.view.activity

import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.databinding.ActivityMainBinding
import com.metoer.ceptedovizborsa.databinding.CustomFallowDialogBinding
import com.metoer.ceptedovizborsa.databinding.CustomLanguageDialogBinding
import com.metoer.ceptedovizborsa.util.showToastShort
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocale()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            val navController = findNavController(R.id.home_fragment)
            bottomMenuBar.setupWithNavController(navController)
            setSupportActionBar(appBar)

            toggle = ActionBarDrawerToggle(
                this@MainActivity,
                mainDrawerLayout,
                R.string.open,
                R.string.close
            )
            mainDrawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            mainNav.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.setting_menu -> {
                        languageDialog()
                    }
                    R.id.about_menu -> {

                    }
                    R.id.instagram_menu -> {
                        fallowDialog(
                            "https://www.instagram.com/omerseyfettinyavuzyigit/",
                            "https://www.instagram.com/ertugrul.koc.06/",
                            "/omerseyfettinyavuzyigit",
                            "/ertugrul.koc.06",
                            R.drawable.instagram_icon
                        )
                    }
                    R.id.twitter_menu -> {
                        fallowDialog(
                            "https://twitter.com/yavuzyigit_omer",
                            "https://twitter.com/koc_etoertugrul",
                            "@yavuzyigit_omer",
                            "@koc_etoertugrul",
                            R.drawable.twitter_icon
                        )
                    }
                    R.id.linkedin_menu -> {
                        fallowDialog(
                            "https://www.linkedin.com/in/%C3%B6mer-yavuzyi%C4%9Fit-12a079223/",
                            "https://www.linkedin.com/in/ertugrul-koc/",
                            "/omer_yavuzyigit", "/ertugrul_koc", R.drawable.linkedin_icon
                        )
                    }
                }
                true
            }
        }
    }

    private fun fallowDialog(
        url1: String,
        url2: String,
        text1: String,
        text2: String,
        drawable: Int
    ) {
        val dialog = Dialog(this)
        val bindingDialog =
            CustomFallowDialogBinding.inflate(
                LayoutInflater.from(binding.root.context),
                binding.root,
                false
            )
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        val window = dialog.window
        window?.attributes!!.windowAnimations = R.style.DialogAnimation
        bindingDialog.apply {
            buttonFallowDialogUrl1.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
            buttonFallowDialogUrl2.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
            buttonFallowDialogUrl1.text = text1
            buttonFallowDialogUrl2.text = text2
            buttonFallowDialogUrl1.setOnClickListener {
                val i = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url1)
                )
                startActivity(i)
            }
            buttonFallowDialogUrl2.setOnClickListener {
                val i = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url2)
                )
                startActivity(i)
            }
        }
        dialog.setCancelable(true)
        window.setLayout(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    private fun languageDialog() {
        val dialog = Dialog(this)
        val bindingDialog =
            CustomLanguageDialogBinding.inflate(
                LayoutInflater.from(binding.root.context),
                binding.root,
                false
            )
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        val window = dialog.window
        window?.attributes!!.windowAnimations = R.style.DialogAnimation
        bindingDialog.apply {
            if (loadLocaleString() == "tr" || loadLocaleString() == "") {
                radioButtonTurkish.isChecked = true
            } else {
                radioButtonEnglish.isChecked = true
            }
            buttonConfirmDialog.setOnClickListener {
                if (radioButtonTurkish.isChecked) {
                    showToastShort("Türkçe")
                    setLocale("tr")
                    recreate()
                } else {
                    showToastShort("İngilizce")
                    setLocale("en")
                    recreate()
                }
                dialog.dismiss()
            }
        }
        dialog.setCancelable(true)
        window.setLayout(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)

        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(
            configuration,
            baseContext.resources.displayMetrics
        )
        val editor = getSharedPreferences("Language", MODE_PRIVATE).edit()
        editor.putString("My_Lang", language)
        editor.apply()
    }

    private fun loadLocaleString(): String {
        val prefs = getSharedPreferences("Language", Activity.MODE_PRIVATE)
        val language = prefs.getString("My_Lang", "")
        return language!!
    }

    private fun loadLocale() {
        setLocale(loadLocaleString())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (binding.mainDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.mainDrawerLayout.closeDrawer(GravityCompat.END)
            if (toggle.onOptionsItemSelected(item)) {
                return true
            }
        } else {
            binding.mainDrawerLayout.openDrawer(GravityCompat.END)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

    }
}