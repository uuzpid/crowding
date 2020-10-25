public class Test {
    public static void main(String[] args) {
        Child.BBQ bbq = new Child.BBQ();
        Father.func2();
    }
}

class Father{
    {
        System.out.println("FB0");
    }
    static {
        System.out.println("FSB0....");
    }
    static int a = 1 ;

    static {
        System.out.println("FSB1 a:"+a);
    }

    int b = 1;

    static {
        a++;
        System.out.println("FSB2 a:"+a);
    }

    final static int c = 1;
    static {
        System.out.println("FSB3 c:"+c);
    }

    public Father(){
        a++;
        b++;
        System.out.println("F CONSTRUCTOR a:"+a+",b:"+b+",c:"+c+",d:"+d);
    }

    {
        a++;
        b++;
        System.out.println("FSB4 a:"+a+",b:"+b+",c:"+c);
    }

    final int d = 1;
    public void func1(){
        a++;
        b++;
        System.out.println("F func1 a:"+a+",b:"+b+",c:"+c+",d:"+d);
    }
    public static void func2(){
        a++;
        System.out.println("F func2 a:"+a+",c:"+c);
    }
}
class Child extends Father{
    {
        System.out.println("CB0");
    }
    static {
        System.out.println("CSB0....");
    }
    static int a = 1 ;

    static {
        System.out.println("CSB1 a:"+a);
    }
    int b = 1;

    static {
        a++;
        System.out.println("CSB2 a:"+a);
    }
    final static int c = 1;
    static {
        System.out.println("CSB3 c:"+c);
    }

    public Child(){
        a++;
        b++;
        System.out.println("C CONSTRUCTOR a:"+a+",b:"+b+",c:"+c+",d:"+d);
    }

    {
        a++;
        b++;
        System.out.println("CSB4 a:"+a+",b:"+b+",c:"+c);
    }

    final int d = 1;
    public void func1(){
        a++;
        b++;
        System.out.println("C func1 a:"+a+",b:"+b+",c:"+c+",d:"+d);
    }
    public static void func2(){
        a++;
        System.out.println("C func2 a:"+a+",c:"+c);
    }
    public static class BBQ{
        static {
            System.out.println("BBQ");
        }
    }
}