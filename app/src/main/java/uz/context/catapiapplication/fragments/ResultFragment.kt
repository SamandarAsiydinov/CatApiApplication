package uz.context.catapiapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import uz.context.catapiapplication.R
import uz.context.catapiapplication.databinding.FragmentResultBinding
import uz.context.catapiapplication.util.SaveImageUrl

class ResultFragment : Fragment() {

    private lateinit var _binding: FragmentResultBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.textName.text = SaveImageUrl.name
        binding.textDes.text = SaveImageUrl.des
        binding.textOrigin.text = SaveImageUrl.origin
        binding.textViki.text = SaveImageUrl.viki

        Glide.with(requireContext())
            .load(SaveImageUrl.url)
            .into(binding.imageView)
    }
}