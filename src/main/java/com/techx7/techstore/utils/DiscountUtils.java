package com.techx7.techstore.utils;

import com.techx7.techstore.model.dto.product.AddProductDTO;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static com.techx7.techstore.utils.StringUtils.isNullOrEmpty;

public class DiscountUtils {

    public static BigDecimal setProductDiscountPrice(AddProductDTO addProductDTO) {
        if(isNullOrEmpty(addProductDTO.getDiscountPercentage())) {
            return null;
        }

        BigDecimal discountPercentage = new BigDecimal(addProductDTO.getDiscountPercentage());

        if(discountPercentage.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal price = new BigDecimal(addProductDTO.getPrice());

        float pricePart = Short.parseShort(String.valueOf(discountPercentage)) / 100.00f;

        BigDecimal discountPrice = price.subtract(price.multiply(BigDecimal.valueOf(pricePart)));

        String formattedDiscountPrice = new DecimalFormat("######.##").format(discountPrice);

        discountPrice = new BigDecimal(formattedDiscountPrice);

        return discountPrice;
    }

}
