/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ym.model;

import java.util.List;

/**
 *
 * @author Mathur
 */
public class GameUsersResponse extends GenericResponse{
    
    private List<UserRegRequest> users;

    public List<UserRegRequest> getUsers() {
        return users;
    }

    public void setUsers(List<UserRegRequest> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "GameUsersResponse{" + "users=" + users + '}';
    }

    
    
    
}
