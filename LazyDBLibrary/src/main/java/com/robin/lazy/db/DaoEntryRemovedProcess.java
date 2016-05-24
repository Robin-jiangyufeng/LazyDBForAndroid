/*
 * 文 件 名:  DaoEntryRemovePro.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2016年1月13日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.db;

import com.j256.ormlite.dao.Dao;
import com.robin.lazy.cache.memory.EntryRemovedProcess;

import java.sql.SQLException;

/**
 * dao数据被回收的处理者
 * 
 * @author  jiangyufeng
 * @version  [版本号, 2016年1月13日]
 * @param <T>
 * @param <ID>
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DaoEntryRemovedProcess<T, ID> extends EntryRemovedProcess<Dao<T, ID>> {

	@Override
	public void entryRemoved(boolean evicted, String key, Dao<T, ID> oldValue,
			Dao<T, ID> newValue) {
		if(oldValue!=null){
			try {
				oldValue.clearObjectCache();
				oldValue.closeLastIterator();
				oldValue = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

