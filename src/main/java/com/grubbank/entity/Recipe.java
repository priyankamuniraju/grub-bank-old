package com.grubbank.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recipe")
public class Recipe {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private int numberOfServings;

    @NonNull
    @Column
    private String name;

    @Column
    private String instructions;

    @ManyToMany(mappedBy = "recipeSet")
    private Set<Ingredient> ingredients;

    private Duration preparationTime;
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
