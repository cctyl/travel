package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * 根据uid和rid查询Favorite对象
     * @param uid
     * @param rid
     * @return
     */
    @Override
    public Favorite findByUidAndRid(String rid,String uid) {
        Favorite favorite = null;
        try {
            String sql = "select * from tab_favorite where rid= ? and uid =?";
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class),rid,uid);
        } catch (DataAccessException e) {
            System.out.println("findByUidAndRid出现异常，猜测是查询未空，未收藏");
        }
        System.out.println("findByUidAndRid: "+favorite);
        return favorite;

    }

    /**
     * 查询线路收藏次数
     * @param rid
     * @return
     */
    @Override
    public int findCountByRid(String rid) {
        String sql = "select count(*) from  tab_favorite where rid = ?";
       int count = template.queryForObject(sql, Integer.class,rid);
        return count;
    }


    /**
     * 修改收藏状态
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public void add(String rid, String uid) {
        String sql = "insert into tab_favorite values(?,?,?)";
        template.update(sql,rid,new Date(),uid);
    }

    @Override
    public void cancel(String rid, String uid) {
        String sql = "delete from tab_favorite where rid=? and uid=?";
        template.update(sql,rid,uid);
    }


}
