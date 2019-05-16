package cafe.orm.sqlSession;

import java.sql.Connection;

public class SqlSession {
    private SqlSessionFactory sqlSessionFactory;
    private Executor executor;
    private Connection connection;
    private boolean isUse = false;

    public SqlSession(SqlSessionFactory sqlSessionFactory,Connection connection,Boolean isUse){
        this.sqlSessionFactory = sqlSessionFactory;
        this.executor = this.sqlSessionFactory.getExecutor(this);
        this.connection = connection;
        this.isUse = isUse;
    }

    public <T> T selectOne(String id,Object parameter){
        this.isUse = true;
        return executor.selectOne(id, parameter, this.connection);
    }

    public <T> T selectList(String id,Object parameter){
        this.isUse = true;
        return executor.selectList(id, parameter, this.connection);
    }

    public void doSql(String id,Object parameter){
        this.isUse = true;
        executor.doSql(id, parameter, this.connection);
    }

    public Connection getConnection(){
        return this.connection;
    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }

    public boolean isUse() {
        return isUse;
    }

    public void setUse(boolean use) {
        isUse = use;
    }

}
