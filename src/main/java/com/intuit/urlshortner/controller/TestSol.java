package com.intuit.urlshortner.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSol {
    /*
            1
           /  \
           2    5
          /\   /\
          3 4  6 7
     */

    public static List<String> getLevelOrder(String pre) {
        Map<Integer, List<String>> levels = new HashMap<>();
        int maxDepth =0;
        for(int i=0; i<pre.length() ;) {
            int depth = 0;
            while(pre.charAt(i) == '-') {
                depth++;
                i++;
                maxDepth = Math.max(depth, maxDepth);
            }
            StringBuilder s = new StringBuilder();

            while (i<pre.length() && pre.charAt(i)!='-' ) {
                s.append(pre.charAt(i));
                i++;
            }
            levels.putIfAbsent(depth, new ArrayList<>());
            levels.get(depth).add(s.toString());

        }
        List<String> levelOrder = new ArrayList<>();
        for (int i=0; i<=maxDepth; i++) {
            List<String> l = levels.get(i);
            levelOrder.addAll(l);
        }
        return levelOrder;
    }

    public static void main(String[] args) {
        List<String> output = getLevelOrder("11-12--13--14-15--16--17");
        for (String c: output) {
            System.out.print(c + " ");
        }
    }
}
