/*
 * 文 件 名:  DBUpdateListener.java
 * 版    权:  jiang yu feng 
 * 描    述:  <描述>
 * 修 改 人:  江钰锋
 * 修改时间:  2014-3-26
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.robin.lazy.db;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;

/**
 * 数据库创建监听
 * 
 * @author  江钰锋
 * @version  [版本号, 2014-3-26]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface DBCreateListener
{
    /**
     * 数据库更新回调方法
     * 
     * @param db 数据库
     * @param connectionSource 数据连接资源
     * @see [类、类#方法、类#成员]
     */
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource);
}
