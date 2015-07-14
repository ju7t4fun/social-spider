package com.epam.lab.spider.controller.command.controller;

import com.epam.lab.spider.controller.command.ActionCommand;
import com.epam.lab.spider.job.util.UserUnlock;
import com.epam.lab.spider.model.db.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hell-engine on 7/14/2015.
 */
public class UnlockCommand implements ActionCommand {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = null;
        {
            Object userObject = request.getSession().getAttribute("user");
            if(userObject!=null && userObject instanceof User)user = (User) userObject;
            if(user==null) {
                response.sendError(401);
                return;
            }
        }
        boolean result = UserUnlock.forceFullUserUnlock(user.getId());
        if(result){
            response.setStatus(200);
            return;
        }else{
            response.setStatus(204);
            return;
        }
    }
}
