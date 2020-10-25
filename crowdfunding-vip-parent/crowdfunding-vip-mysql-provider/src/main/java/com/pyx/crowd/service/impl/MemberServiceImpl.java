package com.pyx.crowd.service.impl;

import com.pyx.crowd.mapper.MemberPOMapper;
import com.pyx.crowd.po.MemberPO;
import com.pyx.crowd.po.MemberPOExample;
import com.pyx.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

    @Resource
    private MemberPOMapper memberPOMapper;

    @Override
    public MemberPO getMemberPOByLoginAcct(String loginacct) {

        MemberPOExample memberPOExample = new MemberPOExample();
        MemberPOExample.Criteria criteria = memberPOExample.createCriteria().andLoginacctEqualTo(loginacct);
        List<MemberPO> memberPOS = memberPOMapper.selectByExample(memberPOExample);
        // 如果没有查询到结果 返回空集合 get0会有异常
        // MemberPO memberPO = memberPOS.get(0);
        if(memberPOS == null || memberPOS.size()==0){
            return null;
        }
        return memberPOS.get(0);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class,readOnly = false)
    @Override
    public void saveMember(MemberPO memberPO) {
        memberPOMapper.insertSelective(memberPO);
    }
}
