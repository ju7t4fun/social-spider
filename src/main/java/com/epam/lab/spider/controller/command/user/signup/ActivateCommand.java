package com.epam.lab.spider.controller.command.user.signup;

import com.epam.lab.spider.controller.command.ActionCommand;
import com.epam.lab.spider.controller.utils.hash.HashSHA;
import com.epam.lab.spider.model.db.entity.User;
import com.epam.lab.spider.model.db.service.ServiceFactory;
import com.epam.lab.spider.model.db.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Dmytro on 12.06.2015.
 */
public class ActivateCommand implements ActionCommand {

    private ServiceFactory factory = ServiceFactory.getInstance();
    private UserService userService = factory.create(UserService.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String email = request.getParameter("email");
        String hash = request.getParameter("hash");
        User user = userService.getByEmail(email);

        // Перевіряєм чи користувач існує
        if (user != null) {
            HashSHA sha = new HashSHA();
            // Перевіряє hash (createTime + state)
            if (hash.equals(sha.hash(user.getCreateTime().toString() + user.getState()))) {
                user.setState(User.State.ACTIVATED);
                userService.update(user.getId(), user);
                response.sendRedirect("/");
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

}