package com.shunsukeshoji.litweet.presentation.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.shunsukeshoji.litweet.R
import com.shunsukeshoji.litweet.presentation.main.MainActivityViewModel
import com.shunsukeshoji.litweet.util.ErrorState
import kotlinx.android.synthetic.main.fragment_dialog_post.view.*

class PostDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "tag"
        fun show(fragmentManager: FragmentManager) {
            PostDialogFragment().show(fragmentManager, TAG)
        }
    }

    private val viewModel: MainActivityViewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val parentView =
            requireActivity().layoutInflater.inflate(R.layout.fragment_dialog_post, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(parentView)
            .create()

        parentView.buttonSubmit.setOnClickListener {
            parentView.editTextField.error = null
            val id = parentView.editText.text.toString()
            viewModel.requestTweets(id) { error ->
                when (error) {
                    ErrorState.NOT_INITIALIZED -> {
                        dialog.dismiss()
                    }
                    ErrorState.ACCOUNT_DOES_NOT_EXIST -> {
                        parentView.editTextField.error = error.message
                    }
                    ErrorState.ACCOUNT_ALREADY_SUBMITTED -> {
                        parentView.editTextField.error = error.message
                    }
                }
            }
        }

        parentView.buttonCancel.setOnClickListener {
            dialog.dismiss()
        }


        return dialog
    }
}