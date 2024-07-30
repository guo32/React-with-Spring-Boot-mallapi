package org.dorastudy.mallapi.repository.search;

import org.dorastudy.mallapi.dto.PageRequestDTO;
import org.dorastudy.mallapi.dto.PageResponseDTO;
import org.dorastudy.mallapi.dto.ProductDTO;

public interface ProductSearch {
    PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO);
}
