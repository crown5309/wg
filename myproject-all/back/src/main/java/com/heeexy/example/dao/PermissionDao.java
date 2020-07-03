package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.Set;

import org.apache.ibatis.annotations.Param;

/**
 * @author: hxy
 * @date: 2017/10/30 13:28
 */
public interface PermissionDao {
	/**
	 * 查询用户的角色 菜单 权限
	 */
	JSONObject getUserPermission(@Param("username")String username,@Param("type")String type);

	/**
	 * 查询所有的菜单
	 */
	Set<String> getAllMenu();

	/**
	 * 查询所有的权限
	 */
	Set<String> getAllPermission();
}
