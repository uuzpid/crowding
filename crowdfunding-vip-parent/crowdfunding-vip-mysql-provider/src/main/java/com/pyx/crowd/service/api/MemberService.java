package com.pyx.crowd.service.api;


import com.pyx.crowd.po.MemberPO;

public interface MemberService {
    MemberPO getMemberPOByLoginAcct(String loginacct);

    void saveMember(MemberPO memberPO);
}
