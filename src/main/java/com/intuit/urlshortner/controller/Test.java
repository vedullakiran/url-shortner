package com.intuit.urlshortner.controller;

import org.apache.tomcat.util.buf.CharChunk;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Test {

    /*
    max = 0
    ))()()   (((()()())))

    (,(,(,
    2+2+2

    (valid, max)
    {(true, 2)} {true,2}  {

    ((

    }
    for(int i=0; i<n; i++) {
        while(stack not empty) {
            if(
        }
    }
    max 4

    ((()()))()()

    (,(,(
    2+2+2
     */

    public static int getMaxValid(String s) {
        Stack<Character> stack = new Stack<>();
        int i=0;
        int maxValid = 0;
        int maxC = 0;

        while (i<s.length()) {
            if (s.charAt(i) == ')' && stack.empty()) {
                maxValid = Math.max(maxC, maxValid);
                maxC = 0;
            }

            if(s.charAt(i) == '(') {
                stack.push('(');
            }

            if(s.charAt(i) == ')' && !stack.empty() && stack.peek() == '(') {
                maxC+=2;
                stack.pop();
            }
            i++;
        }

        return  Math.max(maxC, maxValid);
    }

    public static  void  main(String [] args) {
        System.out.println(getMaxValid("))(()((("));
    }
}