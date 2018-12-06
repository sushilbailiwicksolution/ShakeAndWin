/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao.impl;

import com.ym.dao.CommonDao;
import com.ym.dao.GameDao;
import com.ym.dao.UserDao;
import com.ym.model.UserRegRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import javax.mail.Authenticator;
import javax.sql.DataSource;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 *
 * @author Mathur
 */
public class CommonDaoImple implements CommonDao {

    private final JdbcTemplate jdbcTemplate;

    public CommonDaoImple(DataSource datasource) {
        jdbcTemplate = new JdbcTemplate(datasource);
    }
    @Autowired
    UserDao userdao;
    @Autowired
    GameDao gamedao;

    @Override
    public String getuserToken(int userId) {
        String userToken = null;
        System.out.println("UserId==>" + userId);
        String query = "SELECT userToken from user_login WHERE userId = ?";
        try {
            userToken = jdbcTemplate.queryForObject(query, new Object[]{userId}, String.class);
            System.out.println("User Token==>" + userToken);
        } catch (Exception e) {
            e.printStackTrace();
            userToken = null;
        }
        return userToken;
    }

    @Override
    public String checkOtherLogin(int userId, String userToken) {
        String query = "SELECT userToken FROM user_login WHERE userId = ?";
        String loginFlag = "Y";
        try {
            String existToken = jdbcTemplate.queryForObject(query, new Object[]{userId}, String.class);
            if (existToken.equalsIgnoreCase(userToken)) {
                loginFlag = "N";
            }
        } catch (Exception e) {
            e.printStackTrace();
            loginFlag = "Y";
        }
        return loginFlag;
    }

    @Override
    public String getValueByParam(String paramName) {
        String points = null;
        String paramSql = "SELECT paramValue from game_config WHERE paramName = ?";
        try {
            points = jdbcTemplate.queryForObject(paramSql, String.class, paramName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  System.out.println("Points ===> " + points);
        return points;
    }

    @Override
    public int getGamePoints(int gameId) {
        String sql = "SELECT points from game_master WHERE id = ?";
        int point = jdbcTemplate.queryForObject(sql, new Object[]{gameId}, Integer.class);

        System.out.println("Game points==>" + point);
        return point;
    }

    @Override
    public void pushNotification(String server_key, List<UserRegRequest> user, int currentUserId) {
        String userName = null;
        int userId = 0;
        String title = null;
        //String message = null;
        for (UserRegRequest userdetail : user) {
                    System.out.println(" Notification Push By userId==> "+currentUserId);
            userName = userdetail.getUserName();
            userId = userdetail.getId();
           
            if (userId == currentUserId) {
                System.out.println(" Notification skiped to userId==> "+currentUserId);
                continue;
            }
            try {
                String FCM_URL = "https://fcm.googleapis.com/fcm/send";

                // Create URL instance.
                URL url = new URL(FCM_URL);

                // create connection.
                HttpURLConnection conn;

                conn = (HttpURLConnection) url.openConnection();

                conn.setUseCaches(false);

                conn.setDoInput(true);

                conn.setDoOutput(true);

                //set method as POST or GET
                conn.setRequestMethod("POST");

                //pass FCM server key
                conn.setRequestProperty("Authorization", "key=" + server_key);

                //Specify Message Format
                conn.setRequestProperty("Content-Type", "application/json");

                //Create JSON Object & pass value
                JSONObject infoJson = new JSONObject();
                title = "ShakeAndWin";
                infoJson.put("title", title);

                //message = "User Id : " + id + ", Name:  " + userName;
                JSONObject msz = new JSONObject();
                //s msz.put("id", userId);
                msz.put("name", userName);
                //infoJson.put("body", msz);

                JSONObject json = new JSONObject();

                json.put("to", userdetail.getFcmToken());
                 System.out.println(" Notification Pushed To userId ==> "+userId);
                System.out.println("fcm===>  " + userdetail.getFcmToken());
                // json.put("to", "/topics/notification");

                json.put("notification", infoJson);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(json.toString());

                wr.flush();

                int status = 0;

                if (null != conn) {

                    status = conn.getResponseCode();

                }
                if (status != 0) {

                    if (status == 200) {

                        //SUCCESS message
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        System.out.println("Android Notification Response : " + reader.readLine());

                    } else if (status == 401) {

                        //client side error
                        //  System.out.println("Notification Response : TokenId : " + fcmToken + " Error occurred :");
                    } else if (status == 501) {

                        //server side error
                        // System.out.println("Notification Response : [ errorCode=ServerError ] TokenId : " + fcmToken);
                    } else if (status == 503) {

                        //server side error
                        // System.out.println("Notification Response : FCM Service is Unavailable  TokenId : " + fcmToken);
                    }

                }

            } catch (MalformedURLException mlfexception) {

                // Prototcal Error
                System.out.println("Error occurred while sending push Notification!.." + mlfexception.getMessage());

            } catch (IOException mlfexception) {

                //URL problem
                System.out.println("Reading URL, Error occurred while sending push Notification!.." + mlfexception.getMessage());

            } catch (Exception exception) {

                //General Error or exception.
                System.out.println("Error occurred while sending push Notification!.." + exception.getMessage());

            }
        }
    }

    
    @Override
   public List<UserRegRequest> getHomePageUsers(){
   String sql="SELECT user_login.`fcmToken` AS  fcmToken FROM waiting_users_tracker AS wu  JOIN `user_login` ON user_login.`userId`=wu.`userId` WHERE isHome = 0 AND (updatedTime BETWEEN (NOW() - INTERVAL 10 MINUTE) AND NOW())";
   List<UserRegRequest> dataList = null;
   try{    
    dataList = jdbcTemplate.query(sql, new FcmTokenMapper());
   }catch(Exception e){
   e.printStackTrace();
       System.out.println("fcmToken list to send notification.."+ dataList.size());
   } return dataList;
   }
    @Override
    public void notifyUser(String server_key, List<UserRegRequest> tokens) {
//        String userName = null;
//        int userId = 0;
        String title = null;
        System.out.println("Notify fcmToken list size==> "+tokens.size());
        //String message = null;
        for (UserRegRequest token : tokens) {
//            userName = userdetail.getUserName();
//            userId = userdetail.getId();
//           
            
            try {
                String FCM_URL = "https://fcm.googleapis.com/fcm/send";

                // Create URL instance.
                URL url = new URL(FCM_URL);

                // create connection.
                HttpURLConnection conn;

                conn = (HttpURLConnection) url.openConnection();

                conn.setUseCaches(false);

                conn.setDoInput(true);

                conn.setDoOutput(true);

                //set method as POST or GET
                conn.setRequestMethod("POST");

                //pass FCM server key
                conn.setRequestProperty("Authorization", "key=" + server_key);

                //Specify Message Format
                conn.setRequestProperty("Content-Type", "application/json");

                //Create JSON Object & pass value
                JSONObject infoJson = new JSONObject();
                title = "ShakeAndWin";
                infoJson.put("title", title);

                //message = "User Id : " + id + ", Name:  " + userName;
                JSONObject msz = new JSONObject();
                //s msz.put("id", userId);
                //msz.put("name", userName);
                //infoJson.put("body", msz);

                JSONObject json = new JSONObject();

                json.put("to", token.getFcmToken());
                // System.out.println(" Notification Pushed To userId ==> "+userId);
                System.out.println("fcm===>  "+ token.getFcmToken());
                // json.put("to", "/topics/notification");

                json.put("notification", infoJson);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(json.toString());

                wr.flush();

                int status = 0;

                if (null != conn) {

                    status = conn.getResponseCode();

                }
                if (status != 0) {

                    if (status == 200) {

                        //SUCCESS message
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        System.out.println("Android Notification Response : " + reader.readLine());

                    } else if (status == 401) {

                        //client side error
                        //  System.out.println("Notification Response : TokenId : " + fcmToken + " Error occurred :");
                    } else if (status == 501) {

                        //server side error
                        // System.out.println("Notification Response : [ errorCode=ServerError ] TokenId : " + fcmToken);
                    } else if (status == 503) {

                        //server side error
                        // System.out.println("Notification Response : FCM Service is Unavailable  TokenId : " + fcmToken);
                    }

                }

            } catch (MalformedURLException mlfexception) {

                // Prototcal Error
                System.out.println("Error occurred while sending push Notification!.." + mlfexception.getMessage());

            } catch (IOException mlfexception) {

                //URL problem
                System.out.println("Reading URL, Error occurred while sending push Notification!.." + mlfexception.getMessage());

            } catch (Exception exception) {

                //General Error or exception.
                System.out.println("Error occurred while sending push Notification!.." + exception.getMessage());

            }
        }
    }

    @Override
     public int getPageCount(int userId, int size) {
        String query = "SELECT COUNT(*) FROM game_user_ledger WHERE userId = ?";
        int totalCount = 0;
        int pageCount = 0;
        try {
            totalCount = jdbcTemplate.queryForObject(query, Integer.class, new Object[]{userId});
            pageCount = totalCount / size;
            if (totalCount % size > 0) {
                pageCount++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageCount;
    }

    
    public String generateRandom() {
        String aToZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        System.out.println("Ready to  generate Random No.");
        Random rand = new Random();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randIndex = rand.nextInt(aToZ.length());
            res.append(aToZ.charAt(randIndex));
        }
        return res.toString();
    }

    public void sendEmail(String loginId, String otp) {

        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");
        final String username = "shakandwin@gmail.com";
        final String password = "Shake@1234";
        // String sentTo = null;

        try {
            Session session = Session.getDefaultInstance(props,
                    new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // -- Create a new message --
            MimeMessage msg = new MimeMessage(session);
            //   sentTo = loginId;
            // -- Set the FROM and TO fields --
            //  System.out.println("Send Email Called with emailid==> "+sentTo);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(loginId, false));
            msg.setSubject("OTP confirmation for Shake And Win Login Password ");

            String data = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
                    + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                    + "<head>\n"
                    + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n"
                    + "<title>OTP</title>\n"
                    + "</head>\n"
                    + "<style>\n"
                    + "th {\n"
                    + "	border: solid 1px #ddd;\n"
                    + "	padding: 5px 10px;\n"
                    + "	text-align: left;\n"
                    + "	vertical-align: top;\n"
                    + "	font-family: Verdana, Geneva, sans-serif;\n"
                    + "	font-weight: 600;\n"
                    + "	font-size: 12px;\n"
                    + "	background-color: #ddd;\n"
                    + "}\n"
                    + "td {\n"
                    + "	border: solid 1px #ddd;\n"
                    + "	padding: 5px 10px;\n"
                    + "	text-align: left;\n"
                    + "	vertical-align: top;\n"
                    + "	\n"
                    + "}\n"
                    + "p {\n"
                    + "	font-family: Verdana, Geneva, sans-serif;\n"
                    + "	font-size: 12px;\n"
                    + "	line-height: 16px;\n"
                    + "	margin: 5px 0px;\n"
                    + "}\n"
                    + "b {\n"
                    + "	font-family: Verdana, Geneva, sans-serif;\n"
                    + "	font-size: 11px;\n"
                    + "	line-height: 16px;\n"
                    + "	margin: 5px 0px;\n"
                    + "	font-weight: 600\n"
                    + "}\n"
                    + "</style>\n"
                    + "\n"
                    + "<body>\n"
                    + "\n"
                    + "<div class=\"table-responsive\">\n"
                    + "\n"
                    + "\n"
                    + "					<table class=\"table table-condensed\" width=\"800\" style=\"border-collapse: collapse;  margin:0px auto; \" >\n"
                    + "\n"
                    + "					<tr>\n"
                    + "                <td colspan=\"2\" style=\" width:100%; border-bottom:0px;     padding: 20px;  background-color: #ff6c6c;  text-align: center;\"><img src=\"http://smilemerchant.com/assets/images/logo.png\"></td>\n"
                    + "            </tr>\n"
                    + "					\n"
                    + "						<tr>\n"
                    + "						<td colspan=\"2\"  style=\" border-top:0px; padding-top:10px; padding-bottom:40px; padding-top:30px;\">\n"
                    + "						\n"
                    + "    					<p style=\"text-align:left;\">Dear User,<P>\n"
                    + "						<p style=\"text-align:left;\">Welcome, We thank you for Using Shake And Win Game <P>\n"
                    + "    					\n"
                    + "    					<P style=\"text-align:left; margin-top:30px;\">to reset your password use code : <b>" + otp + "</b>\n"
                    + "</p>\n"
                    + "    					\n"
                    + "						\n"
                    + "						<p style=\"text-align:left; margin-top:30px;\">In case you require any further assistance, please mail us at shakandwin@gmail.com or call us at 24*7 Hrs. Customer Support at 9540643208 </p>\n"
                    + "    				</address></td>\n"
                    + "						</tr>\n"
                    + "						<tr>\n"
                    + "						<td>\n"
                    + "                  <p style=\"text-align:center;\"> © All Rights Reserved to Use and Privacy Policy </p>\n"
                    + "				  </td>\n"
                    + "						</tr>\n"
                    + "						</table>\n"
                    + "</div>\n"
                    + "</body>\n"
                    + "</html>";
            msg.setSentDate(new Date());
            msg.setContent(data, "text/html");
            Transport.send(msg);
            System.out.println("Message sent.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error in catch" + e);
        }

    }
}
