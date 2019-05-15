-- auto Generated on 2019-05-15 15:29:23 
-- DROP TABLE IF EXISTS `exhibit_info`; 
CREATE TABLE exhibit_info(
    `exhibit_code` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '展品编号 15位 EB+年月日小时8位+5位随机数字',
    `exhibit_name` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '展品名称',
    `exhibition_type` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '展品类型（类目第二层）',
    `brand_type` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '品牌类型（类目第四层）',
    `brand_id` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '品牌ID',
    `exhibit_desc` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '展品简介',
    `exhibition_id` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '展会ID',
    `commodity_id` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '商品ID',
    `company_id` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '创建归属公司ID，即用户ID',
    `exhibit_msrp` DECIMAL(14,4) NOT NULL DEFAULT 0 COMMENT '厂家建议零售价格范围（万元）',
    `preferential` DECIMAL(14,4) NOT NULL DEFAULT 0 COMMENT '综合优惠（万元）',
    `is_show` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '是否参展 0：不参展 1：参展',
    `id` INTEGER(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_id` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '创建者id',
    `update_id` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '修改人id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
    `is_delete` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '是否删除',
    `is_enable` INTEGER(12) NOT NULL DEFAULT -1 COMMENT '是否可用',
    `remark` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'exhibit_info';
