package eel.orm.sqlSession;

public class SqlSessionDaoSupport {

    private SqlSessionFactory sqlSessionFactory;

    public SqlSessionDaoSupport(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public SqlSession getSqlSession() {
        return this.sqlSessionFactory.getSqlSession();
    }
}