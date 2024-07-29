package org.dorastudy.mallapi.repository;

import org.dorastudy.mallapi.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
