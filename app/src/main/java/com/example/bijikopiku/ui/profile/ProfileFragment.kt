package com.example.bijikopiku.ui.profile

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bijikopiku.api.ApiInterface
import com.example.bijikopiku.api.RetrofitClient
import com.example.bijikopiku.databinding.FragmentProfileBinding
import com.example.bijikopiku.model.response.ApiRes
import com.example.bijikopiku.model.response.User
import com.example.bijikopiku.ui.EditProfileActivity
import com.example.bijikopiku.ui.LoginActivity
import com.example.bijikopiku.ui.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    private lateinit var retrofit: Retrofit
    private lateinit var apiInterface: ApiInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnGoEditProfile.setOnClickListener {
            val intent = Intent()
            intent.setClass(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnGoLogin.setOnClickListener {
            val intent = Intent()
            intent.setClass(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnGoRegister.setOnClickListener {
            val intent = Intent()
            intent.setClass(requireContext(), RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnGoLogout.setOnClickListener {
            handleLogout()
            val intent = Intent(requireContext(), requireActivity().javaClass)
            startActivity(intent)
            requireActivity().finish()
        }


        retrofit = RetrofitClient.getClient(requireContext())
        apiInterface = retrofit.create(ApiInterface::class.java)

        checkAccessToken()


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkAccessToken() {
        val preferences: SharedPreferences =
            requireContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val token = preferences.getString("ACCESS_TOKEN", null)
        if (token == null) {
            binding.boxProfileEmpty.visibility = View.VISIBLE
            binding.boxProfile.visibility = View.GONE
        } else {
            binding.boxProfile.visibility = View.VISIBLE
            binding.boxProfileEmpty.visibility = View.GONE
            fetchData()
        }
    }

    private fun fetchData() {
        val call = apiInterface.getProfile()
        call.enqueue(object : Callback<ApiRes<User>> {
            override fun onResponse(
                call: Call<ApiRes<User>>,
                response: Response<ApiRes<User>>
            ) {
                if (isAdded && view != null) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data?.success == true) {

                            binding.tvProfileEmail.text = data.data.email
                            binding.tvProfileName.text = data.data.name
                            binding.tvProfilePhone.text = "+62 ${data.data.phone}"

                            binding.boxProfile.visibility = View.VISIBLE
                        } else {
                            Toast.makeText(requireContext(), data?.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Log.e("Error", response.errorBody().toString())
                        Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<ApiRes<User>>, t: Throwable) {
                if (isAdded && view != null) {
                    Log.e("Error", t.message.toString())
                    Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun handleLogout() {
        val sharedPref = requireActivity().getSharedPreferences("my_preferences", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()

        RetrofitClient.retrofit = null
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        Toast.makeText(requireContext(), "Berhasil logout", Toast.LENGTH_SHORT).show()
        activity?.finish()
    }
}