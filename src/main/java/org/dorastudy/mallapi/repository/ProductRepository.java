package org.dorastudy.mallapi.repository;

import org.dorastudy.mallapi.domain.Product;
import org.dorastudy.mallapi.repository.search.ProductSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {
    @EntityGraph(attributePaths = "imageList")
    @Query("select p from Product p where p.pno = :pno")
    Optional<Product> selectOne(@Param("pno") Long pno);

    @Modifying
    @Query("update Product p set p.delFlag = :delFlag where p.pno = :pno")
    void updateToDelete(@Param("delFlag") boolean delFlag, @Param("pno") Long pno);

    @Query("select p, pi from Product p left join p.imageList pi where pi.ord = 0 and p.delFlag = false")
    Page<Object[]> selectList(Pageable pageable);
}
