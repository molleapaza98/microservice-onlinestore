package com.erickmp.onlinestore.shopping.domain.repository;

import com.erickmp.onlinestore.shopping.domain.repository.entity.InvoiceItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceItemsRepository extends JpaRepository<InvoiceItems, Long> {
}
