package uz.context.catapiapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.context.catapiapplication.databinding.ItemRowLayoutBinding
import uz.context.catapiapplication.databinding.ItemSearchLayoutBinding
import uz.context.catapiapplication.model.Breeds
import uz.context.catapiapplication.model.CatList

class SearchAdapter(private val context: Context, private var breedList: ArrayList<Breeds>) :
    RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    lateinit var onClick: ((Breeds) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemSearchLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = breedList[position]
        holder.apply {
            Glide.with(context)
                .load(list.image.url)
                .into(binding.imageView)

            binding.apply {
                textName.text = list.name
                textDes.text = list.description
                textOrigin.text = list.origin
                textViki.text = list.wikipedia_url

                constraintLayout.setOnClickListener {
                    onClick.invoke(list)
                }
            }
        }
    }

    override fun getItemCount(): Int = breedList.size
    inner class MyViewHolder(val binding: ItemSearchLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<Breeds>) {
        breedList = filteredList
        notifyDataSetChanged()
    }
}