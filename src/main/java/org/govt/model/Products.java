package org.govt.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Products {
    private String id;        
    private String name;
    private String description;
    private double price;
    private boolean available;  // availability flag
}


