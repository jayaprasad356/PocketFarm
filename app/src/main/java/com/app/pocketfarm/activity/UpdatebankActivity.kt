package com.app.pocketfarm.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.pocketfarm.databinding.ActivityUpdatebankBinding
import com.app.pocketfarm.helper.Session

class UpdatebankActivity : AppCompatActivity() {

    lateinit var binding: ActivityUpdatebankBinding
    lateinit var activity: Activity
    lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatebankBinding.inflate(layoutInflater)
        activity = this
        session = Session(activity)

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnBankDetails.setOnClickListener {
            if (binding.etHolderName.text.toString().isEmpty()) {
                binding.etHolderName.error = "Please enter account holder name"
                binding.etHolderName.requestFocus()
            }
            else if (binding.etAccountNumber.text.toString().isEmpty()) {
                binding.etAccountNumber.error = "Please enter account number"
                binding.etAccountNumber.requestFocus()
            } else if (binding.etIFSCCode.text.toString().isEmpty()) {
                binding.etIFSCCode.error = "Please enter IFSC code"
                binding.etIFSCCode.requestFocus()
            }
            else if (binding.etBankName.text.toString().isEmpty()) {
                binding.etBankName.error = "Please enter bank name"
                binding.etBankName.requestFocus()
            }
            else if (binding.etBranchName.text.toString().isEmpty()) {
                binding.etBranchName.error = "Please enter branch name"
                binding.etBranchName.requestFocus()
            }

            else {
                //                updateBankDetails()
            }
        }
        setContentView(binding.root)

    }
}