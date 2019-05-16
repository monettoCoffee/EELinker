package cafe.orm.sqlSession;

import cafe.orm.entry.Function;
import cafe.utils.ReflactUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

public class Executor {
    private HashMap<String, Function> functions;
    private SqlSession sqlSessionProxy;

    public Executor(HashMap functions,SqlSession sqlSession){
        this.functions = functions;
        this.sqlSessionProxy = sqlSession;
    }

    public <T> T selectOne(String id, Object parameter,Connection connection){
        Function function;
        if ((function = this.functions.get(id)) == null){
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
            this.sqlSessionProxy.setUse(false);

            if (resultObject == null){
                System.out.println("ORM Logger : No Record Mapping");
            }

            return (T)resultObject;
        } catch (Exception e){
            System.out.println("ORM Logger : Execute SQL Exception");
            e.printStackTrace();
            this.sqlSessionProxy.setUse(false);
            return null;
        }
    }

    public <T> T selectList(String id,Object parameter,Connection connection){
        Function function;
        if ((function = this.functions.get(id)) == null){
            System.out.println("ORM Logger : Unknown Mapper "+ id);
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
                    return (T)list;
                }

                Object resultObject;
                int allValueNum = resultSetMetaData.getColumnCount();
                for(int index = 1 ;index <= allValueNum ;){
                    String s = resultSetMetaData.getColumnName(index++);
                    if ("String".equals(resultType)){
                        resultObject = resultSet.getString(s);
                        list.add(resultObject);
                    } else {
                        resultObject = Class.forName(resultType).newInstance();
                        Class clazz = ((T)resultObject).getClass();
                        Field nameField = clazz.getDeclaredField(s);
                        nameField.setAccessible(true);
                        nameField.set(resultObject, resultSet.getString(s));
                        list.add(resultObject);
                    }
                }
            }

            this.sqlSessionProxy.setUse(false);
            if (list.size() == 0){
                System.out.println("ORM Logger : No Record Mapping");
            }
            return (T)list;
        } catch (Exception e){
            System.out.println("ORM Logger : Execute SQL Exception");
            e.printStackTrace();
            this.sqlSessionProxy.setUse(false);
            return null;
        }

    }


    public void doSql(String id,Object parameter,Connection connection){
        Function function;
        if ((function = this.functions.get(id)) == null){
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
        this.sqlSessionProxy.setUse(false);
    }

    private String fillParameter(String sql, Map<String, String> fields){
        int length = sql.length();
        int sharpIndex = -1;
        String parameterValue;
        Set<String> parameterSet = new HashSet<>();
        for (int index = 0; index < length; ++index){
            if (sql.charAt(index)=='$'){
                sharpIndex = index;
            } else if (sql.charAt(index) == '}' && sharpIndex != -1){
                parameterSet.add(sql.substring(sharpIndex, index+1));
                sharpIndex = -1;
            }
        }

        for (String parameter : parameterSet){
            String parameterName = parameter.substring(2, parameter.length()-1);
            if ((parameterValue = fields.get(parameterName)) == null){
                System.out.println("ORM Logger : No SQL Parameter Mapping, Please Check Parameter Name");
                parameterValue = "";
            }
            sql = sql.replaceFirst("\\{" , "");
            sql = sql.replaceFirst("\\$" , "");
            sql = sql.replaceFirst(parameterName + "\\}" , "'" + parameterValue +"'");
        }
        return sql;
    }

}
