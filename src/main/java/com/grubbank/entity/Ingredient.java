package com.grubbank.entity;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column private String name;

  @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "ingredientSet")
  @JsonIgnore
  List<Recipe> recipeSet;
}
