package com.choco_tyranno.todoapp.ui.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.choco_tyranno.todoapp.R

/*
* This is irrevocable dialog fragment.
* And this dialog act like auto closing dialog after 3 seconds.
* If lifecycle onPause called while this main job is running, job removed.
* And after lifecycle back to onResume, job is restarted.
* */
class SignUpSuccessDialogFragment : BottomSheetDialogFragment() {
    private lateinit var navigateUpAction: Runnable
    private lateinit var handler:Handler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signupsuccessdialog, container, false)
    }

    init {
        isCancelable = false
    }

    private fun startCountDownToNavigateUp(){
        val rootActivity = requireActivity() as LoginActivity
        rootActivity.blockBackPress()
        handler.postDelayed(navigateUpAction,3000)
    }
    private fun stopCountDownToNavigateUp(){
        val rootActivity = requireActivity() as LoginActivity
        rootActivity.unblockBackPress()
        handler.removeCallbacks(navigateUpAction)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler(Looper.getMainLooper())
        val rootActivity = requireActivity() as LoginActivity
        navigateUpAction = Runnable {
            findNavController().navigateUp()
            rootActivity.unblockBackPress()
        }
    }

    override fun onResume() {
        super.onResume()
        startCountDownToNavigateUp()
    }

    override fun onPause() {
        super.onPause()
        stopCountDownToNavigateUp()
    }
}