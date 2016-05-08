package com.mzlion.okhttp.service;

import com.mzlion.okhttp.model.UserInfo;

import java.util.List;

/**
 * <p>
 * 2016-05-08 The service interface of {@linkplain UserInfo}
 * </p>
 *
 * @author mzlion
 */
public interface UserInfoService {

    /**
     * Query for list by {@linkplain UserInfo}
     *
     * @param userInfo The query parameters
     * @return List
     */
    List<UserInfo> queryForList(UserInfo userInfo);

    /**
     * Insert UserInfo data
     *
     * @param userInfo Inserting UserInfo data
     * @return If execute inserting data successful return true,or else return false
     */
    boolean insert(UserInfo userInfo);

    /**
     * Find UserInfo by id
     *
     * @param id The primary key.
     * @return {@linkplain UserInfo}
     */
    UserInfo find(Integer id);

    /**
     * Update UserInfo
     *
     * @param entity Updating UserInfo
     * @return If execute updating data successful return true,or else return false.
     */
    boolean update(UserInfo entity);

}
