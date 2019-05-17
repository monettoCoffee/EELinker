package app.router;

import app.entry.SampleEntry;
import eel.annotation.router.Handler;
import eel.annotation.router.ResponseData;
import eel.annotation.router.Router;
import eel.orm.OrmSession;

@Handler
public class SampleRouter {

    @Router(value = "/index", method = "GET")
    public String index(){
        return "index";
    }

    @ResponseData
    @Router(value = "/gettest", method = "GET")
    public String getTest(String name, String password){
        System.out.println(name);
        System.out.println(password);
        return "GetSuccess";
    }

    @ResponseData
    @Router(value = "/posttest", method = "POST")
    public String postTest(String name, String password){
        System.out.println(name);
        System.out.println(password);
        return "PostSuccess";
    }

    @ResponseData
    @Router(value = "/select", method = "POST")
    public String selectTest(String name, String password){
        SampleEntry s1 = new SampleEntry();
        s1.setName(name);
        s1.setPassword(password);
        SampleEntry s2 = OrmSession.selectOne("sampleSelect", s1);
        if (s2 != null) {
            return "Select Record Success " + s2.getName() + " " + s2.getPassword();
        }
        return "Can't Find The Record";
    }

    @ResponseData
    @Router(value = "/insert", method = "POST")
    public String insertTest(String name, String password){
        SampleEntry s1 = new SampleEntry();
        s1.setName(name);
        s1.setPassword(password);
        OrmSession.doSql("sampleInsert", s1);
        // Type 2:
        // OrmSession.insert("sampleInsert", s1);
        // Type 3:
        // OrmSession.doSql("type_insert", s1, "Other_Such_As_Type1");
        return "Operate Done";
    }

    @ResponseData
    @Router(value = "/delete", method = "POST")
    public String deleteTest(String name, String password){
        SampleEntry s1 = new SampleEntry();
        s1.setName(name);
        s1.setPassword(password);
        OrmSession.doSql("sampleDelete", s1);
        // Type 2:
        // OrmSession.delete("sampleDelete", s1);
        // Type 3:
        // OrmSession.doSql("type_delete", s1, "Just_As_Mark");
        return "Operate Done";
    }

    @ResponseData
    @Router(value = "/update", method = "POST")
    public String updateTest(String name, String password){
        SampleEntry s1 = new SampleEntry();
        s1.setName(name);
        s1.setPassword(password);
        OrmSession.doSql("sampleUpdate", s1);
        // Type 2:
        // OrmSession.update("sampleUpdate", s1);
        // Type 3:
        // OrmSession.doSql("type_update", s1);
        return "Operate Done";
    }

}
