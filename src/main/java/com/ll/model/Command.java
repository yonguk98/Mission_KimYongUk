package com.ll.model;

public enum Command {
    List("목록"),Add("등록"),Update("수정"),Delete("삭제"),Quit("종료"),None("오류");

    private final String kr;

    Command(String kr) {
        this.kr = kr;
    }

    public String getKr(){
        return kr;
    }
}
