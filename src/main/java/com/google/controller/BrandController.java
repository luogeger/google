package com.google.controller;

import com.google.pojo.Brand;
import com.google.pojo.vo.ExhibitInfoVO;
import com.google.service.BrandService;
import com.google.utils.Response;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("")
public class BrandController {

    @Resource
    private BrandService brandService;

    @RequestMapping(value = "/brand/getBrandById", method = RequestMethod.POST)
    public Brand getBrand(@RequestBody Brand brand) {
        Brand result = brandService.selectBrandById(brand);
        return result;
    }

    /**
     * 通过展品id集合查找展品信息
     * @param exhibitInfoVO
     * @return
     */
    @RequestMapping(value = "/getExhibitsByIds", method = RequestMethod.POST)
    public Response getExhibitsByIds(@RequestBody ExhibitInfoVO exhibitInfoVO) {
        List<ExhibitInfoVO> exhibitList = brandService.selectExhibitsByIds(exhibitInfoVO);
        return new Response().setData(exhibitList);
    }
}
