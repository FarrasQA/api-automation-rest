package body.auth;

import org.json.simple.JSONObject;
import utils.ConfigReader;

public class LoginBody {

    public JSONObject logindata(){
        JSONObject loginbody = new JSONObject();
        loginbody.put("email", ConfigReader.getProperty("email"));
        loginbody.put("password", ConfigReader.getProperty("password"));
        return loginbody;
    }
}
