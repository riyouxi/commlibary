package modle.test.com.commlibary.db;


import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import modle.test.com.commlibary.entity.User;

public class TestIml {

    public static boolean insertUser(User user){
        return DBHelper.getHelper().saveEntity(User.class,user);
    }

    public static List<User> getALlData(){
        QueryBuilder<User> builder = DBHelper.getHelper().getDaoSession().getUserDao().queryBuilder();
        return builder.list();
    }


}
