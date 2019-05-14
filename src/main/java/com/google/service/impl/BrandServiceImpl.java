package com.google.service.impl;

import com.google.mapper.BrandMapper;
import com.google.pojo.Brand;
import com.google.service.BrandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BrandServiceImpl implements BrandService {

    @Resource
    private BrandMapper brandMapper;

    @Override
    public Brand selectBrandById(Brand brand) {
        Integer brandId = brand.getId();
        System.out.println(brandMapper);
        Brand result = brandMapper.selectBrandById(brandId);
        return result;
    }
}
