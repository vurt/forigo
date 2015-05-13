package com.vurt.node.server.service;

import java.util.List;

import com.chinacreator.c2.dao.Dao;
import com.chinacreator.c2.dao.DaoFactory;
import com.vurt.node.server.bean.ApplicationVersion;


public class AppService {
	public List<ApplicationVersion> getVersions(){
		Dao<ApplicationVersion> dao=DaoFactory.create(ApplicationVersion.class);
		return dao.selectAll();
	}
}
