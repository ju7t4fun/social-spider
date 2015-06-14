package com.epam.lab.spider.model.dao;

import com.epam.lab.spider.model.dao.mysql.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Boyarsky Vitaliy on 12.06.2015.
 */
public class DAOFactory {

    private static Map<Class<?>, BaseDAO> dao = new HashMap<>();
    private static DAOFactory instance = null;

    private DAOFactory() {
        dao.put(AttachmentDAO.class, new AttachmentDAOImp());
        dao.put(CategoryDAO.class, new CategoryDAOImp());
        dao.put(FilterDAO.class, new FilterDAOImp());
        dao.put(NewPostDAO.class, new NewPostDAOImp());
        dao.put(OwnerDAO.class, new OwnerDAOImp());
        dao.put(PostMetadataDAO.class, new PostMetadataDAOImp());
        dao.put(PostDAO.class, new PostDAOImp());
        dao.put(TaskDAO.class, new TaskDAOImp());
        dao.put(UserDAO.class, new UserDAOImp());
        dao.put(ProfileDAO.class, new ProfileDAOImp());
        dao.put(WallDao.class, new WallDaoImp());

        dao.put(CategoryHasTaskDAO.class, new CategoryHasTaskDAOImp());
        dao.put(TaskSourceDAO.class, new TaskSourceDAOImp());
        dao.put(TaskDestinationDAO.class, new TaskDestinationDAOImp());
    }

    public static DAOFactory getInstance() {
        if (instance == null)
            instance = new DAOFactory();
        return instance;
    }

    public <T> T create(Class<T> daoClass) {
        return (T) dao.get(daoClass);
    }

}
