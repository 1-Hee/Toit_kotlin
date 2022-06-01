package co.kr.toit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

internal class SecondAdapter (
    private val context : Context,
    private val DayNameArray : Array<String>,
    private val CountDataArray : IntArray
) : BaseAdapter(){

    private var layoutInflater: LayoutInflater? = null

    override fun getCount(): Int {
        return DayNameArray.size
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        var convertView = p1
        if(layoutInflater==null){
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if(convertView==null){
            convertView = layoutInflater!!.inflate(R.layout.weekly_item, null)
        }
        val dayName = convertView!!.findViewById<TextView>(R.id.day_name_text1)
        val countNumber = convertView!!.findViewById<TextView>(R.id.day_count)
        dayName.text = DayNameArray[p0]
        countNumber.text = "오늘의 일정:${CountDataArray[p0]}"

        return convertView


    }

}