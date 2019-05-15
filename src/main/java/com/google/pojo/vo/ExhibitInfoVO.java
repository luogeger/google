package com.google.pojo.vo;

import com.google.pojo.ExhibitInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ExhibitInfoVO extends ExhibitInfo {
    // ==================================== 参数
    /** 展品id集合*/
    private List<Integer> exhibitIds;



    // ==================================== 返回值
    private Integer exhibitId;

    // ==================================== 返回值
    /** 图片集合*/
    private List<String> images;
    /** 展品图片*/
    private String exhibitImg;
    /** 车系*/
    private String carSeries;

}
