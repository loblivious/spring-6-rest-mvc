package com.loblivious.spring6restmvc.repositories;

import com.loblivious.spring6restmvc.entities.Beer;
import com.loblivious.spring6restmvc.entities.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CategoryRepositoryTest {

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  BeerRepository beerRepository;

  Beer testBeer;

  @BeforeEach
  void setUp() {
    testBeer = beerRepository.findAll().getFirst();
  }

  @Transactional
  @Test
  void testAddCategory() {
    Category savedCategory = categoryRepository.save(Category.builder()
        .description("Test Category")
        .build());

    testBeer.addCategory(savedCategory);
    Beer savedBeer = beerRepository.save(testBeer);

    System.out.println(savedBeer.getBeerName());
  }
}