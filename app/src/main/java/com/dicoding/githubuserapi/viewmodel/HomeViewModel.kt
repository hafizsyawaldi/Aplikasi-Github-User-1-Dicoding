package com.dicoding.githubuserapi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapi.api.ApiConfig
import com.dicoding.githubuserapi.response.DetailUser
import com.dicoding.githubuserapi.response.Follow
import com.dicoding.githubuserapi.response.GithubResponse
import com.dicoding.githubuserapi.response.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val searchList = MutableLiveData<ArrayList<ItemsItem>>()
    val getSearchList: LiveData<ArrayList<ItemsItem>> = searchList

    private val userDetail = MutableLiveData<DetailUser>()
    val getUserDetail: LiveData<DetailUser> = userDetail

    private val followers = MutableLiveData<ArrayList<Follow>>()
    val getFollowers: LiveData<ArrayList<Follow>> = followers

    private val following = MutableLiveData<ArrayList<Follow>>()
    val getFollowing: LiveData<ArrayList<Follow>> = following

    private val isLoading = MutableLiveData<Boolean>()
    val getIsLoading: LiveData<Boolean> = isLoading

    fun searchUser(username: String) {
        try {
            isLoading.value = true
            val client = ApiConfig.getApiService().search(username)
            client.enqueue(object : Callback<GithubResponse> {
                override fun onResponse(
                    call: Call<GithubResponse>,
                    response: Response<GithubResponse>
                ) {
                    isLoading.value = false
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        searchList.value = ArrayList(responseBody.items)
                    }
                }

                override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                    isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        } catch (e: Exception) {
            Log.d("Token e", e.toString())
        }
    }

    fun detailUser(username: String) {
        try {
            isLoading.value = true
            val client = ApiConfig.getApiService().detailUser(username)
            client.enqueue(object : Callback<DetailUser> {
                override fun onResponse(
                    call: Call<DetailUser>,
                    response: Response<DetailUser>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        userDetail.value = response.body()
                    }
                }

                override fun onFailure(call: Call<DetailUser>, t: Throwable) {
                    isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        } catch (e: Exception) {
            Log.d("Token e", e.toString())
        }
    }

    fun followers(username: String) {
        try {
            isLoading.value = true
            val client = ApiConfig.getApiService().followers(username)
            client.enqueue(object : Callback<ArrayList<Follow>> {
                override fun onResponse(
                    call: Call<ArrayList<Follow>>,
                    response: Response<ArrayList<Follow>>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        followers.value = response.body()
                    }
                }

                override fun onFailure(call: Call<ArrayList<Follow>>, t: Throwable) {
                    isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        } catch (e: Exception) {
            Log.d("Token e", e.toString())
        }
    }

    fun following(username: String) {
        try {
            isLoading.value = true
            val client = ApiConfig.getApiService().following(username)
            client.enqueue(object : Callback<ArrayList<Follow>> {
                override fun onResponse(
                    call: Call<ArrayList<Follow>>,
                    response: Response<ArrayList<Follow>>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        following.value = response.body()
                    }
                }

                override fun onFailure(call: Call<ArrayList<Follow>>, t: Throwable) {
                    isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        } catch (e: Exception) {
            Log.d("Token e", e.toString())
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}