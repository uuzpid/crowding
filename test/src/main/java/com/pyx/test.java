package com.pyx;

import java.util.Scanner;

public class test {
    public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            for(int i = 0;i<3;i++){
                int num = scanner.nextInt();
                System.out.println(num);
                System.out.println("====");
                scanner.nextLine();
                String s = scanner.nextLine();
                System.out.println(s);
                System.out.println("---------");
            }
            scanner.close();
    }
}
