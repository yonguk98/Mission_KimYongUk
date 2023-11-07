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


    private PreparedStatement createState(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
            return preparedStatement;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setObject(PreparedStatement st){
        try {
            for (int i = 0; i < objectList.size(); i++) {
                st.setObject(i + 1, objectList.get(i));
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int setObjectList(PreparedStatement preparedStatement,int i, int j){
        for (Long l: longList) {
            try {
                preparedStatement.setObject(i+j+1,l);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            j++;
        }
        return j;
    }

    private void executeUpdate(PreparedStatement st){
        try {
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ResultSet executeQuery(PreparedStatement st, String sql){
        ResultSet rs;
        try {
            rs = st.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }
    private ResultSet executeQuery(PreparedStatement st){
        ResultSet rs;
        try {
            rs = st.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }
    private boolean next(ResultSet rs){
        try {
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private long getLong(ResultSet rs){
        try {
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private long getUpdateCount(PreparedStatement st){
        try {
            return st.getUpdateCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private String getString(ResultSet rs){
        String result="";
        try{
            if(rs.next()){
                return rs.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

//    private <T> T getObject(Class<T> c){
//
//    }

    public long insert(){

        PreparedStatement preparedStatement = createState();
        setObject(preparedStatement);
        executeUpdate(preparedStatement);
        ResultSet rs = executeQuery(preparedStatement, "select * from article");

        while(next(rs)){
            return getLong(rs);
        }
        return -1L;
    }

    public long update(){
        PreparedStatement preparedStatement = createState();
        setObject(preparedStatement);
        executeUpdate(preparedStatement);
        return getUpdateCount(preparedStatement);
    }

    public long delete(){
        PreparedStatement preparedStatement = createState();
        setObject(preparedStatement);
        executeUpdate(preparedStatement);
        return getUpdateCount(preparedStatement);
    }

    public LocalDateTime selectDatetime(){
        LocalDateTime localDateTime=null;
        PreparedStatement preparedStatement = createState();

        ResultSet rs = executeQuery(preparedStatement,sb.toString());

        if(next(rs)){
            try {
                localDateTime = rs.getObject(1, LocalDateTime.class);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return localDateTime;
    }
    public long selectLong(){
        PreparedStatement preparedStatement = createState();
        int j = 0;
        for (int i = 0; i < objectList.size(); i++) {
            if (objectList.get(i) instanceof List<?>) {setObjectList(preparedStatement, i, j);}
            else {
                try {
                    preparedStatement.setObject(i + j + 1, objectList.get(i));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        ResultSet rs = executeQuery(preparedStatement);
        while(next(rs)){
            return getLong(rs);
        }
        return -1L;
    }
    public List<Long> selectLongs(){
        PreparedStatement preparedStatement = createState();
        setObjectList(preparedStatement,0,0);
        ResultSet rs = executeQuery(preparedStatement);

        List<Long> list = new ArrayList<>();
        while(next(rs)){
            list.add(getLong(rs));
        }
        return list;
    }

    public String selectString(){
        PreparedStatement preparedStatement = createState();
        ResultSet rs = executeQuery(preparedStatement);
        return getString(rs);
    }

    public Map<String ,Object> selectRow(){
        Map<String,Object> map = new HashMap<>();

        PreparedStatement preparedStatement = createState();
        setObject(preparedStatement);
        ResultSet rs = executeQuery(preparedStatement);

        try{
            ResultSetMetaData metaData = rs.getMetaData();
            int columns = metaData.getColumnCount();
            while(rs.next()) {
                for (int i = 0; i < columns; i++) {
                    map.put(metaData.getColumnName(i + 1), rs.getObject(i + 1));
                }
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public Article selectRow(Class c){
        return new Article(selectRow());
    }


    public List<Article> selectRows(Class c){
        Map<String,Object> map = new HashMap<>();
        List<Article> list = new ArrayList<>();

        PreparedStatement preparedStatement = createState();
        setObject(preparedStatement);
        ResultSet rs = executeQuery(preparedStatement);

        try{
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
