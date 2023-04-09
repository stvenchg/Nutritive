package fr.stvenchg.nutritive;

import android.provider.BaseColumns;

public class ProductContract {

    private ProductContract() {
    }

    public static final class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_BARCODE = "barcode";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BRAND = "brand";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_NUTRISCORE = "nutriscore";
        public static final String COLUMN_INGREDIENTS = "ingredients";
        public static final String COLUMN_ALLERGENS = "allergens";
    }
}