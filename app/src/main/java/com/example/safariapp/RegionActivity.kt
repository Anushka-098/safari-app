import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.safariapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class RegionActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_region)

        val countrySpinner: Spinner = findViewById(R.id.country_spinner)
        val completeButton: Button = findViewById(R.id.complete_button)

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://restcountries.com/v3.1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

        val api = retrofit.create(CountryApi::class.java)

        // Make the API call
        api.getAllCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    val countries = response.body()?.map { it.name.common } ?: emptyList()

                    // Set up the spinner
                    val adapter = ArrayAdapter(this@RegionActivity, R.layout.spinner_item, countries)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    countrySpinner.adapter = adapter
                } else {
                    Toast.makeText(this@RegionActivity, "Failed to load countries", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Toast.makeText(this@RegionActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        // Handle complete button click
        completeButton.setOnClickListener {
            val selectedCountry = countrySpinner.selectedItem.toString()
            saveCountryToFirestore(selectedCountry)
        }
    }

    // Retrofit API interface
    interface CountryApi {
        @GET("all")
        fun getAllCountries(): Call<List<Country>>
    }

    // Data model class
    data class Country(val name: Name) {
        data class Name(val common: String)
    }

    // Function to save selected country to Firestore
    private fun saveCountryToFirestore(country: String) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val userCountry = hashMapOf(
                "country" to country
            )

            firestore.collection("users").document(userId)
                .set(userCountry)
                .addOnSuccessListener {
                    Toast.makeText(this, "Country saved successfully!", Toast.LENGTH_SHORT).show()
                    // Proceed to the next activity or logic
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error saving country: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }
}
