package com.pyx.crowd.handler;

import com.pyx.crowd.po.OrderProjectPO;
import com.pyx.crowd.service.api.OrderService;
import com.pyx.crowd.util.ResultEntity;
import com.pyx.crowd.vo.AddressVO;
import com.pyx.crowd.vo.OrderProjectVO;
import com.pyx.crowd.vo.OrderVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class OrderProviderHandler {

    @Resource
    private OrderService orderService;

    @RequestMapping("save/order/remote")
    ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVO){
        try {
            orderService.saveOrder(orderVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }

    @RequestMapping("/save/address/remote")
    ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO){
        try {
            orderService.saveAddress(addressVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/get/order/project/vo/remote")
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(@RequestParam("projectId") Integer projectId,
                                                         @RequestParam("returnId") Integer returnId) {

        try {
            OrderProjectVO orderProjectVO = orderService.getOrderProjectVO(projectId, returnId);
            return ResultEntity.successWithData(orderProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }

    @RequestMapping("get/address/vo/remote")
    ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberLoginVOId") Integer memberLoginVOId) {
        try {
            List<AddressVO> addressVOList = orderService.getAddressVOList(memberLoginVOId);
            return ResultEntity.successWithData(addressVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
}
