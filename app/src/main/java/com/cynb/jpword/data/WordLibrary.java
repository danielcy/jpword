package com.cynb.jpword.data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WordLibrary {
    private int id;

    private String libName;

    private int markWordId;

    public WordLibrary(int id, String libName, int markWordId){
        this.id = id;
        this.libName = libName;
        this.markWordId = markWordId;
    }
}
