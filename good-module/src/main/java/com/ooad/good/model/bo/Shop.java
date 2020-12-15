package com.ooad.good.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import com.ooad.good.model.po.ShopPo;
import com.ooad.good.model.vo.ShopRetVo;
import com.ooad.good.model.vo.ShopSimpleRetVo;
import com.ooad.good.model.vo.ShopVo;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class Shop implements VoObject {
	private Long id;
	private String name;
	private Byte state;
	private LocalDateTime gmtCreate;
	private LocalDateTime gmtModified;

	public Shop(){};
	public Shop(Long shopId) {
		this.id = shopId;
	}

	public ShopPo createUpdatePo(ShopVo vo){
		ShopPo po=new ShopPo();
		po.setId(this.getId());
		po.setGmtCreate(this.getGmtCreate());
		po.setGmtModified(LocalDateTime.now());
		po.setState(this.getState());
		po.setName(vo.getName());
		return po;

	}

	/**
	 * 用po构造bo对象
	 * @param po
	 */
	public Shop(ShopPo po){
		this.id=po.getId();
		this.name=po.getName();
		this.state=po.getState();
		this.gmtCreate=po.getGmtCreate();
		this.gmtModified=po.getGmtModified();

	}


	/**
	 * 用bo对象创建更新po对象
	 * @return
	 */
	public ShopPo gotShopPo(){
		ShopPo po=new ShopPo();
		po.setId(this.getId());
		po.setName(this.getName());
		po.setState(this.getState());
		po.setGmtCreate(this.gmtCreate);
		po.setGmtModified(this.gmtModified);
		return po;
	}

	@Override
	public Object createVo() {
		return new ShopRetVo(this);
	}

	@Override
	public Object createSimpleVo() {
		return new ShopSimpleRetVo(this);
	}
}
