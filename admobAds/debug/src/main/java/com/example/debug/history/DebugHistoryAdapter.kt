package com.example.debug.history

import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.debug.databinding.DebugHistoryRowBinding
import com.example.debug.formatMillisToTime
import com.monetization.core.commons.NativeConstants.makeGone
import com.monetization.core.commons.NativeConstants.makeVisible
import com.monetization.core.models.AdmobAdInfo
import com.monetization.core.models.Failed
import com.monetization.core.models.Loaded
import com.monetization.core.models.getMillis

class DebugHistoryAdapter : ListAdapter<AdmobAdInfo, ViewHolder>(MyCallback) {

    inner class MyViewHolder(val binding: DebugHistoryRowBinding) : ViewHolder(binding.root) {
        fun bindItems(item: AdmobAdInfo) {
            binding.adKey.text = "${item.adKey}(Try:${item.requestCount})"
            binding.adRequestTime.text =
                "Requested At: ${item.adRequestTime.formatMillisToTime()}"
            binding.adLoading.makeVisible(item.adFinalTime == null)
            binding.resultViewBox.makeVisible(item.adFinalTime != null)
            binding.errorMessage.makeGone()
            item.adFinalTime?.let {
                val results = it.getMillis()
                binding.resultTime.text =
                    "Results in ${(results - item.adRequestTime) / 1000} seconds"
                if (it is Loaded) {
                    binding.errorMessage.text = "Ad Successfully Loaded"
                    binding.errorMessage.setTextColor(Color.GREEN)
                    binding.errorMessage.makeVisible()
                    binding.resultViewBox.setBackgroundColor(Color.GREEN)
                } else if (it is Failed) {
                    binding.errorMessage.setTextColor(Color.RED)
                    binding.errorMessage.makeVisible()
                    binding.errorMessage.text = "Error=${it.error},Code=${it.code}"
                    binding.resultViewBox.setBackgroundColor(Color.RED)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return MyViewHolder(
            DebugHistoryRowBinding.inflate(
                android.view.LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as? MyViewHolder)?.bindItems(getItem(position))
    }


    object MyCallback : DiffUtil.ItemCallback<AdmobAdInfo>() {

        override fun areItemsTheSame(oldItem: AdmobAdInfo, newItem: AdmobAdInfo): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: AdmobAdInfo, newItem: AdmobAdInfo): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
}