package com.jasonoh.calendarviewfromgoogle.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.jasonoh.calendarviewfromgoogle.R
import com.jasonoh.calendarviewfromgoogle.models.AnimalData
import com.jasonoh.calendarviewfromgoogle.models.AnimalDataFromCity

class AnimalDataFromCityAdapter(var animalDatas: ArrayList<AnimalDataFromCity>, val context: Context): RecyclerView.Adapter<AnimalDataFromCityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnimalDataFromCityAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_animal_sido, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("TAG", "AnimalDataFromCityAdapter_onBindViewHolder: ${animalDatas[position].cityName}" )

        holder.animalCity.text = animalDatas[position].cityName
        holder.animalCode.text = animalDatas[position].cityCode
    }

    override fun getItemCount(): Int {
        return animalDatas.size
    }

    class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

        var animalCity: TextView = itemView.findViewById(R.id.tv_animal_sido_city)
        var animalCode: TextView = itemView.findViewById(R.id.tv_animal_sido_code)
    }
}