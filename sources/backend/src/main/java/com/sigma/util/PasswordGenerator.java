package com.sigma.util;

import java.util.Random;

public class PasswordGenerator {
	public static String generate() {
	    String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	    Random rnd = new Random();
	    int len = rnd.nextInt(5) + 8;
	    StringBuilder sb = new StringBuilder(len);
	    for (int i = 0; i < len; i++) {
	        sb.append(AB.charAt(rnd.nextInt(AB.length())));
	    }
	    return sb.toString();
	}
}
