package eel.orm.sqlSession;

/**
 * @author monetto
 */
public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(String packageName) {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactory.getInstance();
        sqlSessionFactory.build(packageName);
        return sqlSessionFactory;
    }
}
