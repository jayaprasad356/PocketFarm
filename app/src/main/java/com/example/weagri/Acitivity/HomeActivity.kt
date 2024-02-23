package com.example.weagri.Acitivity

import android.app.Application
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.weagri.Fragment.ExploreFragment
import com.example.weagri.Fragment.HomeFragment
import com.example.weagri.Fragment.MoreFragment
import com.example.weagri.Fragment.MyteamFragment
import com.example.weagri.Fragment.PlanFragment
import com.example.weagri.R
import com.example.weagri.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.zoho.commons.InitConfig
import com.zoho.livechat.android.listeners.InitListener
import com.zoho.salesiqembed.ZohoSalesIQ

class HomeActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {


    private var fragment_container: FrameLayout? = null
    private var bottomNavigationView: BottomNavigationView? = null
    lateinit var binding: ActivityHomeBinding

    private lateinit var fm: FragmentManager

    private lateinit var homeFragment: HomeFragment
    private lateinit var myteamFragment: MyteamFragment
    private lateinit var exploreFragment: ExploreFragment
    private lateinit var moreFragment: MoreFragment
    private lateinit var planFragment: PlanFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //tvTitle text is double color text "We" and "Agri"
        val tvTitle = "<font color='#F8B328'>Pocket</font> "+"<font color='#00B251'>Farm</font>"

        binding.tvTitle.text = Html.fromHtml(tvTitle)

        fm = supportFragmentManager
        homeFragment = HomeFragment()
        myteamFragment = MyteamFragment()
        exploreFragment = ExploreFragment()
        moreFragment = MoreFragment()
        planFragment = PlanFragment()



        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView!!.setOnItemSelectedListener(this)

        fm.beginTransaction().replace(R.id.fragment_container, homeFragment).commit()
        bottomNavigationView!!.selectedItemId = R.id.navHome


        binding.fab.setOnClickListener {
            fm.beginTransaction().replace(R.id.fragment_container, exploreFragment).commit()
            bottomNavigationView!!.selectedItemId = R.id.placeholder

        }


        val initConfig = InitConfig()
        // Set your font configurations

        ZohoSalesIQ.init(application, "q%2FetVzn%2B7suBUISHrv%2B4MAcJ28PTqq9c8NCG12tbTKEPyGbNr2VZjxcURpltcVF97toxu8fzIm4%3D_in", "VXYedrQX8SnJI7EFCpu01dOAbrxweRFJ9LoYwisslnQwtg7o2gI5TDmhdHf%2BpskotIQR5eZeZIfAQHqtrJtg%2Fvrhon6CEgZuOhnVA8woxXjqz2ZMOkOvsw%3D%3D", initConfig, object : InitListener {
            override fun onInitSuccess() {
                // fit place to show the chat launcher
                ZohoSalesIQ.Launcher.show(ZohoSalesIQ.Launcher.VisibilityMode.ALWAYS)

            }

            override fun onInitError(errorCode: Int, errorMessage: String) {
                // Handle initialization errors
            }
        })




    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val transaction: FragmentTransaction = fm.beginTransaction()

        when (item.itemId) {
            R.id.navHome -> {


//                transaction.setCustomAnimations(
//                    R.anim.slide_in_top_right,
//                    R.anim.slide_out_bottom_left
//                )
                transaction.replace(R.id.fragment_container, homeFragment)
            }

            R.id.navplan -> {
//                transaction.setCustomAnimations(
//                    R.anim.slide_in_top_right,
//                    R.anim.slide_out_bottom_left
//                )
                transaction.replace(R.id.fragment_container, planFragment)
            }

//            R.id.navExplore -> {
//                transaction.replace(R.id.fragment_container, exploreFragment)
//            }

            R.id.navMyteam ->{
                transaction.replace(R.id.fragment_container,myteamFragment)
            }

            R.id.navMore ->{
                transaction.replace(R.id.fragment_container,moreFragment)
            }

        }



        transaction.commit()
        return true
    }

    // onBackPress method is not present in SplashScreen Activity.kt
    override fun onBackPressed() {
        if (fm.backStackEntryCount > 0) {
            fm.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}