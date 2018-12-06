/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.controller;

import com.ym.dao.CommonDao;
import com.ym.dao.UserDao;
import com.ym.model.GameInstanceUsers;
import com.ym.model.GenericResponse;
import com.ym.model.HistoryRequest;
import com.ym.model.UserHistoryResponse;
import com.ym.model.UserInfo;
import com.ym.model.UserLedger;
import com.ym.model.UserRegRequest;
import com.ym.model.UserResponse;
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
public class UserController {
    
    @Autowired
    public UserDao userdao;
    
    @Autowired
    public CommonDao commondao;
    
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public GenericResponse addUser(@RequestBody UserRegRequest req) {
        System.out.println("registration API called");
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse = userdao.registeration(req);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("registration API returned Response");
        return genericResponse;
    }
    
    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    public UserResponse login(@RequestBody UserRegRequest req) {
        System.out.println("UserLogin API called");
        UserResponse response = new UserResponse();
        try {
            response = userdao.userLogin(req);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("UserLogin API returned Response");
        return response;
    }
    
    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public GenericResponse upadateUserProfile(@RequestBody UserRegRequest req) {
        System.out.println("updateProfile API Called..");
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse = userdao.updateUserProfile(req);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("updateProfile API Returned Response..");
        return genericResponse;
    }
    
    @RequestMapping(value = "/updateImage", method = RequestMethod.POST)
    public GenericResponse upadateImage(@RequestBody UserRegRequest req) {
        System.out.println("updateProfile Image API Called..");
        
        
        GenericResponse res = new GenericResponse();
        String loginFlag = commondao.checkOtherLogin(req.getUserId(), req.getUserToken());
        if (loginFlag.equalsIgnoreCase("N")) {
            try {
                System.out.println("Image====> "+req.getImageURL());
                res = userdao.updateProfilePic(req.getImageURL(), req.getUserId());
                res.setLoginFlag(loginFlag);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            res.setLoginFlag(loginFlag);
            res.setStatus("Success");
            res.setMessage("User Already login on another device");
            res.setStatusCode(-2);
        }
        
        System.out.println("updateProfile Image API Returned Response..");
        return res;
    }
    
    @RequestMapping(value = "/getProfile", method = RequestMethod.POST)
    public GenericResponse getUserProfile(@RequestBody UserRegRequest req) {
        GenericResponse genericResponse = new GenericResponse();
        System.out.println("getProfile API called");
        String loginFlag = commondao.checkOtherLogin(req.getId(), req.getUserToken());
        
        System.out.println("User Token ==>" + req.getUserToken() + ",  Login falg==> " + loginFlag);
        if (loginFlag.equalsIgnoreCase("N")) {
            try {
                genericResponse = userdao.getUserProfile(req.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            genericResponse.setLoginFlag(loginFlag);
            genericResponse.setStatus("Success");
            genericResponse.setMessage("User already login on another device");
            genericResponse.setStatusCode(-2);
        }
        System.out.println("getProfile API Returned Response");
        return genericResponse;
    }
    
    @RequestMapping(value = "/getUserHistory", method = RequestMethod.POST)
    public UserHistoryResponse getUserUserHistory(@RequestBody HistoryRequest req) {
        UserHistoryResponse res = new UserHistoryResponse();
        System.out.println("getUserHistory API called");
        int userId = req.getUserId();
        try {
            res = userdao.getUserHistory(req);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("getUserHistory API returned Response");
        return res;
    }
    
    @RequestMapping(value = "/debitAmount", method = RequestMethod.POST)
    public GenericResponse debitAmount(@RequestBody GameInstanceUsers req) {
        GenericResponse res = new GenericResponse();
        System.out.println("debitAmount API called ");
        int userId = req.getUserId();
        try {
            res = userdao.debitAmountForGame(req);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("debitAmount API returned Response");
        return res;
    }
    
    @RequestMapping(value = "/addBalanceByPromo", method = RequestMethod.POST)
    public UserResponse addPromoBal(@RequestBody UserRegRequest req) {
        UserResponse res = new UserResponse();
        System.out.println("addBalanceByPromo API called");
        try {
            res = userdao.addPromoBalance(req);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("addBalanceByPromo API returned Response ");
        return res;
    }
    
    @RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
    public GenericResponse resetPassword(@RequestBody UserRegRequest req) {
        GenericResponse res = new GenericResponse();
        System.out.println("Input==>  " + req.getLoginId());
        res = userdao.forgetPassword(req.getLoginId());
        
        return res;
    }
    
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public GenericResponse updatePassword(@RequestBody UserRegRequest req) {
        GenericResponse res = new GenericResponse();
        System.out.println("Input==>  " + req.getLoginId());
        res = userdao.updatePassword(req.getLoginId(), req.getPassword(), req.getOldPassword());
        
        return res;
    }
    
}
