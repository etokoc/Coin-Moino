package com.metoer.ceptedovizborsa.util

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUtil {
    private var db: FirebaseFirestore? = null
    fun initializeFirebase(): FirebaseFirestore? {
        if (db == null)
            db = FirebaseFirestore.getInstance()
        return db
    }

    fun readData(collectionName: String): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        db?.collection(collectionName)?.get()?.addOnCompleteListener { task ->
            when {
                task.isSuccessful -> {
                    val data = task.result.documents[0].data?.values?.first()
                    liveData.value = data.toString().toBoolean()

                }
                task.isCanceled -> {
                    Log.e("FIREBASE ERROR", "is Canceled ")
                    liveData.value = false
                }
            }

        }
        return liveData
    }
}