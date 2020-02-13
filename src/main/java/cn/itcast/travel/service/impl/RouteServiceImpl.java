package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    /**
     * 分页查询
     *
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname) {
        //1.查出总记录数
        int totalCount = routeDao.findTotalCount(cid,rname);
        //2.算出总页数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize + 1);
        //3.算出起始位置，查出list
        int start = (currentPage - 1) * pageSize;
        List<Route> routeList = routeDao.findByPage(cid, start, pageSize,rname);
        //4.封转到PageBean,然后返回
        PageBean<Route> routePageBean = new PageBean<>(totalCount, totalPage, currentPage, pageSize, routeList);
        System.out.println("servlet:查询结果"+routePageBean);
        return routePageBean;
    }


    /**
     * 根据rid查询用户信息
     * @param rid
     * @return
     */
    @Override
    public Route findByRid(String rid) {

        //1.根据rid查询route对象
        Route byRid = routeDao.findByRid(rid);

        //2.查询图片信息集合,并封装到RouteList对象中
        List<RouteImg> imgList = routeImgDao.findImgByRid(rid);
        byRid.setRouteImgList(imgList);

        //3.根据route的sid信息，去查询卖家信息
        Seller sellerBySid = sellerDao.findSellerBySid(byRid.getSid() + "");
        byRid.setSeller(sellerBySid);

        //4.查询收藏次数
        int countByRid = favoriteDao.findCountByRid(rid);
        byRid.setCount(countByRid);


        return byRid;
    }
}
