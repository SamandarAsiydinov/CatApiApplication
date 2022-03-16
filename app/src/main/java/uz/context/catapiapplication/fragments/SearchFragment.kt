package uz.context.catapiapplication.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.context.catapiapplication.R
import uz.context.catapiapplication.adapter.MyAdapter
import uz.context.catapiapplication.adapter.SearchAdapter
import uz.context.catapiapplication.databinding.FragmentSearchBinding
import uz.context.catapiapplication.model.Breeds
import uz.context.catapiapplication.model.CatList
import uz.context.catapiapplication.networking.RetrofitHttp
import uz.context.catapiapplication.util.SaveImageUrl

class SearchFragment : Fragment() {

    private lateinit var _binding: FragmentSearchBinding
    private lateinit var myAdapter: MyAdapter
    private lateinit var searchAdapter: SearchAdapter
    private val lists = ArrayList<CatList>()
    private val searchLists = ArrayList<Breeds>()
    private lateinit var navController: NavController
    private val searchNewLists = ArrayList<Breeds>()
    private val binding get() = _binding
    var isRecycler = true

    private var count = 100

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initViews(view: View) {
        count++
        apiRequest()
        navController = Navigation.findNavController(view)
        myAdapter = MyAdapter(requireContext(), lists)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = myAdapter
            setHasFixedSize(true)
        }

        searchAdapter = SearchAdapter(requireContext(), searchNewLists)
        binding.searchRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
            setHasFixedSize(true)
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    count++
                    apiRequest()
                }
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            if (isRecycler) {
                lists.clear()
                myAdapter.notifyDataSetChanged()
                count++
                apiRequest()
            }
        }
        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isEmpty()) {
                    isRecycler = true
                    binding.recyclerView.isVisible = true
                    binding.searchRecycler.isVisible = false
                    searchLists.clear()
                    searchNewLists.clear()
                } else if (p0!!.length > 3) {
                    isRecycler = false
                    binding.recyclerView.isVisible = false
                    binding.searchRecycler.isVisible = true
                    searchBreeds(p0.toString().lowercase())
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        searchAdapter.onClick = {
            navController.navigate(R.id.action_mainFragment_to_resultFragment)
            SaveImageUrl.url = it.image.url
            SaveImageUrl.name = it.name
            SaveImageUrl.des = it.description
            SaveImageUrl.origin = it.origin
            SaveImageUrl.viki = it.wikipedia_url
        }
    }

    private fun apiRequest() {
        RetrofitHttp.postService.getAllList(50, count, "Desc")
            .enqueue(object : Callback<ArrayList<CatList>> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ArrayList<CatList>>,
                    response: Response<ArrayList<CatList>>
                ) {
                    if (response.isSuccessful) {
                        lists.addAll(response.body()!!)
                        myAdapter.notifyDataSetChanged()
                        Log.d("@@@", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<ArrayList<CatList>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    private fun searchBreeds(text: String) {
        RetrofitHttp.postService.getAllBreeds().enqueue(object : Callback<ArrayList<Breeds>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ArrayList<Breeds>>,
                response: Response<ArrayList<Breeds>>
            ) {
                if (response.isSuccessful) {
                    searchLists.addAll(response.body()!!)
                    val filteredList: ArrayList<Breeds> = ArrayList()
                    for (search in searchLists) {
                        if (search.name.lowercase().contains(text)) {
                            filteredList.add(search)
                            break
                        }
                    }
                    searchAdapter.filterList(filteredList)
                }
            }

            override fun onFailure(call: Call<ArrayList<Breeds>>, t: Throwable) {
                Log.d("@@@@", t.message.toString())
            }
        })
    }
}