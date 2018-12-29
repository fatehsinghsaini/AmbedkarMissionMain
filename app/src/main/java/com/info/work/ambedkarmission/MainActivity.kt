package com.info.work.ambedkarmission

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth

import com.info.work.ambedkarmission.Adapter.ViewPagerAdapter
import com.info.work.ambedkarmission.Fragment.All
import com.info.work.ambedkarmission.Fragment.Audio
import com.info.work.ambedkarmission.Fragment.Video
import com.ois.todo.activity.BaseActivity

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var toolbar: Toolbar? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var logout: MenuItem? = null
    private var login: MenuItem? = null
    private var uploadDoc: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        setupViewPager(viewPager!!)

        tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout!!.setupWithViewPager(viewPager)

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val menu = navigationView.menu
        login=menu.findItem(R.id.login)
        logout=menu.findItem(R.id.logout)
        uploadDoc=menu.findItem(R.id.uploadDoc)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(All(), "All")
       /* adapter.addFragment(Audio(), "Audio")
        adapter.addFragment(Video(), "Video")*/
        viewPager.adapter = adapter
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onStart() {
        super.onStart()
        initLogoutView()
    }

    private fun initLogoutView() {

        logout!!.isVisible = sharedPref!!.contains(getString(R.string.loginAuthKey))
        login!!.isVisible = !sharedPref!!.contains(getString(R.string.loginAuthKey))
        uploadDoc!!.isVisible = sharedPref!!.contains(getString(R.string.loginAuthKey))

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.Home) {

        } else if (id == R.id.Registration) {
            val intent = Intent(this@MainActivity, Registration::class.java)
            startActivity(intent)
        }else if (id == R.id.login) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }else if (id == R.id.uploadDoc) {
            val intent = Intent(this@MainActivity, UploadMediaActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.Feedback) {

        } else if (id == R.id.AboutUs) {

        } else if (id == R.id.AmbedkarMission) {

        }else if (id == R.id.logout) {
            sharedPref!!.edit().clear().apply()
            initLogoutView()
            FirebaseAuth.getInstance().signOut()
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onRestart() {
        super.onRestart()
        logout!!.isVisible = sharedPref!!.contains(getString(R.string.loginAuthKey))

    }

}
