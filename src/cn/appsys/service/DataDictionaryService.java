package cn.appsys.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;

public interface DataDictionaryService {
	// app的状态
	public List<DataDictionary> appState();

	// 所属平台
	public List<DataDictionary> appWhat();

	// 查询一级目录
	public List<AppCategory> appCate1();

	// 查询二级三级目录
	public List<AppCategory> appCate23(@Param("pid") String pid);

	// 查询总数量
	public int select(String softwareNamex, String status, String flatformId, String categoryLevel1,
			String categoryLevel2, String categoryLevel3);

	// 按条件查询信息
	public AppInfo selectId(AppInfo appInfo);

	// 分页并且模糊查询信息
	public List<AppInfo> selectAll(String softwareNamex, String status, String flatformId, String categoryLevel1,
			String categoryLevel2, String categoryLevel3, int index, int currentPageNo);

	// 修改
	public int updateAll(AppInfo appInfo);

	// 按id查询详细信息
	public AppInfo selectId1(String id);

	// 查询历史版本
	public List<AppVersion> selectIds(String id);

	// 删除版本信息
	public int delete(String id);

	// 删除app信息
	public int delete1(String id);

	// 修改版本信息
	public int updateBB(AppVersion appVersion);

	// 查询版本信息
	public AppVersion selectBB(String id);

	// 按id查询状态
	public String selectStatus(@Param("id") String id);

	// 商品上架下架
	public int updateSp(@Param("status") String status);
}
