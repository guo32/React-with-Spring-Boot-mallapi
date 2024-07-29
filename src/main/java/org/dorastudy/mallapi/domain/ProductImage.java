package org.dorastudy.mallapi.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    private String filaName;

    private int ord; // 순번

    public void setOrd(int ord) {
        this.ord = ord;
    }
}
