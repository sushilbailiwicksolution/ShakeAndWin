/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao.impl;

import com.ym.dao.CommonDao;
import com.ym.dao.GameDao;
import com.ym.dao.UserDao;
import com.ym.model.GameInstanceUsers;
import com.ym.model.GamePromoMaster;
import com.ym.model.GenericResponse;
import com.ym.model.HistoryRequest;
import com.ym.model.UserHistoryResponse;
import com.ym.model.UserInfo;
import com.ym.model.UserLedger;
import com.ym.model.UserRegRequest;
import com.ym.model.UserResponse;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 *
 * @author Mathur
 */
public class UserDaoImple implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDaoImple(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    CommonDao commondao;

    @Autowired
    GameDao gamedao;

    @Override
    public UserResponse registeration(UserRegRequest req) {
        UserResponse res = new UserResponse();
        String paramName = "REGISTRATION_POINTS";

        UserInfo userinfo = new UserInfo();
        String checkuser = "SELECT id,userName,emailId,mobileNo,imageURL,promoCode,availableBalance,gender FROM user_info WHERE emailId = ?";
        String addUserInfo = "INSERT INTO user_info (userName,emailId,mobileNo,gender,availableBalance,promoCode,imageURL,createdDate,updatedDate) VALUES(?,?,?,?,?,?,?,?,?)";
        String points = commondao.getValueByParam(paramName);

        userinfo.setUserName(req.getUserName());
        userinfo.setEmailId(req.getEmailId());
        userinfo.setGender(req.getGender());
        userinfo.setMobileNo(req.getMobileNo());
        UserInfo exist = null;
        int userId = 0;
        try {
            exist = jdbcTemplate.queryForObject(checkuser, new UserMapper(), new Object[]{req.getEmailId()});
        } catch (Exception e) {

            exist = null;
        }
        //System.out.println("Existance==> "+exist);
        if (exist == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            System.out.println("Name: " + req.getUserName() + "  mailid : " + req.getEmailId());
            try {
                int update = jdbcTemplate.update(new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(addUserInfo, new String[]{"id"});
                        ps.setString(1, req.getUserName());
                        ps.setString(2, req.getEmailId());
                        ps.setString(3, req.getMobileNo());
                        ps.setString(4, req.getGender());
                        ps.setString(5, points);
                        ps.setString(6, req.getPromoCode());
                        ps.setString(7, req.getImageURL());
                        ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
                        ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
                        return ps;
                    }
                }, keyHolder);
                System.out.println("Generated Key : " + keyHolder.getKey() + "status  ==> " + update);
                if (update > 0) {
                    Number key = keyHolder.getKey();
                    String userToken = generateUserToken(req.getDeviceId(), req.getDeviceIMEI());
                    int loginUpdate = addLoginData(req, userToken, key.intValue());
                    System.out.println("login data added==> " + loginUpdate);
                    int historyUpdate = addUserHistory(key.intValue(), req.getPlateform(), req.getDeviceId(), req.getDeviceIMEI());

                    String updateLedger = "INSERT INTO game_user_ledger (userId,transBalance,availableBalance,userRank,transType,transDescription,status,createdDate) values (?,?,(SELECT availableBalance FROM user_info WHERE id = " + key.intValue() + "),?,?,?,?,?) ";
                    int ledgerUpdate = jdbcTemplate.update(updateLedger, new Object[]{key.intValue(), points, 0, "Credit", "New Registration", 0, new Timestamp(System.currentTimeMillis())});
                    System.out.println("user_ledger updated with status==>" + ledgerUpdate);

                    System.out.println("History data added==> " + historyUpdate);
                    if (historyUpdate > 0 && loginUpdate > 0) {
                        userinfo.setId(key.intValue());
                        userinfo.setAvailableBalance(Double.parseDouble(points));
                        res.setUserInfo(userinfo);
                        res.setUserToken(userToken);
                        res.setStatus("Success");
                        res.setMessage("New User Added Success ");
                        res.setStatusCode(0);

                    } else {
                        res.setStatus("Fail");
                        res.setMessage("Can not add login and History data");
                        res.setStatusCode(-1);

                    }
                }
            } catch (Exception e) {
                res.setStatus("Fail");
                res.setMessage("Can not add User Application Error ");
                res.setStatusCode(-2);
                e.printStackTrace();
            }
        } else {
            String existUserToken = commondao.getuserToken(exist.getId());
            System.out.println("ExistToken ==> " + existUserToken + "  UserToken ==> " + req.getUserToken());

            if (req.getLoginType().equals("S")) {
                try {
                    int updateLogin = updateSocialUser(req.getLoginType(), req.getLoginBy(), req.getPlateform(), req.getFcmToken(), existUserToken, req.getDeviceId(), req.getDeviceIMEI(), req.getStatus(), req.getEmailId());
                    int updateHistory = updateUserHistory(exist.getId(), req.getPlateform(), req.getDeviceId(), req.getDeviceIMEI());
                    if (updateLogin > 0 && updateHistory > 0) {

                        res.setUserInfo(getUserProfile(exist.getId()).getUserInfo());
                        res.setUserToken(existUserToken);
                        res.setStatus("Success");
                        res.setMessage("Social User updated Successfully");
                        res.setStatusCode(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("User exist");

                userinfo.setAvailableBalance(Double.parseDouble(points));
                userinfo.setId(exist.getId());
                res.setUserInfo(userinfo);
                res.setUserToken(existUserToken);
                res.setStatus("Success");
                res.setMessage("User Already Exist as Normal User");
                res.setStatusCode(-3);
            }

        }
        return res;
    }

    @Override
    public UserResponse userLogin(UserRegRequest req) {
        UserResponse res = new UserResponse();
        String loginId = null;
        String mobileNo = null;
        if (req.getLoginId().contains("@")) {
            loginId = req.getLoginId();
            System.out.println("Login by EmailId");
        } else if (req.getLoginId().length() == 10) {
            System.out.println("Login by MobileNo");
            mobileNo = req.getLoginId();
        }
        String query = "SELECT * FROM user_info WHERE id = (SELECT userId FROM user_login WHERE PASSWORD = ? AND (loginId = ? OR mobileNo = ?))";
        System.out.println("loginId==>  " + loginId + ", mobileNo==> " + mobileNo + ",  password==> " + req.getPassword() + ",  PlateForm==> " + req.getPlateform());
        UserInfo userData = null;
        String userToken = null;
        try {
            try {
                userData = jdbcTemplate.queryForObject(query, new UserMapper(), new Object[]{req.getPassword(), loginId, mobileNo});
            } catch (Exception e) {

                userData = null;
            }

            if (userData != null) {

                // String loginFlag = commondao.checkOtherLogin(userData.getId(), req.getUserToken());
                userToken = commondao.getuserToken(userData.getId());
                int updateLogin = updateSocialUser(req.getLoginType(), req.getLoginBy(), req.getPlateform(), req.getFcmToken(), userToken, req.getDeviceId(), req.getDeviceIMEI(), req.getStatus(), userData.getEmailId());
                int updateHistory = updateUserHistory(userData.getId(), req.getPlateform(), req.getDeviceId(), req.getDeviceIMEI());

                res.setUserInfo(userData);
                res.setUserToken(userToken);
                res.setStatus("Success");
                res.setMessage("Successfully Login");
                res.setStatusCode(0);

            } else {
                res.setStatus("Fail");
                res.setMessage("UserName and password Incorrect");
                res.setStatusCode(-2);
            }
        } catch (Exception e) {
            res.setStatus("Fail");
            res.setMessage("Application Error");
            res.setStatusCode(-1);
            e.printStackTrace();
        }

        return res;
    }

    private int updateSocialUser(String loginType, String loginBy, String plateform, String fcmToken, String userToken, String deviceId, String deviceIMEI, int status, String loginId) {
        String query = "UPDATE user_login SET loginType = ?, loginBy = ?,plateform = ?, fcmToken = ?, userToken = ?, deviceId = ?, deviceIMEI = ?, lastLogin = ?, status = ? WHERE loginId = ?";
        int update = 0;
        try {

            update = jdbcTemplate.update(query, loginType, loginBy, plateform, fcmToken, userToken, deviceId, deviceIMEI, new Timestamp(System.currentTimeMillis()), status, loginId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return update;
    }

    private int updateUserHistory(int userId, String plateform, String deviceId, String deviceIMEI) {
        String query = "UPDATE user_login_history SET  plateform = ?,  deviceId = ?, deviceIMEI = ?,loginDate = ? WHERE userId = ?";
        int update = 0;
        try {
            update = jdbcTemplate.update(query, plateform, deviceId, deviceIMEI, new Timestamp(System.currentTimeMillis()), userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return update;
    }

    private String generateUserToken(String deviceId, String deviceIMEI) {

        System.out.println("deviceId  ==> " + deviceId + "IMEI===> " + deviceIMEI);
        StringBuilder simpleString = new StringBuilder(deviceId);
        simpleString.append("NMSS");
        simpleString.append(deviceIMEI);
        String testString = simpleString.toString();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserDaoImple.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] messageDigest = md.digest(testString.getBytes());
        BigInteger number = new BigInteger(1, messageDigest);
        String userToken = number.toString(16);

        return userToken;
    }

    public int addUserHistory(int userId, String plateform, String deviceId, String deviceIMEI) {
        int update = 0;
        String addLoginHistory = "INSERT INTO user_login_history (userId, plateform, deviceId, deviceIMEI, loginDate) VALUES (?,?,?,?,?)";

        try {
            update = jdbcTemplate.update(addLoginHistory, userId, plateform, deviceId, deviceIMEI, new Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return update;
    }

    public int addLoginData(UserRegRequest req, String userToken, int userId) {
        int update = 0;
        String loginquery = "INSERT INTO user_login(userId,loginId,mobileNo,password,loginType,loginBy,plateform,fcmToken,userToken,deviceId,deviceIMEI,promoRequest,lastLogin,status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            update = jdbcTemplate.update(loginquery, userId, req.getEmailId(), req.getMobileNo(), req.getPassword(), req.getLoginType(), req.getLoginBy(),
                    req.getPlateform(), req.getFcmToken(), userToken, req.getDeviceId(), req.getDeviceIMEI(), req.getPromoRequest(), new Timestamp(System.currentTimeMillis()), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return update;
    }

    @Override
    public UserResponse getUserProfile(int userId) {
        UserResponse res = new UserResponse();
        UserInfo userData = null;
        //String loginFalg = commondao.checkOtherLogin(req.getUserId(), req.getUserToken());
        // if (loginFalg.equalsIgnoreCase("N")) {
        String query = "SELECT userName,emailId,mobileNo,gender,imageURL,availableBalance FROM user_info WHERE id = ?";
        //  String tokenquery = "SELECT userToken from user_login WHERE loginId = ?";
        try {
            try {
                userData = jdbcTemplate.queryForObject(query, new ProfileMapper(), userId);
            } catch (Exception e) {
                userData = null;
            }
            if (userData != null) {
                userData.setId(userId);
                //String userToken = jdbcTemplate.queryForObject(tokenquery, String.class, userData.getEmailId());
                //  res.setUserToken(userToken);
                res.setUserInfo(userData);
                res.setStatus("Sucsess");
                res.setMessage("User Data Fetched successfully");
                res.setStatusCode(0);
            } else {
                res.setStatus("Fail");
                res.setMessage("No User available for id " + userId);
                res.setStatusCode(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus("Fail");
            res.setMessage(" cannot Fetched User Data Application Error");
            res.setStatusCode(-1);
        }
//        } else {
//            res.setStatus("Success");
//            res.setMessage("User already login on another device");
//            res.setStatusCode(-2);
//        }
        return res;
    }

    @Override
    public GenericResponse updateUserProfile(UserRegRequest req) {
        GenericResponse res = new GenericResponse();
        String loginFlag = commondao.checkOtherLogin(req.getUserId(), req.getUserToken());
        if (loginFlag.equalsIgnoreCase("N")) {
            String query = "UPDATE user_info SET userName = ?,mobileNo = ?,gender = ?,updatedDate = ? WHERE id = ?";
            try {

                int update = jdbcTemplate.update(query, new Object[]{req.getUserName(), req.getMobileNo(), req.getGender(), new Timestamp(System.currentTimeMillis()), req.getUserId()});

                if (update > 0) {
                    String loginsql = "UPDATE user_login SET mobileNo = ? WHERE userId = ?";
                    jdbcTemplate.update(loginsql, new Object[]{req.getMobileNo(), req.getUserId()});
                    res.setLoginFlag(loginFlag);
                    res.setStatus("Success");
                    res.setMessage("Profile Updated Successfully");
                    res.setStatusCode(1);
                } else {
                    res.setStatus("Fail");
                    res.setMessage("User does not Exist in record");
                    res.setStatusCode(0);
                }

            } catch (Exception e) {
                e.printStackTrace();
                res.setStatus("Fail");
                res.setMessage("Cannot Updated Profile Application Error");
                res.setStatusCode(-1);
            }
        } else {
            res.setLoginFlag(loginFlag);
            res.setStatus("Success");
            res.setMessage("User Already login on another device");
            res.setStatusCode(-2);
        }
        return res;
    }

    @Override
    public GenericResponse updateProfilePic(String profileImage, int userId) {
        GenericResponse res = new GenericResponse();
        int update = 0;
        String query = "UPDATE user_info SET imageURL = ? WHERE id = ?";
        System.out.println("Ready to insert Image in db");
        try {
            update = jdbcTemplate.update(query, new Object[]{profileImage, userId});
            if (update > 0) {
                System.out.println("Image added in db with status code===> " + update);
                System.out.println("Image===> " + profileImage);
                res.setStatus("Success");
                res.setMessage("Image Updated Successfully");
                res.setStatusCode(0);
            } else {
                res.setStatus("Fail");
                res.setMessage("Something went wrong");
                res.setStatusCode(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus("Fail");
            res.setMessage("Cannot Update Image Application Error");
            res.setStatusCode(-1);
        }

        return res;
    }

    @Override
    public UserHistoryResponse getUserHistory(HistoryRequest req) {
        UserHistoryResponse res = new UserHistoryResponse();
        List<UserLedger> userdata;
        int pageWith = 0;
        if (req.getStartWith() == 1) {
            res.setTotalPage(commondao.getPageCount(req.getUserId(), req.getSize()));
            pageWith = 0;
        } else {
            pageWith = (req.getSize()*(req.getStartWith() - 1));
        }
        System.out.println(" req==> " + req.getStartWith() + " size==> " + req.getSize() + " pagewith==> " + pageWith);
        String query = "SELECT * FROM game_user_ledger WHERE userId = ? limit " + pageWith + "," + req.getSize();
        String loginFlag = commondao.checkOtherLogin(req.getUserId(), req.getUserToken());
        if (loginFlag.equalsIgnoreCase("N")) {
            try {
                userdata = jdbcTemplate.query(query, new UserHistoryMapper(), req.getUserId());
                if (!userdata.isEmpty()) {
                    res.setLoginFlag(loginFlag);
                    res.setUserHistory(userdata);
                    res.setStatus("Success");
                    res.setMessage("User History Fetched Successfully");
                    res.setStatusCode(0);
                } else {
                    res.setStatus("Fail");
                    res.setMessage("User not exist for History");
                    res.setStatusCode(1);
                }
            } catch (Exception e) {
                res.setStatus("Fail");
                res.setMessage("Application Error to fetch User History");
                res.setStatusCode(-1);
                e.printStackTrace();
            }
        } else {
            res.setLoginFlag(loginFlag);
            res.setStatus("Success");
            res.setMessage("User already login on another device");
            res.setStatusCode(-2);
        }
        return res;
    }

    @Override
    public GenericResponse debitAmountForGame(GameInstanceUsers req) {
        GenericResponse res = new GenericResponse();
        int gamePoints = commondao.getGamePoints(req.getGameId());
        String updateInfo = "UPDATE user_info SET availableBalance = availableBalance-" + gamePoints + " WHERE id = ?";
        String updateLedger = "INSERT INTO game_user_ledger (gameId,userId,instanceId,transBalance,availableBalance,userRank,transType,transDescription,status,createdDate) values (?,?,?,?,(SELECT availableBalance FROM user_info WHERE id = " + req.getUserId() + "),?,?,?,?,?) ";
        try {
            int infoUpdate = jdbcTemplate.update(updateInfo, new Object[]{req.getUserId()});

            int ledgerUpdate = jdbcTemplate.update(updateLedger, new Object[]{req.getGameId(), req.getUserId(), req.getInstanceId(), gamePoints, 0, "Debit", "spend on Game Play", 0, new Timestamp(System.currentTimeMillis())});
            if (ledgerUpdate > 0 && infoUpdate > 0) {
                res.setStatus("Success");
                res.setMessage("Amount detected Successfully for playing Game");
                res.setStatusCode(0);
            } else {
                res.setStatus("Fail");
                res.setMessage("Something went wrong");
                res.setStatusCode(1);
            }
        } catch (Exception e) {
            res.setStatus("Fail");
            res.setMessage("Application Error");
            res.setStatusCode(-1);
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public UserResponse addPromoBalance(UserRegRequest req) {
        UserResponse res = new UserResponse();
        String promoIds = null;
        String promoCodeIds = "SELECT promoCode FROM user_info WHERE id = ?";

        String loginFlag = commondao.checkOtherLogin(req.getUserId(), req.getUserToken());
        System.out.println("Login Flag to add bal ==>" + loginFlag);
        if (loginFlag.equalsIgnoreCase("N")) {
            try {
                promoIds = jdbcTemplate.queryForObject(promoCodeIds, new Object[]{req.getUserId()}, String.class);
            } catch (Exception e) {

                e.printStackTrace();
            }
            System.out.println("promo Ids ==> " + promoIds);
            String updatePromo = "UPDATE game_promo_master SET remainingValue = remainingValue-promoCodeValue WHERE id = ?";
            try {
                String promoList = "SELECT id,promoCode,maxPromoUse,promoCodeValue,maxAllowedValue,remainingValue,promoType,status FROM game_promo_master WHERE id IN (" + promoIds + ") ";
                List<GamePromoMaster> promoData = jdbcTemplate.query(promoList, new PromoMapper());
                System.out.println("promoIds data fetched size==> " + promoData.size());
                if (promoData.size() > 0) {
                    for (GamePromoMaster promolist : promoData) {
                        System.out.println("ready to if chacks promo code==> " + req.getPromoCode() + "==> " + promolist.getPromoCode());
                        if (promolist.getPromoCode().equalsIgnoreCase(req.getPromoCode()) && promolist.getStatus() == 0 && promolist.getRemainingValue() <= promolist.getMaxAllowedValue() && promolist.getRemainingValue() > 0 && promolist.getPromoCodeValue() <= promolist.getRemainingValue()) {
                            System.out.println("if condition true");
                            String updateUserInfo = "UPDATE user_info SET availableBalance = availableBalance+" + promolist.getPromoCodeValue() + " WHERE id = ?";

                            int promoUpdate = jdbcTemplate.update(updatePromo, promolist.getId());
                            System.out.println("game_promo_master updated with status==>" + promoUpdate);
                            int userInfoUpdate = jdbcTemplate.update(updateUserInfo, req.getUserId());
                            System.out.println("user_info updated with status==>" + userInfoUpdate);
                            String updateLedger = "INSERT INTO game_user_ledger (userId,transBalance,availableBalance,userRank,transType,transDescription,status,createdDate) values (?,?,(SELECT availableBalance FROM user_info WHERE id = " + req.getUserId() + "),?,?,?,?,?) ";
                            int ledgerUpdate = jdbcTemplate.update(updateLedger, new Object[]{req.getUserId(), promolist.getPromoCodeValue(), 0, "Credit", "Credit by PromoCode", 0, new Timestamp(System.currentTimeMillis())});
                            System.out.println("user_ledger updated with status==>" + ledgerUpdate);
                            if (promoUpdate > 0 && userInfoUpdate > 0 && ledgerUpdate > 0) {
                                res.setLoginFlag(loginFlag);
                                //res.setUserToken(req.getUserToken());
                                res.setUserInfo(getUserProfile(req.getUserId()).getUserInfo());
                                res.setStatus("Success");
                                res.setMessage("Promo Code value added Successfully");
                                res.setStatusCode(0);

                            } else {
                                res.setStatus("Fail");
                                res.setMessage("Something went worng");
                                res.setStatusCode(1);
                            }
                        } else {
                            res.setStatus("Fail");
                            res.setMessage("Promo Code is invalid or out of range");
                            res.setStatusCode(-3);
                        }
                    }
                }
            } catch (Exception e) {
                res.setStatus("fail");
                res.setMessage("Application Error");
                res.setStatusCode(-1);
                e.printStackTrace();
            }
        } else {
            res.setLoginFlag(loginFlag);
            res.setStatus("Success");
            res.setMessage("User Already login on another device");
            res.setStatusCode(-2);

        }
        return res;
    }

    @Override
    public GenericResponse updatePassword(String loginId, String password, String oldPassword) {
        GenericResponse res = new GenericResponse();
        String checkUser = "SELECT password FROM user_login WHERE loginId = ?";
        String query = "UPDATE user_login SET password = ? WHERE loginId = ? AND password = ?";
        String existPassword = null;
        try {
            existPassword = jdbcTemplate.queryForObject(checkUser, String.class, new Object[]{loginId});
            if (existPassword.equals(oldPassword)) {
                jdbcTemplate.update(query, new Object[]{password, loginId, oldPassword});
                res.setStatus("Success");
                res.setMessage("Password successfully changed");
                res.setStatusCode(0);

            } else {
                res.setStatus("Success");
                res.setMessage("Password does not matched");
                res.setStatusCode(1);

            }

        } catch (Exception e) {
            res.setStatus("Fail");
            res.setMessage("Application Error");
            res.setStatusCode(-1);
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public GenericResponse forgetPassword(String loginId) {
        GenericResponse res = new GenericResponse();
        String checkUser = "SELECT password FROM user_login WHERE loginId = ?";
        String updateOTP = "UPDATE user_login SET password = ? WHERE loginId = ?";
        String password = null;
        String otp = null;
        try {
            try {
                password = jdbcTemplate.queryForObject(checkUser, String.class, new Object[]{loginId});
            } catch (Exception e) {
                e.printStackTrace();
                password = null;
            }
            if (password != null) {
                otp = commondao.generateRandom();
                System.out.println("OTP==>  " + otp);
                commondao.sendEmail(loginId, otp);
                jdbcTemplate.update(updateOTP, new Object[]{otp, loginId});
                res.setStatus("Success");
                res.setMessage("Data inserted in DB");
                res.setStatusCode(0);

            } else {
                res.setStatus("Success");
                res.setMessage("Entered LoginId does not exist");
                res.setStatusCode(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus("Fail");
            res.setMessage("Application Error");
            res.setStatusCode(-1);
        }
        return res;
    }

}
