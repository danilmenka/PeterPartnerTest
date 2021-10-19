package com.expl.peterpartnertest.screen.cards

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.expl.peterpartnertest.R
import com.expl.peterpartnertest.databinding.FragmentCardsBinding
import com.expl.peterpartnertest.model.User
import com.expl.peterpartnertest.model.Users
import com.expl.peterpartnertest.utilits.APP_ACTIVITY
import com.expl.peterpartnertest.utilits.TYPE_MASTERCARD
import com.expl.peterpartnertest.utilits.TYPE_UNIONPAY
import com.expl.peterpartnertest.utilits.TYPE_VISA

class CardsFragment : Fragment() {
    private lateinit var toolbarTextView:TextView
    private lateinit var toolbarImageView: ImageView
    private var _binding: FragmentCardsBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: CardsAdapter
    private var userList = emptyList<User>()
    private var currentIndex = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardsBinding.inflate(inflater,container,false)
        toolbarTextView = APP_ACTIVITY.findViewById(R.id.toolbar_title)
        toolbarImageView = APP_ACTIVITY.findViewById(R.id.img_back_toolbar)
        toolbarTextView.text = getString(R.string.toolbar_title_cards)
        toolbarImageView.visibility = View.VISIBLE
        toolbarImageView.setOnClickListener {
            APP_ACTIVITY.onBackPressed()
        }

        try {
            val mUsers = arguments?.getSerializable("users") as Users
            userList = mUsers.users
            currentIndex = arguments?.getSerializable("currentIndex") as Int
        }
        catch (e:Exception){
        }
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    private fun initialization() {
        mRecyclerView = mBinding.cardsRecyclerView
        mAdapter = CardsAdapter{
            val bundle = Bundle()
            bundle.putSerializable("currentUser",it)
            APP_ACTIVITY.navController.navigate(R.id.action_cardsFragment_to_mainFragment,bundle)
        }
        mRecyclerView.adapter = mAdapter

        mAdapter.setList(userList,getImgResourceList(),currentIndex)

    }

    private fun getImgResourceList(): List<Int> {
        val imgResourceList = mutableListOf<Int>()
        userList.forEach {
            when(it.type){
                TYPE_MASTERCARD-> imgResourceList.add(R.drawable.ic_mastercard)
                TYPE_VISA -> imgResourceList.add(R.drawable.ic_visa)
                TYPE_UNIONPAY -> imgResourceList.add(R.drawable.ic_unionpay)
            }
        }
        return imgResourceList
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mRecyclerView.adapter = null
    }

}