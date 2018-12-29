package com.info.work.ambedkarmission

import android.app.ProgressDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.ois.todo.activity.BaseActivity
import android.widget.AdapterView.OnItemSelectedListener
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.info.work.ambedkarmission.databinding.UploadMediaActivityBinding
import com.info.work.ambedkarmission.model.UploadMedia
import java.text.SimpleDateFormat
import java.util.*


class UploadMediaActivity : BaseActivity(), View.OnClickListener {


    var mView: UploadMediaActivityBinding? = null
    val PDF: Int = 0
    val DOCX: Int = 1
    val AUDIO: Int = 2
    val VIDEO: Int = 3
    val IMAGE: Int = 4
    val REQUEST_IMAGE_CAPTURE = 5
     var uri: Uri?=null
    lateinit var mStorage: StorageReference
    var mediaPositioion: Int = -1
    var mFirebaseInstance: FirebaseDatabase? = null
    var mFirebaseDatabase: DatabaseReference? = null
    var userId: String? = ""
   // var userName: String? = ""
    var cal = Calendar.getInstance()
    var requestCodeStr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = DataBindingUtil.setContentView(this, R.layout.upload_media_activity)

       // userName = sharedPref!!.getString(getString(R.string.loginAuthKey), "")

        firebaseSetUp()
        initView()
    }


    private fun initView() {

        mView!!.chooseMediaFile.setOnClickListener(this)
        mView!!.submitBt.setOnClickListener(this)
        mView?.imgBack?.setOnClickListener(this)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.media_type))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mView!!.chooseMedia.adapter = adapter
        mView!!.chooseMedia.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
                Log.d("possition", "" + position + arg0.selectedView)
                Log.d("view", "" + arg0.selectedView)

                //text type
                if (position == 0) {
                    mView!!.uploadDocLayout.visibility = View.GONE
                    mView!!.txtMedia.visibility = View.VISIBLE

                } else {
                    mView!!.uploadDocLayout.visibility = View.VISIBLE
                    mView!!.txtMedia.visibility = View.GONE
                }

                mediaPositioion = position
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {}
        }

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.chooseMediaFile -> {
                val intent = Intent()
               /* if (mediaPositioion == 1) {

                    dispatchTakePictureIntent()
                }*/
                if (mediaPositioion == 1) {
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE)
                }
               else if (mediaPositioion == 3) {
                    intent.type = "audio/*"
                    intent.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(Intent.createChooser(intent, "Select Audio"), AUDIO)
                } else if (mediaPositioion == 2) {
                    intent.type = "video/*"
                    intent.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(Intent.createChooser(intent, "Select Video"), VIDEO)
                }

            }
            R.id.submitBt -> {
                var userName = mView!!.userName.text.toString()
                if (mediaPositioion == 0) {

                    if(userName==""){
                        Toast.makeText(this, getString(R.string.username_require), Toast.LENGTH_LONG).show()
                        return
                    }

                    var txtMsg = mView!!.txtMedia.text.toString()
                    if (txtMsg != "") {
                        var insertInDb = UploadMedia(getString(R.string.TEXT), txtMsg, userName!!, Utils.getCurrentDateTime())
                        mFirebaseDatabase!!.push().setValue(insertInDb).addOnCompleteListener {
                            mView!!.txtMedia.setText("")
                            Toast.makeText(this, "Successfully Uploaded ", Toast.LENGTH_LONG).show()

                        }

                    } else
                        Toast.makeText(this, getString(R.string.message_require), Toast.LENGTH_LONG).show()
                }
                if (mediaPositioion > 0) {
                    if(uri!=null)
                    upload(requestCodeStr,userName)
                    else
                        Toast.makeText(this, getString(R.string.message_require), Toast.LENGTH_LONG).show()
                }

            }
            R.id.imgBack ->finish()


        }
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == RESULT_OK) {

            uri = data!!.data
            if(uri!=null)
            mView!!.uriTxt.text = uri.toString()

            if (requestCode == PDF) {

                requestCodeStr = getString(R.string.pdf)

            } else if (requestCode == DOCX) {
                requestCodeStr = getString(R.string.DOCX)
            } else if (requestCode == AUDIO) {

                requestCodeStr = getString(R.string.AUDIO)
            } else if (requestCode == VIDEO) {
                requestCodeStr = getString(R.string.VIDEO)
            } else if (requestCode == IMAGE) {
                requestCodeStr = getString(R.string.IMAGE)
            } /*else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                requestCodeStr = getString(R.string.CAPTUREIMAGE)

            }*/
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun upload(type: String,userName: String) {


        var currentDateandTime = Utils.getCurrentDateTime()

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading")
        progressDialog.show()

        var filepath = cal.timeInMillis.toString() + "" + uri?.path
        val ref = mStorage.child(filepath)
        var uploadTask = ref.putFile(uri!!)


        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
            progressDialog.setMessage("Uploaded " + progress.toInt() + "%...")

        }.addOnPausedListener {
            System.out.println("Upload is paused")
        }.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            // Handle successful uploads on complete
            // ...
        }


        val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                progressDialog.dismiss()

                mView!!.uriTxt.text = getString(R.string.file_name)
                requestCodeStr=""
                uri=null
                Toast.makeText(this, "Successfully Uploaded ", Toast.LENGTH_LONG).show()
                Log.d("upload url", downloadUri.toString())

                var insertInDb = UploadMedia(type, filepath, userName!!, currentDateandTime)
                mFirebaseDatabase!!.push().setValue(insertInDb)
            } else {
                // Handle failures
                // ...
            }
        }


    }

    private fun firebaseSetUp() {

        var storage = FirebaseStorage.getInstance()
        mStorage = storage.reference

        mFirebaseInstance = FirebaseDatabase.getInstance()

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance!!.getReference("uploadedItems")

        // store app title to 'app_title' node
        mFirebaseInstance!!.getReference("app_title").setValue("Missan")
        userId = mFirebaseDatabase!!.push().key


    }

}



