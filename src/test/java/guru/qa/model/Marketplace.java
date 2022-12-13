package guru.qa.model;

import com.google.gson.annotations.SerializedName;

public class Marketplace {

    public String link;

    public Seller[] seller;

    public static class Seller{
        public int id;
        public String name;
        public boolean active;
        public String[] categories;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public boolean getActive(){
            return active;
        }

        public String[] getCategories() {
            return categories;
        }

    }

    public String getLink() {
        return link;
    }

    public Seller[] getSeller() {
        return seller;
    }

}
