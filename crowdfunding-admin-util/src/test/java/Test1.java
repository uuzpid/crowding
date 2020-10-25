import com.pyx.crowd.util.CrowdUtil;
import com.pyx.crowd.util.ResultEntity;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;



public class Test1 {

    public static void main(String[] args) {
        Single single = Single.getSingle();
        System.out.println(single.count1);
        System.out.println(single.count2);
    }
}
class Single{
    public static int count1;
    private static Single single = new Single();
    public static int count2=0;
    private Single(){
        count1++;
        count2++;

    }
    public static Single getSingle(){
        return single;
    }

    @Test
    public void test2() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream("123.jpg");

        ResultEntity<String> resultEntity = CrowdUtil.uploadFileToOss("http://oss-cn-shanghai.aliyuncs.com",
                "LTAI4G7CLbhrfYkQmgpZbsvD",
                "mJ5FcU6cy1YNoGNnyTp0exVR57FjUc",
                inputStream,
                "pyx20201011",
                "http://pyx20201011.oss-cn-shanghai.aliyuncs.com",
                "123.jpg");
        System.out.println(resultEntity);
    }
}
