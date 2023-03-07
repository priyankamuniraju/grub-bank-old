package com.grubbank.repository;

import com.grubbank.entity.Recipe;

public interface GrubRepositoryCustom {
  Recipe update(Recipe recipe);

  Recipe save(Recipe recipe);
}
