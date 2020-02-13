package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据cid（分类）查询总记录数
     * @param cid
     * @return
     */
    @Override
    public int findTotalCount(int cid,String rname) {
        //1.组装sql模版
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);

        //1.5存储参数
        List params = new ArrayList();

        //2.判断参数是否有值
        if(cid!=0){
            //如果为0说明前端传的是null，也就是空
            sb.append(" and cid = ? ");
            params.add(cid);//添加参数到 list中
        }
        if(rname !=null&&!rname.equals("null") && rname.length()>0){
            sb.append(" and rname like ?");
            params.add("%"+rname+"%");
        }
        sql =sb.toString();
        return  template.queryForObject(sql, Integer.class, params.toArray());

    }

    /**
     * 根据cid，start，pageSize查询当前页的数据集合
     * @param cid
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<Route> findByPage(int cid, int start, int pageSize,String rname) {

        //1.sql模版
        String sql = "select * from tab_route where 1=1 ";
        StringBuilder builder = new StringBuilder(sql);
        List params = new ArrayList();
        //2.判断参数
        if (cid!=0){
            builder.append(" and cid = ? ");
            params.add(cid);

        }

        if (rname !=null&&!rname.equals("null")&& rname.length()>0){
            builder.append(" and rname like ? ");
            params.add("%"+rname+"%");
        }

        //3.给另外两个参数留下位置
        builder.append(" limit ? , ?  ");
        sql = builder.toString();
        params.add(start);
        params.add(pageSize);//所有参数都放在一个list中

        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),params.toArray());


    }


    /**
     * 根据rid查询线路信息
     * @param rid
     * @return
     */
    @Override
    public Route findByRid(String rid) {
        Route route = null;
        try {
            String sql = "select * from tab_route where rid = ?";
            route = template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class),rid);
            System.out.println("RouteDao findByRid == "+route);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return route;
    }
}
