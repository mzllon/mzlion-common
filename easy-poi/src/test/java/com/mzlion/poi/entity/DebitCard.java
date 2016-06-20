package com.mzlion.poi.entity;

import com.mzlion.poi.annotation.ExcelCell;
import com.mzlion.poi.annotation.ExcelEntity;

/**
 * Created by mzlion on 2016/6/15.
 */
@ExcelEntity
public class DebitCard {

    private String cardId;

    @ExcelCell("卡号")
    private String cardNo;

    @ExcelCell("银行名称")
    private String cardType;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DebitCard{");
        sb.append("cardId='").append(cardId).append('\'');
        sb.append(", cardNo='").append(cardNo).append('\'');
        sb.append(", cardType='").append(cardType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
