package modle.test.com.commlibary.db;



import android.net.Uri;


import java.util.List;

import modle.test.com.commlibary.base.BaseApplication;
import modle.test.com.commlibary.entity.DaoMaster;
import modle.test.com.commlibary.entity.DaoSession;

public class DBHelper {

    public static final String AUTHORITY = "modle.test.com.commlibary.provider.test_db";
    public static final String uriHeader = "content://" + AUTHORITY + "/";

    private static  DBHelper instance = new DBHelper();
    private DaoSession daoSession;

    public DBHelper(){
        DaoMaster.DevOpenHelper helper  = new DaoMaster.DevOpenHelper(BaseApplication.getContext(),"test_db");
        helper.getReadableDatabase();
        daoSession = new DaoMaster(helper.getWritableDatabase()).newSession();
    }

    public static DBHelper getHelper(){
        return instance;
    }

    public DaoSession getDaoSession(){
       // daoSession.clear();
        return daoSession;
    }

    public Uri getUri(Class<?> entity){
        return Uri.parse(uriHeader + getTableName(entity));
    }

    public String getTableName(Class<?> entityClass){
        return daoSession.getDao(entityClass).getTablename();
    }

    public long countOf(Class<?> entityClass){
        return daoSession.getDao(entityClass).count();
    }

    /**
     * 根据ID获取Entity对象
     *
     * @param entityClass
     * @param id
     * @return
     * @throws Exception
     */
    public <T> T queryById(Class<T> entityClass,Object id){
        List<?> list = daoSession.getDao(entityClass).queryRawCreate("where"+daoSession.getDao(entityClass).getPkProperty().name + " = ?", String.valueOf(id)).list();
        if(!list.isEmpty()){
            return (T) list.get(0);
        }
        return null;
    }

    public <T> List<T> queryForAll(Class<T> entity){
        List<T> list = (List<T>) daoSession.getDao(entity).queryBuilder().list();
        return list;
    }

    /**
     * 根据ID删除Entity对象
     *
     * @param entityClass
     * @param id
     * @return
     * @throws Exception
     */
    public <T> boolean deleteById(Class<T> entityClass,Object id){
        daoSession.delete(entityClass);
        return true;
    }


    /**
     * 保存list Entity对象
     *
     * @param entityClass
     * @param entitys
     * @return
     * @throws Exception
     */
    public <T> void saveEntitys(Class<T> entityClass,List<T> entitys){
        if(!entitys.isEmpty()){
            for(T t : entitys){
                if(t == null){
                    continue;
                }
                daoSession.insertOrReplace(t);
            }
        }
        //daoSession.clear();
    }

    /**
     * 保存list Entity对象
     *
     * @param entityClass
     * @param entity
     * @return
     * @throws Exception
     */

    public <T> boolean saveEntity(Class<T> entityClass, T entity){
        if(entity == null){
            return false;
        }
        daoSession.insertOrReplace(entity);
        return true;
    }


}
