package org.dorastudy.mallapi.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.dorastudy.mallapi.domain.Product;
import org.dorastudy.mallapi.domain.QProduct;
import org.dorastudy.mallapi.domain.QProductImage;
import org.dorastudy.mallapi.dto.PageRequestDTO;
import org.dorastudy.mallapi.dto.PageResponseDTO;
import org.dorastudy.mallapi.dto.ProductDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;

@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {
    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO) {
        log.info("*** searchList ***");

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending());

        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<Product> query = from(product);
        query.leftJoin(product.imageList, productImage);
        query.where(productImage.ord.eq(0));

        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);

        List<Tuple> productList = query.select(product, productImage).fetch();
        long count = query.fetchCount();

        log.info("*** productList: {}", productList);

        return null;
    }
}
