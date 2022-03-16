package uz.context.catapiapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import uz.context.catapiapplication.R

class MainFragment : Fragment() {
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var floatingBtn: FloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        initViews(view)

        return view
    }

    private fun initViews(view: View) {
        replaceFragment(SearchFragment())

        floatingBtn = view.findViewById(R.id.floatingBtn)
        bottomNav = view.findViewById(R.id.bottomNav)

        floatingBtn.setOnClickListener {
            replaceFragment(UploadFragment())
        }
        bottomNav.background = null

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuSearch -> {
                    replaceFragment(SearchFragment())
                }
                R.id.menuList -> {
                    replaceFragment(ListFragment())
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            commit()
        }
    }
}