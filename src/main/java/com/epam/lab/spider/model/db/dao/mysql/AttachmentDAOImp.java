package com.epam.lab.spider.model.db.dao.mysql;

import com.epam.lab.spider.model.db.dao.AttachmentDAO;
import com.epam.lab.spider.model.db.dao.savable.SavableCRUDUtil;
import com.epam.lab.spider.model.db.dao.savable.exception.InvalidEntityException;
import com.epam.lab.spider.model.db.dao.savable.exception.ResolvableDAOException;
import com.epam.lab.spider.model.db.dao.savable.exception.UnsupportedDAOException;
import com.epam.lab.spider.model.db.entity.Attachment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sasha on 12.06.2015.
 */
public class AttachmentDAOImp extends BaseDAO implements AttachmentDAO {

    private static final String SQL_INSERT_QUERY = "INSERT INTO attachment (url, post_id, type, deleted, mode) VALUES " +
            "(?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_QUERY = "UPDATE attachment SET url = ?, post_id = ?, type = ?, deleted " +
            "= ?, mode=?  WHERE id = ?";
    //private static final String SQL_DELETE_QUERY = "DELETE FROM attachment WHERE id = ?";
    private static final String SQL_DELETE_QUERY = "UPDATE attachment SET deleted = true WHERE id = ?";
    private static final String SQL_GET_ALL_QUERY = "SELECT * FROM attachment WHERE deleted = false";
    private static final String SQL_GET_BY_ID_QUERY = "SELECT * FROM attachment WHERE id = ? AND deleted = false";
    private static final String SQL_GET_BY_POST_ID_QUERY = "SELECT * FROM attachment WHERE post_id = ? AND deleted = " +
            "false";
    private static final String SQL_DELETE_BY_POST_ID_QUERY = "UPDATE attachment SET deleted = true WHERE post_id = ?";



    @Override
    public boolean insert(Connection connection, Attachment attachment) throws SQLException {
        boolean res = changeQuery(connection, SQL_INSERT_QUERY,
                attachment.getPayload(),
                attachment.getPostId(),
                attachment.getType().toString().toUpperCase(),
                attachment.getDeleted(),
                attachment.getMode().toString().toUpperCase());
                attachment.setId(getLastInsertId(connection));
        return res;
    }

    @Override
    public boolean update(Connection connection, int id, Attachment attachment) throws SQLException {
        return changeQuery(connection, SQL_UPDATE_QUERY,
                attachment.getPayload(),
                attachment.getPostId(),
                attachment.getType().toString().toUpperCase(),
                attachment.getDeleted(),
                attachment.getMode().toString().toUpperCase(),
                id);
    }


    @Override
    public boolean delete(Connection connection, int id) throws SQLException {
        return changeQuery(connection, SQL_DELETE_QUERY, id);
    }

    @Override
    public List<Attachment> select(Connection connection, String query, Object... args) throws SQLException {
        List<Attachment> attachments = new ArrayList<>();
        ResultSet rs = selectQuery(connection, query, args);
        Attachment attachment;
        while (rs.next()) {
            attachment = new Attachment();
            attachment.setId(rs.getInt("id"));
            attachment.setPayload(rs.getString("url"));
            attachment.setPostId(rs.getInt("post_id"));
            attachment.setType(Attachment.Type.valueOf(rs.getString("type").toUpperCase()));
            attachment.setDeleted(rs.getBoolean("deleted"));
            attachment.setMode(Attachment.Mode.valueOf(rs.getString("mode").toUpperCase()));
            attachments.add(attachment);
        }
        return attachments;
    }

    @Override
    public List<Attachment> getAll(Connection connection) throws SQLException {
        return select(connection, SQL_GET_ALL_QUERY);
    }

    @Override
    public Attachment getById(Connection connection, int id) throws SQLException {
        return first(select(connection, SQL_GET_BY_ID_QUERY, id));
    }

    @Override
    public List<Attachment> getByPostId(Connection connection, int id) throws SQLException {
        return select(connection, SQL_GET_BY_POST_ID_QUERY, id);
    }

    @Override
    public boolean deleteByPostId(Connection connection, int id) throws SQLException {
        return changeQuery(connection, SQL_DELETE_BY_POST_ID_QUERY, id);
    }

    @Override
    public boolean save(Connection conn, Attachment entity) throws UnsupportedDAOException, ResolvableDAOException, InvalidEntityException {
        return SavableCRUDUtil.saveFromInterface(conn,entity);
    }
}
