package com.epam.lab.spider.controller.command.category;

import com.epam.lab.spider.controller.command.ActionCommand;
import com.epam.lab.spider.model.db.entity.Category;
import com.epam.lab.spider.model.db.service.CategoryService;
import com.epam.lab.spider.model.db.service.ServiceFactory;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Marian Voronovskyi on 30.06.2015.
 */
public class AddCategoryCommand implements ActionCommand {
    private static ServiceFactory factory = ServiceFactory.getInstance();
    private static CategoryService service = factory.create(CategoryService.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("category");
        JSONObject json = new JSONObject();
        try {
            Category category = new Category();
            category.setName(name);

            if (service.insert(category)) {
                json.put("status", "success");
                json.put("msg", "Inserted category");
            } else {
                json.put("status", "error");
                json.put("msg", "Error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }
}
