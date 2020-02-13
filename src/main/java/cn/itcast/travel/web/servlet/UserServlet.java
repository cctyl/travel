package cn.itcast.travel.web.servlet;


import cn.itcast.travel.domain.Msg;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    /**
     * 用户激活方法
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.取得激活码
        String code = request.getParameter("code");
        //2.判断激活码是否为空
        if (code != null) {
            //3.调用service进行激活操作
            UserService service = new UserServiceImpl();
            boolean activeResult = service.active(code);

            //4.判断激活结果
            String msg = null;
            if (activeResult) {
                //激活成功
                msg = "激活成功，请<a href='login.html'>登录</a>";
            } else {
                //激活失败
                msg = "激活失败，请联系管理员";

            }

            //5.返回提示，这里由于没有专门的提示页面，因此只能返回一简单的信息
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }


    }


    /**
     * 用户注册方法
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws IOException {
        {

            //0.1获取前台验证码
            String checkCode = request.getParameter("check");
            //0.2从session中取出验证码
            HttpSession session = request.getSession();
            String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
            session.removeAttribute("CHECKCODE_SERVER");

            //0.3校验验证码
            if (checkcode_server == null || !checkCode.equalsIgnoreCase(checkcode_server)) {
                //不通过，返回提示信息，不执行下方逻辑
                Msg msg = new Msg();
                msg.setCode(100);
                msg.setMsg("验证码错误");

                //将msg转为json
                ObjectMapper mapper = new ObjectMapper();
                String resultJson = mapper.writeValueAsString(msg);

                //将json写回客户端
                response.setContentType("application/json;charset=utf-8");//设置请求头
                response.getWriter().write(resultJson);

                return;  //直接返回，不执行下面的逻辑
            }


            //1.获取前台传来的注册数据
            Map<String, String[]> map = request.getParameterMap();

            //2.将注册数据封装成一个实体类

            User user = new User();
            try {
                BeanUtils.populate(user, map);
                System.out.println(user);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }


            //3.调用service的注册方法，并把实体类传入
            UserService userService = new UserServiceImpl();
            boolean registResult = userService.regist(user);
            Msg msg = new Msg();
            //4.向前台返回注册结果
            if (registResult) {
                //注册成功
                msg.setCode(200);
                msg.addMsg("注册成功");
            } else {
                msg.setCode(100);
                msg.addMsg("注册失败");
            }
       /* //4.1将msg转为json
        ObjectMapper mapper = new ObjectMapper();
        String resultJson = mapper.writeValueAsString(msg);

        //4.2将json写回客户端
        response.setContentType("application/json;charset=utf-8");//设置请求头
        response.getWriter().write(resultJson);*/
            //5.返回数据
            returnMsgWithJson(msg);
        }


    }

    /**
     * 退出登陆
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.销毁session
        request.getSession().invalidate();

        //2.请求重定向 跳转页面
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    /**
     * 查询一个用户
     * 判断用户是否登陆
     * @param request
     * @param response
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException {


        //1.从session中取出数据返回给前端
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) { //session中有数据，才执行下方操作
            // System.out.println(user.toString());
            //2.因为不想让密码泄漏，因此只返回姓名和id
            User toJson = new User();
            toJson.setUsername(user.getUsername());
            toJson.setUid(user.getUid());


            //3.将数据写回
            returnMsgWithJson(Msg.success().add("user", toJson));
        } else {
            //4.session中没有数据，提示登陆
            returnMsgWithJson(Msg.fail().addMsg("尚未登陆，请登录"));
        }


    }

    /**
     *
     * 用户登陆方法
     * @param request
     * @param response
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {

            //1.获取用户信息
            Map<String, String[]> map = request.getParameterMap();

            //2.封装成user
            User user = new User();
            try {
                BeanUtils.populate(user, map);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            //3.调用service方法查询用户
            UserService service = new UserServiceImpl();
            User returnUser = service.login(user);

            //4.判断用户是否存在
            if (returnUser ==null){
                //不存在，直接返回信息
                returnMsgWithJson(Msg.fail().addMsg("用户不存在，请检查用户名或密码"));
                return;
            }
            //5.判断用户是否激活
            if(returnUser.getStatus().equals('N')){
                //未激活，返回提示
                returnMsgWithJson(Msg.fail().addMsg("用户未激活，请到邮箱激活"));
                return;
            }


            //6.登陆成功
            //6.1将数据存放到session中
            request.getSession().setAttribute("user",returnUser);
            //6.2将数据返回
            returnMsgWithJson(Msg.success());



    }


}
