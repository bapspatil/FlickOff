package bapspatil.flickoff.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import bapspatil.flickoff.R
import kotlinx.android.synthetic.main.activity_about_me.*
import org.jetbrains.anko.browse

class AboutMeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)
        aboutToolbar.title = ""
        setSupportActionBar(aboutToolbar)
        playImageView.setOnClickListener {
            browse("https://play.google.com/store/apps/dev?id=7368032842071222295")
        }
        githubImageView.setOnClickListener {
            browse("https://github.com/bapspatil")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
