package cn.appsys.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.backend.BackendMapper;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DevUser;

@Service("backendSerivce")
public class BackendSerivceompl implements BackendSerivce {

	@Resource
	private BackendMapper backendMapper;

	public BackendUser sel_backend(String userCode, String userPassword) {
		// 后
		return backendMapper.sel_backend(userCode, userPassword);
	}

	public DevUser sel_dev(String devCode, String devPassword) {
		// 前
		return backendMapper.sel_dev(devCode, devPassword);
	}

	// 总数
	public int select(String softwareName, String flatformId, String categoryLevel1, String categoryLevel2,
			String categoryLevel3) {

		return backendMapper.select(softwareName, flatformId, categoryLevel1, categoryLevel2, categoryLevel3);
	}

	// 分页
	public List<AppInfo> selectAll(String softwareName, String flatformId, String categoryLevel1, String categoryLevel2,
			String categoryLevel3, int index, int currentPageNo) {
		return backendMapper.selectAll(softwareName, flatformId, categoryLevel1, categoryLevel2, categoryLevel3, index,
				currentPageNo);
	}

}
