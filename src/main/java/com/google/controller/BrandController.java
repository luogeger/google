package com.google.controller;

import com.google.pojo.Brand;
import com.google.service.BrandService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Resource
    private BrandService brandService;

    @RequestMapping(value = "/getBrandById", method = RequestMethod.POST)
    public Brand getBrand(@RequestBody Brand brand) {
        Brand result = brandService.selectBrandById(brand);
        return result;
    }
}
