package com.grubbank.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class NutritionalValue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "calorie_value")),
    @AttributeOverride(name = "unit", column = @Column(name = "calorie_unit"))
  })
  private Calorie calories;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "fat_value")),
    @AttributeOverride(name = "unit", column = @Column(name = "fat_unit"))
  })
  private Nutrient fat;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "carb_value")),
    @AttributeOverride(name = "unit", column = @Column(name = "carb_unit"))
  })
  private Nutrient carbs;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "protein_value")),
    @AttributeOverride(name = "unit", column = @Column(name = "protein_unit"))
  })
  private Nutrient proteins;

  @OneToMany
  @JsonIgnore
  @JoinColumn(name = "recipe_id")
  List<Recipe> recipeList;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Nutrient {
    private Number value;
    private Unit unit;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Calorie {
    private Number value;
    private Unit unit;
  }

  public enum Unit {
    MG,
    GM,
    ML,
    JOULE
  }
}
