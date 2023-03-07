package com.grubbank.repository;

import com.grubbank.entity.Recipe;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Integer>, RecipeRepositoryCustom {

  @Query("SELECT r FROM Recipe r WHERE UPPER(r.name) LIKE CONCAT('%',:name, '%')")
  List<Recipe> searchRecipesByName(String name);
}
