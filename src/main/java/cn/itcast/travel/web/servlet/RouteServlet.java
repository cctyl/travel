package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Msg;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService routeService  = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分页查询
     * @param request
     * @param response
     */
    public void pageQuery(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //1.接受参数
        String currentPageStr  = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr  =  request.getParameter("cid");
        String rname = request.getParameter("rname");
        int cid = 0;//分类变量提前声明
        //2.处理参数
        if(cidStr!=null&&!cidStr.equals("null") && cidStr.length()>0){
            cid=Integer.parseInt(cidStr);
        }

        int currentPage  = 0;//当前页码，如果不传，则默认为第一页
        if(currentPageStr !=null && currentPageStr.length()>0){
            currentPage = Integer.parseInt(currentPageStr);
        }else {
            currentPage  = 1;
        }

        int pageSize  = 0; ///每页显示条数，如果不传递，默认每页显示5条记录
        if(pageSizeStr !=null && pageSizeStr.length()>0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else {
            pageSize  = 5;
        }
        //3.调用service查询PageBean对象
        PageBean<Route> pb = routeService.pageQuery(cid,currentPage,pageSize,rname);

        //4.将pageBean对象序列化为
        returnMsgWithJson(Msg.success().add("pageBean",pb));




    }


    /**
     * 根据rid查询线路详细信息
     * @param request
     * @param response
     * @throws IOException
     */
    public void findRoute(HttpServletRequest request,HttpServletResponse response) throws IOException {

        //1.接受rid
        String rid = request.getParameter("rid");
        System.out.println("rid="+rid);
        if (rid==null && rid.length()==0 && rid.equals("null")){
            //没拿到参数，直接请求失败
            returnMsgWithJson(Msg.fail());
            return;
        }

        //2.调用service，得到封装好数据的Route对象
        Route route = routeService.findByRid(rid);
        System.out.println(route.toString());
        //3.将对象返回
        returnMsgWithJson(Msg.success().add("route",route));

    }


    /**
     * 查询已登录用户是否收藏
     * @param request
     * @param response
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request,HttpServletResponse response) throws IOException{
        //1.判断用户是否登陆
        User user = (User) request.getSession().getAttribute("user");
        System.out.println("RouteServlet_isFavorite__session__user:"+user);
        if (user==null){
            //如果没有登陆，按理说，是不能请求这个地址的。但是现在却收到了请求，说明非法请求
            //非法请求，就什么也不做，直接返回
            returnMsgWithJson(Msg.object().addCode(302)); //302表示未登陆
            return;
        }
        //2.获取参数
        String rid = request.getParameter("rid");
        if (rid==null&&rid.length()==0&&rid.equals("")){
            //为空就不做后面的操作了
            returnMsgWithJson(Msg.object().addCode(305)); //305表示缺少参数
            return;
        }
        String uid = user.getUid()+"";

        //3.调用service
        boolean flag = favoriteService.isFavorite(rid, uid);

        //4.返回数据
        if (flag){
            returnMsgWithJson(Msg.success().add("flag",true));
        }else {
            returnMsgWithJson(Msg.success().add("flag",false));
        }

    }

    /**
     * 修改改线路的收藏状态
     * @param request
     * @param response
     * @throws IOException
     */
    public void changeFavorite(HttpServletRequest request,HttpServletResponse response) throws IOException{

        //1.判断用户是否登陆
        User user = (User) request.getSession().getAttribute("user");
        System.out.println("RouteServlet_changeFavorite__session__user:"+user);
        if (user==null){
            //如果没有登陆，按理说，是不能请求这个地址的。但是现在却收到了请求，说明非法请求
            //非法请求，就什么也不做，直接返回
            returnMsgWithJson(Msg.object().addCode(302)); //302表示未登陆
            return;
        }
        //2.获取参数
        String rid = request.getParameter("rid");
        if (rid==null&&rid.length()==0&&rid.equals("")){
            //为空就不做后面的操作了
            returnMsgWithJson(Msg.object().addCode(305)); //305表示缺少参数
            return;
        }
        String uid = user.getUid()+"";



        //3.调用service完成状态的更改
        //true 表示更改成功，false表示更改失败
        boolean result = favoriteService.changeFavorite(rid,uid);

        //4.返回数据
        if(result){
            //更改成功
            returnMsgWithJson(Msg.success());
        }else {
            //更改失败
            returnMsgWithJson(Msg.fail());
        }


    }


}
