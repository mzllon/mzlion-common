package com.mzlion.core.vo;

import java.util.Date;

/**
 * Created by mzlion on 2016/6/7.
 */
public class Student extends Person {

    private String studentNo;

    private Date enrollmentDate;

    private static String genderList;

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public static String getGenderList() {
        return genderList;
    }

    public static void setGenderList(String genderList) {
        Student.genderList = genderList;
    }
}
