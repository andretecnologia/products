package com.company.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    @ApiModelProperty(notes = "Id" , position = 1, name = "Id", dataType = "String", example = "Beer", required = false, hidden = true)
    private String id;
    @ApiModelProperty(notes = "Name" , position = 2, name = "Name", dataType = "String", example = "Beer", required = true)
    private String name;
    @ApiModelProperty(notes = "Description" , position = 3, name = "description", dataType = "String", example = "Weiss", required = false)
    private String description;
    @ApiModelProperty(notes = "Price" , position = 4, name = "price", dataType = "String", example = "10.22", required = true)
    private String price;
    @ApiModelProperty(notes = "Brand should be retrieved via the Brand endpoint" , position = 5, name = "Brand", dataType = "Brand")
    private BrandDTO brand;
}
