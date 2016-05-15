package com.mzlion.okhttp.dao;

import com.mzlion.core.lang.StringUtils;
import com.mzlion.okhttp.model.UserInfo;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 * <p>
 * 2016-05-08 The SQL Builder for {@linkplain UserInfoDao}
 * </p>
 *
 * @author mzlion
 */
public class UserInfoProvider {
    private static final String TABLE_NAME = "user_info";

    /**
     * The statement 'queryForList'
     *
     * @param userInfo query parameters
     * @return return dynamic sql
     */
    public String queryForList(UserInfo userInfo) {
        BEGIN();
        SELECT("*");
        FROM(TABLE_NAME);
        if (StringUtils.hasText(userInfo.getUserName())) {
            WHERE("user_name = #{userName,jdbcType=VARCHAR}");
        }
        if (StringUtils.hasLength(userInfo.getNickName())) {
            WHERE("nick_name = #{nickName,jdbcType=VARCHAR}");
        }
        return SQL();
    }

    /**
     * The statement 'update'
     *
     * @param userInfo Updating Data
     * @return Return dynamic sql.
     */
    public String update(UserInfo userInfo) {
        BEGIN();
        UPDATE(TABLE_NAME);
        SET("user_name = #{userName,jdbcType=VARCHAR}");
        SET("nick_name = #{nickName,jdbcType=VARCHAR}");
        if (StringUtils.hasLength(userInfo.getRealName())) {
            SET("real_name = #{realName,jdbcType=VARCHAR}");
        }
        if (StringUtils.hasLength(userInfo.getGender())) {
            SET("gender = #{gender,jdbcType=CHAR}");
        }
        if (StringUtils.hasLength(userInfo.getBirth())) {
            SET("birth = #{birth,jdbcType=CHAR}");
        }
        if (StringUtils.hasLength(userInfo.getHobby())) {
            SET("hobby = #{hobby,jdbcType=VARCHAR}");
        }
        if (StringUtils.hasLength(userInfo.getAvatar())) {
            SET("avatar = #{avatar,jdbcType=VARCHAR}");
        }
        WHERE("id = #{id,jdbcType=INTEGER}");
        return SQL();
    }

}
