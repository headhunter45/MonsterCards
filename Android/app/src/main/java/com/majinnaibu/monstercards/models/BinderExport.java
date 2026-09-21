package com.majinnaibu.monstercards.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BinderExport {
    @SerializedName("$schema")
    public String schema = "https://majinnaibu.com/schemas/binder.schema.json";

    @SerializedName("schemaVersion")
    public int schemaVersion = 1;

    @SerializedName("collections")
    public List<CollectionExport> collections = new ArrayList<>();

    @SerializedName("dashboard")
    public List<Monster> dashboard = new ArrayList<>();

    public static class CollectionExport {
        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("description")
        public String description;

        @SerializedName("cards")
        public List<Monster> cards = new ArrayList<>();

        public CollectionExport() {
        }

        public CollectionExport(String id, String name, String description, List<Monster> cards) {
            this.id = id != null ? id : UUID.randomUUID().toString();
            this.name = name != null ? name : "";
            this.description = description != null ? description : "";
            this.cards = cards != null ? cards : new ArrayList<>();
        }

        public CollectionExport(String name, List<Monster> cards) {
            this(UUID.randomUUID().toString(), name, "", cards);
        }
    }
}
