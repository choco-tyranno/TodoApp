package com.choco_tyranno.todoapp.ui.univ

import android.content.Context
import android.widget.Toast

class ToastManager {
    companion object {
        private var toast: Toast? = null
        fun toast(context: Context, msg: String) {
            val hotToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
            if (toast != null) {
                toast?.cancel()
            }
            toast = hotToast
            toast?.show()
        }
    }
}