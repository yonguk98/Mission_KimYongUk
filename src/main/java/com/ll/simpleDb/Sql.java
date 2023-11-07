package com.ll.simpleDb;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sql {
    private SimpleDb simpleDb;
    private StringBuilder sb;
    private Connection connection;
    private List<Object> objectList;
    private List<Long> longList;

    public Sql(SimpleDb simpleDb) {
        this.simpleDb = simpleDb;
        this.sb = new StringBuilder();
        try {
            this.connection = simpleDb.connection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.objectList = new ArrayList<>();
        this.longList = new ArrayList<>();

    }

    public Sql append(String sql, Object... objects) {
        sb.append(sql + " ");
        for (Object object : objects) {
            objectList.add(object);
        }
        return this;
    }

    public Sql appendIn(String sql, List<Long> list){
        String s = "?";
        for (int i = 0; i < list.size()-1; i++) {
            s = s.concat(", ?");
        }
        sb.append(sql.replaceFirst("\\?",s) + " ");
        objectList.add(list);
        longList.addAll(list);
        return this;
    }

    public long insert(){
        long id = 0L;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
            for (int i = 0; i < objectList.size(); i++) {
                preparedStatement.setObject(i+1,objectList.get(i));
            }
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.executeQuery("select * from article");
            while(rs.next()){
                id = rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    public long update(){
        long count = -1;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
            for (int i = 0; i < objectList.size(); i++) {
                preparedStatement.setObject(i+1,objectList.get(i));
            }
            preparedStatement.executeUpdate();
            count = preparedStatement.getUpdateCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    public long delete(){
        long count = -1;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
            for (int i = 0; i < objectList.size(); i++) {
                preparedStatement.setObject(i+1,objectList.get(i));
            }
            preparedStatement.executeUpdate();
            count = preparedStatement.getUpdateCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    public LocalDateTime selectDatetime(){
        LocalDateTime localDateTime=null;
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString());
            if(rs.next()){
                localDateTime = rs.getObject(1, LocalDateTime.class);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return localDateTime;
    }
    public long selectLong(){
        long id = -1;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
            int j = 0;
            for (int i = 0; i < objectList.size(); i++) {
                if(objectList.get(i) instanceof List<?>){
                    for (Long l: longList) {
                        preparedStatement.setObject(i+j+1,l);
                        j++;
                    }
                }else{
                    preparedStatement.setObject(i+j+1, objectList.get(i));
                }
            }
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                id = rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }
    public List<Long> selectLongs(){
        List<Long> list = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
            int j = 0;
            for (Long l: longList) {
                preparedStatement.setObject(j+1,l);
                j++;
            }
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                list.add(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public String selectString(){
        String resultString = "";
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString());
            if(rs.next()){
                resultString = rs.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultString;
    }

    public Map<String ,Object> selectRow(){
        Map<String,Object> map = new HashMap<>();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
            for (int i = 0; i < objectList.size(); i++) {
                preparedStatement.setObject(i+1,objectList.get(i));
            }
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columns = metaData.getColumnCount();
            rs.next();
            for (int i = 0; i < columns; i++) {
                map.put(metaData.getColumnName(i + 1), rs.getObject(i + 1));
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public Article selectRow(Class c){
        Map<String,Object> map = new HashMap<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString());
            ResultSetMetaData metaData = rs.getMetaData();
            int columns = metaData.getColumnCount();
            while(rs.next()){
                for (int i = 0; i < columns; i++) {
                    map.put(metaData.getColumnName(i + 1), rs.getObject(i + 1));
                }
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Article(map);
    }


    public List<Article> selectRows(Class c){
        Map<String,Object> map = new HashMap<>();
        List<Article> list = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sb.toString());
            ResultSetMetaData metaData = rs.getMetaData();
            int columns = metaData.getColumnCount();
            while(rs.next()){
                for (int i = 0; i < columns; i++) {
                    map.put(metaData.getColumnName(i + 1), rs.getObject(i + 1));
                }
                list.add(new Article(map));
                map.clear();
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
