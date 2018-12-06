/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao.impl;

import com.ym.dao.CommonDao;
import com.ym.dao.GameDao;
import com.ym.dao.UserDao;
import com.ym.model.Acceleration;
import com.ym.model.AccelerationRequest;
import com.ym.model.GameHistoryResponse;
import com.ym.model.GameInstance;
import com.ym.model.GameInstanceUsers;
import com.ym.model.GameLedger;
import com.ym.model.GameMaster;
import com.ym.model.GameResponse;
import com.ym.model.GenericResponse;
import com.ym.model.HistoryRequest;
import com.ym.model.ReadyToPlayResponse;
import com.ym.model.UserLedger;
import com.ym.model.UserRank;
import com.ym.model.UserRankResponse;
import com.ym.model.UserRegRequest;
import com.ym.model.UserResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 *
 * @author Mathur
 */
public class GameDaoImple implements GameDao {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private final JdbcTemplate jdbcTemplate;

    static final String statTime = "GAME_START_TIME";
    static final String exitTime = "GAME_EXIT_TIME";
    static final String shakingTime = "GAME_SHAKING_TIME";

    public GameDaoImple(DataSource datasource) {
        jdbcTemplate = new JdbcTemplate(datasource);
    }
    @Autowired
    UserDao userdao;
    @Autowired
    CommonDao commondao;
    String server_key = "AAAADtwqwWY:APA91bEJ3x4Gms6CWtZymYGaRskO-rleVRhFZ0Z4lKDTnxfDaP8UcXy9A-oR_Pi-hRP6-zqFXPtdbjdqezsQmqO3pzlIAjq9CJwI_NGL6Ivc0KbkWaGCUxww4PqbobyVdzwYRqD3iiRo";

    @Override
    public GenericResponse addNewGame(GameMaster gameinfo) {
        GenericResponse res = new GenericResponse();
        int exist = 0;
        String checkquery = "SELECT points from game_master WHERE points = ?";
        String query = "INSERT INTO game_master (description,points,status,createdDate,updatedDate) VALUES (?,?,?,?,?)";
        try {
            try {
                exist = jdbcTemplate.queryForObject(checkquery, Integer.class, new Object[]{gameinfo.getPoints()});
            } catch (Exception e) {

                exist = 0;
            }
            System.out.println("points==> " + exist);
            if (exist == 0) {

                int update = jdbcTemplate.update(query, gameinfo.getDescription(), gameinfo.getPoints(), gameinfo.getStatus(), new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
                if (update > 0) {
                    res.setStatus("Succes");
                    res.setMessage("New Game Added Successfully");
                    res.setStatusCode(0);
                } else {
                    res.setStatus("Fail");
                    res.setMessage("Can not add New Game ");
                    res.setStatusCode(1);
                }
            } else {
                res.setStatus("Fail");
                res.setMessage("This Game already exist");
                res.setStatusCode(2);
            }
        } catch (Exception e) {
            res.setStatus("Fail");
            res.setMessage("Application Error");
            res.setStatusCode(-1);
        }
        return res;
    }

    @Override
    public GameResponse getGames(int userId, String userToken) {
        GameResponse res = new GameResponse();
        int usersInGame = 0;
        System.out.println("getGame API called for gameList  ,  UserId ==> " + userId);
        String query = "SELECT id,points,userInGame from game_master";
        System.out.println("UserToken ==> " + userToken);
        String loginFlag = commondao.checkOtherLogin(userId, userToken);
        System.out.println("  Login Falg ==> " + loginFlag);
        if (loginFlag.equalsIgnoreCase("N")) {
            try {
                List<GameMaster> games = jdbcTemplate.query(query, new GamePointsMapper());
                System.out.println("GameList Size ==>  " + games.size());
                UserResponse userProfile = userdao.getUserProfile(userId);
                for (GameMaster game : games) {
                    usersInGame = getUserInGame(game.getId());
                    System.out.println("User In Game==> " + usersInGame);
                    if (usersInGame >= 0) {
                        game.setWaitingUser(usersInGame);
                    }
                    // games.add(game);
                }

                res.setGameinfo(games);
                res.setAvailableBalance(userProfile.getUserInfo().getAvailableBalance());
                res.setLoginFlag(loginFlag);
                res.setStatus("Success");
                res.setMessage("Game Fecthed Successfully");
                res.setStatusCode(0);

            } catch (Exception e) {
                e.printStackTrace();
                res.setStatus("Fail");
                res.setMessage("Cannot fetch Game Application Error");
                res.setStatusCode(-1);

            }
        } else {
            res.setLoginFlag(loginFlag);
            res.setStatus("Success");
            res.setMessage("User already login on another device");
            res.setStatusCode(-2);
        }
        return res;
    }

    private int getUserInGame(int gameId) {
        String query = "SELECT noOfUsersJoin FROM game_instance WHERE gameId = ? AND status = 0";
        int userInGame = 0;
        try {

            userInGame = jdbcTemplate.queryForObject(query, new Object[]{gameId}, Integer.class);

        } catch (Exception e) {

            userInGame = 0;
        }
        return userInGame;
    }

    @Override
    public GenericResponse updateGames(int userInGame, int status, int maxPoints, int gameId) {
        GenericResponse res = new GenericResponse();
        String query = "UPDATE game_master SET userInGame = ?, status = ?, maxPoints = ?, updatedDate = ? WHERE id = ?";
        try {
            int update = jdbcTemplate.update(query, userInGame, status, maxPoints, new Timestamp(System.currentTimeMillis()), gameId);
            if (update > 0) {
                res.setStatus("Success");
                res.setMessage("Game Data updated Successfully");
                res.setStatusCode(0);
            } else {
                res.setStatus("Fail");
                res.setMessage("cannot update Game Data");
                res.setStatusCode(1);
            }
        } catch (Exception e) {
            res.setStatus("Fail");
            res.setMessage("cannot update Game Data Application Error");
            res.setStatusCode(-1);
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public ReadyToPlayResponse readyToPlay(int gameId, int userId, String userToken) {
        int status = 0;

        System.out.println("ReadyTo Play API Called...");
        String loginFlag = commondao.checkOtherLogin(userId, userToken);
        System.out.println("Login Flag ==> " + loginFlag);
        ReadyToPlayResponse res = new ReadyToPlayResponse();
        //int existInst = checkInstance(gameId, userId);
        // System.out.println("  existInstId===> " + existInst);
//        String checkUser = "SELECT status FROM game_instance_users WHERE gameId = ? AND userId = ? AND instanceId = ?";
//        try{
//            status = jdbcTemplate.queryForObject(checkUser, Integer.class, ExistInst);
//        }catch(Exception e){ status = 0;}
        // if (existInst == 0) {

        String query = "INSERT INTO game_instance_users (gameId,instanceId,userId,status,joiningDate) VALUES (?,?,?,?,?)";
        if (loginFlag.equals("N")) {
            int update = 0;
            try {
                int instanceId = updateIntanceId(gameId);

                if (checkUserStatus(gameId, userId, instanceId)) {

                    String updateUser = "UPDATE game_instance_users SET STATUS = 0 WHERE gameId = ? AND instanceId = ? AND userId = ?";

                    update = jdbcTemplate.update(updateUser, new Object[]{gameId, instanceId, userId});
                    System.out.println("status updated in game_instance_users with status==> " + update);
                } else {
                    update = jdbcTemplate.update(query, gameId, instanceId, userId, status, new Timestamp(System.currentTimeMillis()));
                    System.out.println("new entry in game_instance_users with status==> " + update);
                }
                if (update > 0) {
                    List<UserRegRequest> userlist = getUserListbyGameId(gameId, instanceId);
                    commondao.pushNotification(server_key, userlist, userId);
                    commondao.notifyUser(server_key, commondao.getHomePageUsers());
                    res.setInstanceId(instanceId);
                    res.setGameStartTime(commondao.getValueByParam(statTime));
                    res.setGameShakingTime(commondao.getValueByParam(shakingTime));
                    res.setGameExitTime(commondao.getValueByParam(exitTime));
                    res.setUsers(userlist);
                    res.setStatus("Success");
                    res.setMessage("You are ready to play Game");
                    res.setStatusCode(0);
                } else {
                    res.setStatus("Fail");
                    res.setMessage("Something went wrong");
                    res.setStatusCode(1);
                }

            } catch (Exception e) {
                e.printStackTrace();
                res.setStatus("Fail");
                res.setMessage("Application Error");
                res.setStatusCode(-1);
            }
//            } else {
//                res.setStatus("Fail");
//                res.setMessage("User already Login in other device");
//                res.setStatusCode(-2);
//            }
        } else {
            res.setStatus("Fail");
            res.setMessage("User already playing on same Instance");
            res.setStatusCode(-3);
        }

        System.out.println(" Returning  ready to play Response");
        return res;
    }

    public List<UserRegRequest> getUserListbyGameId(int gameId, int instanceId) {
        System.out.println("getUserListbyGameId called...gameId: " + gameId + ",  instId: " + instanceId);
        List<UserRegRequest> userList = new ArrayList<>();
        String query = "SELECT user_info.id,user_info.userName,user_info.emailId,user_info.mobileNo,user_info.gender,user_info.availableBalance,user_info.imageURL,user_info.promoCode,user_login.fcmToken FROM user_info, user_login WHERE user_info.id=user_login.userId AND  user_info.id IN (SELECT userId FROM game_instance_users WHERE gameId = ? AND instanceId = ? AND status = 0)";
        try {
            userList = jdbcTemplate.query(query, new PlayGameMapper(), gameId, instanceId);
            System.out.println(" return userList with Size : " + userList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public List<UserRegRequest> getUsersbyGameId(GameInstanceUsers req) {
        System.out.println("getUserListbyGameId called...gameId: " + req.getGameId() + ",  instId: " + req.getInstanceId());
        List<UserRegRequest> userList = new ArrayList<>();
        String query = "SELECT user_info.id,user_info.userName,user_info.emailId,user_info.mobileNo,user_info.gender,user_info.availableBalance,user_info.imageURL,user_info.promoCode,user_login.fcmToken FROM user_info, user_login WHERE user_info.id=user_login.userId AND  user_info.id IN (SELECT userId FROM game_instance_users WHERE gameId = ? AND instanceId = ? AND status = 0)";
        try {
            userList = jdbcTemplate.query(query, new PlayGameMapper(), req.getGameId(), req.getInstanceId());
            System.out.println(" return userList with Size : " + userList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    private int updateIntanceId(int gameId) {
        String checkInstance = "SELECT id,noOfUsersJoin,STATUS FROM game_instance WHERE gameId = ? AND status = 0";
        String updateInstance = "UPDATE game_instance SET noOfUsersJoin = ?, status = ?, updatedDate = ? WHERE id = ?";
        List<GameInstance> instanceList = null;
        int instanceId = 0;

        instanceList = jdbcTemplate.query(checkInstance, new GameInstanceMapper(), gameId);
        System.out.println("  Instance List Size ==>" + instanceList.size());

        if (instanceList.size() > 0) {
            for (GameInstance list : instanceList) {
                instanceId = list.getId();
                int users = list.getNoOfUsersJoin();
                int status = list.getStatus();
                if (status == 0) {
                    try {
                        if (users < 4) {
                            users++;
                        }
                        if (users == 4) {
                            status = 1;
                        }
                        int update = jdbcTemplate.update(updateInstance, users, status, new Timestamp(System.currentTimeMillis()), instanceId);
                        System.out.println("instance Updated status ==> " + update);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {

            instanceId = createNewInstance(gameId);
        }

        return instanceId;
    }

    private int createNewInstance(int gameId) {
        String query = "INSERT INTO game_instance (gameId, noOfUsersJoin, status, createdDate, updatedDate) VALUES (?,?,?,?,?)";
        int users = 1;
        int status = 0;
        int instanceId = 0;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            int update = jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
                    ps.setInt(1, gameId);
                    ps.setInt(2, users);
                    ps.setInt(3, status);
                    ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                    ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                    return ps;
                }
            }, keyHolder);
            instanceId = keyHolder.getKey().intValue();
            System.out.println(" Generated Key for new Instance ==> " + instanceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instanceId;
    }

    private int checkInstance(int gameId, int userId) {
        String query = "SELECT instanceId FROM game_instance WHERE gameId = ? AND status = 0";
        // String query = "SELECT instanceId FROM game_instance_users WHERE gameId = ? AND userId = ? AND status = 0";

        int instanceId = 0;

        try {
            instanceId = jdbcTemplate.queryForObject(query, new Object[]{gameId, userId}, Integer.class);

        } catch (Exception e) {
            instanceId = 0;
        }
        return instanceId;
    }

    @Override
    public int[] addAcceleration(AccelerationRequest accReq) {
        int[] update = null;
        String sql = "INSERT INTO game_acceleration(userId,gameId,instanceId,shakeId,xEdge,yEdge,zEdge,timeStamp)VALUES (?,?,?,?,?,?,?,?)";
        try {
            update = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Acceleration acc = accReq.getAcc().get(i);
                    ps.setInt(1, accReq.getUserId());
                    ps.setInt(2, accReq.getGameId());
                    ps.setInt(3, accReq.getInstanceId());
                    ps.setInt(4, accReq.getShakeId());
                    ps.setDouble(5, acc.getXEdge());
                    ps.setDouble(6, acc.getYEdge());
                    ps.setDouble(7, acc.getZEdge());
                    ps.setString(8, sdf.format(new Timestamp(Long.parseLong(acc.getTimestamp()))));
                }

                @Override
                public int getBatchSize() {
                    return accReq.getAcc().size();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return update;
    }

    @Override
    public synchronized UserRankResponse getUserRankList(AccelerationRequest req) {
        UserRankResponse res = new UserRankResponse();
        List<AccelerationRequest> datalist;
        List<GameInstanceUsers> userlist;
        List<UserRank> listToSort = new ArrayList<>();
        List<UserRank> sortedList = new ArrayList<>();

        List<UserRank> newList = new ArrayList<>();
        Map<Integer, Double> data = new HashMap<Integer, Double>();

        double gamePoint = 0;
        double xEdge = 0;
        double yEdge = 0;
        double zEdge = 0;
        double totel = 0;
        double percent = 0;
        double grandtotel = 0;
        DecimalFormat df = new DecimalFormat("##.##");

        String query = "SELECT userId from game_instance_users WHERE gameId = ? AND instanceId = ? ";

        try {
            if (checkInstStatus(req.getGameId(), req.getInstanceId())) {
                System.out.println("ifpart");
                userlist = jdbcTemplate.query(query, new PointsMapper(), req.getGameId(), req.getInstanceId());
                for (GameInstanceUsers list : userlist) {
                    String sql = "SELECT COUNT(DISTINCT (shakeId)) AS shakeId,SUM(ABS(xEdge)) AS xEdge,SUM(ABS(yEdge)) AS yEdge,SUM(ABS(zEdge)) AS zEdge FROM game_acceleration WHERE gameId = ? AND userId = ? AND instanceId =?";
                    try {
                        datalist = jdbcTemplate.query(sql, new AccelerationMapper(), req.getGameId(), list.getUserId(), req.getInstanceId());
                        if (!datalist.isEmpty()) {
                            System.out.println(" List fetch from DB with Size==> " + datalist.size());
                            for (AccelerationRequest lst : datalist) {
//                         xEdge = xEdge + Math.abs(lst.getxEdge());
//                         yEdge = yEdge + Math.abs(lst.getyEdge());
//                         zEdge = zEdge + Math.abs(lst.getzEdge());
                                xEdge = lst.getXEdge();
                                yEdge = lst.getYEdge();
                                zEdge = lst.getZEdge();
                                totel = xEdge + yEdge + zEdge;
                                grandtotel = grandtotel + totel;
                                data.put(list.getUserId(), totel);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                int maxGamePoint = commondao.getGamePoints(req.getGameId());
                for (Map.Entry<Integer, Double> entry : data.entrySet()) {
                    percent = entry.getValue() * 100 / grandtotel;
                    // gamePoint = (maxGamePoint * percent) / 100;

                    listToSort.add(new UserRank(entry.getKey(), 0, percent));

                    //gamePoint = Double.parseDouble(df.format(gamePoint));
                    //gamePoint = df.format(gamePoint); 
                    // addHistory(req.getGameId(), entry.getKey(), req.getInstanceId(), gamePoint);
                    // System.out.println("Final Game Point by user==>" + gamePoint);
                }
                //int update = jdbcTemplate.update(cal, req.getGameId(), req.getInstanceId(), 1);
                System.out.println("List To sort size ==>  " + listToSort.size());
                Collections.sort(listToSort, Collections.reverseOrder(new PercentComparator()));
                int count = 0;
                boolean instStatus = false;
                instStatus = checkInstStatus(req.getGameId(), req.getInstanceId());
                UserResponse userProfile = null;
                for (UserRank rankList : listToSort) {
                    count++;

                    int earnedPoints = getEarnedPoints(count, req.getGameId());
                    if (instStatus) {
                        int updateinstStatus = updateInstStatus(req.getGameId(), req.getInstanceId());
                        String updateUserBal = "UPDATE user_info SET availableBalance = availableBalance+" + earnedPoints + " WHERE id = ?";
                        int userInfoBal = jdbcTemplate.update(updateUserBal, new Object[]{rankList.getUserId()});
                        userProfile = userdao.getUserProfile(rankList.getUserId());
                        String updateUserLedger = "INSERT INTO game_user_ledger (gameId,userId,instanceId,transBalance,availableBalance,userRank,transType,transDescription,status,createdDate) values (?,?,?,?,?,?,?,?,?,?) ";

                        int userLedger = jdbcTemplate.update(updateUserLedger, new Object[]{req.getGameId(), rankList.getUserId(), req.getInstanceId(), earnedPoints, userProfile.getUserInfo().getAvailableBalance(), count, "Credit", "Earned Point", 0, new Timestamp(System.currentTimeMillis())});
                    } else {
                        userProfile = userdao.getUserProfile(rankList.getUserId());
                    }
                    //rankList.setPoint(count);
                    rankList.setUserRank(count);
                    rankList.setPoint(earnedPoints);
                    rankList.setEmailId(userProfile.getUserInfo().getEmailId());
                    rankList.setUserName(userProfile.getUserInfo().getUserName());
                    sortedList.add(rankList);
                    System.out.println("Sorted List Size==>  " + sortedList.size());
                    //  System.out.println("userId " + rankList.getUserId() + ",  User Rank==> " + count + ",Update Status from user Info Bal==> " + userInfoBal + "  , status for User Ledger==> " + userLedger);
                    if (rankList.getUserId() == req.getUserId()) {
                        res.setEarnedPoint(earnedPoints);
                        res.setUserName(userProfile.getUserInfo().getUserName());
                        res.setUserInfo(userProfile.getUserInfo());
                    }
                }
                if (sortedList.size() > 0) {
                    res.setUserRank(sortedList);
                    res.setUserId(req.getUserId());
                    res.setStatus("Success");
                    res.setMessage("Game Successfully Completed");
                    res.setStatusCode(0);
                } else {
                    res.setStatus("Fail");
                    res.setMessage("Something went wrong");
                    res.setStatusCode(1);
                }

            } else {

                //sortedList.clear();
                System.out.println("elsePart");

                System.out.println("  GameId==> " + req.getGameId() + "  instId==> " + req.getInstanceId());

                String queryNew = "SELECT id,gameId,userId,instanceId,transBalance,availableBalance,userRank,transType,transDescription FROM game_user_ledger WHERE gameId = ? AND instanceId = ? AND transType = 'credit'";
                List<UserLedger> ranks = jdbcTemplate.query(queryNew, new Object[]{req.getGameId(), req.getInstanceId()}, new UserRankMapper());
                System.out.println("rankList Size==> " + ranks.size());
                for (UserLedger player : ranks) {
                    UserRank rankData = new UserRank();
                    UserResponse userProfile = userdao.getUserProfile(player.getUserId());
                    System.out.println("player.getUserId() == req.getUserId()" + player.getUserId() + "===" + req.getUserId());
                    if (player.getUserId() == req.getUserId()) {
                        res.setEarnedPoint((int) player.getTransBalance());
                        res.setUserName(userProfile.getUserInfo().getUserName());
                        res.setUserInfo(userProfile.getUserInfo());
                        res.setUserId(player.getUserId());
                    }

                    rankData.setUserRank(player.getUserRank());
                    rankData.setPoint((int) player.getTransBalance());
                    rankData.setEmailId(userProfile.getUserInfo().getEmailId());
                    rankData.setUserName(userProfile.getUserInfo().getUserName());
                    rankData.setUserId(player.getUserId());
                    newList.add(rankData);

                }
                if (newList.size() > 0) {
                    res.setUserRank(newList);
                    res.setUserId(req.getUserId());
                    res.setStatus("Success");
                    res.setMessage("Game Successfully Completed");
                    res.setStatusCode(0);
                } else {
                    res.setStatus("Fail");
                    res.setMessage("Something went wrong");
                    res.setStatusCode(1);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus("Fail");
            res.setMessage("Application Error");
            res.setStatusCode(-1);
        }
        return res;
    }

    @Override
    public GenericResponse trackWaitingUsers(int userId, int isHome) {
        GenericResponse res = new GenericResponse();
        try {
            String query = "INSERT INTO waiting_users_tracker (userId,isHome,updatedTime) VALUES(?,?,?) ON DUPLICATE KEY UPDATE isHome=VALUES(isHome),updatedTime = VALUES(updatedTime)";

            int update = jdbcTemplate.update(query, userId, isHome, new Timestamp(System.currentTimeMillis()));

            if (update > 0) {
                System.out.println("Data updated successfully");
                res.setStatus("Success");
                res.setMessage("User TracKed Succesfully");
                res.setStatusCode(0);
            } else {
                res.setStatus("Fail");
                res.setMessage("Something went wrong");
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

    @Override
    public synchronized UserRankResponse getUserRank(AccelerationRequest req) {
        UserRankResponse res = new UserRankResponse();
        List<AccelerationRequest> accData;
        List<GameInstanceUsers> userlist;
        List<UserRank> listToSort = new ArrayList<>();
        List<UserRank> sortList = new ArrayList<>();

        List<UserRank> newRankList = new ArrayList<>();
        Map<Integer, Double> data = new HashMap<Integer, Double>();

        double gamePoint = 0;
        double xEdge = 0;
        double yEdge = 0;
        double zEdge = 0;
        double totel = 0;
        double percent = 0;
        double grandtotel = 0;
        DecimalFormat df = new DecimalFormat("##.##");

        String query = "SELECT userId from game_instance_users WHERE gameId = ? AND instanceId = ? ";

        try {
            if (checkInstStatus(req.getGameId(), req.getInstanceId())) {
                System.out.println("ifpart");
                userlist = jdbcTemplate.query(query, new PointsMapper(), req.getGameId(), req.getInstanceId());
                for (GameInstanceUsers list : userlist) {
                    String sql = "SELECT COUNT(DISTINCT (shakeId)) AS shakeId,SUM(ABS(xEdge)) AS xEdge,SUM(ABS(yEdge)) AS yEdge,SUM(ABS(zEdge)) AS zEdge FROM game_acceleration WHERE gameId = ? AND userId = ? AND instanceId =?";
                    try {
                        accData = jdbcTemplate.query(sql, new AccelerationMapper(), req.getGameId(), list.getUserId(), req.getInstanceId());
                        if (!accData.isEmpty()) {
                            for (AccelerationRequest lst : accData) {
                                xEdge = lst.getXEdge();
                                yEdge = lst.getYEdge();
                                zEdge = lst.getZEdge();
                                totel = xEdge + yEdge + zEdge;
                                grandtotel = grandtotel + totel;
                                data.put(list.getUserId(), totel);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                int maxGamePoint = commondao.getGamePoints(req.getGameId());
                for (Map.Entry<Integer, Double> entry : data.entrySet()) {
                    percent = entry.getValue() * 100 / grandtotel;
                    listToSort.add(new UserRank(entry.getKey(), 0, percent));

                    //gamePoint = Double.parseDouble(df.format(gamePoint));
                    //gamePoint = df.format(gamePoint); 
                    // addHistory(req.getGameId(), entry.getKey(), req.getInstanceId(), gamePoint);
                    // System.out.println("Final Game Point by user==>" + gamePoint);
                }
                //int update = jdbcTemplate.update(cal, req.getGameId(), req.getInstanceId(), 1);
                Collections.sort(listToSort, Collections.reverseOrder(new PercentComparator()));
                int count = 0;
                boolean instStatus = false;
                instStatus = checkInstStatus(req.getGameId(), req.getInstanceId());
                for (UserRank rankList : listToSort) {
                    count++;
                    UserResponse userProfile = null;
                    int earnedPoints = getEarnedPoints(count, req.getGameId());
                    if (instStatus) {
                        int updateinstStatus = updateInstStatus(req.getGameId(), req.getInstanceId());
                        int updateUserStatus = userUserStatus(req.getInstanceId());
                        String updateUserBal = "UPDATE user_info SET availableBalance = availableBalance+" + earnedPoints + " WHERE id = ?";
                        int userInfoBal = jdbcTemplate.update(updateUserBal, new Object[]{rankList.getUserId()});
                        userProfile = userdao.getUserProfile(rankList.getUserId());
                        String updateUserLedger = "INSERT INTO game_user_ledger (gameId,userId,instanceId,transBalance,availableBalance,userRank,transType,transDescription,status,createdDate) values (?,?,?,?,?,?,?,?,?,?) ";

                        int userLedger = jdbcTemplate.update(updateUserLedger, new Object[]{req.getGameId(), rankList.getUserId(), req.getInstanceId(), earnedPoints, userProfile.getUserInfo().getAvailableBalance(), count, "Credit", "Earned Point", 0, new Timestamp(System.currentTimeMillis())});
                    } else {
                        userProfile = userdao.getUserProfile(rankList.getUserId());
                    }
                    //rankList.setPoint(count);
                    rankList.setUserRank(count);
                    rankList.setPoint(earnedPoints);
                    rankList.setEmailId(userProfile.getUserInfo().getEmailId());
                    rankList.setUserName(userProfile.getUserInfo().getUserName());
                    sortList.add(rankList);
                    //  System.out.println("userId " + rankList.getUserId() + ",  User Rank==> " + count + ",Update Status from user Info Bal==> " + userInfoBal + "  , status for User Ledger==> " + userLedger);
                    if (rankList.getUserId() == req.getUserId()) {
                        res.setEarnedPoint(earnedPoints);
                        res.setUserName(userProfile.getUserInfo().getUserName());
                        res.setUserInfo(userProfile.getUserInfo());
                    }
                }
                if (sortList.size() > 0) {
                    res.setUserRank(sortList);
                    res.setUserId(req.getUserId());
                    res.setStatus("Success");
                    res.setMessage("Game Successfully Completed");
                    res.setStatusCode(0);
                } else {
                    res.setStatus("Fail");
                    res.setMessage("Something went wrong");
                    res.setStatusCode(1);
                }

            } else {

                //sortedList.clear();
                System.out.println("elsePart");

                String queryNew = "SELECT id,gameId,userId,instanceId,transBalance,availableBalance,userRank,transType,transDescription FROM game_user_ledger WHERE gameId = ? AND instanceId = ? AND transType = 'credit'";
                List<UserLedger> ranks = jdbcTemplate.query(queryNew, new UserRankMapper(), new Object[]{req.getGameId(), req.getInstanceId()});
                System.out.println("RankList Size==> " + ranks.size());
                for (UserLedger player : ranks) {
                    UserRank rankData = new UserRank();
                    UserResponse userProfile = userdao.getUserProfile(player.getUserId());
                    System.out.println("player.getUserId() == req.getUserId()" + player.getUserId() + "===" + req.getUserId());
                    if (player.getUserId() == req.getUserId()) {
                        res.setEarnedPoint((int) player.getTransBalance());
                        res.setUserName(userProfile.getUserInfo().getUserName());
                        res.setUserInfo(userProfile.getUserInfo());
                        res.setUserId(player.getUserId());
                    }

                    rankData.setUserRank(player.getUserRank());
                    rankData.setPoint((int) player.getTransBalance());
                    rankData.setEmailId(userProfile.getUserInfo().getEmailId());
                    rankData.setUserName(userProfile.getUserInfo().getUserName());
                    rankData.setUserId(player.getUserId());
                    newRankList.add(rankData);

                }
                if (newRankList.size() > 0) {
                    res.setUserRank(newRankList);
                    res.setUserId(req.getUserId());
                    res.setStatus("Success");
                    res.setMessage("Game Successfully Completed");
                    res.setStatusCode(0);
                } else {
                    res.setStatus("Fail");
                    res.setMessage("Something went wrong");
                    res.setStatusCode(1);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus("Fail");
            res.setMessage("Application Error");
            res.setStatusCode(-1);
        }
        return res;
    }

    @Override
    public GenericResponse userLeft(GameInstanceUsers req) {
        GenericResponse res = new GenericResponse();
        int status = 1;
        System.out.println("gameId=>  " + req.getGameId() + ",  userId==> " + req.getUserId() + ",  instId==> " + req.getInstanceId());
        String instUser = "UPDATE game_instance_users SET status = ? WHERE gameId = ? AND instanceId = ? AND userId = ?";
        String updateInst = "UPDATE game_instance SET noOfUsersJoin = noOfUsersJoin-1 WHERE gameId = ? AND id = ? AND noOfUsersJoin > 0";
        try {

            int instUserupdate = jdbcTemplate.update(instUser, new Object[]{status, req.getGameId(), req.getInstanceId(), req.getUserId()});
            int instUpdate = jdbcTemplate.update(updateInst, new Object[]{req.getGameId(), req.getInstanceId()});
            System.out.println("update status==> " + instUserupdate + " ,==>" + instUpdate);
            if (instUserupdate > 0 && instUpdate > 0) {
                List<UserRegRequest> userlist = getUserListbyGameId(req.getGameId(), req.getInstanceId());
                commondao.pushNotification(server_key, userlist, req.getUserId());

                List<UserRegRequest> homePageUsers = commondao.getHomePageUsers();
                System.out.println("ready to send notification to Home Page users.. with size==> " + homePageUsers.size());
                commondao.notifyUser(server_key, homePageUsers);
                res.setStatus("Success");
                res.setMessage("User left and Instance Update Successfully");
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
    public GenericResponse requestForPromo(GameInstanceUsers req) {
        GenericResponse res = new GenericResponse();
        String loginFlag = commondao.checkOtherLogin(req.getUserId(), req.getUserToken());
        if (loginFlag.equalsIgnoreCase("N")) {
            String query = "UPDATE user_login SET promoRequest = 1  WHERE userId = ?";
            try {
                int update = jdbcTemplate.update(query, new Object[]{req.getUserId()});
                System.out.println("UserId==> for promo Code==> " + req.getUserId());
                if (update > 0) {
                    res.setLoginFlag(loginFlag);
                    res.setStatus("Success");
                    res.setMessage("Request recieved for Promo Code ");
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
        } else {
            res.setLoginFlag(loginFlag);
            res.setStatus("Success");
            res.setMessage("User already login on another Device");
            res.setStatusCode(-2);
        }
        return res;
    }

    @Override
    public GameHistoryResponse getGameHistory(HistoryRequest req) {
        GameHistoryResponse res = new GameHistoryResponse();
        List<GameLedger> userdata;
        int currentGameId = 0;
        List<GameLedger> resdata = new ArrayList<>();
        int pageWith = 0;

        if (req.getStartWith() == 1) {

            res.setTotalPage(commondao.getPageCount(req.getUserId(), req.getSize()));
            pageWith = 0;
        } else {
            pageWith = ((req.getStartWith() - 1) * req.getSize());
        }
        System.out.println(" req==> " + req.getStartWith() + " size==> " + req.getSize() + " pagewith==> " + pageWith);
        String query = "SELECT * FROM game_user_ledger WHERE userId = ? limit " + pageWith + "," + req.getSize();
        // String loginFlag = commondao.checkOtherLogin(req.getUserId(), req.getUserToken());
        // if (loginFlag.equalsIgnoreCase("N")) {s
        try {
            userdata = jdbcTemplate.query(query, new Object[]{req.getUserId()}, new GameHistoryMapper());
            System.out.println("userData Size==> " + userdata.size() + " userId==> " + req.getUserId());
            if (!userdata.isEmpty()) {
                for (GameLedger item : userdata) {
                    GameLedger itemToSend = new GameLedger();
                    if (item.getGameId() != 0) {

                    }

                }

                // res.setLoginFlag(loginFlag);
                res.setUserHistory(resdata);
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
//        } else {
//            res.setLoginFlag(loginFlag);
//            res.setStatus("Success");
//            res.setMessage("User already login on another device");
//            res.setStatusCode(-2);
//        }
        return res;
    }

    private int getEarnedPoints(int rank, int gameId) {
        int earnedPoints = 0;
        String query = "SELECT rank" + rank + " FROM game_dist_matrix WHERE gameId =?";
        try {

            earnedPoints = jdbcTemplate.queryForObject(query, Integer.class, new Object[]{gameId});

        } catch (Exception e) {
            e.printStackTrace();
        }
        return earnedPoints;
    }

    private boolean checkInstStatus(Integer gameId, Integer instanceId) {
        String query = "SELECT status FROM game_instance WHERE gameId = ? AND id = ?";
        boolean status = false;
        System.out.println("gameId==> " + gameId + ",  instId==> " + instanceId);
        try {

            int instStatus = jdbcTemplate.queryForObject(query, new Object[]{gameId, instanceId}, Integer.class);
            System.out.println("game  instStatus ===> " + instStatus);
            if (instStatus != 3) {
                status = true;
            }

        } catch (Exception e) {

            return status;
        }
        return status;
    }

    private int updateInstStatus(Integer gameId, Integer instanceId) {
        String query = "UPDATE game_instance SET status = 3 WHERE gameId = ? AND id = ?";
        int update = 0;
        try {

            update = jdbcTemplate.update(query, gameId, instanceId);
        } catch (Exception e) {
            return update;
        }
        return update;

    }

    private boolean checkUserStatus(int gameId, int userId, int instanceId) {
        String query = " SELECT STATUS FROM game_instance_users WHERE userId = ? AND gameId = ? AND instanceId = ?";
        boolean status = false;
        try {
            Integer userStatus = jdbcTemplate.queryForObject(query, new Object[]{userId, gameId, instanceId}, Integer.class);
            if (userStatus > 0) {
                status = true;
            }
        } catch (Exception e) {
            status = false;
        }
        System.out.println("UserStatus in gameInstance user==> " + status);
        return status;
    }

    private int userUserStatus(int instanceId) {
        int update = 0;
        String query = "UPDATE game_instance_users SET STATUS = 1 WHERE instanceId = ?";

        try {
            update = jdbcTemplate.update(query, new Object[]{instanceId});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return update;
    }

   
}
