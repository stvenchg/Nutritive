package fr.stvenchg.nutritive;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "product_history.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_PRODUCTS = "products";
    public static final String COL_ID = "_id";
    public static final String COL_BARCODE = "barcode";
    public static final String COL_NAME = "name";
    public static final String COL_BRAND = "brand";
    public static final String COL_IMAGE_URL = "image_url";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_NUTRISCORE = "nutriscore";
    public static final String COL_INGREDIENTS = "ingredients";
    public static final String COL_ALLERGENS = "allergens";

    private static final String SQL_CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_BARCODE + " TEXT UNIQUE NOT NULL," +
            COL_NAME + " TEXT NOT NULL," +
            COL_BRAND + " TEXT," +
            COL_IMAGE_URL + " TEXT," +
            COL_QUANTITY + " TEXT," +
            COL_NUTRISCORE + " TEXT," +
            COL_INGREDIENTS + " TEXT," +
            COL_ALLERGENS + " TEXT);";

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_PRODUCTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }
}