package com.example.newsplatform

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsplatform.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NewsItemClicked {

//    private lateinit var myButton: Button

    private lateinit var mAdapter: NewsListAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var currentNewsUrl: String
    private var currentNewsType: String = ""


//    @SuppressLint("SetTextI18n", "MissingInflatedId")
//    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

////        findViewById<Button>(R.id.shareButton).setOnClickListener {
////            shareNews(it)
////        }
//
//    myButton = findViewById(R.id.shareButton)
//
//    // Set a click listener for the button
//    myButton.setOnClickListener {
//        shareNews(it)
//        // Handle button click event here
//    }

        auth= FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)

        val displayName= intent.getStringExtra("Username")

        val recyclerView= findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager= LinearLayoutManager(this)
        fetchData(query = String())
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter=mAdapter
        val searchView: SearchView = findViewById<SearchView>(R.id.SearchView)
        searchView.isIconifiedByDefault = true
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    fetchData(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                // You can optionally implement live search here
                return true
            }
        })
        findViewById<TextView>(R.id.textView).text= displayName
//        findViewById<ImageView>(R.id.imageView).image=
        findViewById<Button>(R.id.signOutButton).setOnClickListener{
            auth.signOut()
            googleSignInClient.revokeAccess().addOnCompleteListener {
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
        }
    }
    private fun fetchData(query: String) {
        val url="https://saurav.tech/NewsAPI/top-headlines/category/$query/in.json"
        currentNewsType= query
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,url,null,
            { response ->
                val newsJsonArray= response.getJSONArray("articles")
                val newsArray=ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject=newsJsonArray.getJSONObject(i)

                    val news=News (
                        newsJsonObject.getString("title"),
                        News.Source(newsJsonObject.getJSONObject("source").getString("name")),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage"),
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
                if (newsArray.isNotEmpty()) {
//                    // Assign the URL of the first news item to currentImageUrl
                    currentNewsUrl = newsArray[0].url
                }
            },
            {
                    error ->
                Toast.makeText(this,"Search Keywords: Sports Entertainment Technology General Science", Toast.LENGTH_LONG).show()

            }

        )

MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
}


    override fun onItemClicked(item: News) {
        currentNewsUrl = item.url
        val intent = CustomTabsIntent.Builder().build()
        intent.launchUrl(this@MainActivity, Uri.parse(item.url))
    }

    fun shareNews(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey, Checkout this amazing $currentNewsType News from $currentNewsUrl")
        val chooser= Intent.createChooser(intent,"Share using...")
        startActivity(chooser)
    }

//    fun shareNews(view: View, currentNewsUrl: String) {
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.type = "text/plain"
//        intent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout this amazing $currentNewsType News from $currentNewsUrl")
//        val chooser = Intent.createChooser(intent, "Share using...")
//        startActivity(chooser)
//    }


//    fun shareNews(news: News) {
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.type="text/plain"
//        intent.putExtra(Intent.EXTRA_TEXT,"Hey, Checkout this amazing $currentNewsType News from $currentNewsUrl")
//        val chooser= Intent.createChooser(intent,"Share using...")
//        startActivity(chooser)
//    }

}