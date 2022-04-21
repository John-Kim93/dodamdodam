package com.ssafy.family

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.databinding.ItemFamilyStatusBinding

// mainactivity - familyfragment : 우리가족(상태)
class StatusAdapter(private val context: Context) : RecyclerView.Adapter<StatusAdapter.ViewHolder>() {

    private var datas = mutableListOf<Int>(1,2,3)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFamilyStatusBinding.inflate(inflater, parent, false)
            return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(datas[position])
//        val currentBook = getItemId(position)

        val itemBinding = holder.binding as ItemFamilyStatusBinding
        itemBinding.familyMessageButton.setOnClickListener {
            if(itemBinding.alarmRecycler.visibility == VISIBLE){
                Log.d("log닷", "onBindViewHolder: 1")
                itemBinding.alarmRecycler.visibility = GONE
            }else{
                Log.d("log닷", "onBindViewHolder: 2")

                itemBinding.alarmRecycler.visibility = VISIBLE
                val adapter = AlarmAdapter(context)
                val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
                itemBinding.alarmRecycler.layoutManager = gridLayoutManager
                itemBinding.alarmRecycler.adapter = adapter
            }
        }

        itemBinding.executePendingBindings()
    }

    inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {}
}