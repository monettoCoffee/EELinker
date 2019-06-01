package eel.orm.core;

import eel.entry.Node;
import eel.orm.entry.Function;
import eel.orm.sqlSession.SqlSession;
import eel.utils.ReflactUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

/**
 * @author monetto
 */
public class Executor {

    private SqlSession sqlSessionProxy;

    public Executor(SqlSession sqlSession){
        this.sqlSessionProxy = sqlSession;
    }

    public <T> T selectOne(String id, Object parameter, Connection connection){
        Function function = MapperReflact.getSelectSql(id);
        if (function == null){
            System.out.println("ORM Logger : Unknown Mapper "+ id);
            return null;
        }
        String resultType = function.getResultType();
        try {
            String sql = function.getSql();
            Map<String, String> parameterMap = ReflactUtil.getFieldAndValue(parameter);
            sql = fillParameter(sql, parameterMap);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            Object resultObject = Class.forName(resultType).newInstance();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            if (!resultSet.next()){
                // 没有查到记录
                return null;
            }

            int allValueNum = resultSetMetaData.getColumnCount();
            for(int index = 1 ;index <= allValueNum ;){
                String s = resultSetMetaData.getColumnName(index++);
                Class clazz = ((T)resultObject).getClass();
                Field nameField = clazz.getDeclaredField(s);
                nameField.setAccessible(true);
                nameField.set(resultObject, resultSet.getString(s));
            }

            if (resultObject == null){
                System.out.println("ORM Logger : No Record Mapping");
            }
            this.sqlSessionProxy.setUseFalse();
            return (T)resultObject;
        } catch (Exception e){
            System.out.println("ORM Logger : Execute SQL Exception");
            e.printStackTrace();
            this.sqlSessionProxy.setUseFalse();
            return null;
        }
    }

    public <T> T selectList(String id,Object parameter,Connection connection){
        Function function = MapperReflact.getSelectSql(id);
        if (function == null){
            System.out.println("ORM Logger : Unknown Mapper "+ id);
            this.sqlSessionProxy.setUseFalse();
            return null;
        }
        String resultType = function.getResultType();
        LinkedList<Object> list = new LinkedList<Object>();
        try {
            String sql = function.getSql();
            Boolean hasResult;
            Map<String, String> parameterMap = ReflactUtil.getFieldAndValue(parameter);
            sql = fillParameter(sql, parameterMap);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            while(hasResult = resultSet.next()){
                if (!hasResult){
                    this.sqlSessionProxy.setUseFalse();
                    return (T)list;
                }

                Object resultObject;
                int allValueNum = resultSetMetaData.getColumnCount();
                if ("String".equals(resultType)) {
                    resultObject = resultSet.getString(1);
                    list.add(resultObject);
                } else {
                    resultObject = Class.forName(resultType).newInstance();
                    for(int index = 1 ;index <= allValueNum ;){
                        String s = resultSetMetaData.getColumnName(index++);
                        Class clazz = ((T)resultObject).getClass();
                        Field nameField = clazz.getDeclaredField(s);
                        nameField.setAccessible(true);
                        nameField.set(resultObject, resultSet.getString(s));
                    }
                    list.add(resultObject);
                }
            }

            if (list.size() == 0){
                System.out.println("ORM Logger : No Record Mapping");
            }
            this.sqlSessionProxy.setUseFalse();
            return (T)list;
        } catch (Exception e){
            System.out.println("ORM Logger : Execute SQL Exception");
            e.printStackTrace();
            this.sqlSessionProxy.setUseFalse();
            return null;
        }
    }

    public void doSql(String id,Object parameter,Connection connection, String type){
        Function function = MapperReflact.getSql(id, type);
        if (function == null){
            System.out.println("ORM Logger : Unknown Mapper "+ id);
            return ;
        }
        try {
            String sql = function.getSql();
            Map<String, String> parameterMap = ReflactUtil.getFieldAndValue(parameter);
            sql = fillParameter(sql, parameterMap);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (Exception e){
            e.printStackTrace();
        }
        this.sqlSessionProxy.setUseFalse();

    }

    public String fillParameter(String _sql, Map<String, String> parameterMap) {
        StringBuffer sql = new StringBuffer(_sql);
        // Replace special character, key:index value: character
        LinkedList<Node<Integer, Character>> originCharacterList = new LinkedList<>();
        while (true) {
            int parameterLeftEdge = sql.indexOf("${");
            if (parameterLeftEdge == -1) {
                break;
            }

            int parameterRightEdge = sql.indexOf("}");
            if (parameterRightEdge > parameterLeftEdge){
                String sqlParameterKey = sql.substring(parameterLeftEdge+2, parameterRightEdge);
                String sqlParameterValue = parameterMap.get(sqlParameterKey);
                if (sqlParameterValue == null){
                    sqlParameterValue = sql.substring(parameterLeftEdge, parameterRightEdge+1);
                    System.out.println("ORM Logger : Unknown SQL Mapper Value " + sqlParameterValue);
                }
                sqlParameterValue = "\"" + sqlParameterValue + "\"";
                // replace value from Param Object Reflact Map
                sql = sql.replace(parameterLeftEdge, parameterRightEdge+1, sqlParameterValue);
                checkSpecialSqlParameterCharater(sql, sqlParameterValue, parameterLeftEdge, originCharacterList);
            } else {
                break;
            }
        }

        for(Node<Integer, Character> node: originCharacterList) {
            int replaceIndex = node.getKey();
            sql.replace(replaceIndex, replaceIndex+1, node.getValue().toString());
        }

        return sql.toString();
    }

    private void checkSpecialSqlParameterCharater(StringBuffer sql,
                                                  String sqlParameterValue,
                                                  int parameterLeftEdge,
                                                  List originCharacterList) {
        saveSqlOriginalCharacter(sql, sqlParameterValue, parameterLeftEdge, originCharacterList, '$');
        saveSqlOriginalCharacter(sql, sqlParameterValue, parameterLeftEdge, originCharacterList, '{');
        saveSqlOriginalCharacter(sql, sqlParameterValue, parameterLeftEdge, originCharacterList, '}');
    }

    private void saveSqlOriginalCharacter(StringBuffer sql,
                                          String _sqlParameterValue,
                                          int parameterLeftEdge,
                                          List<Node<Integer, Character>> originCharacterList, char replacement) {
        int index;
        StringBuffer sqlParameterValue = new StringBuffer(_sqlParameterValue);
        while ( (index = sqlParameterValue.indexOf("" + replacement)) != -1) {
            sqlParameterValue = sqlParameterValue.replace(index, index+1, "@");
            int replaceIndex = parameterLeftEdge + index;
            // parameterLeftEdge: replace begin index
            // index: replace value spacial character index
            originCharacterList.add(new Node<>(replaceIndex, replacement));
            sql.replace(replaceIndex, replaceIndex+1 ,"@");
        }
    }

}
