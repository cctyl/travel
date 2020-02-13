package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {
    /**
     * 根据uid和rid查询Favorite对象
     * @param uid
     * @param rid
     * @return
     */
    public Favorite findByUidAndRid(String rid,String uid);

    /**
     * 查询rid路线的收藏次数
     * @param rid
     * @return
     */
    public int findCountByRid(String rid);

    /**
     * 添加收藏
     * @param rid
     * @param uid
     */
    public void add(String rid, String uid);

    /**
     * 取消收藏
     * @param rid
     * @param uid
     */
    void cancel(String rid, String uid);
}
