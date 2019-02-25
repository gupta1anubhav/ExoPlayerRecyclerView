package com.example.anubhav.exoplayer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import androidx.core.content.ContextCompat
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    lateinit var rvFeed: ExoPlayerRecyclerView
    private val videoPojoList = ArrayList<VideoPojo>()
    private var mAdapter: ExoRecyclerViewAdapter? = null
    private var isFirstTime = true
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvFeed = findViewById(R.id.recyclerViewFeed)
        initVideoList()
        rvFeed.setVideoList(videoPojoList)
        mAdapter = ExoRecyclerViewAdapter(videoPojoList)
        rvFeed.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, 1, false)
        val dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_drawable)
        rvFeed.addItemDecoration(ItemDivider(dividerDrawable!!))
        rvFeed.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        rvFeed.adapter = mAdapter
        val toolbar:Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_open
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        if (isFirstTime) {
            Handler(Looper.getMainLooper()).post { rvFeed.playExoPlayerVideo() }
            isFirstTime = false
        }

    }

    private fun initVideoList() {

        val videoPojo1 = VideoPojo()
        videoPojo1.id = (1)
        videoPojo1.user = "User1"
        videoPojo1.title = videoPojo1.user+" Testimony"
        videoPojo1.thumbnail = ("https://s3-ap-southeast-1.amazonaws.com/magicpin-selfie/productimages/213308_cover_pic.png")
        videoPojo1.url = ("https://player.vimeo.com/external/286837767.m3u8?s=42570e8c4a91b98cdec7e7bfdf0eccf54e700b69")

        val videoPojo2 = VideoPojo()
        videoPojo2.id = (2)
        videoPojo2.user = "User2"
        videoPojo2.title = (videoPojo2.user+" Testimony")
        videoPojo2.thumbnail = ("https://s3-ap-southeast-1.amazonaws.com/magicpin-selfie/productimages/213308_cover_pic.png")
        videoPojo2.url = ("https://player.vimeo.com/external/286837810.m3u8?s=610b4fee49a71c2dbf22c01752372ff1c6459b9e")

        val videoPojo3 = VideoPojo()
        videoPojo3.id = (3)
        videoPojo3.user = "User3"
        videoPojo3.title = (videoPojo3.user+" Testimony")
        videoPojo3.thumbnail = ("https://s3-ap-southeast-1.amazonaws.com/magicpin-selfie/productimages/213308_cover_pic.png")
        videoPojo3.url = ("https://player.vimeo.com/external/286837723.m3u8?s=3df60d3c1c6c7a11df4047af99c5e05cc2e7ae96")

        val videoPojo4 = VideoPojo()
        videoPojo4.id = (4)
        videoPojo4.user = "User4"
        videoPojo4.title = (videoPojo4.user+" Testimony")
        videoPojo4.thumbnail = ("https://s3-ap-southeast-1.amazonaws.com/magicpin-selfie/productimages/213308_cover_pic.png")
        videoPojo4.url = ("https://player.vimeo.com/external/286837649.m3u8?s=9e486e9b932be72a8875afc6eaae21bab124a35a")

        val videoPojo5 = VideoPojo()
        videoPojo5.id = (5)
        videoPojo5.user = "User5"
        videoPojo5.title = (videoPojo5.user+" Testimony")
        videoPojo5.thumbnail = ("https://s3-ap-southeast-1.amazonaws.com/magicpin-selfie/productimages/213308_cover_pic.png")
        videoPojo5.url = ("https://player.vimeo.com/external/286837529.m3u8?s=20f83af6ea8fbfc8ce0c2001f32bf037f8b0f65f")

        val videoPojo6 = VideoPojo()
        videoPojo6.id = (6)
        videoPojo6.user = "User6"
        videoPojo6.title = (videoPojo6.user+" Testimony")
        videoPojo6.thumbnail = ("https://s3-ap-southeast-1.amazonaws.com/magicpin-selfie/productimages/213308_cover_pic.png")
        videoPojo6.url = ("https://player.vimeo.com/external/286837402.m3u8?s=7e01c398e2f01c29ecbd46e5e2dd53e0d6c1905d")

        videoPojoList.add(videoPojo1)
        videoPojoList.add(videoPojo2)
        videoPojoList.add(videoPojo3)
        videoPojoList.add(videoPojo4)
        videoPojoList.add(videoPojo5)
        videoPojoList.add(videoPojo6)
    }

    override fun onPause() {
        Handler(Looper.getMainLooper()).post { rvFeed.onPausePlayer() }
        super.onPause()
    }

    override fun onResume() {
        Handler(Looper.getMainLooper()).post { rvFeed.onRestartPlayer() }
        super.onResume()
    }

    override fun onDestroy() {
        rvFeed.onRelease()
        super.onDestroy()
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId


        when (id) {
            //to be implemented. Just shown
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
           R.id.tool_main_search -> {
                return true
            }
            R.id.tool_main_settings -> {
                return true
            }
            R.id.tool_main_share -> {
                return true
            }
            R.id.tool_main_rate -> {
                return true
            }
            R.id.tool_main_feedback -> {
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
