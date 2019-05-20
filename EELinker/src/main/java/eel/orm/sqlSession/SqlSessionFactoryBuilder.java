package eel.orm.sqlSession;

public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(String packageName) {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactory.getInstance();
        sqlSessionFactory.build(packageName);
        return sqlSessionFactory;
    }
}
