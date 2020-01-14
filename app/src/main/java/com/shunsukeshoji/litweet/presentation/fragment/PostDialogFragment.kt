package com.shunsukeshoji.litweet.presentation.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.shunsukeshoji.litweet.R
import kotlinx.android.synthetic.main.fragment_dialog_post.view.*

class PostDialogFragment(
    private val searchIds: List<String>,
    private val submit: (String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val parentView =
            requireActivity().layoutInflater.inflate(R.layout.fragment_dialog_post, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(parentView)
            .create()

        parentView.buttonSubmit.setOnClickListener {
            val id = parentView.editText.text.toString()
            if (searchIds.contains(id)) {
                submit(id)
            } else {
                parentView.editTextField.error = "このアカウントは存在しません"
            }
        }

        parentView.buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }
}