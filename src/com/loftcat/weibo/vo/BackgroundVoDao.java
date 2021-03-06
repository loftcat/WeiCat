package com.loftcat.weibo.vo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.loftcat.weibo.vo.BackgroundVo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table BACKGROUND_VO.
*/
public class BackgroundVoDao extends AbstractDao<BackgroundVo, Void> {

    public static final String TABLENAME = "BACKGROUND_VO";

    /**
     * Properties of entity BackgroundVo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property SmallImage = new Property(0, Integer.class, "smallImage", false, "SMALL_IMAGE");
        public final static Property LargeImage = new Property(1, Integer.class, "largeImage", false, "LARGE_IMAGE");
    };


    public BackgroundVoDao(DaoConfig config) {
        super(config);
    }
    
    public BackgroundVoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'BACKGROUND_VO' (" + //
                "'SMALL_IMAGE' INTEGER," + // 0: smallImage
                "'LARGE_IMAGE' INTEGER);"); // 1: largeImage
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'BACKGROUND_VO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, BackgroundVo entity) {
        stmt.clearBindings();
 
        Integer smallImage = entity.getSmallImage();
        if (smallImage != null) {
            stmt.bindLong(1, smallImage);
        }
 
        Integer largeImage = entity.getLargeImage();
        if (largeImage != null) {
            stmt.bindLong(2, largeImage);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public BackgroundVo readEntity(Cursor cursor, int offset) {
        BackgroundVo entity = new BackgroundVo( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // smallImage
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1) // largeImage
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, BackgroundVo entity, int offset) {
        entity.setSmallImage(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setLargeImage(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(BackgroundVo entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(BackgroundVo entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
