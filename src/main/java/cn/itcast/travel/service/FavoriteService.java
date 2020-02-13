package cn.itcast.travel.service;

public interface FavoriteService {

    /**
     * 判断是否收藏
     * @param rid
     * @param uid
     * @return
     */
    public boolean isFavorite(String rid,String uid);

    /**
     * 修改收藏状态
     * @param rid
     * @param uid
     * @return
     */
    boolean changeFavorite(String rid, String uid);
}
