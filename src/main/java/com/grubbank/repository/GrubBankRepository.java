package com.grubbank.repository;

import com.grubbank.entity.Recipe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GrubBankRepository extends CrudRepository<Recipe, Integer> {

    @Query("SELECT r FROM Recipe r WHERE r.name LIKE CONCAT('%',:name, '%')")
    List<Recipe> searchRecipesByName(String name);

}
