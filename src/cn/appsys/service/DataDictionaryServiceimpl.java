package cn.appsys.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.dictionary.DataDictionaryMapper;
import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;

@Service
public class DataDictionaryServiceimpl implements DataDictionaryService {

	@Resource
	private DataDictionaryMapper dataDictionaryMapper;

	public List<DataDictionary> appState() {
		return dataDictionaryMapper.appState();
	}

	public List<DataDictionary> appWhat() {
		return dataDictionaryMapper.appWhat();
	}

	@Override
	public List<AppCategory> appCate1() {
		return dataDictionaryMapper.appCate1();
	}

	@Override
	public List<AppCategory> appCate23(String pid) {

		return dataDictionaryMapper.appCate23(pid);
	}

	@Override
	public List<AppInfo> selectAll(String softwareNamex, String status, String flatformId, String categoryLevel1,
			String categoryLevel2, String categoryLevel3, int index, int currentPageNo) {

		return dataDictionaryMapper.selectAll(softwareNamex, status, flatformId, categoryLevel1, categoryLevel2,
				categoryLevel3, index, currentPageNo);
	}

	@Override
	public int select(String softwareNamex, String status, String flatformId, String categoryLevel1,
			String categoryLevel2, String categoryLevel3) {

		return dataDictionaryMapper.select(softwareNamex, status, flatformId, categoryLevel1, categoryLevel2,
				categoryLevel3);
	}

	@Override
	public AppInfo selectId(AppInfo appInfo) {

		return dataDictionaryMapper.selectId(appInfo);
	}

	@Override
	public int updateAll(AppInfo appInfo) {

		return dataDictionaryMapper.updateAll(appInfo);
	}

	@Override
	public AppInfo selectId1(String id) {

		return dataDictionaryMapper.selectId1(id);
	}

	@Override
	public List<AppVersion> selectIds(String id) {

		return dataDictionaryMapper.selectIds(id);
	}

	@Override
	public int delete(String id) {

		return dataDictionaryMapper.delete(id);
	}

	@Override
	public int delete1(String id) {

		return dataDictionaryMapper.delete1(id);
	}

	@Override
	public int updateBB(AppVersion appVersion) {

		return dataDictionaryMapper.updateBB(appVersion);
	}

	@Override
	public AppVersion selectBB(String id) {

		return dataDictionaryMapper.selectBB(id);
	}

	@Override
	public String selectStatus(String id) {

		return dataDictionaryMapper.selectStatus(id);
	}

	@Override
	public int updateSp(String status, String id) {

		return dataDictionaryMapper.updateSp(status, id);
	}

	@Override
	public int insertVersion(AppVersion appVersion) {

		return dataDictionaryMapper.insertVersion(appVersion);
	}

	@Override
	public int updateVersionId(Integer id, Integer versionId) {

		return dataDictionaryMapper.updateVersionId(id, versionId);
	}

	@Override
	public int bb(Integer appId) {

		return dataDictionaryMapper.bb(appId);
	}

	@Override
	public int addAppinfo(AppInfo appinfo) {
		
		return dataDictionaryMapper.addAppinfo(appinfo);
	}

}
