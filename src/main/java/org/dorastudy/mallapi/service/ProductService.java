package org.dorastudy.mallapi.service;

import org.dorastudy.mallapi.dto.PageRequestDTO;
import org.dorastudy.mallapi.dto.PageResponseDTO;
import org.dorastudy.mallapi.dto.ProductDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProductService {
    PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);
}
