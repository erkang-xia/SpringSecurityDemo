package com.example.springsecuritydemo;



import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List <Integer> nums = Arrays.asList(1, 3, 6, 8, 10, 18, 36);
        List <String> colors = Arrays.asList("RED", "grEEn", "white", "Orange", "pink","Red", "Green", "Blue", "Pink", "Brown");
        List <Integer> numsWithDuplicate = Arrays.asList(10, 23, 22, 23, 24, 24, 33, 15, 26, 15);

        // Calculate the average using streams
        double average = nums.stream().mapToDouble(n->n).average().orElse(0.0);
        List <String> upper = colors.stream().map(s->s.toUpperCase()).toList();
        int sumOfEven = nums.stream().filter(n->n%2==0).mapToInt(n->n).sum();
        List<Integer> uniqueNums = numsWithDuplicate.stream().distinct().toList();
        int colorStartWithR = (int) colors.stream().filter(s->s.startsWith("R")).count();
        List<String> sortedColors = colors.stream().sorted((String a,String b)->b.compareTo(a)).toList();
        Integer maxValue =  nums.stream().mapToInt(n->n).max().orElse(0);
        Integer secondLargest = nums.stream().sorted((Integer a, Integer b)->b.compareTo(a)).skip(1).findFirst().get();
        System.out.println(secondLargest);
        System.out.println("Average value of the said numbers: " + average);
    }
}