package eel.orm;

import eel.orm.sqlSession.SqlSession;
import eel.orm.sqlSession.SqlSessionFactory;
import eel.orm.sqlSession.SqlSessionFactoryBuilder;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * @author monetto
 */
public class OrmSession {


    private static SqlSessionFactory sqlSessionFactory;

    private synchronized static void createSqlSessionFactory(){
        WeakReference<SqlSessionFactoryBuilder> reference = new WeakReference<SqlSessionFactoryBuilder>(new SqlSessionFactoryBuilder());
        OrmSession.sqlSessionFactory = Objects.requireNonNull(reference.get()).build("mapper");
    }

    private static SqlSession getSqlSession(){
        if (OrmSession.sqlSessionFactory == null){
            createSqlSessionFactory();
        }
        return sqlSessionFactory.getSqlSession();
    }

    public static void doSql(String sqlId, Object param, String type){
        OrmSession.getSqlSession().doSql(sqlId, param, type);
    }

    public static void doSql(String sqlId, Object param){
        OrmSession.getSqlSession().doSql(sqlId, param, null);
    }

    public static <T> T selectOne(String sqlId, Object param){
        return OrmSession.getSqlSession().selectOne(sqlId, param);
    }

    public static <T> T selectOne(String sqlId){
        return OrmSession.getSqlSession().selectOne(sqlId, null);
    }

    public static <T> T selectList(String sqlId, Object param){
        return OrmSession.getSqlSession().selectList(sqlId, param);
    }

    public static <T> T selectList(String sqlId){
        return OrmSession.getSqlSession().selectList(sqlId, null);
    }

    public static void update(String sqlId, Object param) {
        OrmSession.getSqlSession().doSql(sqlId, param, "update");
    }

    public static void delete(String sqlId, Object param) {
        OrmSession.getSqlSession().doSql(sqlId, param, "delete");
    }

    public static void insert(String sqlId, Object param) {
        OrmSession.getSqlSession().doSql(sqlId, param, "insert");
    }

}
