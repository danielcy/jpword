package com.cynb.jpword.data;

import java.util.Stack;

public class GlobalManager {
    public static int currentLibrary = 1;
    public static Stack<Word> cancelDelWordCache = new Stack<>();
}
