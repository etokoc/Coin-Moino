package com.metoer.ceptedovizborsa.view.activity

import android.app.ActionBar
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.databinding.ActivityMainBinding
import com.metoer.ceptedovizborsa.databinding.CustomAboutDialogBinding
import com.metoer.ceptedovizborsa.databinding.CustomFallowDialogBinding
import com.metoer.ceptedovizborsa.databinding.CustomFontsizeDialogBinding
import com.metoer.ceptedovizborsa.databinding.CustomLanguageDialogBinding
import com.metoer.ceptedovizborsa.util.SharedPrefencesUtil
import com.metoer.ceptedovizborsa.util.showToastShort
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {

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
            val drawerSwitch: SwitchCompat =
                binding.mainNav.menu.findItem(R.id.thema_menu).actionView as SwitchCompat
            getDarkAndLightThema(drawerSwitch)
            mainDrawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            drawerSwitch.setOnCheckedChangeListener { _, isChecked ->
                Handler(Looper.getMainLooper()).postDelayed({
                    if (isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        setDarkAndLightThema(true)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        setDarkAndLightThema(false)
                    }
                    showToastShort(getString(R.string.thema_change_message))
                }, 500)
            }
            mainNav.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.setting_menu -> {
                        languageDialog()
                    }

                    R.id.fontsize_menu -> {
                        fontSizeDialog()
                    }

                    R.id.about_menu -> {
                        aboutDialog()
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

    private fun getDarkAndLightThema(switchCompat: SwitchCompat) {
        val prefs = SharedPrefencesUtil(applicationContext)
        val thema = prefs.getLocal("night", Boolean)
        switchCompat.isChecked = thema == true
    }

    private fun setDarkAndLightThema(b: Boolean) {
        val prefs = SharedPrefencesUtil(applicationContext)
        if (b) {
            prefs.addLocal("night", true)
        } else {
            prefs.addLocal("night", false)
        }
    }

    private var secili: Boolean = false
    private fun fontSizeDialog() {
        val dialog = Dialog(this)
        val bindingDialog = CustomFontsizeDialogBinding.inflate(layoutInflater, binding.root, false)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        val window = dialog.window
        window?.attributes!!.windowAnimations = R.style.DialogAnimation
        bindingDialog.apply {
            when (getFontScale()) {
                0f -> radioButtonDefault.isChecked = true
                0.9f -> radioButtonSmall.isChecked = true
                1.0f -> radioButtonMid.isChecked = true
                1.1f -> radioButtonLarge.isChecked = true
            }
            fontsizeConfirmButton.setOnClickListener {
                val selectedRadioButtonId = fontsizeRadioGroup.checkedRadioButtonId
                when (selectedRadioButtonId) {
                    radioButtonDefault.id -> {
                        secili = false
                        updateFontScale(resources.configuration.fontScale, true)
                        recreate()
                    }

                    radioButtonSmall.id -> {
                        secili = true
                        updateFontScale(0.9f)
                        recreate()
                    }

                    radioButtonMid.id -> {
                        secili = true
                        updateFontScale(1.0f)
                        recreate()
                    }

                    radioButtonLarge.id -> {
                        secili = true
                        updateFontScale(1.1f)
                        recreate()
                    }

                    else -> {}
                }
                dialog.dismiss()
            }
        }
        window.setLayout(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat("fontScale", resources.configuration.fontScale)
        outState.putBoolean("secili", secili)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val fontScale = savedInstanceState.getFloat("fontScale")
        val secili = savedInstanceState.getBoolean("secili")
        if (secili == false)
            updateFontScale(resources.configuration.fontScale, true)
        else
            updateFontScale(fontScale)
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
                dialog.dismiss()
            }
            buttonFallowDialogUrl2.setOnClickListener {
                val i = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url2)
                )
                startActivity(i)
                dialog.dismiss()
            }
            imageViewFallowCancel.setOnClickListener {
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

    private fun aboutDialog() {
        val dialog = Dialog(this)
        val bindingDialog =
            CustomAboutDialogBinding.inflate(
                LayoutInflater.from(binding.root.context),
                binding.root,
                false
            )
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        val window = dialog.window
        window?.attributes!!.windowAnimations = R.style.DialogAnimation
        bindingDialog.apply {
            imageViewAboutCancel.setOnClickListener {
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

    private fun getFontScale(): Float {
        val prefs = SharedPrefencesUtil(applicationContext)
        val fontSize = prefs.getLocal("Font_Size", Float)
        return fontSize as Float
    }

    private fun updateFontScale(scale: Float, ifDefault: Boolean? = false) {
        val sharedPref = SharedPrefencesUtil(applicationContext)
        val configuration = resources.configuration
        configuration.fontScale = scale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        if (ifDefault == true)
            sharedPref.addLocal("Font_Size", 0f)
        else
            sharedPref.addLocal("Font_Size", scale)
    }

    private fun setLocale(language: String) {
        supportLocale()
        val sharedPref = SharedPrefencesUtil(applicationContext)
        sharedPref.addLocal("My_Lang", language)
    }

    private fun loadLocaleString(): String {
        val prefs = SharedPrefencesUtil(applicationContext)
        val language = prefs.getLocal("My_Lang", String)
        return language.toString()
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
        supportLocale()
    }
}