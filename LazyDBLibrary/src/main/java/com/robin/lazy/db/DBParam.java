/*
 * 文 件 名:  DBInfor.java
 * 版    权:  jiang yu feng 
 * 描    述:  <描述>
 * 修 改 人:  江钰锋
 * 修改时间:  2014-3-26
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.robin.lazy.db;

/**
 * 数据库信息配置
 * 
 * @author 江钰锋
 * @version [版本号, 2014-3-26]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DBParam
{
    /** 数据库名字 */
    private String dbName = "text.db";
    
    /** 数据库版本,默认为1 */
    private int dbVersion = 1;
    
    // private boolean saveOnSDCard = false;//是否保存到SD卡
    
    // /** 数据库文件在sd卡中的目录 */
    // private String targetDirectory = null;
    
    public String getDbName()
    {
        return dbName;
    }
    
    public void setDbName(String dbName)
    {
        this.dbName = dbName;
    }
    
    public int getDbVersion()
    {
        return dbVersion;
    }
    
    public void setDbVersion(int dbVersion)
    {
        this.dbVersion = dbVersion;
    }
    
}
