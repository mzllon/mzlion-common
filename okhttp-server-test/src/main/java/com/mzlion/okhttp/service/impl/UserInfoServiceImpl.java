package com.mzlion.okhttp.service.impl;

import com.mzlion.okhttp.dao.UserInfoDao;
import com.mzlion.okhttp.model.UserInfo;
import com.mzlion.okhttp.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 2016-05-08 The implement of {@linkplain UserInfoService}
 * </P>
 *
 * @author mzlion
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Resource(name = "userInfoDao")
    private UserInfoDao userInfoDao;

    /**
     * Query for list by {@linkplain UserInfo}
     *
     * @param userInfo The query parameters
     * @return List
     */
    @Override
    public List<UserInfo> queryForList(UserInfo userInfo) {
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        return this.userInfoDao.queryForList(userInfo);
    }

    /**
     * Insert UserInfo data
     *
     * @param userInfo Inserting UserInfo data
     * @return If execute inserting data successful return true,or else return false
     */
    @Override
    public boolean insert(UserInfo userInfo) {
        return userInfo != null && this.userInfoDao.insert(userInfo) == 1;
    }

    /**
     * Find UserInfo by id
     *
     * @param id The primary key.
     * @return {@linkplain UserInfo}
     */
    @Override
    public UserInfo find(Integer id) {
        if (id == null) {
            return null;
        }
        return this.userInfoDao.find(id);
    }

    /**
     * Update UserInfo
     *
     * @param entity Updating UserInfo
     * @return If execute updating data successful return true,or else return false.
     */
    @Override
    public boolean update(UserInfo entity) {
        return entity != null && entity.getId() != null && this.userInfoDao.update(entity) == 1;
    }
}
