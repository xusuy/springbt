package com.xusy.springbt.rabbitMq;

import com.xusy.springbt.domain.MiaoshaUser;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MiaoshaMessage {
	private MiaoshaUser user;
	private long goodsId;
}
