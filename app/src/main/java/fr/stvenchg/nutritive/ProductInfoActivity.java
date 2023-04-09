package fr.stvenchg.nutritive;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.elyeproj.loaderviewlibrary.LoaderImageView;
import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.squareup.picasso.Picasso;

import fr.stvenchg.nutritive.api.OpenFoodFactsApi;
import fr.stvenchg.nutritive.api.ProductResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductInfoActivity extends AppCompatActivity {

    private LoaderTextView barCodeTextView;
    private LoaderTextView nameTextView;
    private TextView brandsTextView;

    private LoaderImageView nutriscoreImageView;

    private TextView quantityTextView;
    private LoaderImageView imageImageView;

    private Drawable nutriscoreA;
    private Drawable nutriscoreB;
    private Drawable nutriscoreC;
    private Drawable nutriscoreD;
    private Drawable nutriscoreE;

    private TextView closeTextView;

    private TextView ingredientsTextView;

    private TextView allergensTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productinfo);
        getSupportActionBar().hide();

        barCodeTextView = findViewById(R.id.productinfo_loadertextview_barcode);
        nameTextView = findViewById(R.id.productinfo_loadertextview_name);
        brandsTextView = findViewById(R.id.productinfo_textview_brand);
        imageImageView = findViewById(R.id.productinfo_loaderimageview_image);
        quantityTextView = findViewById(R.id.productinfo_textview_quantity);
        nutriscoreImageView = findViewById(R.id.productinfo_loaderimageview_nutriscore);
        closeTextView = findViewById(R.id.productinfo_textview_close);
        ingredientsTextView = findViewById(R.id.productinfo_textview_ingredients);
        allergensTextView = findViewById(R.id.productinfo_textview_allergens);

        closeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nutriscoreA = ContextCompat.getDrawable(this, R.drawable.nutriscore_a);
        nutriscoreB = ContextCompat.getDrawable(this, R.drawable.nutriscore_b);
        nutriscoreC = ContextCompat.getDrawable(this, R.drawable.nutriscore_c);
        nutriscoreD = ContextCompat.getDrawable(this, R.drawable.nutriscore_d);
        nutriscoreE = ContextCompat.getDrawable(this, R.drawable.nutriscore_e);

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
                        brandsTextView.setText("Marque : " + productResponse.getProduct().getBrands());
                        Picasso.get().load(productResponse.getProduct().getImageUrl()).into(imageImageView);
                        quantityTextView.setText("Quantité : " + productResponse.getProduct().getQuantity());
                        ingredientsTextView.setText(productResponse.getProduct().getIngredientsText());
                        allergensTextView.setText(productResponse.getProduct().getAllergens());

                        if (productResponse.getProduct().getNutriscoreGrade() != null) {
                            if (productResponse.getProduct().getNutriscoreGrade().equals("a")) {
                                nutriscoreImageView.setImageDrawable(nutriscoreA);
                            } else if (productResponse.getProduct().getNutriscoreGrade().equals("b")) {
                                nutriscoreImageView.setImageDrawable(nutriscoreB);
                            } else if (productResponse.getProduct().getNutriscoreGrade().equals("c")) {
                                nutriscoreImageView.setImageDrawable(nutriscoreC);
                            } else if (productResponse.getProduct().getNutriscoreGrade().equals("d")) {
                                nutriscoreImageView.setImageDrawable(nutriscoreD);
                            } else if (productResponse.getProduct().getNutriscoreGrade().equals("e")) {
                                nutriscoreImageView.setImageDrawable(nutriscoreE);
                            }
                        }

                        // Ajoutez le produit scanné à la base de données
                        String barcodeValue = getIntent().getStringExtra("barcode");

                        ProductDbHelper dbHelper = new ProductDbHelper(ProductInfoActivity.this);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(ProductContract.ProductEntry.COLUMN_BARCODE, barcodeValue);
                        contentValues.put(ProductContract.ProductEntry.COLUMN_NAME, productResponse.getProduct().getProductName());
                        contentValues.put(ProductContract.ProductEntry.COLUMN_BRAND, productResponse.getProduct().getBrands());
                        contentValues.put(ProductContract.ProductEntry.COLUMN_IMAGE_URL, productResponse.getProduct().getImageUrl());
                        contentValues.put(ProductContract.ProductEntry.COLUMN_QUANTITY, productResponse.getProduct().getQuantity());
                        contentValues.put(ProductContract.ProductEntry.COLUMN_NUTRISCORE, productResponse.getProduct().getNutriscoreGrade());
                        contentValues.put(ProductContract.ProductEntry.COLUMN_INGREDIENTS, productResponse.getProduct().getIngredientsText());
                        contentValues.put(ProductContract.ProductEntry.COLUMN_ALLERGENS, productResponse.getProduct().getAllergens());

                        long newRowId = db.insert(ProductContract.ProductEntry.TABLE_NAME, null, contentValues);
                        if (newRowId == -1) {
                            Log.d("ProductInfoActivity", "Erreur lors de l'insertion du produit dans la base de données");
                        } else {
                            Log.d("ProductInfoActivity", "Produit ajouté à l'historique avec l'ID: " + newRowId);
                        }

                        db.close();
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