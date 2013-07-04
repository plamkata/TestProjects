package com.startup.solynchron.obj;

import java.util.Date;

public interface IAuditable {

	public Date getCreateDate();

	public void setCreateDate(Date data);

	public MyUser getCreatedBy();

	public void setCreatedBy(MyUser user);

	
	public Date getUpdateDate();

	public void setUpdateDate(Date date);

	public MyUser getUpdatedBy();

	public void setUpdatedBy(MyUser user);

}