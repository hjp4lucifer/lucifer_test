CREATE TABLE `adminOpRecord` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`createTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`uType` TINYINT(4) NOT NULL DEFAULT '2' COMMENT '1偶家uid, 2hiyd的uid',
	`uid` BIGINT(20) NOT NULL DEFAULT '0',
	`bType` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '1app帖子相关, 2用户相关',
	`bid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '对应的业务id',
	`text` VARCHAR(150) NULL DEFAULT NULL COMMENT '简单描述, 作为对bType的补充' COLLATE 'utf8mb4_unicode_ci',
	PRIMARY KEY (`id`),
	INDEX `uid` (`uid`)
)
COMMENT='管理员操作记录, 含用户自己删贴记录'
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB;
