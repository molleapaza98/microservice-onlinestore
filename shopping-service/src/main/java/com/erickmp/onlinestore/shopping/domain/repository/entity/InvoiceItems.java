package com.erickmp.onlinestore.shopping.domain.repository.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@Data
@Table(name = "tbl_invoice_items")
public class InvoiceItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Positive(message = "El stock debe ser mayor que cero")
    private Double quantity;

    private Double price;

    @Column(name = "product_id")
    private Long productId;

    @Transient
    private Double subTotal;

    public Double getSubtotal(){
        if(this.price>0 && this.quantity > 0){
            return this.quantity*this.price;
        }else{
            return (double)0;
        }
    }
    public InvoiceItems(){
        this.quantity=(double) 0;
        this.price=(double) 0;
    }
}
