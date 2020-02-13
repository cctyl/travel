package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {
    /**
     * 根据sid查找商家
     * @param sid
     * @return
     */
    public Seller findSellerBySid(String sid);
}
