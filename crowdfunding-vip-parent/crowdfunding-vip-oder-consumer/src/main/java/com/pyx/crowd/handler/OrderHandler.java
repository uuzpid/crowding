package com.pyx.crowd.handler;

import com.pyx.crowd.api.MySQLRemoteService;
import com.pyx.crowd.constant.CrowdConstant;
import com.pyx.crowd.util.ResultEntity;
import com.pyx.crowd.vo.AddressVO;
import com.pyx.crowd.vo.MemberLoginVO;
import com.pyx.crowd.vo.OrderProjectVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderHandler {

    @Resource
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO,
                              HttpSession session){
        ResultEntity<String> resultEntity = mySQLRemoteService.saveAddressRemote(addressVO);
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        Integer returnCount = orderProjectVO.getReturnCount();

        // 重定向避免表单重复提交 重新进入确认订单页面
        return "redirect:http://localhost/order/confirm/order/"+returnCount;
    }

    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(@PathVariable("returnCount")Integer returnCount,
                                       HttpSession session){
        // 把接收到的回报数量合并到session域
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        orderProjectVO.setReturnCount(returnCount);
        session.setAttribute("orderProjectVO",orderProjectVO);
        
        // 获取当前的用户
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberLoginVOId = memberLoginVO.getId();


        // 查询目前的收货地址数据
        ResultEntity<List<AddressVO>> resultEntity = mySQLRemoteService.getAddressVORemote(memberLoginVOId);

        if(ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            List<AddressVO> addressVOList = resultEntity.getData();
            session.setAttribute("addressVOList",addressVOList);
        }
        return "confirm_order";
    }

    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(@PathVariable("projectId")Integer projectId,
                                        @PathVariable("returnId")Integer returnId,
                                        HttpSession session){

        ResultEntity<OrderProjectVO> resultEntity = mySQLRemoteService.getOrderProjectVORemote(projectId,returnId);

        if(ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            OrderProjectVO orderProjectVO = resultEntity.getData();
            // 这里不使用model因为确认回报页面只提交订单需要的金额，其他信息不会进行提交，为了下个页面可以知道该信息。存入session中
            /*model.addAttribute("orderProjectVO",orderProjectVO);*/
            session.setAttribute("orderProjectVO",orderProjectVO);
        }
        return "confirm_return";
    }
}
