package com.cynb.jpword.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Word {
    private int id;

    private String chinese;

    private String japanese;

    private int category;

    private int learningStatus;

    private int libId;

    public Word(String chinese, String japanese, int category, int learningStatus, int libId) {
        this.chinese = chinese;
        this.japanese = japanese;
        this.category = category;
        this.learningStatus = learningStatus;
        this.libId = libId;
    }
}
