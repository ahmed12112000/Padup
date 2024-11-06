package com.nevaDev.padeliummarhaba.retrofit

import android.content.Context
import com.nevaDev.padeliummarhaba.repository.Constant
import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import retrofit2.converter.scalars.ScalarsConverterFactory

import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import okhttp3.CookieJar
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

object RetrofitClient {
    private lateinit var retrofit: Retrofit

    fun init(context: Context) {
        // Logging interceptor for debugging
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        // Create the custom cookie jar
        val cookieJar = CustomCookieJar()

        // Build OkHttpClient with all interceptors and the cookie jar
        val client = OkHttpClient.Builder()
            .addInterceptor(logging) // Logs request/response body
            .cookieJar(cookieJar) // Adds the custom cookie jar
            .build()

        // Initialize Retrofit with base URL, OkHttp client, and converters
        retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create()) // For handling plain text responses
            .addConverterFactory(GsonConverterFactory.create(createLenientGson())) // For JSON responses
            .build()
    }

    // Lazy initialization of the ServerApi interface
    val serverApi: ServerApi by lazy {
        if (!::retrofit.isInitialized) {
            throw IllegalStateException("RetrofitClient is not initialized. Call init() first.")
        }
        retrofit.create(ServerApi::class.java)
    }

    // Custom Gson instance with lenient parsing enabled
    private fun createLenientGson(): Gson {
        return GsonBuilder()
            .setLenient()  // Allows more flexible JSON parsing
            .create()
    }
    class CustomCookieJar : CookieJar {
        private val cookies: HashMap<String, List<Cookie>> = HashMap()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            this.cookies[url.host] = cookies
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookies[url.host] ?: ArrayList()
        }
    }

    // Custom CookieJar implementation to ignore cookies
    private class NoCookieJar : CookieJar {
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {}
        override fun loadForRequest(url: HttpUrl): List<Cookie> = emptyList()
    }
}

// Interceptor to remove Set-Cookie header from responses
class NoCookieInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalResponse = chain.proceed(chain.request())
        return originalResponse.newBuilder()
            .removeHeader("Set-Cookie") // Remove the Set-Cookie header
            .build()
    }
}


// Custom GsonConverterFactory with lenient parsing
class LenientGsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {
    companion object {
        fun create(gson: Gson): LenientGsonConverterFactory {
            return LenientGsonConverterFactory(gson)
        }
    }

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return Converter<ResponseBody, Any> { body ->
            val jsonReader: JsonReader = gson.newJsonReader(body.charStream())
            jsonReader.isLenient = true
            adapter.read(jsonReader)
        }
    }



}


// Optional: Create an EmptyResponseInterceptor to handle empty responses
class EmptyResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalResponse = chain.proceed(chain.request())

        // Check if the response body is empty
        if (originalResponse.body != null && originalResponse.body?.contentLength() == 0L) {
            // Create a new empty response to avoid parsing errors
            return originalResponse.newBuilder()
                .code(204) // No Content status code
                .build()
        }

        return originalResponse
    }
}

// Optional: In the `init` method, you can add the EmptyResponseInterceptor if needed
