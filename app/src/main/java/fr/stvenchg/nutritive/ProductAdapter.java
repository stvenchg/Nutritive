package fr.stvenchg.nutritive;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private SQLiteDatabase mDb;

    public ProductAdapter(Context context, Cursor cursor, SQLiteDatabase db) {
        mContext = context;
        mCursor = cursor;
        mDb = db;
    }

    public void close() {
        if (mCursor != null) {
            mCursor.close();
        }

        if (mDb != null) {
            mDb.close();
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        int productNameIndex = mCursor.getColumnIndex(ProductDbHelper.COL_NAME);
        int productBrandIndex = mCursor.getColumnIndex(ProductDbHelper.COL_BRAND);
        int productImageUrlIndex = mCursor.getColumnIndex(ProductDbHelper.COL_IMAGE_URL);
        int idIndex = mCursor.getColumnIndex(ProductDbHelper.COL_ID);

        if (productNameIndex >= 0 && productBrandIndex >= 0 && productImageUrlIndex >= 0 && idIndex >= 0) {
            String productName = mCursor.getString(productNameIndex);
            String productBrand = mCursor.getString(productBrandIndex);
            String productImageUrl = mCursor.getString(productImageUrlIndex);
            long id = mCursor.getLong(idIndex);

            // Utilisez ces valeurs pour mettre à jour votre ProductViewHolder
            holder.productNameTextView.setText(productName);
            holder.productBrandTextView.setText(productBrand);
            Picasso.get().load(productImageUrl).into(holder.productImageView);

            // Vous pouvez également définir un OnClickListener pour chaque élément, si vous le souhaitez
            holder.itemView.setTag(id);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    mCursor.moveToPosition(position);
                    int barcodeIndex = mCursor.getColumnIndex(ProductDbHelper.COL_BARCODE);
                    if (barcodeIndex >= 0) {
                        String barcode = mCursor.getString(barcodeIndex);

                        Intent intent = new Intent(mContext, ProductInfoActivity.class);
                        intent.putExtra("barcode", barcode);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        TextView productBrandTextView;
        ImageView productImageView;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productNameTextView = itemView.findViewById(R.id.product_name);
            productBrandTextView = itemView.findViewById(R.id.product_brand);
            productImageView = itemView.findViewById(R.id.product_image);
        }
    }
}