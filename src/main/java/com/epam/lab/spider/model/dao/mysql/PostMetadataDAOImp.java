package com.epam.lab.spider.model.dao.mysql;

import com.epam.lab.spider.model.dao.BaseDAO;
import com.epam.lab.spider.model.dao.PostMetadataDAO;
import com.epam.lab.spider.model.entity.PostMetadata;
import com.epam.lab.spider.model.vk.Post;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marian Voronovskyi on 12.06.2015.
 */
public class PostMetadataDAOImp extends BaseDAO implements PostMetadataDAO {
    private static final String SQL_INSERT_QUERY = "INSERT INTO post_metadata (like, repost) " +
            "VALUES (?, ?)";
    private static final String SQL_UPDATE_QUERY = "UPDATE post_metadata SET like = ?, repost = ? " +
            "WHERE id = ?";
    private static final String SQL_DELETE_QUERY = "DELETE * FROM post_metadata WHERE id = ?";

    private static final String SQL_GET_ALL_QUERY = "SELECT * FROM post_metadata";
    private static final String SQL_GET_BY_ID_QUERY = "SELECT * FROM post_metadata WHERE id = ?";


    @Override
    public boolean insert(Connection connection, PostMetadata postMetadata) throws SQLException {
        return changeQuery(connection, SQL_INSERT_QUERY,
                postMetadata.getLike(),
                postMetadata.getRepost());
    }

    @Override
    public boolean update(Connection connection, int i, PostMetadata postMetadata) throws SQLException {
        return changeQuery(connection, SQL_UPDATE_QUERY,
                postMetadata.getLike(),
                postMetadata.getRepost(),
                i);
    }

    @Override
    public boolean delete(Connection connection, int id) throws SQLException {
        return changeQuery(connection, SQL_DELETE_QUERY, id);
    }

    @Override
    public List<PostMetadata> select(Connection connection, String query, Object... args) throws SQLException {
        List<PostMetadata> listPostMetadata = new ArrayList<>();
        ResultSet rs = selectQuery(connection, query, args);
        PostMetadata postMetadata;
        while (rs.next()) {
            postMetadata = new PostMetadata();
            postMetadata.setId(rs.getInt("id"));
            postMetadata.setLike(rs.getInt("like"));
            postMetadata.setRepost(rs.getInt("repost"));
            listPostMetadata.add(postMetadata);
        }
        return listPostMetadata;
    }

    @Override
    public List<PostMetadata> getAll(Connection connection) throws SQLException {
        return select(connection, SQL_GET_ALL_QUERY);
    }

    @Override
    public PostMetadata getById(Connection connection, int id) throws SQLException {
        return first(select(connection, SQL_GET_BY_ID_QUERY, id));
    }
}