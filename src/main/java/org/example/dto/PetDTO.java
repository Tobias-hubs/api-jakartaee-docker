package org.example.dto;
import jakarta.validation.constraints.*;

public class PetDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String species;

    @Min(0) @Max(100)
    private int hungerLevel;

    @Min(0) @Max(100)
    private int happinesLevel;

    // Getters & setters
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getSpecies() {return species;}
    public void setSpecies(String species) {this.species = species;}

    public int getHungerLevel() {return hungerLevel;}
    public void setHungerLevel(int hungerLevel) {this.hungerLevel = hungerLevel;}

    public int getHappinesLevel() {return happinesLevel;}
    public void setHappinesLevel(int happinesLevel) {this.happinesLevel = happinesLevel;}

}
