package org.dorastudy.mallapi.repository;

import lombok.extern.log4j.Log4j2;
import org.dorastudy.mallapi.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testInsert() {
        Product product = Product.builder()
                .pname("Test")
                .pdescription("Test Description")
                .price(1000)
                .build();
        product.addImageString(UUID.randomUUID() + "_" + "Image1.jpg");
        product.addImageString(UUID.randomUUID() + "_" + "Image2.jpg");

        productRepository.save(product);
    }
}
