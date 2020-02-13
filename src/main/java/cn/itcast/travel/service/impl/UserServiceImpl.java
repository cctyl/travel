package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {

    private UserDao dao = new UserDaoImpl();


    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    public boolean regist(User user) {
        //1.查询用户
        User u = dao.findByName(user.getUsername());
        //2.判断用户是否存在
        if (u == null) {
            //不存在
            //3.获取激活码
            user.setCode(UuidUtil.getUuid() );
            //3.1设置激活状态
            user.setStatus("N");
            //4.调用mailUtis
            String content = "欢迎注册黑马旅游网，这是您的激活链接，请点击<a href='http://localhost:8080/travel/user/active?code="+user.getCode()+" '>链接</a>进行激活";
            MailUtils.sendMail(user.getEmail(), content, "黑马旅游网激活");
            //执行注册
            dao.save(user);
            return true;
        }
        //存在，注册失败
        return false;


    }

    /**
     * 激活用户
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //1.查询用户是否存在
        User u = dao.findByCode(code);
        if(u!=null){
            //2.用户存在，进行激活操作
            dao.updateStatus(u);
            return true;
        }else {
            //3.用户不存在，激活失败
            return false;
        }


    }

    /**
     * 用户登陆
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return dao.findByUNameAndPwd(user);
    }
}
