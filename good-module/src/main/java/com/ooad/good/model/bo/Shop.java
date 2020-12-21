package com.ooad.good.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import com.ooad.good.model.po.ShopPo;
import com.ooad.good.model.vo.ShopRetVo;
import com.ooad.good.model.vo.ShopSimpleRetVo;
import com.ooad.good.model.vo.ShopVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Data
public class Shop implements VoObject {

	static Byte b0=0;
	static Byte b1=1;
	static Byte b2=2;
	static Byte b3=3;
	static Byte b4=4;
	/**
	 * 请求类型
	 */
	//默认0为未审核状态，1为未上线状态，2为上线状态,3为审核未通过状态，4为逻辑删除
	public enum StateType {

		UNCHECK(b0, "未审核"),
		OFFLINE(b1, "未上线"),
		ONLINE(b2, "上线"),
		NOTPASS(b3, "审核未通过"),
		ISDELETE(b4,"逻辑删除");

		private static final Map<Byte, StateType> typeMap;

		static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
			typeMap = new HashMap();
			for (StateType enum1 : values()) {
				typeMap.put(enum1.code, enum1);
			}
		}

		private Byte code;
		private String description;

		StateType(Byte code, String description) {
			this.code = code;
			this.description = description;
		}


		public static StateType getTypeByCode(Byte code) {
			return typeMap.get(code);
		}

		public Byte getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}

		public void setCode(Byte state)
		{
			this.code=state;
		}
	}

	private Long id;
	private String name;
	private StateType state;
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
		po.setState(this.getState().getCode());
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
		this.state.code=po.getState();
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
		po.setState(this.getState().getCode());
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
