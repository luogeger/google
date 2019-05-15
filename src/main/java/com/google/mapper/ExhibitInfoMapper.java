package com.google.mapper;

import com.google.pojo.ExhibitInfo;
import com.google.pojo.vo.ExhibitInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExhibitInfoMapper {

    int insert(@Param("pojo") ExhibitInfo pojo);


    /**
     * 通过展品id集合查找展品信息
     * @param exhibitIds
     * @return
     */
    List<ExhibitInfoVO> selectExhibitsByIds(@Param("exhibitIds") List<Integer> exhibitIds);

    /** 展品图片*/
    List<ExhibitInfoVO> selectExhibitPic(@Param("exhibitIds") List<Integer> exhibitIds);

    /** 车系*/
    List<ExhibitInfoVO> selectExhibitCarSeries(@Param("exhibitIds") List<Integer> exhibitIds);
}
