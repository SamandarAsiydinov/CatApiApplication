package uz.context.catapiapplication.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.context.catapiapplication.R
import uz.context.catapiapplication.adapter.MyAdapter
import uz.context.catapiapplication.database.CatDatabase
import uz.context.catapiapplication.database.CatEntity
import uz.context.catapiapplication.databinding.FragmentUploadBinding
import uz.context.catapiapplication.model.CatList
import uz.context.catapiapplication.networking.ApiClient
import uz.context.catapiapplication.networking.ApiService
import uz.context.catapiapplication.networking.RetrofitHttp
import java.io.File
import java.io.FileOutputStream

class UploadFragment : Fragment(R.layout.fragment_upload) {

    lateinit var ivPhoto: ImageView
    lateinit var btnUpload: MaterialButton
    private lateinit var catDatabase: CatDatabase

    lateinit var fileUri: Uri
    lateinit var file: File

    private val PICK_IMAGE = 1001
    lateinit var service: ApiService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        service = ApiClient(requireContext()).createService(ApiService::class.java)
        initViews(view)
    }

    private fun initViews(view: View) {
        catDatabase = CatDatabase.getInstance(requireContext())
        ivPhoto = view.findViewById(R.id.imageView)
        btnUpload = view.findViewById(R.id.btnUpload)

        ivPhoto.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    listOf(Manifest.permission.READ_EXTERNAL_STORAGE).toTypedArray(),
                    2000
                )
            } else {
                startGallery()
            }
        }

        btnUpload.setOnClickListener {
            val reqFile: RequestBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())
            val body: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file.name, reqFile)

            service.uploadFile(body, "sub_idjjhdbfdsaid").enqueue(object : Callback<CatList> {
                override fun onResponse(call: Call<CatList>, response: Response<CatList>) {
                    if (response.isSuccessful) {
                        val catEntity = CatEntity(response.body()!!.id, response.body()!!.url)
                        catDatabase.dao().saveCat(catEntity)
                        Toast.makeText(requireContext(), "Uploaded!", Toast.LENGTH_SHORT).show()
                        btnUpload.text = getString(R.string.uploaded)
                    }
                }

                override fun onFailure(call: Call<CatList>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("TAG", "onFailure: $t")
                }
            })
        }
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            fileUri = data?.data!!

            val ins = requireActivity().contentResolver.openInputStream(fileUri)
            file = File.createTempFile(
                "file",
                ".jpg",
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            )

            val fileOutputStream = FileOutputStream(file)

            ins?.copyTo(fileOutputStream)
            ins?.close()
            fileOutputStream.close()

            if (file.length() != 0L) {
                Glide.with(requireContext())
                    .load(file)
                    .into(ivPhoto)
            }
            btnUpload.isVisible = true
        }
    }
}