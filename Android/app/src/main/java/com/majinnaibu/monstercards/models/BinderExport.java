package com.majinnaibu.monstercards.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BinderExport {
    @SerializedName("schemaVersion")
    public int schemaVersion = 1;

    @SerializedName("collections")
    public List<CollectionExport> collections = new ArrayList<>();

    public static class CollectionExport {
        @SerializedName("name")
        public String name;

        @SerializedName("cards")
        public List<Monster> cards = new ArrayList<>();

        public CollectionExport() {
        }

        public CollectionExport(String name, List<Monster> cards) {
            this.name = name != null ? name : "";
            this.cards = cards != null ? cards : new ArrayList<>();
        }
    }
}
