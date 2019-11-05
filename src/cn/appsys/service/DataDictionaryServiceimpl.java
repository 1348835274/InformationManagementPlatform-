package cn.appsys.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.dictionary.DataDictionaryMapper;
import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
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
	public AppInfo selectId(String id) {

		return dataDictionaryMapper.selectId(id);
	}

}
