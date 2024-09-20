package com.example.first_app

import android.os.Bundle
import android.os.PersistableBundle
import android.renderscript.ScriptGroup.Binding
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), OnClickListener {
    private lateinit var edtWidth: EditText
    private lateinit var edtHeight: EditText
    private lateinit var edtLength: EditText
    private lateinit var btnCalculate: Button
    private lateinit var tvResult: TextView



    companion object {
        private const val STATE_RESULT = "state_result"
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        bindIDWithComponent()

        btnCalculate.setOnClickListener(this)

        if (savedInstanceState != null) {
            val savedResult = savedInstanceState.getString(STATE_RESULT)
            tvResult.text = savedResult
        }


        // Handling window insets for edge-to-edge experience
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_calculate) {
//            val inputLength = edtLength.text.toString().trim()
            val inputLength = findViewById<EditText>(R.id.edt_length).text.toString().trim()
            val inputWidth = edtWidth.text.toString().trim()
            val inputHeight = edtHeight.text.toString().trim()
             var isAnyEmptyField: Boolean = false

            if (inputLength.isEmpty()) {
                isAnyEmptyField = true
                edtLength.error = "Field ini tidak boleh kosong"
            }
            if (inputWidth.isEmpty()) {
                isAnyEmptyField = true
                edtWidth.error = "Field ini tidak boleh kosong"
            }
            if (inputHeight.isEmpty()) {
                isAnyEmptyField = true
                edtHeight.error = "Field ini tidak boleh kosong"
            }
            if (!isAnyEmptyField) {
                val volume = inputLength.toDouble() * inputWidth.toDouble() * inputHeight.toDouble()
                tvResult.text = volume.toString()
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_RESULT, tvResult.text.toString())
    }

    private fun bindIDWithComponent ():Unit{
        edtWidth = findViewById(R.id.edt_lebar)
        edtHeight = findViewById(R.id.edt_tinggi)
        edtLength = findViewById(R.id.edt_length)
        btnCalculate = findViewById(R.id.btn_calculate)
        tvResult = findViewById(R.id.tv_result)
    }
}
