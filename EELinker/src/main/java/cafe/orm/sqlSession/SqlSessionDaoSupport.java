package cafe.orm.sqlSession;

public class SqlSessionDaoSupport {
    public SqlSessionDaoSupport(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }
    private SqlSessionFactory sqlSessionFactory;
    public SqlSession getSqlSession() {
        return this.sqlSessionFactory.getSqlSession();
    }
}