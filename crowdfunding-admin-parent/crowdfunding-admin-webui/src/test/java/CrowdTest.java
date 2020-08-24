import com.pyx.crowd.entity.Admin;
import com.pyx.crowd.entity.Role;
import com.pyx.crowd.mapper.AdminMapper;
import com.pyx.crowd.mapper.RoleMapper;
import com.pyx.crowd.service.api.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

//Spring整合junit
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testTx(){
        Admin admin = new Admin(null, "jerry", "123456", "杰瑞", "jerry@qq.com", null);
        adminService.saveAdmin(admin);
    }
    @Test
    public void testlog(){
        //这里传入的class对象就是当前打印日志的类
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);
        //根据不同的日志级别打印日志
        logger.debug("Hello");
        logger.debug("Hello");
        logger.debug("Hello");

        logger.info("Info");
        logger.info("Info");
        logger.info("Info");

        logger.warn("warn");
    }

    @Test
    public void test1(){
        Admin admin = new Admin(null, "tom", "123123", "汤姆", "tom@qq.com", null);
        int insert = adminMapper.insert(admin);
        System.out.println(insert);
    }

    @Test
    public void test() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @Test
    public void test2(){
        for(int i = 2;i<238;i++){
            adminMapper.insert(new Admin(null,"loginAcct"+i,"userPswd"+i,"userName"+i,"email"+i,null));
        }
    }

    @Test
    public void test3(){
        for(int i = 1;i<235;i++){
            roleMapper.insert(new Role(null,"roleName"+i));
        }
    }
}

