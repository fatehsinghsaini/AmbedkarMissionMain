package com.info.work.ambedkarmission.Fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.info.work.ambedkarmission.R
import com.info.work.ambedkarmission.databinding.FragmentAllBinding
import com.info.work.ambedkarmission.Adapter.DiscussionAdapter
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.info.work.ambedkarmission.Registration
import com.info.work.ambedkarmission.Utils
import com.info.work.ambedkarmission.model.UploadMedia


class All : Fragment() {

    var mView: FragmentAllBinding? = null
    var mAdapter: DiscussionAdapter? = null
    var itemList: ArrayList<UploadMedia>? = null
      var databaseReference:FirebaseDatabase?=null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mView = DataBindingUtil.inflate<FragmentAllBinding>(inflater, R.layout.fragment_all, container, false)

        initView()


        return mView?.root
    }

    private fun initView() {

         var storage = FirebaseStorage.getInstance()
         databaseReference = FirebaseDatabase.getInstance()

        itemList = ArrayList()
        mAdapter = DiscussionAdapter(context!!, itemList, storage,databaseReference!!)
        val mLayoutManager = LinearLayoutManager(context)
        mView!!.allList.layoutManager = mLayoutManager
        mView!!.allList.itemAnimator = DefaultItemAnimator()
        mView!!.allList.adapter = mAdapter


    }

    override fun onStart() {
        super.onStart()
        if (itemList?.size!! > 0)
            itemList?.clear()

        basicQueryValueListener()

    }

    fun basicQueryValueListener() {


        val myTopPostsQuery = databaseReference?.reference?.child("uploadedItems")?.orderByChild("date")

        myTopPostsQuery?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (postSnapshot in dataSnapshot.children) {
                    // TODO: handle the post
                    Log.d("ALLData", postSnapshot.toString())
                    postSnapshot.key

                   var item= postSnapshot.getValue(UploadMedia::class.java)
                    item?.key=postSnapshot.key!!

                    itemList?.add(item!!)

                }
                if (mAdapter != null && itemList!=null) {
                    if(itemList!!.size>0)
                        mView!!.noDataFound.visibility=View.VISIBLE
                     else
                        mView!!.noDataFound.visibility=View.GONE

                    mAdapter!!.updateList(itemList!!)
                    mAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(Registration.TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })

    }


}
