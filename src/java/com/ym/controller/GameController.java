/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.controller;

import com.ym.dao.CommonDao;
import com.ym.dao.GameDao;
import com.ym.dao.UserDao;
import com.ym.model.AccelerationRequest;
import com.ym.model.GameInstanceUsers;
import com.ym.model.GameMaster;
import com.ym.model.GameResponse;
import com.ym.model.GameUsersResponse;
import com.ym.model.GenericResponse;
import com.ym.model.HistoryRequest;
import com.ym.model.ReadyToPlayResponse;
import com.ym.model.UserRankResponse;
import com.ym.model.UserRegRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 *
 * @author Mathur
 */
@RestController
@EnableWebMvc
public class GameController {

    @Autowired
    public GameDao gamedao;

    @Autowired
    public UserDao userdao;

    @Autowired
    public CommonDao commondao;

    static final String statTime = "GAME_START_TIME";
    static final String exitTime = "GAME_EXIT_TIME";
    static final String shakingTime = "GAME_SHAKING_TIME";

    @RequestMapping(value = "/addNewGame", method = RequestMethod.POST)
    public GenericResponse addGame(@RequestBody GameMaster gameinfo) {
        GenericResponse res = new GenericResponse();
        try {
            res = gamedao.addNewGame(gameinfo);
        } catch (Exception e) {
        }
        return res;
    }

    @RequestMapping(value = "/getGameList", method = RequestMethod.POST)
    public GenericResponse getGames(@RequestBody GameInstanceUsers req) {
        int userId = req.getUserId();
        String userToken = req.getUserToken();
        System.out.println("getGames API called with userId ==> " + userId);
        GameResponse res = new GameResponse();
        try {
            res = gamedao.getGames(userId, userToken);
            System.out.println("Response==> " + res);
        } catch (Exception e) {

        }
        System.out.println("getGames API Retured Response with userId ==> " + userId);
        return res;
    }

    @RequestMapping(value = "/updateGames", method = RequestMethod.POST)
    public GenericResponse updateGame(@RequestBody GameMaster gameinfo) {
        GenericResponse res = new GenericResponse();
        int gameId = gameinfo.getId();
        int userInGame = gameinfo.getUserInGame();
        int status = gameinfo.getStatus();
        int maxPoints = gameinfo.getMaxPoints();

        try {
            res = gamedao.updateGames(userInGame, status, maxPoints, gameId);
        } catch (Exception e) {
        }
        return res;
    }

    @RequestMapping(value = "/readyToPlay", method = RequestMethod.POST)
    public GenericResponse playGame(@RequestBody GameInstanceUsers req) {
        GenericResponse res = new GenericResponse();
        int userId = req.getUserId();
        int gameId = req.getGameId();
        String userToken = req.getUserToken();
        System.out.println("Request param==> userid ==>" + userId + "  gameId==> " + gameId + "token  ==>" + userToken);
        try {
            res = gamedao.readyToPlay(gameId, userId, userToken);
        } catch (Exception e) {
        }
        return res;
    }
    
     @RequestMapping(value = "/trackUser", method = RequestMethod.POST)
    public GenericResponse trackUser(@RequestBody UserRegRequest req) {
        GenericResponse res = new GenericResponse();
         System.out.println("trackUser API called with userId ==> "+req.getUserId()+" isHome==> "+req.getId());
        
        try {
            res = gamedao.trackWaitingUsers(req.getUserId(), req.getId());
        } catch (Exception e) {
        }
         System.out.println("trackUser API returned Response");
        return res;
    }

    @RequestMapping(value = "/getUsersByGameId", method = RequestMethod.POST)
    public GenericResponse getUsers(@RequestBody GameInstanceUsers req) {
        System.out.println("getUsersByGameId API called");
        ReadyToPlayResponse res = new ReadyToPlayResponse();
        List<UserRegRequest> userlist;
        System.out.println("requet===> " + req);
        try {
            userlist = gamedao.getUsersbyGameId(req);
            if (userlist.size() > 0) {
                res.setGameStartTime(commondao.getValueByParam(statTime));
                res.setGameShakingTime(commondao.getValueByParam(shakingTime));
                res.setGameExitTime(commondao.getValueByParam(exitTime));
                res.setUsers(userlist);
                res.setStatus("Success");
                res.setMessage("Game Online users List fetched successfully");
                res.setStatusCode(0);
            } else {
                res.setStatus("fail");
                res.setMessage("Game Online users List can not fetched");
                res.setStatusCode(1);
            }
        } catch (Exception e) {
            res.setStatus("fail");
            res.setMessage("Application Error");
            res.setStatusCode(-1);
        }
        System.out.println("getUsersByGameId API returned Response");
        return res;
    }

    @RequestMapping(value = "/saveAcceleration", method = RequestMethod.POST)
    public GenericResponse saveAcceleration(@RequestBody AccelerationRequest accReq) {

        GenericResponse res = new GenericResponse();
        System.out.println("saveAcceleration API called and AccReq ==>" + accReq);
        try {

            int[] update = gamedao.addAcceleration(accReq);
            if (update.length > 0) {
                res.setMessage("Acceleration Data Added Successfully");
                res.setStatus("success");
                res.setStatusCode(0);
            } else {
                res.setMessage("Acceleration cound't Add");
                res.setStatus("fail");
                res.setStatusCode(1);
            }
        } catch (Exception ex) {
            res.setMessage("Application Error");
            res.setStatus("fail");
            res.setStatusCode(-1);
            ex.printStackTrace();
        }
        System.out.println("saveAcceleration API returned Response");
        return res;
    }

    @RequestMapping(value = "/getUsersRank", method = RequestMethod.POST)
    public UserRankResponse getUserRanks(@RequestBody AccelerationRequest req) {
        System.out.println("getUserRank API called");
        UserRankResponse res = new UserRankResponse();
        try {
            res = gamedao.getUserRankList(req);
        } catch (Exception e) {
        }
        System.out.println("getUserRank API returned Response");
        return res;
    }

    @RequestMapping(value = "/userLeft", method = RequestMethod.POST)
    public GenericResponse userLeft(@RequestBody GameInstanceUsers req) {
        System.out.println("userLeft API called");
        GenericResponse res = new GenericResponse();

        try {
            res = gamedao.userLeft(req);
        } catch (Exception e) {
        }
        System.out.println("userLeft API returned Response");
        return res;
    }

    @RequestMapping(value = "/requestForPromo", method = RequestMethod.POST)
    public GenericResponse requestPromo(@RequestBody GameInstanceUsers req) {
        System.out.println("requestForPromo API called ");
        GenericResponse res = new GenericResponse();

        try {
            res = gamedao.requestForPromo(req);
        } catch (Exception e) {
        }
        System.out.println("requestForPromo API returned Response ");
        return res;
    }

    @RequestMapping(value = "/getGameHistory", method = RequestMethod.POST)
    public GenericResponse gameHistory(@RequestBody HistoryRequest req) {
        System.out.println("getGameHistory API called ");
        GenericResponse res = new GenericResponse();

        try {
            res = gamedao.getGameHistory(req);
        } catch (Exception e) {
        }
        System.out.println("getGameHistory API returned Response ");
        return res;
    }

}
