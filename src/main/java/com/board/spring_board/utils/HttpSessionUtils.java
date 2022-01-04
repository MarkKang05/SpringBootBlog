package com.board.spring_board.utils;

import com.board.spring_board.model.User;

import javax.servlet.http.HttpSession;

public class HttpSessionUtils {

    public static final String USER_SESSION_KEY = "sessionedUser";

    public static User getUserFromSession(HttpSession session){
        User sessionedUser = (User) session.getAttribute(USER_SESSION_KEY);
        if (sessionedUser == null)
            return null;

        return sessionedUser;

    }

    public static boolean isLoginUser(HttpSession session){
        if(getUserFromSession(session) == null)
            return false;
        return true;

    }

}
