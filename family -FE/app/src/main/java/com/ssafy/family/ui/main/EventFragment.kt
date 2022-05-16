package com.ssafy.family.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.data.remote.res.ScheduleInfo
import com.ssafy.family.data.remote.req.OpinionReactionReq
import com.ssafy.family.data.remote.res.Opinion
import com.ssafy.family.ui.Adapter.TodayScheduleAdapter
import com.ssafy.family.databinding.FragmentEventBinding
import com.ssafy.family.ui.Adapter.OpinionAdapter
import com.ssafy.family.ui.home.LoginViewModel
import com.ssafy.family.ui.roulette.RouletteActivity
import com.ssafy.family.ui.schedule.ScheduleActivity
import com.ssafy.family.ui.wishtree.WishTreeActivity
import com.ssafy.family.util.CalendarUtil
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class EventFragment : Fragment() {

    private lateinit var binding: FragmentEventBinding
    private val eventViewModel by activityViewModels<EventViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()

    private var eventsAdapter: TodayScheduleAdapter? = null
    private var opinionAdapter: OpinionAdapter? = null
    private var scheduleDayList = mutableListOf<ScheduleInfo>()
    private var opinionsList = mutableListOf<Opinion>()
    private val today = LocalDate.now()

    private val itemClickListener = object : OpinionAdapter.ItemClickListener {
        override fun deleteClick(opinion: Opinion) {
            eventViewModel.deleteOpinion(opinion.suggestionId)
        }
        override fun likeClick(opinion: Opinion) {
            eventViewModel.addOpinionReaction(OpinionReactionReq(opinion.suggestionId,true))
        }
        override fun unlikeClick(opinion: Opinion) {
            eventViewModel.addOpinionReaction(OpinionReactionReq(opinion.suggestionId,false))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        eventViewModel.getDaySchedule(CalendarUtil.dayLocalDateToString(today))
        eventViewModel.getOpinion()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

    }

    private fun initView() {
        eventsAdapter = TodayScheduleAdapter {
            val intent = Intent(requireContext(),ScheduleActivity::class.java)
            intent.putExtra("sID", it.scheduleId)
            startActivity(intent)
        }
        binding.todayScheduleRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = eventsAdapter
        }

        opinionAdapter = OpinionAdapter().apply {
            itemClickListener = this@EventFragment.itemClickListener
        }
        binding.opinionRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = opinionAdapter
        }

        binding.addScheduleButton.setOnClickListener {
            val intent = Intent(requireContext(), ScheduleActivity::class.java)
            startActivity(intent)
        }

        binding.addOpinionButton.setOnClickListener {
            eventViewModel.addOpinion(binding.opinionText.text.toString())
        }

        eventViewModel.getDayRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    if(!it.data!!.schedules.isNullOrEmpty()){
                        scheduleDayList = it.data.schedules.toMutableList()
                        updateScheduleAdapter()
                    }else{
                        scheduleDayList = mutableListOf()
                        updateScheduleAdapter()
                    }
                    dismissScheduleLoading()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message!!, Toast.LENGTH_SHORT).show()
                    dismissScheduleLoading()
                }
                Status.LOADING -> {
                    setScheduleLoading()
                }
                Status.EXPIRED -> {
                    dismissScheduleLoading()
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        eventViewModel.getOpinionRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    if(!it.data!!.opinions.isNullOrEmpty()){
                        opinionsList = it.data.opinions.toMutableList()
                        updateOpinionAdapter()
                    }else{
                        opinionsList = mutableListOf()
                        updateOpinionAdapter()
                    }
                    dismissOpinionLoading()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message!!, Toast.LENGTH_SHORT).show()
                    dismissOpinionLoading()
                }
                Status.LOADING -> {
                    setOpinionLoading()
                }
                Status.EXPIRED -> {
                    dismissOpinionLoading()
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        eventViewModel.addOpinionReactionRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    if(!it.data!!.opinions.isNullOrEmpty()){
                        opinionsList = it.data.opinions.toMutableList()
                        updateOpinionAdapter()
                    }else{
                        opinionsList = mutableListOf()
                        updateOpinionAdapter()
                    }
                    dismissOpinionLoading()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message!!, Toast.LENGTH_SHORT).show()
                    dismissOpinionLoading()
                }
                Status.LOADING -> {
                    setOpinionLoading()
                }
                Status.EXPIRED -> {
                    dismissOpinionLoading()
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        eventViewModel.addOpinionRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    dismissOpinionLoading()
                    eventViewModel.getOpinion()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message!!, Toast.LENGTH_SHORT).show()
                    dismissOpinionLoading()
                }
                Status.LOADING -> {
                    setOpinionLoading()
                }
                Status.EXPIRED -> {
                    dismissOpinionLoading()
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        eventViewModel.deleteOpinionRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    dismissOpinionLoading()
                    eventViewModel.getOpinion()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message!!, Toast.LENGTH_SHORT).show()
                    dismissOpinionLoading()
                }
                Status.LOADING -> {
                    setOpinionLoading()
                }
                Status.EXPIRED -> {
                    dismissOpinionLoading()
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.rouletteIcon.setOnClickListener {
            val intent = Intent(requireContext(), RouletteActivity::class.java)
            startActivity(intent)
        }

        binding.wishtreeIcon.setOnClickListener {
            val intent = Intent(requireContext(), WishTreeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateScheduleAdapter() {
        eventsAdapter?.apply {
            scheduleList.clear()
            scheduleList.addAll(scheduleDayList)
            notifyDataSetChanged()
        }
    }

    private fun updateOpinionAdapter() {
        opinionAdapter?.apply {
            if(opinionsList.size >= 3){
                binding.addOpinionView.visibility = GONE
            }else{
                binding.addOpinionView.visibility = VISIBLE
            }
            binding.opinionText.text = null
            opinionList.clear()
            opinionList.addAll(opinionsList)
            notifyDataSetChanged()
        }
    }

    private fun setScheduleLoading() {
        binding.progressBarDayLoading.visibility = VISIBLE
    }
    private fun dismissScheduleLoading() {
        binding.progressBarDayLoading.visibility = GONE
    }

    private fun setOpinionLoading() {
        binding.progressBarOpinionLoading.visibility = VISIBLE
    }
    private fun dismissOpinionLoading() {
        binding.progressBarOpinionLoading.visibility = GONE
    }
}