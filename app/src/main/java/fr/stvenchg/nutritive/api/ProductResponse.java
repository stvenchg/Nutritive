package fr.stvenchg.nutritive.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductResponse {

    @SerializedName("status_verbose")
    private String statusVerbose;

    private Product product;

    public String getStatusVerbose() {
        return statusVerbose;
    }

    public Product getProduct() {
        return product;
    }

    public static class Product {

        @SerializedName("product_name")
        private String productName;

        private String brands;

        @SerializedName("image_url")
        private String imageUrl;

        private List<NutrientLevel> nutrientLevels;

        public String getProductName() {
            return productName;
        }

        public String getBrands() {
            return brands;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public List<NutrientLevel> getNutrientLevels() {
            return nutrientLevels;
        }

        public static class NutrientLevel {

            @SerializedName("nutrient_id")
            private String nutrientId;

            private String level;

            public String getNutrientId() {
                return nutrientId;
            }

            public String getLevel() {
                return level;
            }
        }
    }
}