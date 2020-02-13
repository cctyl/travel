package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    User findByName(String username);


    /**
     * 用户添加
     * @param user
     */
    void save(User user);

    /**
     * 通过激活码查询用户
     * @param code
     * @return
     */
    User findByCode(String code);

    /**
     * 修改用户的激活状态
     * @param u
     */
    void updateStatus(User u);

    /**
     * 根据用户名和密码查询
     * @param user
     * @return
     */
    User findByUNameAndPwd(User user);
}
