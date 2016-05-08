package com.mzlion.okhttp.dao;

import com.mzlion.okhttp.model.UserInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 2016-05-07 UserInfo Dao
 * </p>
 *
 * @author mzlion
 */
@Repository("userInfoDao")
public interface UserInfoDao {

    /**
     * Query list by parameters
     *
     * @param userInfo query parameters
     * @return Return list
     */
    @SelectProvider(type = UserInfoProvider.class, method = "queryForList")
    @Results({
            @Result(id = true, column = "id", property = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(column = "user_name", property = "userName", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_pwd", property = "userPwd", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "real_name", property = "realName", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "nick_name", property = "nickName", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "gender", property = "gender", javaType = String.class, jdbcType = JdbcType.CHAR),
            @Result(column = "birth", property = "birth", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "hobby", property = "hobby", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "avatar", property = "avatar", javaType = String.class, jdbcType = JdbcType.VARCHAR)
    })
    List<UserInfo> queryForList(UserInfo userInfo);

    /**
     * Insert data for {@linkplain UserInfo}
     *
     * @param userInfo Inserted data
     * @return Return an affective row
     */
    @Insert("INSERT INTO user_info(user_name, user_pwd, real_name, nick_name, gender, birth, hobby, avatar) " +
            "VALUES (#{userName,jdbcType=VARCHAR},#{userPwd,jdbcType=VARCHAR},#{realName,jdbcType=VARCHAR}," +
            "#{nickName,jdbcType=VARCHAR},#{gender,jdbcType=CHAR},#{birth,jdbcType=CHAR},#{hobby,jdbcType=VARCHAR},NULL)")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(UserInfo userInfo);

    /**
     * Query UserInfo by id
     *
     * @param id The primary key
     * @return {@linkplain UserInfo}
     */
    @Select("select * from user_info ui where ui.id = #{id,jdbcType=INTEGER}")
    @Results({
            @Result(id = true, column = "id", property = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(column = "user_name", property = "userName", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "user_pwd", property = "userPwd", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "real_name", property = "realName", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "nick_name", property = "nickName", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "gender", property = "gender", javaType = String.class, jdbcType = JdbcType.CHAR),
            @Result(column = "birth", property = "birth", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "hobby", property = "hobby", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(column = "avatar", property = "avatar", javaType = String.class, jdbcType = JdbcType.VARCHAR)
    })
    UserInfo find(Integer id);

    /**
     * Update UserInfo by id
     *
     * @param entity Updating UserInfo
     * @return Return an affective row.
     */
    @UpdateProvider(type = UserInfoProvider.class, method = "update")
    int update(UserInfo entity);
}
