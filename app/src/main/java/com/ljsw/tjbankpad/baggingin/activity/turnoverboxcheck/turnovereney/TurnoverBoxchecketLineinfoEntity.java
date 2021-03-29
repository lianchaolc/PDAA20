package com.ljsw.tjbankpad.baggingin.activity.turnoverboxcheck.turnovereney;

import java.io.Serializable;

/**
 * Created by Administrator on 2021/1/28.
 * 创建实体类代码  扫描周转箱核对线路名称
 */

public class TurnoverBoxchecketLineinfoEntity implements Serializable {

    public TurnoverBoxchecketLineinfoEntity() {
    }

    @Override
    public String toString() {
        return "TurnoverBoxchecketLineinfoEntity{" +
                "LineName='" + LineName + '\'' +
                ", BoxCounts='" + BoxCounts + '\'' +
                '}';
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public String getBoxCounts() {
        return BoxCounts;
    }

    public void setBoxCounts(String boxCounts) {
        BoxCounts = boxCounts;
    }

    public TurnoverBoxchecketLineinfoEntity(String lineName, String boxCounts) {
        LineName = lineName;
        BoxCounts = boxCounts;

    }

    private String LineName;
    private String BoxCounts;
}
