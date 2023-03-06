package com.grubbank.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private int numberOfServings;

    @Column
    private String name;

    @Column
    private String instructions;


    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "ingredient_contain",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> ingredientSet;

    @Column
    private Duration preparationTime;

    @Column
    private Duration totalTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nutritionalvalue_id")
    private NutritionalValue nutritionalValue;

    @Column
    private RecipeType recipeType;

    public enum RecipeType {
        VEGAN,
        WHITE_MEAT,
        RED_MEAT,
        VEGETARIAN,
        SEA_FOOD
    }
}
