package com.example.famstoryappkotlin.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.famstoryappkotlin.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private lateinit var passwordIconDrawable: Drawable

    private var isPasswordVisible: Boolean = false
    private var eyeIcon: Drawable? = null
    private var eyeOffIcon: Drawable? = null


    init {
        init()
//        setup()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        transformationMethod = PasswordTransformationMethod.getInstance()
    }

    private fun init() {
        passwordIconDrawable =
            ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_24) as Drawable
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        compoundDrawablePadding = 16

        setHint(R.string.password)
        setAutofillHints(AUTOFILL_HINT_PASSWORD)
        setDrawable(passwordIconDrawable)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Password validation
                // Display error automatically if the password doesn't meet certain criteria
                if (!s.isNullOrEmpty() && s.length < 8) {
                    error = context.getString(R.string.et_password_error_message)
                    setError(error, null)
                } else {
                    error = null
                }
            }
        })
    }

    private fun setDrawable(
        start: Drawable? = null,
        top: Drawable? = null,
        end: Drawable? = null,
        bottom: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }

    private fun setup() {
        eyeIcon = ContextCompat.getDrawable(context, R.drawable.ic_eye_show)
        eyeOffIcon = ContextCompat.getDrawable(context, R.drawable.ic_eye_hide)

        setEndIcon()
        setOnTouchListener { _, event ->
            if (event.rawX >= (right - compoundPaddingRight)) {
                togglePasswordVisibility()
                return@setOnTouchListener true
            }
            false
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        setEndIcon()
        inputType = if (isPasswordVisible) {
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        setSelection(text?.length ?: 0)
    }

    private fun setEndIcon() {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            null, null,
            if (isPasswordVisible) eyeIcon else eyeOffIcon, null
        )
    }
}