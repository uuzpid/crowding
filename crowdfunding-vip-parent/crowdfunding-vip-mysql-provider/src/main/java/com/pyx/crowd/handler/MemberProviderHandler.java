package com.pyx.crowd.handler;

import com.pyx.crowd.constant.CrowdConstant;
import com.pyx.crowd.mapper.MemberPOMapper;
import com.pyx.crowd.po.MemberPO;
import com.pyx.crowd.po.MemberPOExample;
import com.pyx.crowd.service.api.MemberService;
import com.pyx.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberProviderHandler {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO){

        try {

            memberService.saveMember(memberPO);
            return ResultEntity.successWithoutData();

        }catch (Exception e){
            if(e instanceof DuplicateKeyException){
                return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/get/memberPO/by/login/acct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginacct){

        try {
            // 如果没有查询到结果 返回空集合 get0会有异常
            MemberPO memberPO= memberService.getMemberPOByLoginAcct(loginacct);
            return ResultEntity.successWithData(memberPO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }
}
