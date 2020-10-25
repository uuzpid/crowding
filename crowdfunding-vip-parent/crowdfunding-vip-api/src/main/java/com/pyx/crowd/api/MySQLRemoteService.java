package com.pyx.crowd.api;

import com.pyx.crowd.po.MemberPO;
import com.pyx.crowd.util.ResultEntity;
import com.pyx.crowd.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "crowd-mysql")
public interface MySQLRemoteService {

    @RequestMapping("/get/memberPO/by/login/acct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginacct);

    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO);

    @RequestMapping("/save/project/vo/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO,@RequestParam("memberId") Integer memberId);

    @RequestMapping("/get/portal/type/project/data/remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote();

    @RequestMapping("/get/project/detail/remote/{id}")
    ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("id")Integer id);

    /*@RequestMapping("/get/order/project/vo/remote/{projectId}/{returnId}") //两种写法都可以
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(@PathVariable("projectId")Integer projectId,
                                                         @PathVariable("returnId")Integer returnId);*/
    @RequestMapping("/get/order/project/vo/remote")
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(@RequestParam("projectId")Integer projectId,
                                                         @RequestParam("returnId")Integer returnId);
    @RequestMapping("/get/address/vo/remote")
    ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberLoginVOId") Integer memberLoginVOId);

    @RequestMapping("/save/address/remote")
    ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO);

    @RequestMapping("save/order/remote")
    ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVO);
}
