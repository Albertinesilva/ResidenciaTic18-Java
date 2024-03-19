package com.swproject.sellgenius.web.dto.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductForm {

    private String name;
    private String description;
    private Double price;
    private String imgUrl;
}
