/*
 * 文 件 名:  LazyDBSqlite.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2016年1月5日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.robin.lazy.logger.LazyLogger;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据库管理类，通过此类进行数据库的操作
 * 
 * @author jiangyufeng
 * @version [版本号, 2016年1月5日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LazyDB {
	/**
	 * 数据库创建与更新管理对象
	 */
	private LazyDBHelper dbHelper;

	/**
	 * <默认构造函数>
	 * 
	 * @param context 上下文
	 * @param daoMaxSize dao的最大缓存数量(小于等于0则使用默认,默认为10)
	 */
	public LazyDB(Context context,int daoMaxSize) {
		DBParam dbParam = new DBParam();
		dbHelper = new LazyDBHelper(context, dbParam.getDbName(), null,
				dbParam.getDbVersion(),daoMaxSize);
	}

	/**
	 * 构造函数
	 * 
	 * @param context 上下文
	 * @param dbParam 数据库信息
	 * @param daoMaxSize dao的最大缓存数量(小于等于0则使用默认,默认为10)
	 */
	public LazyDB(Context context, DBParam dbParam,int daoMaxSize) {
		dbHelper = new LazyDBHelper(context, dbParam.getDbName(), null,
				dbParam.getDbVersion(),daoMaxSize);
	}

	/**
	 * 设置数据库升级监听器
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void setDBUpdateListener(DBUpdateListener listener) {
		if (listener != null && dbHelper != null) {
			dbHelper.setDbUpdateListener(listener);
		}
	}

	/**
	 * 设置数据库创建监听器
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void setDBCreateListener(DBCreateListener listener) {
		if (listener != null && dbHelper != null) {
			dbHelper.setDbCreateListener(listener);
		}
	}

	/***
	 * 得到dao操作工具
	 * 
	 * @param clazz
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 *             Dao<T,ID>
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T, ID> Dao<T, ID> getDao(Class<T> clazz) throws SQLException,
			NullPointerException {
		return dbHelper.getDao(clazz);
	}
	
	/**
	 * 删除表
	 * @param clazz 
	 * @param ignoreErrors 是否忽视错误
	 * void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> void dropTable(Class<T> clazz,boolean ignoreErrors){
		dbHelper.dropTable(clazz, ignoreErrors);
	}
	
	/**
	 * 删除表
	 * @param tableConfig 
	 * @param ignoreErrors 是否忽视错误
	 * void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> void dropTable(DatabaseTableConfig<T> tableConfig,boolean ignoreErrors){
		dbHelper.dropTable(tableConfig, ignoreErrors);
	}
	
	/**
	 * 清理表
	 * @param clazz 是否忽视错误
	 * void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> void clearTable(Class<T> clazz){
		dbHelper.clearTable(clazz);
	}
	
	/**
	 * 清理表
	 * @param tableConfig
	 * void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> void clearTable(DatabaseTableConfig<T> tableConfig){
		dbHelper.clearTable(tableConfig);
	}
	
	/***
	 * 获取查询语句构造器
	 * 
	 * @param clazz
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 *             QueryBuilder<T,?>
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> QueryBuilder<T, ?> getQueryBuilder(Class<T> clazz)
			throws SQLException, NullPointerException {
		return dbHelper.getQueryBuilder(clazz);
	}

	/***
	 * 获取删除语句构造器
	 * 
	 * @param clazz
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 *             DeleteBuilder<T,?>
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> DeleteBuilder<T, ?> getDeleteBuilder(Class<T> clazz)
			throws SQLException, NullPointerException {
		return dbHelper.getDeleteBuilder(clazz);
	}

	/***
	 * 获取更新语句构造器
	 * 
	 * @param clazz
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 *             DeleteBuilder<T,?>
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> UpdateBuilder<T, ?> getUpdateBuilder(Class<T> clazz)
			throws SQLException, NullPointerException {
		return dbHelper.getUpdateBuilder(clazz);
	}
	
	/**
	 * 查询是否存在此表
	 * 
	 * @param clazz
	 * @return boolean 不存在 
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> boolean isTableExists(Class<T> clazz) {
		try {
			return getDao(clazz).isTableExists();
		} catch (NullPointerException e) {
			LazyLogger.e(e, "没有这张表");
		} catch (SQLException e) {
			LazyLogger.e(e, "不存在这张表");
		} catch (Exception e) {
			LazyLogger.e(e, "不存在这张表");
		}
		return false;
	}
	
	/**
	 * 获取此表中数据的数量
	 * 
	 * @param clazz
	 * @return long 这张表的数据的数量 
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> long countOf(Class<T> clazz) {
		try {
			return getDao(clazz).countOf();
		} catch (NullPointerException e) {
			LazyLogger.e(e, "没有这张表");
		} catch (SQLException e) {
			LazyLogger.e(e, "查询表中数据数量错误");
		} catch (Exception e) {
			LazyLogger.e(e, "查询表中数据数量错误");
		}
		return 0;
	}
	
	/**
	 * 插入一条数据(如果存在则更新,不存在则直接更新)
	 * 
	 * @param clazz
	 * @return CreateOrUpdateStatus 
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> CreateOrUpdateStatus insertOrUpdate(Class<T> clazz, T values) {
		try {
			return getDao(clazz).createOrUpdate(values);
		} catch (NullPointerException e) {
			LazyLogger.e(e, "没有这张表");
		} catch (SQLException e) {
			LazyLogger.e(e, "插入错误");
		} catch (Exception e) {
			LazyLogger.e(e, "插入错误");
		}
		return null;
	}

	/**
	 * 插入一条数据(如果不存在则插入)
	 * 
	 * @param clazz
	 * @return T
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> T insertIfNotExists(Class<T> clazz, T values) {
		try {
			return getDao(clazz).createIfNotExists(values);
		} catch (NullPointerException e) {
			LazyLogger.e(e, "没有这张表");
		} catch (SQLException e) {
			LazyLogger.e(e, "插入错误");
		} catch (Exception e) {
			LazyLogger.e(e, "插入错误");
		}
		return null;
	}
	
	/**
	 * 插入一条数据
	 * 
	 * @param clazz
	 * @return int
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> int insert(Class<T> clazz, T values) {
		try {
			return getDao(clazz).create(values);
		} catch (NullPointerException e) {
			LazyLogger.e(e, "没有这张表");
		} catch (SQLException e) {
			LazyLogger.e(e, "插入错误");
		} catch (Exception e) {
			LazyLogger.e(e, "插入错误");
		}
		return 0;
	}
	
	/**
	 * 查询所有此表的所有数据
	 * 
	 * @param clazz
	 * @return T
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> T queryForId(Class<T> clazz,Object id) {
		try {
			return getDao(clazz).queryForId(id);
		} catch (NullPointerException e) {
			LazyLogger.e(e, "没有这张表");
		} catch (SQLException e) {
			LazyLogger.e(e, "查询错误");
		} catch (Exception e) {
			LazyLogger.e(e, "查询错误");
		}
		return null;
	}

	/**
	 * 查询所有此表的所有数据
	 * 
	 * @param clazz
	 * @return List<T>
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> List<T> queryForAll(Class<T> clazz) {
		try {
			return getDao(clazz).queryForAll();
		} catch (NullPointerException e) {
			LazyLogger.e(e, "没有这张表");
		} catch (SQLException e) {
			LazyLogger.e(e, "查询错误");
		} catch (Exception e) {
			LazyLogger.e(e, "查询错误");
		}
		return null;
	}

	/**
	 * 查询startRow到maxRows位置之间的数据集合
	 * 
	 * @param clazz
	 * @param startRow
	 * @param maxRows
	 * @return List<T>
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> List<T> queryStartToMax(Class<T> clazz, long startRow, long maxRows) {
		try {
			if (startRow <= 0 && maxRows <= 0) {
				return null;
			}
			if (startRow <= 0 && maxRows > 0) {
				return getDao(clazz).queryBuilder().limit(maxRows).query();
			} else if (startRow > 0 && maxRows <= 0) {
				return getDao(clazz).queryBuilder().offset(startRow).query();
			}
			return getDao(clazz).queryBuilder().offset(startRow).limit(maxRows)
					.query();
		} catch (NullPointerException e) {
			LazyLogger.e(e, "没有这张表");
		} catch (SQLException e) {
			LazyLogger.e(e, "查询错误");
		} catch (Exception e) {
			LazyLogger.e(e, "查询错误");
		}
		return null;
	}
	
	/**
	 * 查询与此map的所有数据
	 * 
	 * @param clazz
	 * @return List<T>
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> List<T> queryForFieldValues(Class<T> clazz,Map<String,Object> map) {
		try {
			return getDao(clazz).queryForFieldValues(map);
		} catch (NullPointerException e) {
			LazyLogger.e(e, "没有这张表");
		} catch (SQLException e) {
			LazyLogger.e(e, "查询错误");
		} catch (Exception e) {
			LazyLogger.e(e, "查询错误");
		}
		return null;
	}
	
	/**
	 * 删除数据集合
	 * 
	 * @param clazz
	 * @return int
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> int delete(Class<T> clazz,Collection<T> list) {
		try {
			return getDao(clazz).delete(list);
		} catch (NullPointerException e) {
			LazyLogger.e(e, "没有这张表");
		} catch (SQLException e) {
			LazyLogger.e(e, "删除数据错误");
		} catch (Exception e) {
			LazyLogger.e(e, "删除数据错误");
		}
		return 0;
	}
	
	/**
	 * 删除数据
	 * 
	 * @param clazz
	 * @return int
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> int delete(Class<T> clazz,T t) {
		try {
			return getDao(clazz).delete(t);
		} catch (NullPointerException e) {
			LazyLogger.e(e, "没有这张表");
		} catch (SQLException e) {
			LazyLogger.e(e, "删除数据错误");
		} catch (Exception e) {
			LazyLogger.e(e, "删除数据错误");
		}
		return 0;
	}
	
	/**
	 * 根据id删除对应的数据
	 * 
	 * @param clazz
	 * @return int
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> int deleteById(Class<T> clazz,Object id) {
		try {
			return getDao(clazz).deleteById(id);
		} catch (NullPointerException e) {
			LazyLogger.e(e, "没有这张表");
		} catch (SQLException e) {
			LazyLogger.e(e, "删除数据错误");
		} catch (Exception e) {
			LazyLogger.e(e, "删除数据错误");
		}
		return 0;
	}
	
	/**
	 * 根据id集合删除对应的数据
	 * 
	 * @param clazz
	 * @return int
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> int deleteIds(Class<T> clazz,Collection<Object> ids) {
		try {
			return getDao(clazz).deleteIds(ids);
		} catch (NullPointerException e) {
			LazyLogger.e(e, "没有这张表");
		} catch (SQLException e) {
			LazyLogger.e(e, "删除数据错误");
		} catch (Exception e) {
			LazyLogger.e(e, "删除数据错误");
		}
		return 0;
	}
	
	/**
	 * 更新数据
	 * 
	 * @param clazz
	 * @return int
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> int update(Class<T> clazz,T t) {
		try {
			return getDao(clazz).update(t);
		} catch (NullPointerException e) {
			LazyLogger.e(e, "没有这张表");
		} catch (SQLException e) {
			LazyLogger.e(e, "更新数据错误");
		} catch (Exception e) {
			LazyLogger.e(e, "更新数据错误");
		}
		return 0;
	}
	
	/**
	 * 根据ID更新对应的数据
	 * 
	 * @param clazz
	 * @return int
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> int updateId(Class<T> clazz,T t, Object id) {
		try {
			return getDao(clazz).updateId(t, id);
		} catch (NullPointerException e) {
			LazyLogger.e(e, "没有这张表");
		} catch (SQLException e) {
			LazyLogger.e(e, "更新数据错误");
		} catch (Exception e) {
			LazyLogger.e(e, "更新数据错误");
		}
		return 0;
	}
	
	/**
	 * 刷新数据
	 * 
	 * @param clazz
	 * @return int
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> int refresh(Class<T> clazz,T t) {
		try {
			return getDao(clazz).refresh(t);
		} catch (NullPointerException e) {
			LazyLogger.e(e, "没有这张表");
		} catch (SQLException e) {
			LazyLogger.e(e, "更新数据错误");
		} catch (Exception e) {
			LazyLogger.e(e, "更新数据错误");
		}
		return 0;
	}
	

	/**
	 * 关闭数据库连接 void
	 * 
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void close() {
		if (dbHelper != null) {
			dbHelper.close();
			dbHelper = null;
		}
	}
}
