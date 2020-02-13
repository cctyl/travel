package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 通过用户名查询用户
     *
     * @param username
     * @return
     */
    public User findByName(String username) {
        User user = null;
        try {
            //1.定义sql
            String sql = "select * from tab_user where username = ?";
            //2.执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (Exception e) {

        }

        return user;
    }


    /**
     * 用户添加
     *
     * @param user
     */
    @Override
    public void save(User user) {

        //1.定义sql
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";

        //2.执行sql
        template.update(sql, user.getUsername(), user.getPassword(), user.getName(), user.getBirthday(), user.getSex(), user.getTelephone(), user.getEmail(),user.getStatus(),user.getCode());
    }


    /**
     * 通过激活码查询用户
     *
     * @param code
     * @return
     */
    @Override
    public User findByCode(String code) {

        User user = null;
        try {
            //1.编写sql语句
            String sql = "select * from tab_user where code = ?";
            //2.执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        } catch (Exception e) {

        }
        return user;
    }


    /**
     * 修改用户的激活状态
     *
     * @param u
     */
    @Override
    public void updateStatus(User u) {
        //1.编写sql语句
        String sql = "update tab_user set status = 'Y' where username =?";
        //2.执行sql语句
        template.update(sql, u.getUsername());
    }

    /**
     * 根据用户名和密码查询
     * @param user
     * @return 返回查询得到的user
     */
    @Override
    public User findByUNameAndPwd(User user) {
        User returnUser = null;
        try {
            //1.定义sql
            String sql = "select * from tab_user where username = ? and password =?";
            //2.执行sql
            returnUser = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), user.getUsername(),user.getPassword());
        } catch (Exception e) {

        }

        return returnUser;
    }


}
