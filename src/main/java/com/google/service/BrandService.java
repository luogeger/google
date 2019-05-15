package com.google.service;

import com.google.pojo.Brand;
import com.google.pojo.vo.ExhibitInfoVO;

import java.util.List;

public interface BrandService {

    Brand selectBrandById(Brand brand);

    /**
     * 通过展品id集合查找展品信息
     * @param exhibitInfoVO
     * @return
     */
    List<ExhibitInfoVO> selectExhibitsByIds(ExhibitInfoVO exhibitInfoVO);
}
