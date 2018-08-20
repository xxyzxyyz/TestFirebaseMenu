package com.satoshi.testfirebasemenu

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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.database.*

import java.util.ArrayList

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    internal var buttonDataList: MutableList<ButtonData> = ArrayList()

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

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val text :StringBuilder = StringBuilder("")
                //行単位Databaseオブジェクトを取得
                for (button in dataSnapshot.children) {
                    //行単位のViewオブジェクトを作成
                    val rowView : LinearLayout = LinearLayout(this)
                    //ImageViewを追加する
                        val V = this.getLayoutInflater().inflate(R.layout.xmlfile, null)
                        this.addView(V)



/* レイアウトから生成する方向で試すため、一旦凍結
                    //行単位のViewオブジェクトを作成
                    val rowView : LinearLayout = LinearLayout(this)
                    //ImageViewを追加する
                    val cell : ImageView = ImageView(this)
                    cell.layoutParams.width = 0
                    cell.layoutParams.height = wrap_content
                    cell.scaleType = fitXY
*/

                    //※↓の.javaはエラー表記されているが、無視して問題なし
                    //buttonDataList.add(button.getValue<ButtonData>(ButtonData::class.java))
                    //画像取得、ボタン設定 List<Map<String Object>>で列、行、ImageButtonを作る。
                    val data:ButtonData = button.getValue(ButtonData::class.java) as ButtonData
                    button
                    //val data : ButtonData = button.getValue<ButtonData>(ButtonData::class.java)
                    text.append(data?.ImageName + "：1→" + button.key)

                    //①Imagebuttonの追加。パラメータ設定
                    //②画像ダウンロード、Imagebuttonへの設定
                    //③onclickの設定(ボタンタイプによって)


                }
                Log.d("dev","for文終了")
                val textView: TextView = findViewById(R.id.textView)
                textView.setText(text)
                Log.d("dev", "データ更新成功")
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
