package com.example.putinder.content_screen.profile_screen.view

import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.putinder.R

class EditTextDialog : DialogFragment() {

    companion object {
        private const val EXTRA_TEXT = "text"
        fun newInstance(text: String? = null): EditTextDialog {
            val dialog = EditTextDialog()
            val args = Bundle().apply {
                putString(EXTRA_TEXT, text)
            }
            dialog.arguments = args
            return dialog
        }
    }
    lateinit var editText: EditText
    var onOk: (() -> Unit)? = null
    var onCancel: (() -> Unit)? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val text: String? = arguments?.getString(EXTRA_TEXT)

        val view = layoutInflater.inflate(R.layout.dialog_edit_text, null)
        editText = view.findViewById(R.id.name_edit_text)

        if (text != null) {
            editText.append(text)
        }
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Изменить имя")
            .setView(view)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                onOk?.invoke()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                onCancel?.invoke()
            }
        val dialog = builder.create()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        return dialog
    }

}