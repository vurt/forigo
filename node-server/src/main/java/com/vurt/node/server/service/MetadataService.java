package com.vurt.node.server.service;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.chinacreator.c2.config.ConfigManager;
import com.chinacreator.c2.dao.Dao;
import com.chinacreator.c2.dao.DaoFactory;
import com.chinacreator.c2.fs.FileServer;
import com.chinacreator.c2.ioc.ApplicationContextManager;
import com.vurt.node.data.Constants;
import com.vurt.node.server.bean.Metadata;
import com.vurt.node.server.exception.InvalidMetadataException;

public class MetadataService {
	private FileServer fileServer = ApplicationContextManager.getContext().getBean("dirFileServer", FileServer.class);
	
	public int addMetadata(Metadata metadata){
		if(StringUtils.isEmpty(metadata.getSettingsXml())){
			throw new InvalidMetadataException("没有选择元数据配置文件");
		}
		try {
			if(!fileServer.exsits(metadata.getSettingsXml())){
				throw new InvalidMetadataException("元数据配置文件["+metadata.getSettingsXml()+"]不存在!");
			}
		} catch (Exception e) {
			throw new InvalidMetadataException("元数据配置文件["+metadata.getSettingsXml()+"]不存在!");
		}
		metadata.setId(null);
		metadata.setCreateat(new Timestamp(System.currentTimeMillis()));
		
		Dao<Metadata> dao=DaoFactory.create(Metadata.class);
		dao.insert(metadata);
		return metadata.getId();
	}
	
	public Metadata getCurrMetadata(){
		Dao<Metadata> dao=DaoFactory.create(Metadata.class);
		Metadata metadata=dao.selectOne(new Metadata());
		return metadata;
	}
	
	public String getCurrHost(){
		return ConfigManager.getInstance().getConfig(Constants.MQ_CONFIG_HOST);
	}
}
