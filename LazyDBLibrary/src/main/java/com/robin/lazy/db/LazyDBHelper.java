/*
 * 文 件 名:  LazyDBHelper.java
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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;
import com.robin.lazy.cache.memory.MemoryCache;
import com.robin.lazy.cache.util.MemoryCacheUtils;
import com.robin.lazy.logger.LazyLogger;

import java.io.File;
import java.sql.SQLException;

/**
 * 管理数据库的创建和版本更新
 * 
 * @author jiangyufeng
 * @version [版本号, 2016年1月5日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LazyDBHelper extends OrmLiteSqliteOpenHelper {
	private static final int MAX_MENORY_SIZE = 10;
	/** 内存缓存 */
	private MemoryCache lruMemoryCache;
	/**
	 * 数据库更新监听器
	 */
	private DBUpdateListener dbUpdateListener;

	/**
	 * 数据库创建监听
	 */
	private DBCreateListener dbCreateListener;

	/**
	 * @param <T>
	 * @param <ID>
	 * @param context
	 *            上下文
	 * @param databaseName 数据库名
	 * @param factory 可选的数据库游标工厂类，当查询(query)被提交时，该对象会被调用来实例化一个游标
	 * @param databaseVersion 数据库版本
	 * @param configFile 数据库存储路径
	 * @param daoMaxSize dao的最大缓存数量(小于等于0则使用默认,默认为10)
	 */
	public <T, ID> LazyDBHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion, File configFile,int daoMaxSize) {
		super(context, databaseName, factory, databaseVersion, configFile);
		lruMemoryCache = MemoryCacheUtils.createLruMemoryCache(
				daoMaxSize > 0 ? daoMaxSize : MAX_MENORY_SIZE, new DaoEntryRemovedProcess<T, ID>());
	}

	/**
	 * @param <T>
	 * @param <ID>
	 * @param context 上下文
	 * @param databaseName 数据库名
	 * @param factory 可选的数据库游标工厂类，当查询(query)被提交时，该对象会被调用来实例化一个游标
	 * @param databaseVersion 数据库版本
	 * @param daoMaxSize dao的最大缓存数量(小于等于0则使用默认,默认为10)
	 */
	public <T, ID> LazyDBHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion,int daoMaxSize) {
		super(context, databaseName, factory, databaseVersion);
		lruMemoryCache = MemoryCacheUtils.createLruMemoryCache(
				daoMaxSize>0?daoMaxSize:MAX_MENORY_SIZE, new DaoEntryRemovedProcess<T, ID>());
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		if (dbCreateListener != null) {
			dbCreateListener.onCreate(db, connectionSource);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		if (dbUpdateListener != null) {
			dbUpdateListener.onUpgrade(db, connectionSource, oldVersion,
					newVersion);
		}
	}

	@Override
	public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz)
			throws SQLException {
		D d = null;
		String clazzName = clazz.getSimpleName();
		if (lruMemoryCache.snapshot().containsKey(clazzName)) {
			return lruMemoryCache.get(clazzName);
		} else {
			TableUtils.createTableIfNotExists(getConnectionSource(), clazz);
			d = super.getDao(clazz);
			lruMemoryCache.put(clazzName, d);
		}
		return d;
	}

	/**
	 * 设置数据库升级监听器
	 * 
	 * @param dbUpdateListener
	 *            监听器
	 * @see [类、类#方法、类#成员]
	 */
	public void setDbUpdateListener(DBUpdateListener dbUpdateListener) {
		this.dbUpdateListener = dbUpdateListener;
	}

	/***
	 * 设置数据库创建监听器
	 * 
	 * @param dbCreateListener
	 *            void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void setDbCreateListener(DBCreateListener dbCreateListener) {
		this.dbCreateListener = dbCreateListener;
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
		String clazzName = clazz.getSimpleName();
		if (lruMemoryCache.snapshot().containsKey(clazzName)) {
			lruMemoryCache.remove(clazzName);
			try {
				TableUtils.dropTable(getConnectionSource(), clazz, ignoreErrors);
			} catch (SQLException e) {
				LazyLogger.e(e, "删除表错误");
				e.printStackTrace();
			}
		}
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
		String clazzName = tableConfig.getDataClass().getSimpleName();
		if (lruMemoryCache.snapshot().containsKey(clazzName)) {
			lruMemoryCache.remove(clazzName);
			try {
				TableUtils.dropTable(getConnectionSource(), tableConfig, ignoreErrors);
			} catch (SQLException e) {
				LazyLogger.e(e, "删除表错误");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 清理表
	 * @param clazz 是否忽视错误
	 * void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> void clearTable(Class<T> clazz){
		String clazzName = clazz.getSimpleName();
		if (lruMemoryCache.snapshot().containsKey(clazzName)) {
			try {
				TableUtils.clearTable(getConnectionSource(), clazz);
			} catch (SQLException e) {
				LazyLogger.e(e, "清理表错误");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 清理表
	 * @param tableConfig
	 * void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> void clearTable(DatabaseTableConfig<T> tableConfig){
		String clazzName = tableConfig.getDataClass().getSimpleName();
		if (lruMemoryCache.snapshot().containsKey(clazzName)) {
			try {
				TableUtils.clearTable(getConnectionSource(), tableConfig);
			} catch (SQLException e) {
				LazyLogger.e(e, "清理表错误");
				e.printStackTrace();
			}
		}
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
		return getDao(clazz).queryBuilder();
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
		return getDao(clazz).deleteBuilder();
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
		return getDao(clazz).updateBuilder();
	}
	
	@Override
	public void close() {
		super.close();
		if (lruMemoryCache != null) {
			lruMemoryCache.clear();
			lruMemoryCache.close();
			lruMemoryCache = null;
		}
		if (connectionSource != null) {
			connectionSource.close();
			connectionSource = null;
		}
		DaoManager.clearCache();
		DaoManager.clearDaoCache();
		dbCreateListener = null;
		dbUpdateListener = null;
	}

}
