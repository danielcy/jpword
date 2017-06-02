package com.cynb.jpword.data;

import java.util.List;
import java.util.Stack;

public class GlobalManager {
    public static int currentLibrary = 1;
    public static List<WordLibrary> wordLibs;
    public static Stack<Word> cancelDelWordCache = new Stack<>();
}
