/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.dao;

import com.ym.model.AccelerationRequest;
import com.ym.model.GameHistoryResponse;
import com.ym.model.GameInstanceUsers;
import com.ym.model.GameMaster;
import com.ym.model.GameResponse;
import com.ym.model.GenericResponse;
import com.ym.model.HistoryRequest;
import com.ym.model.ReadyToPlayResponse;
import com.ym.model.UserRankResponse;
import com.ym.model.UserRegRequest;
import java.util.List;

/**
 *
 * @author Mathur
 */
public interface GameDao {

    public GenericResponse addNewGame(GameMaster gameinfo);

    public GameResponse getGames(int userId, String userToken);

    public GenericResponse updateGames(int userInGame, int status, int maxPoints, int GameId);

    public ReadyToPlayResponse readyToPlay(int gameId, int userId, String userToken);

    public int[] addAcceleration(AccelerationRequest req);

    public UserRankResponse getUserRankList(AccelerationRequest req);
    
    public UserRankResponse getUserRank(AccelerationRequest req);

    public List<UserRegRequest> getUsersbyGameId(GameInstanceUsers req);
    
    public GenericResponse userLeft(GameInstanceUsers req);
    
    public GenericResponse requestForPromo(GameInstanceUsers req);
    
    public GameHistoryResponse getGameHistory(HistoryRequest req);
    
    public GenericResponse trackWaitingUsers(int userId,int isHome);
    
    


}
