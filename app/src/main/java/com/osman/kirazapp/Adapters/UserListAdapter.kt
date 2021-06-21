package com.osman.kirazapp.Adapters

import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.osman.kirazapp.Models.Kullanicilar
import com.osman.kirazapp.R


class UserListAdapter(private val userList:ArrayList<Kullanicilar>) : RecyclerView.Adapter<UserListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.tek_satir_user_list_item,parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=userList[position]
        holder.email.text=currentItem.email
        holder.adiSoyadi.text=currentItem.name_lastname
        holder.cinsiyet.text=currentItem.gender
        holder.medeniHal.text=currentItem.merital_status
        holder.yas.text=currentItem.age





    }

    override fun getItemCount(): Int {

        return userList.size
    }
    class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){

        var email :TextView=itemView.findViewById(R.id.tvEmailUserList)
        var adiSoyadi :TextView=itemView.findViewById(R.id.tvAdSoyadUserList)
        var yas :TextView=itemView.findViewById(R.id.tvYasUserList)
        var cinsiyet :TextView=itemView.findViewById(R.id.tvCinsiyetUserList)
        var medeniHal :TextView=itemView.findViewById(R.id.tvMedeniHalUserList)
        //var btnDuzenle:Button=itemView.findViewById(R.id.btnDuzenle)



    }


}