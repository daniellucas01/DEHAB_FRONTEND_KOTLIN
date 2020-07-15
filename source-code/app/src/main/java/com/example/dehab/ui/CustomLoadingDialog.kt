package com.example.dehab.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import com.example.dehab.R
import com.example.dehab.databinding.LoadingDialogBinding
import com.ldoublem.loadingviewlib.view.LVBlock

class CustomLoadingDialog(myActivity: FragmentActivity) {

    private var activity : Activity = myActivity
    private lateinit var dialog : AlertDialog
    private lateinit var block : LVBlock

    public fun startLoadingDialog() {
        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater : LayoutInflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(true)
        dialog = builder.create()
        dialog.show()
        block = dialog.findViewById(R.id.LVBlock)
        block.startAnim()
    }

    public fun dismissDialog() {
        block.stopAnim()
        dialog.dismiss()
    }

}