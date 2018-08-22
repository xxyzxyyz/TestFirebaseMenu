package com.satoshi.testfirebasemenu

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.appcompat.R.id.wrap_content
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.nio.file.Files

import java.util.ArrayList

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    internal var buttonDataList: MutableList<ButtonData> = ArrayList()
//    private var storageRef: StorageReference? = null
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        //データ更新
        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("/Button")
        //キー(行番号)ごとにオブジェクトを取得できるように変更
        val query : Query = myRef.orderByKey()

        //データに変更があった場合
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //アプリデータフォルダ内のファイルリストを取得
                var files  = context.filesDir.listFiles()

                //一覧のファイル画像削除
                for(file:File in files){
                    file.delete()
                }

                //firebase storageの参照を取得
                //storageRef = FirebaseStorage.getInstance().reference

                //メニューの格納先ViewGroupを取得
                val parent : LinearLayout = findViewById<LinearLayout>(R.id.parent)
                //一行ごとの処理
                for(row in dataSnapshot.children){
                    layoutInflater.inflate(R.layout.app_menu_row,parent)
                    val rowLayout : LinearLayout = parent.getChildAt(row.key.toInt()-1) as LinearLayout

                    //一列ごとの処理
                    for(column in row.children){
                        val button : ImageButton = rowLayout.getChildAt(column.key.toInt()-1) as ImageButton
                        val buttonData : ButtonData = column.getValue(ButtonData::class.java) as ButtonData

                        //画像データのダウンロード先指定
                        val file : File = File(context.filesDir,buttonData.ImageName)
                        Log.d("dev","ダウンロード先パス：　"+ file.path.toString())

                        //ファイル取得
                        FirebaseStorage.getInstance().reference.child("ButtonImage/" + buttonData.ImageName).getFile(file)
//取得成否による分岐                                .addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                                })

                        button.setImageURI(Uri.parse(context.filesDir.toString() + "/" + buttonData.ImageName))
                        //ボタンのタイプにより、処理を分岐させる
                        when (buttonData.type){
                            "dummy" ->Log.d("dev","ダミーだぜ")
                                //button.setOnClickListener(onClick).d("dev","ダミーだぜ")
                            "app" -> Log.d("dev", "アプリ起動だぜ")
                            "appurl" -> Log.d("dev", "アプリのURL起動だぜ")
                            else -> Log.d("dev", "何も設定されてないんだぜ")
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.d("dev", "エラー発生：", error.toException())
            }
        })


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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
