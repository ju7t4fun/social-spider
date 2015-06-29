package com.epam.lab.spider.controller.command.post;

import com.epam.lab.spider.controller.command.ActionCommand;
import com.epam.lab.spider.model.db.entity.NewPost;
import com.epam.lab.spider.model.db.service.NewPostService;
import com.epam.lab.spider.model.db.service.ServiceFactory;
import com.epam.lab.spider.model.db.service.WallService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ����� on 6/28/2015.
 */
public class FillQueuededPostsCommand implements ActionCommand {

    String GLOBAL_SEARCH_TERM ="";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSONObject jsonResult = new JSONObject();
        int listDisplayAmount = 5;
        int start = 0;
        String pageNo = request.getParameter("iDisplayStart");
        String pageSize = request.getParameter("iDisplayLength");


        if (pageNo != null) {
            start = Integer.parseInt(pageNo);
            if (start < 0) {
                start = 0;
            }
        }
        if (pageSize != null) {
            try {
                listDisplayAmount = Integer.parseInt(pageSize);
            } catch (Exception e) {
                listDisplayAmount = 5;
            }
            if (listDisplayAmount < 3 || listDisplayAmount > 50) {
                listDisplayAmount = 10;
            }
        }
        int totalRecords = getTotalRecordCount();
        GLOBAL_SEARCH_TERM = request.getParameter("sSearch");

        try {
            jsonResult = getPersonDetails(start,listDisplayAmount,totalRecords, request);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");
        PrintWriter out = response.getWriter();
        out.print(jsonResult);
    }

    public JSONObject getPersonDetails(int start, int listDisplayAmount, int totalRecords, HttpServletRequest request)
            throws SQLException, ClassNotFoundException {

        int totalAfterSearch = totalRecords;
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        String searchSQL = "";
        String sql = "SELECT  new_post.post_id AS post_id , " +
                " new_post.wall_id AS wall_id , " +
                " new_post.post_time AS post_time, " +
                " new_post.state AS state " +
                " FROM  post JOIN new_post ON " +
                " new_post.post_id=post.id  " +
                " AND new_post.deleted=false " +
                "AND ( new_post.state='CREATED' OR new_post.state='ERROR' )";
        String globeSearch = " AND  post.message LIKE '%" + GLOBAL_SEARCH_TERM + "%' ";

        if (GLOBAL_SEARCH_TERM != "") {
            searchSQL = globeSearch;
        }

        sql += searchSQL;
        sql += " limit " + start + ", " + listDisplayAmount;
        List<NewPost> resList = ServiceFactory.getInstance().create(NewPostService.class).getAllWithQuery(sql);
        if (resList != null) {
            for (int i = 0; i < resList.size(); ++i) {
                JSONArray ja = new JSONArray();
                NewPost currPost = resList.get(i);
                //message
                String msg;
                if (currPost.getPost().getMessage().length() > 20) {
                    msg = currPost.getPost().getMessage().substring(0, 19);
                } else {
                    msg = currPost.getPost().getMessage();
                }
                //group name
                ja.put(msg);
                try {
                    WallService wallService = ServiceFactory.getInstance().create(WallService.class);
                    String groupName = wallService.getById(currPost.getWallId()).getOwner().getName();
                    ja.put(groupName);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getMessage());
                    ja.put("Empty Wall!");
                }
                ja.put(currPost.getPostTime());
                ja.put(currPost.getPostId());
                array.put(ja);
            }
        }
        String query = "SELECT COUNT(*) FROM new_post WHERE deleted=false AND ( state='CREATED' OR state='ERROR' ) ";
        //for pagination
        if (GLOBAL_SEARCH_TERM != "") {
            query += searchSQL;
            NewPostService npostServ = ServiceFactory.getInstance().create(NewPostService.class);
            totalAfterSearch = npostServ.getCountWithQuery(query);
        }
        try {
            result.put("iTotalRecords", totalRecords);
            result.put("iTotalDisplayRecords", totalAfterSearch);
            result.put("aaData", array);
        } catch (Exception e) {

        }
        return result;
    }

    public int getTotalRecordCount() {
        String sql = "SELECT COUNT(*) FROM new_post WHERE deleted=false AND ( state='CREATED' OR state='ERROR' ) ";
        NewPostService serv = ServiceFactory.getInstance().create(NewPostService.class);
        int totalRecords = serv.getCountWithQuery(sql);
        return totalRecords;
    }
}