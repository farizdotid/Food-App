package org.codejudge.application.view.adapter;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.codejudge.application.R
import org.codejudge.application.databinding.ItemNearbyPlaceBinding
import org.codejudge.application.model.ui.NearbyPlace
import org.codejudge.application.utils.generatePhotoPlace
import org.codejudge.application.utils.loadUrlCenterCropRadius

class NearbyPlaceAdapter constructor(private val list: ArrayList<NearbyPlace>) :
    RecyclerView.Adapter<NearbyPlaceAdapter.NearbyPlaceAdapterHolder>() {

    private var onItemClickCallback: NearbyPlaceAdapterCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbyPlaceAdapterHolder {
        val binding = ItemNearbyPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return NearbyPlaceAdapterHolder(binding)
    }

    override fun onBindViewHolder(holder: NearbyPlaceAdapterHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setOnItemClickCallback(adapterCallback: NearbyPlaceAdapterCallback) {
        this.onItemClickCallback = adapterCallback
    }

    fun setData(list: List<NearbyPlace>) {
        this.list.apply {
            clear()
            addAll(list)
            notifyDataSetChanged()
        }
    }

    inner class NearbyPlaceAdapterHolder(val binding: ItemNearbyPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: NearbyPlace) {
            val image = item.image
            binding.ivImage.loadUrlCenterCropRadius(image.generatePhotoPlace(), 8)

            val storeName = item.storeName
            binding.tvName.text = storeName

            val isOpen = item.isOpen
            if (isOpen){
                binding.tvStatusOpen.text = binding.tvStatusOpen.context.getString(R.string.open)
                binding.tvStatusOpen.setBackgroundResource(R.drawable.bg_rounded_green)
            } else {
                binding.tvStatusOpen.text = binding.tvStatusOpen.context.getString(R.string.close)
                binding.tvStatusOpen.setBackgroundResource(R.drawable.bg_rounded_red)
            }

            val storeAddress = item.storeAddress
            binding.tvAddress.text = storeAddress

            val rating = item.rating
            binding.tvRating.text = rating.toString()
        }
    }

    interface NearbyPlaceAdapterCallback {
        fun onNearbyPlaceAdapterClicked()
    }
}