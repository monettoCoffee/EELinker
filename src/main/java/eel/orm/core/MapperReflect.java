package eel.orm.core;

import eel.orm.entry.Function;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author monetto
 */
public class MapperReflect {

    // Select SQL
    private static HashMap<String, Function> selectSqlMap = new HashMap<>();
    // Insert SQL
    private static HashMap<String, Function> insertSqlMap = new HashMap<>();
    // Delete SQL
    private static HashMap<String, Function> deleteSqlMap = new HashMap<>();
    // Update SQL
    private static HashMap<String, Function> updateSqlMap = new HashMap<>();
    // Undefined SQL
    private static HashMap<String, Function> undefinedSqlMap = new HashMap<>();
    // All sql id in this set to check same name
    private static HashSet<String> sameNameChecker = new HashSet<String>();

    public static void setSql(String sqlId, Function function, String sqlType) {
        checkSqlMapper(sqlId);
        switch (sqlType) {
            case "select":
                selectSqlMap.put(sqlId, function);
                break;
            case "insert":
                insertSqlMap.put(sqlId, function);
                break;
            case "update":
                updateSqlMap.put(sqlId, function);
                break;
            case "delete":
                deleteSqlMap.put(sqlId, function);
                break;
            default:
                undefinedSqlMap.put(sqlId, function);
        }
    }

    // CheckMapper, including but not limited to:
    // same name check.
    public static void checkSqlMapper(String sqlId) {
        if (sameNameChecker.contains(sqlId)) {
            System.out.println("ORM Logger : Same mapper " + sqlId + ", It could overwrite the original mapper");
        }
        sameNameChecker.add(sqlId);
    }

    public static Function getSelectSql(String sqlId){
        return selectSqlMap.get(sqlId);
    }

    public static Function getInsertSql(String sqlId){
        return insertSqlMap.get(sqlId);
    }

    public static Function getDeleteSql(String sqlId){
        return deleteSqlMap.get(sqlId);
    }

    public static Function getUpdateSql(String sqlId){
        return updateSqlMap.get(sqlId);
    }

    public static Function getUndefindedSql(String sqlId){
        return undefinedSqlMap.get(sqlId);
    }

    public static Function getUnknownTypeSql(String sqlId){
        Function function;
        if ( (function = selectSqlMap.get(sqlId)) != null)
            return function;
        if ( (function = insertSqlMap.get(sqlId)) != null)
            return function;
        if ( (function = updateSqlMap.get(sqlId)) != null)
            return function;
        if ( (function = deleteSqlMap.get(sqlId)) != null)
            return function;
        return undefinedSqlMap.get(sqlId);
    }

    public static Function getSql(String sqlId, String sqlType){
        if (sqlType == null){
            return getUnknownTypeSql(sqlId);
        }
        switch (sqlType) {
            case "select":
                return getSelectSql(sqlId);
            case "insert":
                return getInsertSql(sqlId);
            case "update":
                return getUpdateSql(sqlId);
            case "delete":
                return getDeleteSql(sqlId);
            default:
                return getUndefindedSql(sqlId);
        }
    }

}