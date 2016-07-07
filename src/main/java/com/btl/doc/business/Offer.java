package com.btl.doc.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "offer")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    private Double price;

    private String comment;

    private String name;
}
