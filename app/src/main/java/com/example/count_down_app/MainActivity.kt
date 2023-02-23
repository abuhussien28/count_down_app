package com.example.count_down_app

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.count_down_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var title_tv:TextView
    lateinit var timer_tv:TextView
    lateinit var rest_tv:TextView
    lateinit var btn:Button
    lateinit var pb:ProgressBar
    lateinit var toggle: ActionBarDrawerToggle
    var IsTimingRunning:Boolean=false
    val Start_Time_In_Millis:Long=25*60*1000
    var RemainingTime=Start_Time_In_Millis
    var timer:CountDownTimer?=null
lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        timer_tv=findViewById(R.id.time_tv)
        title_tv=findViewById(R.id.title_tv)
        btn=findViewById(R.id.start_btn)
        rest_tv=findViewById(R.id.rest_tv)
        pb=findViewById(R.id.progressBar)
        pb.progressDrawable=resources.getDrawable(R.drawable.progrees_bar_desgin)
        btn.setOnClickListener {

            if (!IsTimingRunning){
            Start_Time(Start_Time_In_Millis)
            }
        }
        view_navigation()
    }
    private fun Start_Time(Start_Time:Long){
         timer=object:CountDownTimer(Start_Time,1000){
            override fun onTick(time_left: Long) {
                RemainingTime=time_left
                update_time()
                title_tv.text=resources.getText(R.string.keep_going)

                pb.progress=RemainingTime.toDouble().div(Start_Time_In_Millis.toDouble()).times(100).toInt()
            }

            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Finish!!", Toast.LENGTH_SHORT).show()
                IsTimingRunning=false
            }
        }.start()
        IsTimingRunning=true
    }
    private fun update_time(){
        //for formating time not to be in milli sec
        val minute=RemainingTime.div(1000).div(60)
        val seconds=RemainingTime.div(1000)%60
        val formating_time=String.format("%02d:%02d",minute,seconds)
        timer_tv.text=formating_time

    }
    private fun Reset_Timer(){
        timer?.cancel()
        RemainingTime=Start_Time_In_Millis
        update_time()
        title_tv.text=resources.getText(R.string.take_pomodoro)
    IsTimingRunning=false
        pb.progress=100

    }
    private fun view_navigation(){
        binding.apply {
            toggle= ActionBarDrawerToggle(this@MainActivity,Drawer,R.string.open,R.string.close)
            Drawer.addDrawerListener(toggle)
            toggle.syncState()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            navView?.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.Timer_id -> startActivity(Intent(this@MainActivity,MainActivity::class.java))
                    R.id.Stopwatch -> startActivity(Intent(this@MainActivity,Stop_watch::class.java))

                }
                true
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("remaining time",RemainingTime)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
       val SavedTimer= savedInstanceState.getLong("remaining time")
        //عملت if علشان لو قلبت هيتعمل لوحده اي يعني هيشتغل الtimer فعمل if علشان يثبت
        if (SavedTimer!=Start_Time_In_Millis)
           Start_Time(SavedTimer)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            true
        return super.onOptionsItemSelected(item)
    }
    private fun closeDrawer(){
        binding.Drawer.closeDrawer(GravityCompat.START)}
    override fun onBackPressed() {
        if (binding.Drawer.isDrawerOpen(GravityCompat.START))
            closeDrawer()
        else
            super.onBackPressed()
    }
}