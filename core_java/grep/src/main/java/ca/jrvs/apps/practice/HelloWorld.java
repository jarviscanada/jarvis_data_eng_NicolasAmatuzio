package ca.jrvs.apps.practice;


import java.util.ArrayList;

class HelloWorld {
    //Your program begins with a call to main().
    //Prints "Hello, World!" to the terminal window
    public static void main(String[] args) {
        ArrayList<Integer> li = new ArrayList<Integer>();
        System.out.println("Hello World!");

        RegexExcImp m = new RegexExcImp();
        System.out.println(m.isEmptyLine("Hello World!"));
        System.out.println(m.isEmptyLine(""));
        System.out.println(m.isEmptyLine(" "));
        System.out.println(m.isEmptyLine("    "));
    }
}