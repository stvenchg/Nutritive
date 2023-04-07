package fr.stvenchg.nutritive;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import fr.stvenchg.nutritive.api.OpenFoodFactsApi;
import fr.stvenchg.nutritive.api.ProductResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductInfoActivity extends AppCompatActivity {

    private TextView barCodeTextView;
    private TextView nameTextView;
    private TextView brandsTextView;
    private ImageView imageImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productinfo);
        getSupportActionBar().hide();

        barCodeTextView = findViewById(R.id.productinfo_textview_barcode);
        nameTextView = findViewById(R.id.productinfo_textview_name);
        brandsTextView = findViewById(R.id.productinfo_textview_brand);
        imageImageView = findViewById(R.id.productinfo_imageview_image);

        // Récupérez la valeur du code-barres à partir de l'objet Intent
        String barcodeValue = getIntent().getStringExtra("barcode");

        // Affichez la valeur du code-barres dans le TextView
        barCodeTextView.setText(barcodeValue);

        // Récupérez les informations du produit à partir de l'API OpenFoodFacts
        fetchProductDetails(barcodeValue);
    }

    private void fetchProductDetails(String barcode) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenFoodFactsApi api = retrofit.create(OpenFoodFactsApi.class);
        Call<ProductResponse> call = api.getProductByBarcode(barcode);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    if (productResponse != null) {
                        // Affichez les informations du produit dans votre vue
                        nameTextView.setText(productResponse.getProduct().getProductName());
                        brandsTextView.setText(productResponse.getProduct().getBrands());
                        Picasso.get().load(productResponse.getProduct().getImageUrl()).into(imageImageView);
                    }
                } else {
                    Log.d("ProductInfoActivity", "Erreur : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.d("ProductInfoActivity", "Erreur de réseau : " + t.getMessage());
            }
        });
    }
}