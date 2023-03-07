package com.grubbank.entity;

import com.fasterxml.jackson.annotation.*;
import com.grubbank.apimodel.Streamable;
import java.time.Duration;
import java.util.List;
import javax.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "recipe")
public class Recipe implements Streamable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column private int numberOfServings;

  @Column private String name;

  @Column private String instructions;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "ingredient_contain",
      joinColumns = @JoinColumn(name = "recipe_id"),
      inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
  private List<Ingredient> ingredientSet;

  @Column private Duration preparationTime;

  @Column private Duration totalTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "nutritionalvalue_id")
  private NutritionalValue nutritionalValue;

  @Column private RecipeType recipeType;

  public enum RecipeType {
    VEGAN,
    WHITE_MEAT,
    RED_MEAT,
    VEGETARIAN,
    SEA_FOOD
  }
}
