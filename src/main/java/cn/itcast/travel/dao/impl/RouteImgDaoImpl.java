package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class RouteImgDaoImpl implements RouteImgDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * 根据rid查询图片信息
     * @param rid
     * @return
     */
    @Override
    public List<RouteImg> findImgByRid(String rid) {
        String sql = "select * from tab_route_img where rid = ?";
        List<RouteImg> routeImgList = template.query(sql, new BeanPropertyRowMapper<RouteImg>(RouteImg.class),rid);
        System.out.println("RouteImgDaoImpl --findImgByRid  routeImgList长度为 "+routeImgList.size());
        return routeImgList;
    }
}
