package com.heeexy.example.dao;

import java.util.List;

import com.heeexy.example.entity.StoreGoodsList;

public interface CartDao {

	List<StoreGoodsList> getStoreList(String[] cartIds);

}
