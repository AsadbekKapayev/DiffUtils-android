package com.example.recyclerviewanddiffutilsroman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewanddiffutilsroman.databinding.ActivityMainBinding
import com.example.recyclerviewanddiffutilsroman.model.User
import com.example.recyclerviewanddiffutilsroman.model.UsersListener
import com.example.recyclerviewanddiffutilsroman.model.UsersServices

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter
    private val usersServices: UsersServices
        get() = (applicationContext as App).usersServices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        adapter = UsersAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int) {
                usersServices.moveUser(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                usersServices.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                Toast.makeText(this@MainActivity, "User: ${user.name}", Toast.LENGTH_SHORT).show()
            }

            override fun onUserFire(user: User) {
                usersServices.fireUser(user)
            }
        })

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        val itemAnimator = binding.recyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false 
        }

        usersServices.addListener(usersListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        usersServices.removeListener(usersListener)
    }

    private val usersListener: UsersListener = {
        adapter.users = it
    }
}