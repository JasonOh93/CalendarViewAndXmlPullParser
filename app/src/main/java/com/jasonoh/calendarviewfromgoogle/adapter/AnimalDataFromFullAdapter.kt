package com.jasonoh.calendarviewfromgoogle.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jasonoh.calendarviewfromgoogle.R
import com.jasonoh.calendarviewfromgoogle.models.AnimalData
import com.jasonoh.calendarviewfromgoogle.models.AnimalDataFromCity
import com.jasonoh.calendarviewfromgoogle.models.AnimalDataFull

class AnimalDataFromFullAdapter(var animalDatas: ArrayList<AnimalDataFull>, val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val SAMPLE_VIEW_TYPE: Int = 1
        private const val ANIMAL_FROM_FULL_VIEW_TYPE: Int = 2
    }
    var animalDatasCity = java.util.ArrayList<AnimalDataFromCity>()

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> SAMPLE_VIEW_TYPE
            else -> ANIMAL_FROM_FULL_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        animalDatasCity.add(AnimalDataFromCity("Busan"))

        return when(viewType){
            SAMPLE_VIEW_TYPE -> {
                SampleViewHolder(
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_animal_sido_inner, parent, false)
                )
            }
            else -> ViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.item_animal_full, parent, false)
            )
        }

//        return ViewHolder(
//            LayoutInflater.from(context).inflate(R.layout.item_animal_full, parent, false)
//        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is SampleViewHolder){

            holder.animalCity.text = animalDatasCity[position].cityName
            holder.animalCode.text = animalDatasCity[position].cityCode

        }else if(holder is ViewHolder){
            var animalDatasFromFull = animalDatas[position-1]
            holder.age.text = animalDatasFromFull.age
            holder.careAddr.text = animalDatasFromFull.careAddr
            holder.careNm.text = animalDatasFromFull.careNm
            holder.careTel.text = animalDatasFromFull.careTel
            holder.chargeNm.text = animalDatasFromFull.chargeNm

            holder.colorCd.text = animalDatasFromFull.colorCd
            holder.desertionNo.text = animalDatasFromFull.desertionNo
            holder.filename.text = animalDatasFromFull.filename
            holder.happenDt.text = animalDatasFromFull.happenDt
            holder.careAddr.text = animalDatasFromFull.careAddr
            holder.happenPlace.text = animalDatasFromFull.happenPlace
            holder.kindCd.text = animalDatasFromFull.kindCd
            holder.neuterYn.text = animalDatasFromFull.neuterYn
            holder.noticeEdt.text = animalDatasFromFull.noticeEdt
            holder.noticeNo.text = animalDatasFromFull.noticeNo
            holder.noticeSdt.text = animalDatasFromFull.noticeSdt
            holder.officetel.text = animalDatasFromFull.officetel
            holder.orgNm.text = animalDatasFromFull.orgNm
            Glide.with(context).load(animalDatasFromFull.popfile).into(holder.popfile)
            holder.processState.text = animalDatasFromFull.processState
            holder.sexCd.text = animalDatasFromFull.sexCd
            holder.specialMark.text = animalDatasFromFull.specialMark
            holder.weight.text = animalDatasFromFull.weight
        } else{
            Throwable("맞는 데이터가 없습니다.")
        }
    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
////        Log.e("TAG", "AnimalDataFromCityAdapter_onBindViewHolder: ${animalDatas[position].age}" )
//
//        holder.age.text = animalDatas[position].age
//        holder.careAddr.text = animalDatas[position].careAddr
//        holder.careNm.text = animalDatas[position].careNm
//        holder.careTel.text = animalDatas[position].careTel
//        holder.chargeNm.text = animalDatas[position].chargeNm
//
//        holder.colorCd.text = animalDatas[position].colorCd
//        holder.desertionNo.text = animalDatas[position].desertionNo
//        holder.filename.text = animalDatas[position].filename
//        holder.happenDt.text = animalDatas[position].happenDt
//        holder.careAddr.text = animalDatas[position].careAddr
//        holder.happenPlace.text = animalDatas[position].happenPlace
//        holder.kindCd.text = animalDatas[position].kindCd
//        holder.neuterYn.text = animalDatas[position].neuterYn
//        holder.noticeEdt.text = animalDatas[position].noticeEdt
//        holder.noticeNo.text = animalDatas[position].noticeNo
//        holder.noticeSdt.text = animalDatas[position].noticeSdt
//        holder.officetel.text = animalDatas[position].officetel
//        holder.orgNm.text = animalDatas[position].orgNm
//        Glide.with(context).load(animalDatas[position].popfile).into(holder.popfile)
//        holder.processState.text = animalDatas[position].processState
//        holder.sexCd.text = animalDatas[position].sexCd
//        holder.specialMark.text = animalDatas[position].specialMark
//        holder.weight.text = animalDatas[position].weight
//    }

    override fun getItemCount(): Int {
        return animalDatas.size+1
    }

    class SampleViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

        var animalCity: TextView = itemView.findViewById(R.id.tv_animal_sido_city)
        var animalCode: TextView = itemView.findViewById(R.id.tv_animal_sido_code)
    }

    class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

        var age: TextView = itemView.findViewById(R.id.tv_animal_full_age)
        var careAddr: TextView = itemView.findViewById(R.id.tv_animal_full_care_addr)
        var careNm: TextView = itemView.findViewById(R.id.tv_animal_full_care_nm)
        var careTel: TextView = itemView.findViewById(R.id.tv_animal_full_care_tel)
        var chargeNm: TextView = itemView.findViewById(R.id.tv_animal_full_charge_nm)
        var colorCd: TextView = itemView.findViewById(R.id.tv_animal_full_color_cd)
        var desertionNo: TextView = itemView.findViewById(R.id.tv_animal_full_desertion_no)
        var filename: TextView = itemView.findViewById(R.id.tv_animal_full_file_name)
        var happenDt: TextView = itemView.findViewById(R.id.tv_animal_full_happen_dt)
        var happenPlace: TextView = itemView.findViewById(R.id.tv_animal_full_happen_place)
        var kindCd: TextView = itemView.findViewById(R.id.tv_animal_full_kind_cd)
        var neuterYn: TextView = itemView.findViewById(R.id.tv_animal_full_neuter_yn)
        var noticeEdt: TextView = itemView.findViewById(R.id.tv_animal_full_notice_edt)
        var noticeNo: TextView = itemView.findViewById(R.id.tv_animal_full_notice_no)
        var noticeSdt: TextView = itemView.findViewById(R.id.tv_animal_full_notice_sdt)
        var officetel: TextView = itemView.findViewById(R.id.tv_animal_full_office_tel)
        var orgNm: TextView = itemView.findViewById(R.id.tv_animal_full_org_nm)
        var popfile: ImageView = itemView.findViewById(R.id.iv_animal_full_popfile)
        var processState: TextView = itemView.findViewById(R.id.tv_animal_full_process_state)
        var sexCd: TextView = itemView.findViewById(R.id.tv_animal_full_sex_Cd)
        var specialMark: TextView = itemView.findViewById(R.id.tv_animal_full_special_mark)
        var weight: TextView = itemView.findViewById(R.id.tv_animal_full_weight)
    }

}