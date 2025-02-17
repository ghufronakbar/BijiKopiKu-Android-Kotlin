package com.example.bijikopiku.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bijikopiku.R
import com.example.bijikopiku.adapter.DetailHistoryAdapter
import com.example.bijikopiku.api.ApiInterface
import com.example.bijikopiku.api.RetrofitClient
import com.example.bijikopiku.databinding.ActivityDetailOrderBinding
import com.example.bijikopiku.helper.Helper
import com.example.bijikopiku.model.response.ApiRes
import com.example.bijikopiku.model.response.Order
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class DetailOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailOrderBinding
    private lateinit var retrofit: Retrofit
    private lateinit var apiInterface: ApiInterface

    private val CAMERA_REQUEST_CODE = 101
    private val GALLERY_REQUEST_CODE = 102
    private lateinit var selectedImageUri: Uri

    private var orderId = ""

    companion object {
        const val ID = "id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        supportActionBar?.hide()

        // Ambil ID dari intent
        val id = intent.getStringExtra(ID) ?: ""

        // Inisialisasi Retrofit
        retrofit = RetrofitClient.getClient(this)
        apiInterface = retrofit.create(ApiInterface::class.java)

        // Ambil data pesanan
        fetchData(id)

        // Cek izin kamera
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }

        // Tombol untuk membatalkan pesanan
        binding.btnCancel.setOnClickListener {
            handleCancel(id)
        }

        // Tombol untuk membayar pesanan
        binding.btnPay.setOnClickListener {
            handlePay(id)
        }

        // Penyesuaian padding untuk edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Fungsi untuk mengambil data pesanan
    private fun fetchData(id: String) {
        val call = apiInterface.getDetailOrder(id)
        call.enqueue(object : Callback<ApiRes<Order>> {
            override fun onResponse(call: Call<ApiRes<Order>>, response: Response<ApiRes<Order>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data?.success == true) {
                        val order = data.data

                        // Set data pesanan ke tampilan
                        binding.tvOrderId.text = "#${order.id}"
                        binding.tvTotal.text = "Rp ${order.total}"
                        binding.tvDate.text = Helper.formatDate(order.createdAt)
                        binding.tvStatus.text = order.status

                        // Atur warna berdasarkan status
                        binding.badgeStatus.setCardBackgroundColor(getColor(getStatusColor(order.status)))

                        // Tampilkan atau sembunyikan tombol berdasarkan status
                        setButtonVisibility(order.status)

                        // Tampilkan data item dalam RecyclerView
                        val adapter = DetailHistoryAdapter(order.orderItems)
                        binding.rvItemDetailOrder.layoutManager =
                            GridLayoutManager(this@DetailOrderActivity, 1)
                        binding.rvItemDetailOrder.adapter = adapter

                        // Sembunyikan progress bar dan tampilkan konten
                        binding.progressBar.visibility = View.GONE
                        binding.lnContent.visibility = View.VISIBLE

                        // Simpan orderId
                        orderId = order.id
                    } else {
                        Toast.makeText(this@DetailOrderActivity, data?.message, Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                } else {
                    Log.e("API Error", "Error code: ${response.code()}, message: ${response.message()}")
                    Log.e("API Error Body", response.errorBody()?.string() ?: "No error body")
                    Toast.makeText(this@DetailOrderActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }

            override fun onFailure(call: Call<ApiRes<Order>>, t: Throwable) {
                Log.e("API Failure", "Message: ${t.message}", t)
                Toast.makeText(this@DetailOrderActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    // Fungsi untuk menangani pembatalan pesanan
    private fun handleCancel(id: String) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Pembatalan")
        builder.setMessage("Apakah Anda yakin ingin membatalkan pesanan ini?")

        builder.setPositiveButton("Ya") { _, _ ->
            val call = apiInterface.cancelOrder(id)
            Toast.makeText(this, "Harap tunggu sebentar", Toast.LENGTH_SHORT).show()
            call.enqueue(object : Callback<ApiRes<Order>> {
                override fun onResponse(call: Call<ApiRes<Order>>, response: Response<ApiRes<Order>>) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data?.success == true) {
                            Toast.makeText(this@DetailOrderActivity, data.message, Toast.LENGTH_SHORT)
                                .show()
                            fetchData(id)
                        } else {
                            Toast.makeText(this@DetailOrderActivity, data?.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@DetailOrderActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<ApiRes<Order>>, t: Throwable) {
                    Toast.makeText(this@DetailOrderActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    // Fungsi untuk menangani pembayaran pesanan
    private fun handlePay(id: String) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Pembayaran")
        builder.setMessage("Upload bukti pembayaran Anda")

        builder.setPositiveButton("Kamera") { _, _ ->
            openCamera()
        }

        builder.setNeutralButton("Galeri") { _, _ ->
            openGallery()
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    if (bitmap != null) {
                        val file = bitmapToFile(bitmap, "bukti_pembayaran.jpg")
                        if (file != null) {
                            uploadPayment(file)
                        } else {
                            Toast.makeText(this, "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Gagal mengambil gambar dari kamera", Toast.LENGTH_SHORT).show()
                    }
                }
                GALLERY_REQUEST_CODE -> {
                    val uri = data?.data
                    if (uri != null) {
                        val file = uriToFile(uri)
                        if (file != null) {
                            uploadPayment(file)
                        } else {
                            Toast.makeText(this, "Gagal memproses gambar dari galeri", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun bitmapToFile(bitmap: Bitmap, fileName: String): File? {
        val file = File(cacheDir, fileName)
        try {
            val outputStream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            return file
        } catch (e: Exception) {
            Log.e("File Error", "Gagal menyimpan file: ${e.message}", e)
        }
        return null
    }

    private fun uriToFile(uri: Uri): File? {
        val file = File(cacheDir, "gallery_image.jpg")
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return file
        } catch (e: Exception) {
            Log.e("File Error", "Gagal menyimpan file dari URI: ${e.message}", e)
        }
        return null
    }

    private fun uploadPayment(file: File) {
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("picture", file.name, requestFile)

        val call = apiInterface.postPayment(orderId, body)
        call.enqueue(object : Callback<ApiRes<Order>> {
            override fun onResponse(call: Call<ApiRes<Order>>, response: Response<ApiRes<Order>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data?.success == true) {
                        Toast.makeText(this@DetailOrderActivity, data.message, Toast.LENGTH_SHORT).show()
                        fetchData(orderId)
                    } else {
                        Toast.makeText(this@DetailOrderActivity, data?.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("API Error", "Error code: ${response.code()}, message: ${response.message()}")
                    Log.e("API Error Body", response.errorBody()?.string() ?: "No error body")
                    Toast.makeText(this@DetailOrderActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiRes<Order>>, t: Throwable) {
                Toast.makeText(this@DetailOrderActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getStatusColor(status: String): Int {
        return when (status) {
            "Dipesan" -> R.color.gray
            "Dibayar" -> R.color.yellow
            "Dibatalkan" -> R.color.orange
            "Ditolak" -> R.color.red
            "Diterima" -> R.color.green
            "Selesai" -> R.color.primary
            else -> R.color.gray
        }
    }

    private fun setButtonVisibility(status: String) {
        when (status) {
            "Dipesan" -> {
                binding.btnCancel.visibility = View.VISIBLE
                binding.btnPay.visibility = View.VISIBLE
            }
            else -> {
                binding.btnCancel.visibility = View.GONE
                binding.btnPay.visibility = View.GONE
            }
        }
    }
}
