package com.loftcat.weibo.vo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.loftcat.weibo.vo.UserVO;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table USER_VO.
*/
public class UserVODao extends AbstractDao<UserVO, Void> {

    public static final String TABLENAME = "USER_VO";

    /**
     * Properties of entity UserVO.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", false, "ID");
        public final static Property Screen_name = new Property(1, String.class, "screen_name", false, "SCREEN_NAME");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Province = new Property(3, String.class, "province", false, "PROVINCE");
        public final static Property City = new Property(4, String.class, "city", false, "CITY");
        public final static Property Location = new Property(5, String.class, "location", false, "LOCATION");
        public final static Property Description = new Property(6, String.class, "description", false, "DESCRIPTION");
        public final static Property Url = new Property(7, String.class, "url", false, "URL");
        public final static Property Profile_image_url = new Property(8, String.class, "profile_image_url", false, "PROFILE_IMAGE_URL");
        public final static Property Domain = new Property(9, String.class, "domain", false, "DOMAIN");
        public final static Property Gender = new Property(10, String.class, "gender", false, "GENDER");
        public final static Property Remark = new Property(11, String.class, "remark", false, "REMARK");
        public final static Property Created_at = new Property(12, String.class, "created_at", false, "CREATED_AT");
        public final static Property Avatar_large = new Property(13, String.class, "avatar_large", false, "AVATAR_LARGE");
        public final static Property Verified_reason = new Property(14, String.class, "verified_reason", false, "VERIFIED_REASON");
        public final static Property Follow_me = new Property(15, String.class, "follow_me", false, "FOLLOW_ME");
        public final static Property Online_status = new Property(16, Integer.class, "online_status", false, "ONLINE_STATUS");
        public final static Property Followers_count = new Property(17, Integer.class, "followers_count", false, "FOLLOWERS_COUNT");
        public final static Property Friends_count = new Property(18, Integer.class, "friends_count", false, "FRIENDS_COUNT");
        public final static Property Statuses_count = new Property(19, Integer.class, "statuses_count", false, "STATUSES_COUNT");
        public final static Property Favourites_count = new Property(20, Integer.class, "favourites_count", false, "FAVOURITES_COUNT");
        public final static Property Following = new Property(21, Boolean.class, "following", false, "FOLLOWING");
        public final static Property Allow_all_act_msg = new Property(22, Boolean.class, "allow_all_act_msg", false, "ALLOW_ALL_ACT_MSG");
        public final static Property Geo_enabled = new Property(23, Boolean.class, "geo_enabled", false, "GEO_ENABLED");
        public final static Property Verified = new Property(24, Boolean.class, "verified", false, "VERIFIED");
        public final static Property Allow_all_comment = new Property(25, Boolean.class, "allow_all_comment", false, "ALLOW_ALL_COMMENT");
    };


    public UserVODao(DaoConfig config) {
        super(config);
    }
    
    public UserVODao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'USER_VO' (" + //
                "'ID' INTEGER," + // 0: id
                "'SCREEN_NAME' TEXT," + // 1: screen_name
                "'NAME' TEXT," + // 2: name
                "'PROVINCE' TEXT," + // 3: province
                "'CITY' TEXT," + // 4: city
                "'LOCATION' TEXT," + // 5: location
                "'DESCRIPTION' TEXT," + // 6: description
                "'URL' TEXT," + // 7: url
                "'PROFILE_IMAGE_URL' TEXT," + // 8: profile_image_url
                "'DOMAIN' TEXT," + // 9: domain
                "'GENDER' TEXT," + // 10: gender
                "'REMARK' TEXT," + // 11: remark
                "'CREATED_AT' TEXT," + // 12: created_at
                "'AVATAR_LARGE' TEXT," + // 13: avatar_large
                "'VERIFIED_REASON' TEXT," + // 14: verified_reason
                "'FOLLOW_ME' TEXT," + // 15: follow_me
                "'ONLINE_STATUS' INTEGER," + // 16: online_status
                "'FOLLOWERS_COUNT' INTEGER," + // 17: followers_count
                "'FRIENDS_COUNT' INTEGER," + // 18: friends_count
                "'STATUSES_COUNT' INTEGER," + // 19: statuses_count
                "'FAVOURITES_COUNT' INTEGER," + // 20: favourites_count
                "'FOLLOWING' INTEGER," + // 21: following
                "'ALLOW_ALL_ACT_MSG' INTEGER," + // 22: allow_all_act_msg
                "'GEO_ENABLED' INTEGER," + // 23: geo_enabled
                "'VERIFIED' INTEGER," + // 24: verified
                "'ALLOW_ALL_COMMENT' INTEGER);"); // 25: allow_all_comment
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'USER_VO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, UserVO entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String screen_name = entity.getScreen_name();
        if (screen_name != null) {
            stmt.bindString(2, screen_name);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String province = entity.getProvince();
        if (province != null) {
            stmt.bindString(4, province);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(5, city);
        }
 
        String location = entity.getLocation();
        if (location != null) {
            stmt.bindString(6, location);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(7, description);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(8, url);
        }
 
        String profile_image_url = entity.getProfile_image_url();
        if (profile_image_url != null) {
            stmt.bindString(9, profile_image_url);
        }
 
        String domain = entity.getDomain();
        if (domain != null) {
            stmt.bindString(10, domain);
        }
 
        String gender = entity.getGender();
        if (gender != null) {
            stmt.bindString(11, gender);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(12, remark);
        }
 
        String created_at = entity.getCreated_at();
        if (created_at != null) {
            stmt.bindString(13, created_at);
        }
 
        String avatar_large = entity.getAvatar_large();
        if (avatar_large != null) {
            stmt.bindString(14, avatar_large);
        }
 
        String verified_reason = entity.getVerified_reason();
        if (verified_reason != null) {
            stmt.bindString(15, verified_reason);
        }
 
        String follow_me = entity.getFollow_me();
        if (follow_me != null) {
            stmt.bindString(16, follow_me);
        }
 
        Integer online_status = entity.getOnline_status();
        if (online_status != null) {
            stmt.bindLong(17, online_status);
        }
 
        Integer followers_count = entity.getFollowers_count();
        if (followers_count != null) {
            stmt.bindLong(18, followers_count);
        }
 
        Integer friends_count = entity.getFriends_count();
        if (friends_count != null) {
            stmt.bindLong(19, friends_count);
        }
 
        Integer statuses_count = entity.getStatuses_count();
        if (statuses_count != null) {
            stmt.bindLong(20, statuses_count);
        }
 
        Integer favourites_count = entity.getFavourites_count();
        if (favourites_count != null) {
            stmt.bindLong(21, favourites_count);
        }
 
        Boolean following = entity.getFollowing();
        if (following != null) {
            stmt.bindLong(22, following ? 1l: 0l);
        }
 
        Boolean allow_all_act_msg = entity.getAllow_all_act_msg();
        if (allow_all_act_msg != null) {
            stmt.bindLong(23, allow_all_act_msg ? 1l: 0l);
        }
 
        Boolean geo_enabled = entity.getGeo_enabled();
        if (geo_enabled != null) {
            stmt.bindLong(24, geo_enabled ? 1l: 0l);
        }
 
        Boolean verified = entity.getVerified();
        if (verified != null) {
            stmt.bindLong(25, verified ? 1l: 0l);
        }
 
        Boolean allow_all_comment = entity.getAllow_all_comment();
        if (allow_all_comment != null) {
            stmt.bindLong(26, allow_all_comment ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public UserVO readEntity(Cursor cursor, int offset) {
        UserVO entity = new UserVO( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // screen_name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // province
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // city
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // location
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // description
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // url
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // profile_image_url
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // domain
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // gender
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // remark
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // created_at
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // avatar_large
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // verified_reason
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // follow_me
            cursor.isNull(offset + 16) ? null : cursor.getInt(offset + 16), // online_status
            cursor.isNull(offset + 17) ? null : cursor.getInt(offset + 17), // followers_count
            cursor.isNull(offset + 18) ? null : cursor.getInt(offset + 18), // friends_count
            cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19), // statuses_count
            cursor.isNull(offset + 20) ? null : cursor.getInt(offset + 20), // favourites_count
            cursor.isNull(offset + 21) ? null : cursor.getShort(offset + 21) != 0, // following
            cursor.isNull(offset + 22) ? null : cursor.getShort(offset + 22) != 0, // allow_all_act_msg
            cursor.isNull(offset + 23) ? null : cursor.getShort(offset + 23) != 0, // geo_enabled
            cursor.isNull(offset + 24) ? null : cursor.getShort(offset + 24) != 0, // verified
            cursor.isNull(offset + 25) ? null : cursor.getShort(offset + 25) != 0 // allow_all_comment
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, UserVO entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setScreen_name(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setProvince(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCity(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLocation(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDescription(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setUrl(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setProfile_image_url(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setDomain(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setGender(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setRemark(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setCreated_at(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setAvatar_large(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setVerified_reason(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setFollow_me(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setOnline_status(cursor.isNull(offset + 16) ? null : cursor.getInt(offset + 16));
        entity.setFollowers_count(cursor.isNull(offset + 17) ? null : cursor.getInt(offset + 17));
        entity.setFriends_count(cursor.isNull(offset + 18) ? null : cursor.getInt(offset + 18));
        entity.setStatuses_count(cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19));
        entity.setFavourites_count(cursor.isNull(offset + 20) ? null : cursor.getInt(offset + 20));
        entity.setFollowing(cursor.isNull(offset + 21) ? null : cursor.getShort(offset + 21) != 0);
        entity.setAllow_all_act_msg(cursor.isNull(offset + 22) ? null : cursor.getShort(offset + 22) != 0);
        entity.setGeo_enabled(cursor.isNull(offset + 23) ? null : cursor.getShort(offset + 23) != 0);
        entity.setVerified(cursor.isNull(offset + 24) ? null : cursor.getShort(offset + 24) != 0);
        entity.setAllow_all_comment(cursor.isNull(offset + 25) ? null : cursor.getShort(offset + 25) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(UserVO entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(UserVO entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}