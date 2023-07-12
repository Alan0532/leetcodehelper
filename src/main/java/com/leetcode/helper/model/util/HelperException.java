package com.leetcode.helper.model.util;

public class HelperException extends Exception {

    public HelperException(String message) {
        super("LeetCodeHelper ERROR: " + message);
    }

}
