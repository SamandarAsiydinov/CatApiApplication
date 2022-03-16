package uz.context.catapiapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.context.catapiapplication.databinding.ItemRowLayoutBinding
import uz.context.catapiapplication.model.Breeds
import uz.context.catapiapplication.model.CatList

class MyAdapter(private val context: Context, private val breedList: ArrayList<CatList>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = breedList[position]
        holder.apply {
            Glide.with(context)
                .load(list.url)
                .into(binding.imageView)

            binding.apply {
                textName.text = list.id
                textSize.text = "${list.height} x ${list.width}"
            }
        }
    }

    override fun getItemCount(): Int = breedList.size
    inner class MyViewHolder(val binding: ItemRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}