package com.techx7.techstore.utils;

import com.techx7.techstore.model.dto.product.AddProductDTO;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class PriceUtils {

    public static BigDecimal setProductDiscountPrice(AddProductDTO addProductDTO) {
        BigDecimal discountPrice = BigDecimal.ZERO;

        BigDecimal discountPercentage = new BigDecimal(addProductDTO.getDiscountPercentage());

        if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal price = new BigDecimal(addProductDTO.getPrice());

            float pricePart = Short.parseShort(String.valueOf(discountPercentage)) / 100.00f;

            discountPrice = price.subtract(price.multiply(BigDecimal.valueOf(pricePart)));

            String formattedDiscountPrice = new DecimalFormat("######.##").format(discountPrice);

            discountPrice = new BigDecimal(formattedDiscountPrice);
        }

        return discountPrice;
    }

}
