package com.pyx.crowd.handler;

import com.pyx.crowd.api.MySQLRemoteService;
import com.pyx.crowd.api.RedisRemoteService;
import com.pyx.crowd.config.ShortMessageProperties;
import com.pyx.crowd.constant.CrowdConstant;
import com.pyx.crowd.vo.MemberLoginVO;
import com.pyx.crowd.po.MemberPO;
import com.pyx.crowd.vo.MemberVO;
import com.pyx.crowd.util.CrowdUtil;
import com.pyx.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
public class MemberHandler {

    @Autowired
    private ShortMessageProperties shortMessageProperties;

    @Resource
    private RedisRemoteService redisRemoteService;

    @Resource
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping("/auth/member/do/login")
    public String login(@RequestParam(value = "loginacct") String loginacct,
                        @RequestParam(value = "userpswd") String userpswd,
                        ModelMap modelMap,
                        HttpSession httpSession){
        // 根据登录账号查询该用户是否注册过（存在）
        ResultEntity<MemberPO> resultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);
        if(ResultEntity.FAILED.equals(resultEntity.getResult())){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,resultEntity.getMessage());
            return "member-login";
        }
        MemberPO memberPO = resultEntity.getData();
        if(memberPO==null){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        String dataBasePswd = memberPO.getUserpswd();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 盐值是随机的 每次加密出来属性不一样。因此不能再把密码加密和数据库中加密过得密码比较
        // String encode = passwordEncoder.encode(userpswd);
        // matches方法用相同盐值比较
        boolean matches = passwordEncoder.matches(userpswd, dataBasePswd);

        if(!matches){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        // 如果密码正确，创建MemberLoginVO对象存入Session域
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());
        httpSession.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER,memberLoginVO);
        return "redirect:http://localhost/auth/member/to/center/page";
    }


    /**
     * 注册功能
     * @param memberVO
     * @param modelMap
     * @return
     */
    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap){

        // 获取用户输入的手机号
        String phoneNum = memberVO.getPhoneNum();
        // 拼redis中存储验证码的key
        String code = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
        // 从redis中读取key对应的value
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKeyRemote(code);

        // 检查查询操作是否有效
        String result = resultEntity.getResult();
        if(ResultEntity.FAILED.equals(result)){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,resultEntity.getMessage());
            return "member-reg";
        }
        String redisCode = resultEntity.getData();
        if(redisCode==null){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-reg";
        }
        // 如果能够从redis查询到value则比较表单验证码和redis中的验证码。
        String formCode = memberVO.getCode();
        if(!Objects.equals(formCode,redisCode)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_CODE_INVALID);
            return "member-reg";
        }

        // 执行密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(memberVO.getUserpswd());
        // 将加密完成的密码重新封装进memberVO
        memberVO.setUserpswd(encode);
        // 执行保存操作
        MemberPO memberPO = new MemberPO();
        BeanUtils.copyProperties(memberVO, memberPO);
        ResultEntity<String> saveResultEntity = mySQLRemoteService.saveMember(memberPO);

        // 注册时失败
        if(ResultEntity.FAILED.equals(saveResultEntity.getResult())){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveResultEntity.getMessage());
            return "member-reg";
        }
        // 注册成功，才到这里
        // 如果验证码一致，则删除redis中的验证码
        redisRemoteService.removeRedisKeyRemote(code);
        // 使用重定向避免刷新浏览器导致重新执行注册流程
        return "redirect:/auth/member/to/login/page";
    }

    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum")String phoneNum){
        // 发送验证码到phoneNum这个手机
        ResultEntity<String> sendMessageResultEntity = CrowdUtil.sendShortMessage(
                shortMessageProperties.getHost(),
                shortMessageProperties.getPath(),
                shortMessageProperties.getMethod(),
                phoneNum,
                shortMessageProperties.getAppCode(),
                shortMessageProperties.getTemplateId());
        // 判断短信发送结果
        if(ResultEntity.SUCCESS.equals(sendMessageResultEntity.getResult())){
            // 如果发送成功 则将验证码存入redis
            String code = sendMessageResultEntity.getData();
            String key = CrowdConstant.REDIS_CODE_PREFIX+phoneNum;
            ResultEntity<String> saveCodeResultEntity =
                    redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code, 15, TimeUnit.MINUTES);
            if(ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())){
                return ResultEntity.successWithoutData();
            } else {
                return saveCodeResultEntity;
            }
        } else {
            return sendMessageResultEntity;
        }
    }
}
