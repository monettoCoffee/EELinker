package eel.orm.sqlSession;

import eel.orm.core.BatisConfig;
import eel.orm.core.Executor;
import eel.orm.core.MapperReflact;
import eel.orm.entry.Function;
import eel.utils.Utils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class SqlSessionFactory {

    private static SqlSessionFactory instance;
    private ClassLoader loader;
    private String mapperPath;
    public ArrayList<SqlSession> connections;

    public void build(String packagePath){
        if(this.loader == null){
            this.loader = ClassLoader.getSystemClassLoader();
        }
        this.mapperPath = ClassLoader.getSystemResource("").getPath()+packagePath.replace(".", "/");
    }

    public Executor getExecutor(SqlSession sqlSession){
        return new Executor(sqlSession);
    }

    private void initial() throws DocumentException, FileNotFoundException {
        this.connections = new ArrayList<SqlSession>();
        Element node = new SAXReader().read(loader.getResourceAsStream("ormConfig.xml")).getRootElement();
        for (Object object : node.elements("property")) {
            Element element = (Element) object;
            String value = getValue(element);
            String name = element.attributeValue("name");
            if (name != null) { // && value != null
                if (name.equals("jdbcUrl")) {
                    BatisConfig.jdbcUrl = value;
                } else if (name.equals("password")) {
                    BatisConfig.password = value;
                } else if (name.equals("jdbcDriver")) {
                    BatisConfig.jdbcDriver = value;
                } else if (name.equals("username")) {
                    BatisConfig.username = value;
                } else if (name.equals("initConnectCount")) {
                    BatisConfig.initConnectCount = Integer.parseInt(value);
                } else if (name.equals("maxConnects")) {
                    BatisConfig.maxConnects = Integer.parseInt(value);
                } else if (name.equals("incrementCount")) {
                    BatisConfig.incrementCount = Integer.parseInt(value);
                }
            } else {
                System.out.println("ORM Logger : Unknown Property Value");
            }
        }

        File file = new File(mapperPath);
        File [] files = file.listFiles();
        for(File f:files){
            this.getMapper(f.getPath());
        }
    }

    private void getMapper(String path) throws DocumentException, FileNotFoundException {
        Element root = new SAXReader().read(new FileInputStream(path)).getRootElement();
        for(Iterator iterator = root.elementIterator(); iterator.hasNext(); ){
            Function function = new Function();
            Element element = (Element)iterator.next();
            String sqlType = element.getName().trim().toLowerCase();

            if ("select".equals(sqlType)) {
                String resultType = element.attributeValue("resultType");
                // todo 测试resultType是否存在
                if (Utils.isEmpty(resultType)){
                    System.out.println("ORM Logger : Unknown Result Type");
                    continue;
                }
                function.setResultType(resultType.trim());
            }
            function.setSql(element.getText().trim());
            MapperReflact.setSql(element.attributeValue("id").trim(), function, sqlType);
        }
    }

    private SqlSessionFactory(){}
    public static SqlSessionFactory getInstance(){
        if(instance == null) {
            return createInstance();
        }else{
            return instance;
        }
    }

    private  String getValue(Element element) {
        return element.hasContent() ? element.getText() : element.attributeValue("value");
    }

    public SqlSession getSqlSession() {
        if (this.connections == null){
            try {
                initial();
            } catch (DocumentException e) {
                e.printStackTrace();
                return null;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        while (true) {
            for (SqlSession sqlSession : connections) {
                if (!sqlSession.isUse()) {
                    Connection connection = sqlSession.getConnection();
                    try {
                        if (!connection.isValid(0)) {
                            sqlSession.setConnection(DriverManager.getConnection(BatisConfig.jdbcUrl, BatisConfig.username, BatisConfig.password));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return null;
                    }
//                    sqlSession.setUse(true);
                    sqlSession.setUseTrue();
                    return sqlSession;
                }
            }
            //根据连接池中连接数量从而判断是否增加对应的数量的连接
            if (connections.size() <= BatisConfig.maxConnects - BatisConfig.incrementCount) {
                createConnections(BatisConfig.incrementCount);
            } else if (connections.size() < BatisConfig.maxConnects && connections.size() > BatisConfig.maxConnects - BatisConfig.incrementCount) {
                createConnections(BatisConfig.maxConnects - connections.size());
            }
        }
    }

    private void createConnections(int count) {
        for (int i = 0; i < count; i++) {
            if (BatisConfig.maxConnects > 0 && connections.size() >= BatisConfig.maxConnects) {
                System.out.println("ORM Logger : Connection Overpressure");
                throw new RuntimeException("ORM Logger : Connection Overpressure");
            }
            try {
                Connection connection = DriverManager.getConnection(BatisConfig.jdbcUrl, BatisConfig.username, BatisConfig.password);
                //将连接放入连接池中，并将状态设为可用
                connections.add(new SqlSession(this, connection));
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public <T>T getMapper(Class<T> clazz){
        //动态代理调用。
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},new MapperProxy(this.getSqlSession()));
    }

    private static synchronized SqlSessionFactory createInstance(){
        if(instance==null)
            instance = new SqlSessionFactory();
        return instance;
    }
}
