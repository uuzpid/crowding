package com.pyx.crowd.service.impl;

import com.pyx.crowd.mapper.AddressPOMapper;
import com.pyx.crowd.mapper.OrderPOMapper;
import com.pyx.crowd.mapper.OrderProjectPOMapper;
import com.pyx.crowd.po.AddressPO;
import com.pyx.crowd.po.AddressPOExample;
import com.pyx.crowd.po.OrderPO;
import com.pyx.crowd.po.OrderProjectPO;
import com.pyx.crowd.service.api.OrderService;
import com.pyx.crowd.vo.AddressVO;
import com.pyx.crowd.vo.OrderProjectVO;
import com.pyx.crowd.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderProjectPOMapper orderProjectPOMapper;

    @Resource
    private OrderPOMapper orderPOMapper;

    @Resource
    private AddressPOMapper addressPOMapper;


    @Override
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {
        return orderProjectPOMapper.selectOrderProjectVO(returnId);
    }

    @Override
    public List<AddressVO> getAddressVOList(Integer memberLoginVOId) {
        AddressPOExample example = new AddressPOExample();
        example.createCriteria().andMemberIdEqualTo(memberLoginVOId);
        List<AddressPO> addressPOList = addressPOMapper.selectByExample(example);
        List<AddressVO> addressVOList = new ArrayList<>();
        for (AddressPO addressPO : addressPOList) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO,addressVO);
            addressVOList.add(addressVO);
        }
        return addressVOList;
    }

    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void saveAddress(AddressVO addressVO) {
        AddressPO addressPO = new AddressPO();
        BeanUtils.copyProperties(addressVO,addressPO);
        addressPOMapper.insert(addressPO);
    }

    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void saveOrder(OrderVO orderVO) {
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO,orderPO);
        OrderProjectPO orderProjectPO = new OrderProjectPO();
        BeanUtils.copyProperties(orderVO.getOrderProjectVO(),orderProjectPO);
        orderPOMapper.insert(orderPO);
        /*
            由于t_order_project表中需要保存orderid。
            而orderid又是在保存完order后才会自增生成的，因此先取出orderid再保存
            要在mapper中的sql语句内设置useGeneratedKeys="true" keyProperty="id"
         */
        Integer id = orderPO.getId();
        orderProjectPO.setOrderId(id);
        orderProjectPOMapper.insert(orderProjectPO);
    }
}
