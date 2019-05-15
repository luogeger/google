package com.google.mapper;

import com.google.pojo.Brand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BrandMapper {

    Brand selectBrandById(@Param("id") Integer brandId);
}
