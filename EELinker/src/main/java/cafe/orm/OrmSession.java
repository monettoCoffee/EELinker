package cafe.orm;

import cafe.orm.sqlSession.SqlSession;
import cafe.orm.sqlSession.SqlSessionFactory;
import cafe.orm.sqlSession.SqlSessionFactoryBuilder;

import java.lang.ref.WeakReference;

public class OrmSession {

    private static SqlSessionFactory sqlSessionFactory;

    private synchronized static void createSqlSessionFactory(){
        if (OrmSession.sqlSessionFactory == null){
            WeakReference<SqlSessionFactoryBuilder> reference = new WeakReference<SqlSessionFactoryBuilder>(new SqlSessionFactoryBuilder());
            OrmSession.sqlSessionFactory = reference.get().build("mapper");
        }
    }

    private static SqlSession getSqlSession(){
        if (OrmSession.sqlSessionFactory == null){
            createSqlSessionFactory();
        }
        return sqlSessionFactory.getSqlSession();
    }

    public static void doSql(String sqlId, Object param){
        OrmSession.getSqlSession().doSql(sqlId, param);
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

}
