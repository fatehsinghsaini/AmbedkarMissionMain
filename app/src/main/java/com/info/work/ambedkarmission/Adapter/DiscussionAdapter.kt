package com.info.work.ambedkarmission.Adapter

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.info.work.ambedkarmission.R
import com.info.work.ambedkarmission.Registration
import com.info.work.ambedkarmission.databinding.DiscussionAdapterBinding
import com.info.work.ambedkarmission.model.UploadMedia
import com.info.work.ambedkarmission.VideoPlayActivity
import com.info.work.ambedkarmission.model.LikeCount
import com.squareup.picasso.Picasso
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast


class DiscussionAdapter(internal var mContext: Context, var imageList: ArrayList<UploadMedia>?, var storageRef: FirebaseStorage, var fDb: FirebaseDatabase) : RecyclerView.Adapter<DiscussionAdapter.SingleViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleViewHolder {
        val view = DataBindingUtil.inflate<DiscussionAdapterBinding>(LayoutInflater.from(parent.context), R.layout.discussion_adapter, parent, false)
        return SingleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SingleViewHolder, position: Int) {

        var model = imageList?.get(position)


        holder.mView.name.text = model?.userName
        holder.mView.dateTime.text = model?.date
        holder.mView.fileTypeIcon.visibility = View.VISIBLE
        holder.mView.textLayoutView.visibility = View.VISIBLE
        holder.mView.imageLayout.visibility = View.GONE


        var maxLenthTxt = 20

        when (model?.type) {

            mContext.getString(R.string.IMAGE) -> {
                maxLenthTxt = 1
                holder.mView.fileTypeIcon.setImageResource(R.drawable.ic_file_icon_24dp)
                holder.mView.textMessage.text = mContext.getString(R.string.IMAGE)
                holder.mView.imageLayout.visibility = View.VISIBLE
                holder.mView.textLayoutView.visibility = View.GONE
            }
            mContext.getString(R.string.VIDEO) -> {
                maxLenthTxt = 1
                holder.mView.fileTypeIcon.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                holder.mView.textMessage.text = mContext.getString(R.string.VIDEO)
            }
            mContext.getString(R.string.AUDIO) -> {
                maxLenthTxt = 1
                holder.mView.fileTypeIcon.setImageResource(R.drawable.ic_audiotrack_black_24dp)
                holder.mView.textMessage.text = mContext.getString(R.string.AUDIO)
            }

            mContext.getString(R.string.TEXT) -> {
                maxLenthTxt = 20
                holder.mView.fileTypeIcon.visibility = View.GONE
                holder.mView.textMessage.text = model?.path
            }

        }

        holder.mView.textMessage.maxLines = maxLenthTxt

        if (model?.type != "" && model?.type != mContext.getString(R.string.TEXT)) {
            val ref = storageRef!!.reference.child(model?.path!!)
            ref.downloadUrl.addOnSuccessListener { url ->
                Log.d("url path", url.toString())


                if (model?.type == mContext.getString(R.string.IMAGE)) {
                    holder.mView.textMessage.tag = url.toString()
                    Picasso.get()
                            .load(url)
                            .placeholder(R.drawable.profile_pic)
                            .error(R.drawable.profile_pic)
                            .into(holder.mView.imageView1)
                }
                else
                    holder.mView.textMessage.tag = url.toString()


            }.addOnFailureListener {
                // Handle any errors
            }
        }

        var allLikeList = fDb.reference.child("likeTable").child(model?.key!!)
        allLikeList?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var size = dataSnapshot.childrenCount
                if(size>0) {
                    holder.mView.likeCount.visibility=View.VISIBLE
                    holder.mView.likeCount.text = size.toString()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(Registration.TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })


        holder.mView.textLayoutView.setOnClickListener {

            if (holder.mView.textMessage.tag != null && holder.mView.textMessage.tag != "" ) {

                if(model.type==mContext.getString(R.string.AUDIO) || model.type==mContext.getString(R.string.VIDEO)) {
                    var intent = Intent(mContext, VideoPlayActivity::class.java)
                    intent.putExtra("url", holder.mView.textMessage.tag.toString())
                    mContext.startActivity(intent)
                }

            }

        }

        holder.mView.rightTick.setOnClickListener {
            var dbInsertData = fDb.reference.child("likeTable").child(model?.key!!).push().setValue(LikeCount(model.userName, "1"))
            dbInsertData.addOnCompleteListener {

                Toast.makeText(mContext, "Like Successfully", Toast.LENGTH_LONG).show()

                holder.mView.likeCount.visibility=View.VISIBLE
               var lengthStr= holder.mView.likeCount.text.toString().toInt()
                holder.mView.likeCount.text=""+(lengthStr+1)

            }
        }
        holder.mView.shareImg.setOnClickListener{
            if(holder.mView.textMessage.tag!=null)
            shareFile(holder.mView.textMessage.tag.toString())
            else
                shareFile(model.path)

        }


    }

    override fun getItemCount(): Int {
        return imageList?.size!!
    }

    inner class SingleViewHolder(view: DiscussionAdapterBinding) : RecyclerView.ViewHolder(view.root) {
        var mView: DiscussionAdapterBinding = view
    }

    fun updateList(list: ArrayList<UploadMedia>) {
        imageList = list
    }

    fun shareFile(txt:String){

        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ambedkar Mission")
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Ambedkar Mission \n"+txt)
        mContext.startActivity(Intent.createChooser(sharingIntent, mContext!!.getString(R.string.share_using)))
    }


}