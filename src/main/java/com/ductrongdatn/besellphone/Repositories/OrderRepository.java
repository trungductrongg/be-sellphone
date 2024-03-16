package com.ductrongdatn.besellphone.Repositories;

import com.ductrongdatn.besellphone.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
