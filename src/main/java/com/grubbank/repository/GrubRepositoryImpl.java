package com.grubbank.repository;

import com.grubbank.entity.Recipe;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GrubRepositoryImpl implements GrubRepositoryCustom {

  @PersistenceContext EntityManager entityManager;

  @Override
  public Recipe update(Recipe recipe) {
    return null;
  }

  @Override
  public Recipe save(Recipe recipe) {
    return null;
  }
}
