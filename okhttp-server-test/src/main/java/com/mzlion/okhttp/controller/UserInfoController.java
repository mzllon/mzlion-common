package com.mzlion.okhttp.controller;

import com.mzlion.core.lang.StringUtils;
import com.mzlion.okhttp.model.UserInfo;
import com.mzlion.okhttp.service.UserInfoService;
import com.mzlion.okhttp.vo.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 2016-05-08 The Controller Class for {@linkplain UserInfo}
 * </p>
 *
 * @author mzlion
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    //log
    private final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    @Resource(name = "userInfoService")
    private UserInfoService userInfoService;

    /**
     * Query for list of UserInfo
     */
    @RequestMapping(value = "/pageSelect", method = RequestMethod.GET)
    public List<UserInfo> pageSelect(String gender, String userName) {
        logger.info(" <=== query for userInfo result list,query parameters->gender={},userName={}", gender, userName);
        List<UserInfo> userInfoList;

        UserInfo userInfo = new UserInfo();
        if (StringUtils.hasLength(userName)) {
            userInfo.setUserName(userName);
        }
        if (StringUtils.hasLength(gender)) {
            userInfo.setGender(gender);
        }
        userInfoList = this.userInfoService.queryForList(userInfo);

        logger.info(" ===> query for userInfo result list is successful,result->{}", userInfoList);
        return userInfoList;
    }

    /**
     * Insert UserInfo
     *
     * @param userInfo Inserting UserInfo
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResponse<String> create(UserInfo userInfo) {
        logger.info(" <=== Create UserInfo data,parameters->{}", userInfo);
        CommonResponse<String> commonResponse = new CommonResponse<>();
        if (StringUtils.isEmpty(userInfo.getUserName())) {
            commonResponse.setResultCode("1001");
            commonResponse.setResultDesc("Username must not be null.");
            return commonResponse;
        }
        if (StringUtils.isEmpty(userInfo.getUserPwd())) {
            commonResponse.setResultCode("1001");
            commonResponse.setResultDesc("User password must not be null.");
            return commonResponse;
        }
        if (StringUtils.isEmpty(userInfo.getNickName())) {
            commonResponse.setResultCode("1001");
            commonResponse.setResultDesc("Nickname must not be null.");
            return commonResponse;
        }

        if (this.userInfoService.insert(userInfo)) {
            commonResponse.setResultCode("0000");
            commonResponse.setResultDesc("Creating UserInfo data success.");
            commonResponse.setResult(String.valueOf(userInfo.getId()));
        } else {
            commonResponse.setResultCode("1101");
            commonResponse.setResultDesc("Creating UserInfo data failed.");
        }
        return commonResponse;
    }

    /**
     * Find UserInfo by id.
     *
     * @param id primary key
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public CommonResponse<UserInfo> get(@PathVariable Integer id) {
        CommonResponse<UserInfo> commonResponse = new CommonResponse<>();
        logger.info(" <=== Getting UserInfo by id->{}", id);
        if (null == id) {
            commonResponse.setResultCode("1001");
            commonResponse.setResultDesc("Id must not be null.");
            logger.info(" ===> {}", commonResponse.toString());
            return commonResponse;
        }
        UserInfo userInfo = this.userInfoService.find(id);
        if (userInfo == null) {
            commonResponse.setResultCode("1101");
            commonResponse.setResultDesc("The UserInfo cannot find by id");
            logger.info(" ===> {}", commonResponse.toString());
            return commonResponse;
        }
        commonResponse.setResultCode("0000");
        commonResponse.setResultDesc("Finding UserInfo is success.");
        commonResponse.setResult(userInfo);
        logger.info(" ===> {}", commonResponse.toString());
        return commonResponse;
    }

    /**
     * Update UserInfo
     *
     * @param userInfo Updateing data.
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public CommonResponse<String> update(UserInfo userInfo) {
        logger.info(" <=== Updating UserInfo,parameters->{}", userInfo);
        CommonResponse<String> commonResponse = new CommonResponse<>();
        if (null == userInfo.getId()) {
            commonResponse.setResultCode("1001");
            commonResponse.setResultDesc("Id must not be null.");
            logger.info(" ===> {}", commonResponse.toString());
            return commonResponse;
        }
        if (StringUtils.isEmpty(userInfo.getUserName())) {
            commonResponse.setResultCode("1001");
            commonResponse.setResultDesc("Username must not be null.");
            logger.info(" ===> {}", commonResponse.toString());
            return commonResponse;
        }
        if (StringUtils.isEmpty(userInfo.getUserPwd())) {
            commonResponse.setResultCode("1001");
            commonResponse.setResultDesc("User password must not be null.");
            logger.info(" ===> {}", commonResponse.toString());
            return commonResponse;
        }
        if (StringUtils.isEmpty(userInfo.getNickName())) {
            commonResponse.setResultCode("1001");
            commonResponse.setResultDesc("Nickname must not be null.");
            logger.info(" ===> {}", commonResponse.toString());
            return commonResponse;
        }

        UserInfo entity = this.userInfoService.find(userInfo.getId());
        if (entity == null) {
            commonResponse.setResultCode("1001");
            commonResponse.setResultDesc("The UserInfo cannot find.");
            logger.info(" ===> {}", commonResponse.toString());
            return commonResponse;
        }
        entity.setGender(userInfo.getGender());
        entity.setUserName(userInfo.getUserName());
        entity.setAvatar(userInfo.getAvatar());
        entity.setBirth(userInfo.getBirth());
        entity.setHobby(userInfo.getHobby());
        entity.setNickName(userInfo.getNickName());
        entity.setRealName(userInfo.getRealName());
        entity.setUserPwd(userInfo.getUserPwd());

        if (this.userInfoService.update(entity)) {
            commonResponse.setResultCode("0000");
            commonResponse.setResultDesc("Updating UserInfo is successful.");
        } else {
            commonResponse.setResultCode("1101");
            commonResponse.setResultDesc("Updating UserInfo is failed.");
        }
        logger.info(" ===> {}", commonResponse.toString());
        return commonResponse;
    }


}
