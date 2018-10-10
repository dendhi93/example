package com.example.myapplication.connection

import android.content.Context
import android.net.ConnectivityManager
import com.example.myapplication.utils.Constants
import com.example.myapplication.BuildConfig
import com.example.myapplication.application.BaseApplication
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Connection{
    const val TAG = "Connection"
    private const val TIMEOUT = 60

    private fun host(urlType: Int): String{
        return when(urlType){
            Constants.KEY_URL_1 -> "http"
            else -> {
                return ""
            }
        }
    }

    fun open(hostType: Int): ConnectionInterface {
        val baseUrl = host(hostType)

        val gson: Gson = GsonBuilder()
                .setLenient()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client(false, TIMEOUT))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        return retrofit.create(ConnectionInterface::class.java)
    }

    fun connectRx(hostType: Int): ConnectionInterface {
        val baseUrl = host(hostType)

        val gson: Gson = GsonBuilder()
                .setLenient()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client(false, TIMEOUT))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        return retrofit.create(ConnectionInterface::class.java)
    }

    private fun client(retry: Boolean, duration: Int): OkHttpClient {

        val builder = OkHttpClient.Builder()
                .connectTimeout(duration.toLong(), TimeUnit.SECONDS)
                .readTimeout(duration.toLong(), TimeUnit.SECONDS)
                .writeTimeout(duration.toLong(), TimeUnit.SECONDS)
                .retryOnConnectionFailure(retry)
        if(BaseApplication.DEBUG){
            builder.addNetworkInterceptor(
                    HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        return builder.build()
    }

    /**
     * check respond http response
     */
    fun checkHttpCode(httpCode: String): Boolean = httpCode == "200"

    fun checkHttpBadRequest(httpCode: Int): Boolean = httpCode == 401

    /**
     * check if network available either wi-fi or 3g data
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


}
