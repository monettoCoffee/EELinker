package eel.orm.sqlSession;

import eel.orm.core.Executor;

import java.sql.Connection;

public class SqlSession {
    private SqlSessionFactory sqlSessionFactory;
    private Executor executor;
    private Connection connection;
    private volatile boolean useStatus = false;

    public SqlSession(SqlSessionFactory sqlSessionFactory,Connection connection){
        this.sqlSessionFactory = sqlSessionFactory;
        this.executor = this.sqlSessionFactory.getExecutor(this);
        this.connection = connection;
    }

    public <T> T selectOne(String id, Object parameter){
        this.setUseTrue();
        return executor.selectOne(id, parameter, this.connection);
    }

    public <T> T selectList(String id, Object parameter){
        this.useStatus = true;
        return executor.selectList(id, parameter, this.connection);
    }

    public void doSql(String id,Object parameter, String type){
        type = type == null ? type : type.toLowerCase();
        this.setUseTrue();
        executor.doSql(id, parameter, this.connection, type);
    }

    public Connection getConnection(){
        return this.connection;
    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }

    public boolean isUse() {
        return useStatus;
    }

    public synchronized void setUse(boolean useStatus) {
        this.useStatus = useStatus;
    }

    public void setUseFalse(){
        this.useStatus = false;
    }

    public void setUseTrue() {
        this.useStatus = true;
    }

}
