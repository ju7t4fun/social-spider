package com.epam.lab.spider.controller.servlet.owner;

import com.epam.lab.spider.controller.command.ActionFactory;
import com.epam.lab.spider.controller.command.owner.ShowBindAccountCommand;
import com.epam.lab.spider.controller.command.post.ShowAllPostsCommand;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Орест on 23/06/2015.
 */
public class OwnerServlet extends HttpServlet {
    private static ActionFactory factory = new OwnerActionFactory();

    private static class OwnerActionFactory extends ActionFactory {

        public OwnerActionFactory() {
            commands = new HashMap<>();
            commands.put("default", new ShowBindAccountCommand());
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        factory.action(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        factory.action(request, response);
    }
}