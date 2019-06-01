package eel.orm.sqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author monetto
 */
public class MapperProxy implements InvocationHandler {
    SqlSession sqlSessionProxy;

    public MapperProxy(SqlSession sqlSessionProxy){
        this.sqlSessionProxy = sqlSessionProxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return this.sqlSessionProxy.selectOne(method.getName(), (Object[])args[0]);
    }
}
