package com.ll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {

    public static void main(String[] args){
        int sequence = 1;
        Map<Integer,Say> map = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        try{
            map = load();
        } catch (Exception e) {
            System.out.println("cannot find save files.");
        }

        System.out.println("===명언 앱===");
        while(true){
            System.out.print("명령) ");
            String cmd = scanner.next();

            String[] cmdArr = cmd.split("\\?");
            String action = cmdArr[0];

            if(action.contains("종료")){ // 1단계
                System.exit(0);
            }
            if(action.contains("빌드")){
                save(map);
                System.out.println("save.json 파일의 내용이 갱신되었습니다.");
            }
            if(action.contains("등록")){ // 2단계
                System.out.print("명언 : ");
                String content = scanner.next();
                System.out.print("작가 : ");
                String writer = scanner.next();
                System.out.println(sequence + "번 명언이 등록되었습니다."); // 3단계

                map.put(sequence,new Say(sequence,content,writer));
                sequence++; // 4단계
            }
            if(action.contains("목록")){ // 5단계
                System.out.println("번호 / 작가 / 명언");
                System.out.println("-------------------");
                for (Say say : map.values()) {
                    System.out.println(say.getId()+" / "+say.getWriter()+" / "+say.getContent());
                }
            }
            if(action.contains("삭제")){ // 6,7 단계
                Integer id = null;
                try {
                    id = Integer.parseInt(cmdArr[1].split("id=",-1)[1].trim());
                }catch (Exception e){
                    System.out.println("id parsing error");
                }

                if(!id.equals(null)){
                    if(map.containsKey(id)) {
                        map.remove(id);
                        System.out.println(id + "번 명언이 삭제되었습니다.");

                    }else{
                        System.out.println(id + "번 명언은 존재하지 않습니다.");
                    }
                }
            }
            if(action.equals("수정")){ // 8단계
                Integer id = null;
                try {
                    id = Integer.parseInt(cmdArr[1].split("id=",-1)[1].trim());
                }catch (Exception e){
                    System.out.println("id parsing error");
                }
                if(map.containsKey(id)){
                    Say say = map.get(id);
                    System.out.println("명언(기존)" + say.getContent() );
                    System.out.print("명언 : ");
                    String content = scanner.next();
                    System.out.println("작가(기존)" + say.getWriter());
                    System.out.print("작가 : ");
                    String writer = scanner.next();
                    say.setContent(content);
                    say.setWriter(writer);
                }else {
                    System.out.println(id + "번 명언은 존재하지 않습니다.");
                }
            }

        }
    }

    public static String mapToJson(Map<Integer,Say> map) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Say> sayList = new ArrayList<>();
        for (Say data : map.values()) {
            sayList.add(data);
        }
        return objectMapper.writeValueAsString(sayList);
    }

    public static boolean save(Map<Integer, Say> map) {
        try {
            FileWriter fileWriter = new FileWriter("save.json");
            fileWriter.write(mapToJson(map));
            fileWriter.flush();
            fileWriter.close();
            return true;
        }catch (Exception e){
            System.out.println("save failed. try again");
            return false;
        }
    }
    public static Map<Integer,Say> load() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // json file to String
        String path = "save.json";
        String jsonFile = Files.readString(Path.of(path));

        // json string to List<Obj>
        List<Say> list = objectMapper.readValue(jsonFile, new TypeReference<List<Say>>() {});

        // Object List to Map
        Map<Integer,Say> map = new HashMap<>();
        for (Say data : list) {
            map.put(data.getId(),data);
        }
        return map;
    }
}