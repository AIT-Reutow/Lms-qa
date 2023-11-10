package de.aittr.lms.fwRA;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class UserHelperRA extends BaseHelperRA {

    public UserHelperRA() {
    }

    public static String loginDataEncoded(String mail, String password) {
        String encodedMail;
        String encodedPassword;

        try {
            encodedMail = URLEncoder.encode(mail, "UTF-8");
            encodedPassword = URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return "username=" + encodedMail + "&password=" + encodedPassword;

    }

    public  Cookie getLoginCookie(String email, String password){
        Response response = given()
                .contentType(ContentType.fromContentType("application/x-www-form-urlencoded"))
                .body(loginDataEncoded(email, password))
                .when()
                .post("/login");

        return response.getDetailedCookie("JSESSIONID");
    }

    public static String getUserUuidByEmail(int userId) throws SQLException {
        String userUuid = db.request("SELECT uuid FROM confirmation_code WHERE user_id = " + userId + ";")
                .getString(1);
        return userUuid;
    }

    public static int getUserIdByEmail(String email) throws SQLException {
        int userId = db.request("SELECT id FROM account WHERE email = \"" + email + "\";")
                .getInt(1);
        return userId;
    }

    public static Response loginUserRA(String email, String password) {
        return given()
                .contentType(ContentType.fromContentType("application/x-www-form-urlencoded"))
                .body(loginDataEncoded(email, password))
                .when()
                .post("/login");
    }

    public Response setPasswordByEmail(String email, String password) throws SQLException {
        int userId = getUserIdByEmail(email);
        String userUuid = getUserUuidByEmail(userId);
        return given().contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"uuid\": \"" + userUuid + "\",\n" +
                        "  \"password\": \"" + password + "\"\n" +
                        "}")
                .when()
                .post("/users/"  + userId +"/password");
    }


}
