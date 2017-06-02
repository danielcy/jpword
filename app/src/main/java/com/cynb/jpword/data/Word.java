package com.cynb.jpword.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Word {
    private int id;

    private String chinese;

    private String japanese;

    private String announce;

    private int category;

    private int learningStatus;

    private int libId;

    public Word(int id, String chinese, String japanese, String announce, int category, int learningStatus, int libId) {
        this.id = id;
        this.chinese = chinese;
        this.japanese = japanese;
        this.announce = announce;
        this.category = category;
        this.learningStatus = learningStatus;
        this.libId = libId;
    }
}
