package cn.appsys.dao.backend;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DevUser;

//专门用来验证登入
public interface BackendMapper {
	// 验证账号是否可以的登入后端
	public BackendUser sel_backend(@Param("userCode") String userCode, @Param("userPassword") String userPassword);

	// 验证账号密码是否能登入到前端
	public DevUser sel_dev(@Param("devCode") String devCode, @Param("devPassword") String devPassword);

	// 查询app的总数量
	public int select(@Param("softwareName") String softwareName, @Param("flatformId") String flatformId,
			@Param("categoryLevel1") String categoryLevel1, @Param("categoryLevel2") String categoryLevel2,
			@Param("categoryLevel3") String categoryLevel3);

	// 分页查询
	public List<AppInfo> selectAll(@Param("softwareName") String softwareName, @Param("flatformId") String flatformId,
			@Param("categoryLevel1") String categoryLevel1, @Param("categoryLevel2") String categoryLevel2,
			@Param("categoryLevel3") String categoryLevel3, @Param("index") int index,

			@Param("currentPageNo") int currentPageNo);

	// 审批详细
	public AppInfo selAppinfo(@Param("id") String id);

	// 版本升级表
	public AppVersion alter(@Param("id") String id);

	// 修改app当前状态
	public Boolean alterApp(@Param("id") String id, @Param("status") String status);

}
