import com.pyx.crowd.util.CrowdUtil;
import org.junit.Test;

public class StringTest {
    @Test
    public void testMd5(){
        String source = "123123";
        String md5 = CrowdUtil.md5(source);
        System.out.println(md5);
    }
}
