CREATE TABLE `bb_course` (
	`bb_cid` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'bodybuilding课程id',
	`name` VARCHAR(100) NOT NULL COMMENT '课程名' COLLATE 'utf8mb4_unicode_ci',
	`cover` VARCHAR(200) NULL DEFAULT NULL COMMENT '封面图' COLLATE 'utf8mb4_general_ci',
	`bg_img` VARCHAR(100) NOT NULL COMMENT '背景图' COLLATE 'utf8mb4_unicode_ci',
	`bg_img_m` VARCHAR(256) NULL DEFAULT NULL COMMENT 'H5背景图' COLLATE 'utf8mb4_general_ci',
	`overview` VARCHAR(1000) NOT NULL COMMENT '课程概述' COLLATE 'utf8mb4_unicode_ci',
	`keywords` VARCHAR(200) NULL DEFAULT NULL COMMENT 'SEO页面keywords' COLLATE 'utf8mb4_general_ci',
	`description` VARCHAR(500) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`difficulty` TINYINT(4) NULL DEFAULT NULL COMMENT '难度',
	`weeks` INT(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '多少周',
	`days` INT(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '多少天',
	`rating` FLOAT(3,1) UNSIGNED NOT NULL DEFAULT '0.0' COMMENT '评分',
	`orgin_url` VARCHAR(100) NOT NULL COMMENT '来源url' COLLATE 'utf8mb4_unicode_ci',
	`created_from` TINYINT(3) UNSIGNED NOT NULL DEFAULT '1' COMMENT '来源 1:bodybuilding 2:hiyd',
	`is_finish` INT(11) NOT NULL DEFAULT '0' COMMENT '是否爬虫完毕',
	`group_id` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT '分组ID',
	`group_show_name` VARCHAR(200) NULL DEFAULT NULL COMMENT '组内显示名字' COLLATE 'utf8mb4_general_ci',
	`group_sn` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '同组排序值（大靠前',
	`is_translated` TINYINT(3) NULL DEFAULT '0' COMMENT '1 已翻译 0否',
	`status` TINYINT(1) UNSIGNED NOT NULL DEFAULT '1' COMMENT '状态',
	`week_days` INT(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '一周训练多少天',
	PRIMARY KEY (`bb_cid`),
	UNIQUE INDEX `idx_name` (`name`),
	INDEX `group_id` (`group_id`, `group_sn`) USING BTREE,
	INDEX `status` (`status`) USING BTREE
)
COMMENT='bodybuilding课程'
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB
AUTO_INCREMENT=563;
