package com.example.mygarden.model;
import android.graphics.Bitmap;

public class Plant{

    private int _id;
    private String name;
    private String localization;
    private String species;
    private String notes;
    private Bitmap image;

    public Plant(){}

    public Plant(String name, String localization, String species, String notes, Bitmap image) {
        this.name = name;
        this.localization = localization;
        this.species = species;
        this.notes = notes;
        this.image = image;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocalization() { return localization; }
    public void setLocalization(String localization) { this.localization = localization; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Bitmap getImage() { return image; }
    public void setImage(Bitmap image) { this.image = image; }

    public int getId() { return _id; }
    public void setId(int id) { this._id = id; }
}
