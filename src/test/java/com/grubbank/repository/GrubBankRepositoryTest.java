package com.grubbank.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class GrubBankRepositoryTest {
  @Autowired RecipeRepository grubBankRepository;
}
