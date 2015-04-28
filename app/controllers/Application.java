package controllers;

import play.*;
import play.db.DB;
import play.mvc.*;

import views.html.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Application extends Controller {

    public static Result index() {
        Connection connection = DB.getConnection();
        Map<String, String> map = new HashMap<>();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("select * from votetypes");
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                map.put(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok(map.toString());
    }

}
