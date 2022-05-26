package co.kr.toit

import android.content.Intent
import android.icu.number.IntegerWidth
import android.net.MailTo.parse
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.Time
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kr.toit.databinding.ActivityMainBinding
import co.kr.toit.databinding.MainRecyclerRowBinding
import java.net.HttpCookie.parse
import java.text.DateFormat
import java.text.Format
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.Date.parse
import java.util.logging.Level.parse
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    lateinit var b : ActivityMainBinding

    val subject_list = ArrayList<String>()
    val idx_list = ArrayList<Int>()
    val date2_list = ArrayList<String>()
    val time2_list = ArrayList<String>()

    private var TimerTask : Timer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        title = "To Do List"

        //Recycler View 셋팅
        val main_recycler_adapter = MainRecylcerAdapter()
        b.memoRecycler.adapter = main_recycler_adapter
        b.memoRecycler.layoutManager = LinearLayoutManager(this)

        b.mainFb1.setOnClickListener {
            val memo_add_intent = Intent(this, ToitAddActivity::class.java)
            startActivity(memo_add_intent)
        }

        TimerTask = timer(period = 60000){

            runOnUiThread {
                b.memoRecycler.adapter?.notifyDataSetChanged()
            }
        }



    }

    override fun onResume() {
        super.onResume()

        // ArrayList를 비워준다 why? 안비워주면 다시 돌아올때마다 누적되기 때문
        subject_list.clear()
        idx_list.clear()
        date2_list.clear()
        time2_list.clear()

        //데이터베이스 오픈
        val helper = DBHelper(this)

        //쿼리문
        val sql = """
            select rec_subject, rec_idx, rec_date2, rec_time2
            from Recordtable
            order by rec_idx desc
        """.trimIndent()

        val c1 = helper.writableDatabase.rawQuery(sql, null)
        while (c1.moveToNext()) {
            // 컬럼 index를 가져온다
            val idx1 = c1.getColumnIndex("rec_subject")
            val idx2 = c1.getColumnIndex("rec_idx")
            val idx3 = c1.getColumnIndex("rec_date2")
            val idx4 = c1.getColumnIndex("rec_time2")

            //데이터를 가져온다
            val rec_subject = c1.getString(idx1)
            val rec_idx = c1.getInt(idx2)
            val rec_date2 = c1.getString(idx3)
            val rec_time2 = c1.getString(idx4)

            // 데이터를 담는다
            subject_list.add(rec_subject)
            idx_list.add(rec_idx)
            date2_list.add(rec_date2)
            time2_list.add(rec_time2)

            //RecyclerView에가 갱신하라고 함
            b.memoRecycler.adapter?.notifyDataSetChanged()
        }

    }


    // Recycler의 어댑터
    inner class MainRecylcerAdapter : RecyclerView.Adapter<MainRecylcerAdapter.ViewHolderClass>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val mainRecyclerBinding = MainRecyclerRowBinding.inflate(layoutInflater)
            val holder = ViewHolderClass(mainRecyclerBinding)

            // 생성되는 항목 View의 가로 세로 길이를 설정
            val layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            mainRecyclerBinding.root.layoutParams = layoutParams

            // 항목 View에 이벤트를 설정
            mainRecyclerBinding.root.setOnClickListener(holder)

            return holder
        }

        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

            holder.recSubject.text = subject_list[position]
            holder.mainTimer.text = ""
        }

        override fun getItemCount(): Int {
            return subject_list.size
        }


        // HolderClass
        inner class ViewHolderClass(mainRecyclerBinding: MainRecyclerRowBinding) : RecyclerView.ViewHolder(mainRecyclerBinding.root), View.OnClickListener{
            //view의 주소값을 담는다.
            val recSubject = mainRecyclerBinding.recSubject
            val mainIndiCater = mainRecyclerBinding.mainIndicator
            val mainTimer = mainRecyclerBinding.mainDeadline

            override fun onClick(p0: View?) {
                val rec_idx = idx_list[adapterPosition]

                val memoModifyActivity = Intent(baseContext, ToitModifyActivity::class.java)
                memoModifyActivity.putExtra("rec_idx", rec_idx)
                startActivity(memoModifyActivity)
            }


        }

    }

    override fun onStop() {
        super.onStop()
        TimerTask?.cancel()

    }

}