package cn.appsys.dao.dictionary;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DevUser;

//字典表   用来加载App开发的维护
public interface DataDictionaryMapper {
	// app的状态
	public List<DataDictionary> appState();

	// 所属平台
	public List<DataDictionary> appWhat();

	// 查询一级目录
	public List<AppCategory> appCate1();

	// 查询二级三级目录
	public List<AppCategory> appCate23(@Param("pid") String pid);

	// 查询总数量
	public int select(@Param("softwareName") String softwareNamex, @Param("status") String status,
			@Param("flatformId") String flatformId, @Param("categoryLevel1") String categoryLevel1,
			@Param("categoryLevel2") String categoryLevel2, @Param("categoryLevel3") String categoryLevel3);
	
	// 分页并且模糊查询信息
	public List<AppInfo> selectAll(@Param("softwareName") String softwareNamex, @Param("status") String status,
			@Param("flatformId") String flatformId, @Param("categoryLevel1") String categoryLevel1,
			@Param("categoryLevel2") String categoryLevel2, @Param("categoryLevel3") String categoryLevel3,
			@Param("index") int index, @Param("currentPageNo") int currentPageNo);
}
