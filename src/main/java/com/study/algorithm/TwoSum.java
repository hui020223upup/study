package com.study.algorithm;

import java.util.HashMap;

/**
 * Created by wanghh on 2018-8-9.
 */
public class TwoSum {
    public static int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                result[0] = map.get(nums[i]);
                result[1] = i;
                return result;
            }
            map.put(target - nums[i],i);
        }
        return result;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{2,7,11,15};
        int[] result = twoSum(nums, 9);
        System.out.println(result[0] + "," + result[1]);
    }
}
