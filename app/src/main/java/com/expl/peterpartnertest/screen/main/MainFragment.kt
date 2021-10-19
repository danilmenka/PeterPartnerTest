package com.expl.peterpartnertest.screen.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.expl.peterpartnertest.R
import com.expl.peterpartnertest.databinding.FragmentMainBinding
import com.expl.peterpartnertest.model.CbrResponse
import com.expl.peterpartnertest.model.User
import com.expl.peterpartnertest.model.Users
import com.expl.peterpartnertest.network.Event
import com.expl.peterpartnertest.network.Status
import com.expl.peterpartnertest.utilits.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel by viewModels<MainFragmentViewModel>()
    private lateinit var toolbarTextView:TextView
    private lateinit var toolbarImageView: ImageView
    private lateinit var observerUsers: Observer<Event<Users>>
    private lateinit var observerCbr: Observer<Event<CbrResponse>>
    private lateinit var observerCurrentUser: Observer<User>
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: HistoryAdapter
    private var currentUserIndex = 0
    private var currentUserIndexArgument = ""
    private var currentCurrencySymbol = ""
    private var userList = emptyList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try { currentUserIndexArgument = arguments?.getSerializable("currentUser") as String }
        catch (e:Exception){Log.e("Main","Default user")}
        toolbarTextView = APP_ACTIVITY.findViewById(R.id.toolbar_title)
        toolbarImageView = APP_ACTIVITY.findViewById(R.id.img_back_toolbar)
        toolbarTextView.text = getString(R.string.toolbar_title_main)
        toolbarImageView.visibility = View.GONE

        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    private fun initialization() {
        mRecyclerView = mBinding.recyclerViewHistory
        mAdapter = HistoryAdapter()
        mRecyclerView.adapter = mAdapter
        observeUsers()
        observeCbr()
        observeCurrentUser()
        mViewModel.getUsers()
    }

    @SuppressLint("SetTextI18n")
    private fun observeCurrentUser() {
        observerCurrentUser = Observer {
            if (it!=null) {
                mBinding.mainCardNumber.text = it.card_number
                mBinding.mainPersonName.text = it.cardholder_name
                mBinding.mainBalanceText.text = "$ ${String.format("%.2f", it.balance)}"
                mBinding.mainBalanceCurrency.text = "$currentCurrencySymbol${it.balanceCurrency}"
                mBinding.mainTextValid.text = it.valid
                val symbolList = mutableListOf<String>()
                it.transaction_history.forEach { transaction ->
                    if (transaction.currentAmount > 0)
                        symbolList.add(currentCurrencySymbol) else
                        symbolList.add("- $currentCurrencySymbol")
                }
                mAdapter.setList(it.transaction_history, symbolList)
                when (it.type) {
                    TYPE_MASTERCARD -> mBinding.mainIconCard.setImageResource(R.drawable.ic_mastercard)
                    TYPE_VISA -> mBinding.mainIconCard.setImageResource(R.drawable.ic_visa)
                    TYPE_UNIONPAY -> mBinding.mainIconCard.setImageResource(R.drawable.ic_unionpay)
                }

                mBinding.mainCurtainCardInfo.visibility = View.GONE
                mBinding.mainCurtainHistory.visibility = View.GONE
            }
        }
        mViewModel.currentUserView.observe(this,observerCurrentUser)
    }

    private fun observeUsers() {
        observerUsers = Observer {
            when (it.status) {
                Status.LOADING -> loadResponse()
                Status.SUCCESS -> responseUsersSuccess(it.data)
                Status.ERROR -> processError(it.error)
            }
        }
        mViewModel.usersLiveData.observe(this, observerUsers)
    }

    private fun observeCbr() {
        observerCbr = Observer {
            when (it.status) {
                Status.LOADING -> loadResponse()
                Status.SUCCESS -> responseCbrSuccess(it.data)
                Status.ERROR -> processError(it.error)
            }
        }
        mViewModel.cbrResponseLiveData.observe(this, observerCbr)
    }

    private fun findIndexByNumber(number:String):Int{
        userList.forEachIndexed{index, user ->
            if (user.card_number == number)
                return index
        }
        return 0
    }

    private fun processError(error: String?) {
        error?.let { showToast(it) }
    }

    private fun loadResponse() {
        mBinding.mainCurtainCardInfo.visibility = View.VISIBLE
        mBinding.mainCurtainHistory.visibility = View.VISIBLE
    }

    private fun responseCbrSuccess(data: CbrResponse?) {
        if (data!=null){
             currentUserIndex = findIndexByNumber(currentUserIndexArgument)
             mViewModel.setCurrentUser(currentUserIndex)
             CurrencyChooser.choseCurrency(listOf(
                 CurrencySlot(mBinding.cardGbp,mBinding.gbpText,mBinding.gbpSymbol),
                 CurrencySlot(mBinding.cardEur,mBinding.eurText,mBinding.eurSymbol),
                 CurrencySlot(mBinding.cardRub,mBinding.rubText,mBinding.rubSymbol),
             )){txt,symbol ->
                 currentCurrencySymbol = symbol
                 mViewModel.chooseCurrency(txt)
             }
        } else
            showToast(getString(R.string.error_null_cbr_response))
    }

    private fun responseUsersSuccess(data: Users?) {
        if(data!=null){
            userList = data.users
            mViewModel.getCbr()
            mBinding.mainInfoCard.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable("users",data)
                bundle.putSerializable("currentIndex", currentUserIndex)
                APP_ACTIVITY.navController.navigate(R.id.action_mainFragment_to_cardsFragment,bundle)
            }
        } else showToast(getString(R.string.error_null_user_response))
    }
    
    override fun onDestroy() {
        super.onDestroy()
        mRecyclerView.adapter = null
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel.usersLiveData.removeObserver { observerUsers }
        mViewModel.cbrResponseLiveData.removeObserver { observerCbr }
    }
}