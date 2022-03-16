package uz.context.catapiapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import uz.context.catapiapplication.R
import uz.context.catapiapplication.adapter.MyAdapter
import uz.context.catapiapplication.adapter.MyAdapterTwo
import uz.context.catapiapplication.database.CatDatabase
import uz.context.catapiapplication.databinding.FragmentListBinding
import uz.context.catapiapplication.model.CatList

class ListFragment : Fragment() {
    private lateinit var _binding: FragmentListBinding
    private lateinit var catDatabase: CatDatabase
    private lateinit var myAdapter: MyAdapterTwo
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(layoutInflater,container,false)
        initViews()
        return binding.root
    }
    private fun initViews() {
        catDatabase = CatDatabase.getInstance(requireContext())
        myAdapter = MyAdapterTwo(requireContext(),catDatabase.dao().getCats())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = myAdapter
        }
    }
}