package org.dorastudy.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dorastudy.mallapi.dto.PageRequestDTO;
import org.dorastudy.mallapi.dto.PageResponseDTO;
import org.dorastudy.mallapi.dto.ProductDTO;
import org.dorastudy.mallapi.service.ProductService;
import org.dorastudy.mallapi.util.CustomFileUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final CustomFileUtil fileUtil;
    private final ProductService productService;

    /*@PostMapping("/")
    public Map<String, String> register(ProductDTO productDTO) {
        log.info("register: {}", productDTO);
        List<MultipartFile> files = productDTO.getFiles();
        List<String> uploadedFileNames = fileUtil.saveFiles(files);
        productDTO.setUploadFileNames(uploadedFileNames);
        log.info(uploadedFileNames);

        return Map.of("RESULT", "SUCCESS");
    }*/

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable("fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        return productService.getList(pageRequestDTO);
    }

    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO) {
        List<MultipartFile> files = productDTO.getFiles();
        List<String> uploadFileNames = fileUtil.saveFiles(files);

        productDTO.setUploadFileNames(uploadFileNames);
        log.info("upload file names: {}", uploadFileNames);

        Long pno = productService.register(productDTO);
        return Map.of("RESULT", pno);
    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable("pno") Long pno) {
        return productService.get(pno);
    }

    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable("pno") Long pno, ProductDTO productDTO) {
        productDTO.setPno(pno);

        // old product database saved
        ProductDTO oldProductDTO = productService.get(pno);

        // file upload
        List<MultipartFile> files = productDTO.getFiles();
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        // keep files
        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        if (currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        productService.modify(productDTO);

        List<String> oldFileNames = oldProductDTO.getUploadFileNames();
        if (oldFileNames != null && !oldFileNames.isEmpty()) {
            List<String> removeFiles =
                    oldFileNames.stream().filter(fileName -> uploadedFileNames.indexOf(fileName) == -1).collect(Collectors.toList());
            fileUtil.deleteFiles(removeFiles);
        }

        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable("pno") Long pno) {
        List<String> oldFileNames = productService.get(pno).getUploadFileNames();
        productService.remove(pno);
        fileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT", "SUCCESS");
    }
}
