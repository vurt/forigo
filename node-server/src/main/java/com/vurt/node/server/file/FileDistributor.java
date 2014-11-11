package com.vurt.node.server.file;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.vurt.node.server.comunication.ChannelHolder;
import com.vurt.node.server.comunication.Constants;
import com.vurt.node.server.comunication.data.FilePullRequest;
import com.vurt.node.server.exception.GroupNotValidException;
import com.vurt.node.server.service.NodeService;

/**
 * 文件分发工具
 * <p/>
 * 静态资源目录同步逻辑：
 * <p/>
 * 1.节点心跳时会将节点上的静态资源目录的最后修改时间发送回服务器
 * 2.如果最后修改时间不匹配，则服务器主动向节点发送一个同步资源的消息
 * 3.节点收到资源同步消息后，向服务器请求最新的服务器资源目录结构(文件树+每个文件的MD5+最后修改时间)
 * 4.节点收到目录结构后，遍历自身资源目录，找出差异节点，然后向服务器发送下载请求
 * 5.节点所有的下载任务都完成后，更新静态资源目录的最后修改时间
 *
 * @author Vurt
 */
public class FileDistributor {
    private static FileDistributor instance;

    private static Object lock = new Object();

    public static final FileDistributor getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new FileDistributor();
                }
            }
        }
        return instance;
    }

    private Channel channel;

    private FileDistributor() {
        try {
            channel = ChannelHolder.createChannel();
            channel.exchangeDeclare(Constants.EXCHANGE_FILE, "direct", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向指定分组的节点推送文件获取请求，如果推送的是目录，那么节点端会遍历目录下的所有文件，在节点上不存或者有修改的文件全部都会被同步过去
     *
     * @param group   节点id，还支持{@link Constants#EMPTY_GROUP_SEVERITY}和{@link Constants#ALL_GROUP_SEVERITY}
     * @param request 请求详情
     * @throws IOException
     */
    public void broadcastFilePullRequest(String group, FilePullRequest request) throws IOException {
        if (StringUtils.isEmpty(group)) {
            //未指定分组即为发送到所有分组
            group = Constants.ALL_GROUP_SEVERITY;
        }
        byte[] requestContent = JSON.toJSONBytes(request);
        if (StringUtils.equals(Constants.ALL_GROUP_SEVERITY, group)) {
            channel.basicPublish(Constants.EXCHANGE_FILE, Constants.EMPTY_GROUP_SEVERITY, null, requestContent);
            for (String target : new NodeService().getGroups().keySet()) {
                channel.basicPublish(Constants.EXCHANGE_FILE, target, null, requestContent);
            }
        } else {
            if (!new NodeService().getGroups().containsKey(group)) {
                throw new GroupNotValidException("分组：" + group + "不存在");
            }
            channel.basicPublish(Constants.EXCHANGE_FILE, group, null, requestContent);
        }
    }


    /**
     * 读取整个目录的最后修改时间(查看所有文件和目录的最后修改时间，找到最晚的那个)
     *
     * @param directory 目标目录
     * @return 目录的最后修改时间
     */
    public static long getLastModified(File directory) {
        File[] files = directory.listFiles();
        if (files.length == 0) return directory.lastModified();
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File o1, File o2) {
                return new Long(o2.lastModified()).compareTo(o1.lastModified()); //latest 1st
            }
        });
        return files[0].lastModified();
    }
}
