create table product(
  id varchar(50) not null comment '产品编号',
  NAME varchar(50) not null comment '产品名称',
  threshold_amount decimal(15,3) NOT null comment '起投金额',
  set_amount decimal(15,3) not null comment '投资步长',
  lock_term smallint not null comment '锁定期',
  reward_rate decimal(5,3) not null comment '收益率，0-100 百分比值',
  STATUS varchar(20) not null comment '状态，AUDINTING:审核中,IN_SELL:销售中,LOCKED:暂停销售,FINISHED:已结束',
  memo varchar(200) comment '备注',
  create_at datetime comment '创建时间',
  create_user varchar(20) comment '创建者',
  update_at datetime comment '更新时间',
  update_user varchar(20) comment '更新者',
  primary key(id)

)engine=innodb default charset=utf8 collate=utf8_unicode_ci;




CREATE TABLE order_t(
	order_id VARCHAR(50) NOT NULL COMMENT '订单编号',
	chan_id VARCHAR(50) NOT NULL COMMENT '渠道编号',
	product_id VARCHAR(50) NOT NULL COMMENT '产品编号',
	chan_user_id VARCHAR(50) NOT NULL COMMENT '渠道用户编号',
	order_type VARCHAR(50) NOT NULL COMMENT '类型，APPLY:申购,REDEEM:赎回',
	order_status VARCHAR(50) NOT NULL COMMENT '状态，INIT:初始化,PROCESS:处理中，SUCCESS:成功,FAIL:失败',
	outer_order_id VARCHAR(50) NOT NULL COMMENT '外部订单编号',
	amount DECIMAL(15,3) NOT NULL COMMENT '金额',
	memo VARCHAR(200) COMMENT '备注',
	create_at DATETIME COMMENT '创建时间',
  update_at DATETIME COMMENT '更新时间',
  primary key(order_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;