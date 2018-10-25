package com.ois.todo.countrylist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.info.work.ambedkarmission.R
import com.info.work.ambedkarmission.model.State

import com.ois.todo.delegate.CountrySelectListener


class StateAdapter(internal var mContext: Context?, private val stateList: List<State>) : RecyclerView.Adapter<StateAdapter.MyViewHolder>() {
    internal lateinit var countrySelectListener: CountrySelectListener

    fun onclickListener(countrySelectListener: CountrySelectListener) {
        this.countrySelectListener = countrySelectListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.dialog_select_country_code_raw, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val country = stateList[position]
        if (country != null) {
            holder.txtCountryName.text = country.state
           // holder.txtCountryCode.text = country.countryCodeStr
           // holder.imgCountryFlag.setImageResource(country.resId)
        }

    }

    override fun getItemCount(): Int {
        return stateList.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        //var imgCountryFlag: ImageView
        var txtCountryName: TextView
       // var txtCountryCode: TextView
        var llCountryRow: LinearLayout

        init {

            //imgCountryFlag = view.findViewById<View>(R.id.imgCountryFlag) as ImageView
            txtCountryName = view.findViewById<View>(R.id.txtCountryName) as TextView
            //txtCountryCode = view.findViewById<View>(R.id.txtCountryCode) as TextView
            llCountryRow = view.findViewById<View>(R.id.llCountryRow) as LinearLayout
            llCountryRow.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            when (v.id) {
                R.id.llCountryRow -> countrySelectListener.onCountryClick(adapterPosition)
            }
        }
    }
}