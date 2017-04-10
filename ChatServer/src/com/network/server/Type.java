/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.network.server;

/**
 *
 * @author Abed
 */
public class Type {

    public static final byte UNKNOWN_MESSAGE = -2, FAILD_LOGIN = -1,
            LOGIN = 0, LOGOUT = 1, CHAT_MESSAGE = 2, CREATE_GROUP = 3,
            GROUP_MESSAGE = 4, IMAGE = 5, FILE = 6, GROUP_IMAGE = 7,
            GROUP_FILE = 8, ERROR = 9, ALL_USERS = 10, NEW_USER = 11;

}
