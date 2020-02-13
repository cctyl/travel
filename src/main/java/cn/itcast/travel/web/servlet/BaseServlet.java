package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Msg;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@WebServlet(name = "BaseServlet")
public class BaseServlet extends HttpServlet {
    private HttpServletResponse response = null;
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("执行了baseServlet");
        /*
        完成方法的分发
         */
        //1.获取请求路径
        String uri = req.getRequestURI(); // 会拿到 /user/add
//        System.out.println(uri);
        //2.获取方法名称（指userServlet中的方法）
            //通过拆字符串的方式，拿到方法名
            //这里先拿到最后一根斜杠的索引index，再让index+1，此时指针指向'add'的 ‘a’
            //拿到位置之后，用String对象的substring(索引)方式，拿到/ 后面的 add
            //这样我就知道了他的原理，那么这种方式只能拿到最后一级路径，显然只支持第二级目录，如果变成http://localhost:8080/travel/user/add/abb 就只能拿到abb而不是add了
        String methodName = uri.substring(uri.lastIndexOf('/')+1);
//        System.out.println(methodName);

        //3.获取方法对象method
        //这里会用到反射的知识
        //首先要拿到UserServlet的字节码对象，通过字节码对象的getMethod(name,param1,...) 传入方法名和参数，即可拿到方法对象
        try {
            //谁调用this，this就表示谁
            //当userservlet被访问，他就会调用自己的service方法，而自己的service方法里的this，显然也是表示UserServlet
            //发现，要想获取到userservlet的方法，这个方法不能为protected，也不能为private,应该为public
            //如果连private和protected也要反射，就需要修改访问权限，也就是暴力反射，但是尽量不要用
            //将方法改为public即可

//            System.out.println(this);
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

            //在执行方法之前赋值
            //附加1：每次调用service就将成员变量response填充
            this.response=resp;

            //4.执行方法
            //invoke(obj,param1..) 首先要传入一个对象，不然谁执行了这个方法都不知道。一般写this，就是UserServlet执行的
            //后面的参数，全部都是方法本身需要用到的。这里代理的是servlet的方法，总共只需要两个参数，request和response
            method.invoke(this,req,resp);




        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }



    }

    /**
     * 将msg对象以json形式返回
     * @param msg 封装信息的对象
     * @throws IOException
     */
    public  void returnMsgWithJson(Msg msg) throws IOException {
        //.1将msg转为json
        ObjectMapper mapper = new ObjectMapper();
        String resultJson = mapper.writeValueAsString(msg);

        //.2将json写回客户端
        response.setContentType("application/json;charset=utf-8");//设置请求头
        response.getWriter().write(resultJson);


    }
}
