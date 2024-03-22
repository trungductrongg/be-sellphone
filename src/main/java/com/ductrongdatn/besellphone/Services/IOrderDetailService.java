package com.ductrongdatn.besellphone.Services;

import com.ductrongdatn.besellphone.DTO.OrderDetailDTO;
import com.ductrongdatn.besellphone.Exceptions.DataNotFoundException;
import com.ductrongdatn.besellphone.Models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception;
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData)
            throws DataNotFoundException;
    void deleteById(Long id);
    List<OrderDetail> findByOrderId(Long orderId);
}
