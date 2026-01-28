package com.example.proje5

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.Query



class FeedFragment : Fragment(R.layout.fragment_feed) {

    private lateinit var recyclerView: RecyclerView
    private val postList = mutableListOf<Gönderi>()
    private lateinit var adapter: Adapter

    // ana akıs fragmnet sayfası

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvPosts)
        adapter = Adapter(postList)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadPosts()
    }

    private fun loadPosts() {
        Firebase.firestore
            .collection("posts")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->

                if (error != null || snapshot == null) return@addSnapshotListener

                postList.clear()

                for (doc in snapshot.documents) {
                    val post = doc.toObject(Gönderi::class.java)
                    if (post != null) {
                        postList.add(post.copy(postId = doc.id))
                    }
                }

                adapter.notifyDataSetChanged()
            }
    }

}
