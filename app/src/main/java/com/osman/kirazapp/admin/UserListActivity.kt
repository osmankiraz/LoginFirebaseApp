package com.osman.kirazapp.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.osman.kirazapp.Adapters.UserListAdapter
import com.osman.kirazapp.Models.Kullanicilar
import com.osman.kirazapp.R

class UserListActivity : AppCompatActivity() {


    private lateinit var dbRef:DatabaseReference

    lateinit var userRecyclerView: RecyclerView
    lateinit var userArrayList:ArrayList<Kullanicilar>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


       // requestWindowFeature(Window.FEATURE_ACTION_BAR)
        //this.window.setFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR,WindowManager.LayoutParams.TYPE_STATUS_BAR)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_user_list)



        userRecyclerView=findViewById(R.id.userList)
        userRecyclerView.isNestedScrollingEnabled=false
        userRecyclerView.layoutManager=LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf<Kullanicilar>()
        getUserData()




    }


    //  KULLANICININ VERİLERİNİ GETİR
    private fun getUserData() {

        dbRef=FirebaseDatabase.getInstance().getReference("users")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val user=userSnapshot.getValue(Kullanicilar::class.java)
                        val userVerified=userSnapshot.getValue(Kullanicilar::class.java)!!.verified

                        if (userVerified!!.equals("waiting")) {
                            userArrayList.add(user!!)
                        } else {
                        }


                    }
                    userRecyclerView.adapter=UserListAdapter(userArrayList)

                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }
}