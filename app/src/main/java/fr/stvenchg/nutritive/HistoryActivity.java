package fr.stvenchg.nutritive;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;

    private ProductAdapter productAdapter;

    private ImageView backImageView;

    String TAG = "HistoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().hide();

        historyRecyclerView = findViewById(R.id.history_recyclerview);
        backImageView = findViewById(R.id.history_imageview_back);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadProductHistory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (productAdapter != null) {
            productAdapter.close();
        }
    }

    private void loadProductHistory() {
        ProductDbHelper dbHelper = new ProductDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                ProductDbHelper.COL_ID,
                ProductDbHelper.COL_BARCODE,
                ProductDbHelper.COL_NAME,
                ProductDbHelper.COL_BRAND,
                ProductDbHelper.COL_IMAGE_URL,
                ProductContract.ProductEntry.COLUMN_QUANTITY,
                ProductContract.ProductEntry.COLUMN_NUTRISCORE,
                ProductContract.ProductEntry.COLUMN_INGREDIENTS,
                ProductContract.ProductEntry.COLUMN_ALLERGENS
        };

        Cursor cursor = db.query(
                ProductDbHelper.TABLE_PRODUCTS,
                projection,
                null,
                null,
                null,
                null,
                ProductDbHelper.COL_ID + " DESC"
        );

        // Utilisez un adaptateur pour remplir le RecyclerView avec les données des produits
        if (productAdapter == null) {
            productAdapter = new ProductAdapter(this, cursor, db);
            RecyclerView recyclerView = findViewById(R.id.history_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(productAdapter);
        } else {
            productAdapter.swapCursor(cursor);
        }
    }
}
