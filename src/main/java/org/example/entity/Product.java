package org.example.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;



}
