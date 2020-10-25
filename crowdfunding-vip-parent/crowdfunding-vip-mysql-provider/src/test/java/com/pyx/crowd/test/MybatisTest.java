package com.pyx.crowd.test;

import com.pyx.crowd.mapper.MemberPOMapper;
import com.pyx.crowd.mapper.ProjectPOMapper;
import com.pyx.crowd.po.MemberPO;
import com.pyx.crowd.vo.DetailProjectVO;
import org.junit.Test;


import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Resource
    private ProjectPOMapper projectPOMapper;

    private Logger logger = LoggerFactory.getLogger(MybatisTest.class);

    @Test
    public void test222(){
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(11);
        logger.info(detailProjectVO.getProjectName());
        logger.info(detailProjectVO.getHeaderPicturePath());
        logger.info(detailProjectVO.getStatus()+"");
        logger.info(detailProjectVO.getDeployDate()+"");
        logger.info(detailProjectVO.getFollowerCount()+"");
        logger.info(detailProjectVO.getPercentage()+"");
    }


    @Test
    public void test3333(){
        Scanner scanner = new Scanner(System.in);
        for(int i = 0;i<3;i++){
            String s = scanner.nextLine();
            System.out.println(s);
            int num = scanner.nextInt();
            System.out.println(num);
        }
        scanner.close();
    }


    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        logger.debug(connection.toString());
    }

    @Test
    public void test1(){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String source = "123123";
        String encode = passwordEncoder.encode(source);

        MemberPO memberPO = new MemberPO(null, "jack", encode, "杰 克",
                "jack@qq.com", 1, 1,
                "杰克", "123123", 2);
        memberPOMapper.insert(memberPO);

    }
}
