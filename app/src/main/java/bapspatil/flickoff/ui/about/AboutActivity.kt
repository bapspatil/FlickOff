package bapspatil.flickoff.ui.about

import android.os.Bundle
import bapspatil.flickoff.BR
import bapspatil.flickoff.R
import bapspatil.flickoff.databinding.ActivityAboutBinding
import bapspatil.flickoff.ui.base.BaseActivity
import bapspatil.flickoff.utils.GlideApp
import kotlinx.android.synthetic.main.activity_about.*
import org.jetbrains.anko.browse
import javax.inject.Inject

class AboutActivity : BaseActivity<ActivityAboutBinding, AboutViewModel>(), AboutNavigator {

    @Inject
    lateinit var mAboutViewModel: AboutViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_about

    override fun getViewModel(): AboutViewModel = mAboutViewModel

    override fun handleError(throwable: Throwable) {
        // Handle any errors here
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAboutViewModel.setNavigator(this)
        aboutToolbar.title = ""
        setSupportActionBar(aboutToolbar)

        GlideApp.with(this)
                .load("https://github.com/bapspatil.png")
                .placeholder(R.drawable.baps)
                .error(R.drawable.baps)
                .fallback(R.drawable.baps)
                .into(bapsImageView)
        websiteImageView.setOnClickListener {
            browse("https://bapspatil.com")
        }
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
