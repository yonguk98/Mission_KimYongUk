package com.ll.controller;

import com.ll.model.Command;

public class Request {
    public Command findCmd(String input){
        for (Command value : Command.values()) {
            if(input.startsWith(value.getKr())){
                return value;
            }
        }
        return Command.None;
    }
    public Long getIdParam(String input){
        int eqIndex = input.indexOf("=");
        if(eqIndex==-1){
            return -1L;
        }else{
            try{
                return Long.parseLong(input.substring(eqIndex+1,input.length()));
            } catch (Exception e){
                return -1L;
            }
        }
    }

}
