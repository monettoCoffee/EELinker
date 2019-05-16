package app.router;

import cafe.annotation.router.Handler;
import cafe.annotation.router.ResponseData;
import cafe.annotation.router.Router;

@Handler
public class TestRouter {

    @Router(value = "/test", method = "GET")
    public String test(){
        return "routertest";
    }

    @ResponseData
    @Router(value = "/gettest", method = "GET")
    public String getTest(String p1, String p2){
        System.out.println(p1);
        System.out.println(p2);
        return "GetSuccess";
    }

    @ResponseData
    @Router(value = "/posttest", method = "POST")
    public String postTest(String p1, String p2){
        System.out.println(p1);
        System.out.println(p2);
        return "PostSuccess";
    }



}
