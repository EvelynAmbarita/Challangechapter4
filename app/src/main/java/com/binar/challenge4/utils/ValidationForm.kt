package com.binar.challenge4.utils

import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar

object ValidationForm {

    fun EditText.isValid(): Boolean{
        return if (this.text.trim().isEmpty()){
            this.callError("Wajib diisi")
            false
        }else{
            true
        }
    }

    fun EditText.isValidEmail(): Boolean{
        return if (Patterns.EMAIL_ADDRESS.matcher(this.text).matches()){
            true
        }else{
            this.callError("Email tidak sesuai format")
            false
        }
    }

    fun EditText.isValidPassword(): Boolean{
        return when {
            this.text.toString().isContainWhiteSpace() ->{
                this.callError("Format salah")
                false
            }
            this.text.length < 6 -> {
                this.callError("Format salah")
                false
            }
            else -> {
                true
            }
        }
    }

    fun eventTypeValid(spinner: Spinner, editText: EditText):Boolean{
        return if (spinner.selectedItemPosition==0){
            false
        }else !(spinner.selectedItemPosition==5 && editText.text.isEmpty())
    }

    fun Spinner.isValid(): Boolean{
        return if (this.selectedItemPosition==0){
            false
        }else{
            true
        }
    }

    fun EditText.isValidPhone(): Boolean{
        return if (Patterns.PHONE.matcher(this.text.toString()).matches()){
            true
        }else{
            this.callError("Format nomor salah")
            false
        }
    }

    private fun String.isContainWhiteSpace() = all { it.isWhitespace() }

    fun EditText.isValidRePassword(password:String):Boolean{
        return if (this.text.toString() == password){
            true
        }else{
            this.callError("Password tidak sama")
            false
        }
    }

    private fun EditText.callError(errorMsg: String){
        this.onChange {
            this.error = null
        }
        this.error = errorMsg
    }

    private fun EditText.onChange(change:(String)->Unit){
        this.addTextChangedListener {
            change(it.toString())
        }
    }


    private fun RadioGroup.callError(errorMsg: String){
        val radioButton = this.getChildAt(0) as RadioButton
        radioButton.error = errorMsg
        this.setOnCheckedChangeListener { radioGroup, i ->
            radioButton.error = null
        }
    }

    fun RadioGroup.isValidRadioGroup():Boolean{
        return if (this.checkedRadioButtonId == -1){
            this.callError("Harap dipilih")
            false
        }else{
            true
        }
    }

    fun Spinner.isValid(v: View):Boolean{
        return if (this.selectedItemPosition !=0){
            true
        }else{
            callError(v)
            false
        }
    }

    fun CheckBox.isValid():Boolean{
        return if(!this.isChecked){
            this.callError("Harap dipilih")
            false
        }else{
            true
        }
    }

    fun CheckBox.isValidOnly():Boolean{
        return this.isChecked
    }

    private fun CheckBox.callError(errorMsg: String){
        this.setOnCheckedChangeListener { compoundButton, b ->
            this.error = null
        }
        this.error = errorMsg
    }



    private fun callError(v: View){
        Snackbar.make(v, "Harap mengisi pilihan", Snackbar.LENGTH_SHORT).show()
    }

}