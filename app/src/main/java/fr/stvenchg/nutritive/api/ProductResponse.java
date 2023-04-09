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

        private String quantity;

        @SerializedName("nutriscore_grade")
        private String nutriscoreGrade;

        // Ajout des champs pour les ingrédients et les allergènes
        @SerializedName("ingredients_text")
        private String ingredientsText;

        @SerializedName("allergens")
        private String allergens;

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

        public String getQuantity() {
            return quantity;
        }

        public String getNutriscoreGrade() {
            return nutriscoreGrade;
        }

        public List<NutrientLevel> getNutrientLevels() {
            return nutrientLevels;
        }

        public String getIngredientsText() {
            return ingredientsText;
        }

        public String getAllergens() {
            return allergens;
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