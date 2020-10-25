package com.pyx.crowd.service.api;

import com.pyx.crowd.vo.AddressVO;
import com.pyx.crowd.vo.OrderProjectVO;
import com.pyx.crowd.vo.OrderVO;

import java.util.List;

public interface OrderService {
    OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId);

    List<AddressVO> getAddressVOList(Integer memberLoginVOId);

    void saveAddress(AddressVO addressVO);

    void saveOrder(OrderVO orderVO);
}
