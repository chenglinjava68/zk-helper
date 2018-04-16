package org.orclight.java.middlerware.zk.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.orclight.java.middlerware.zk.result.DirReadResult;

/**
 * Nothing seek, Nothing find.
 * author: shuzhilong
 * Date: 2018/4/12 下午2:13
 * desc: (The role of this class is to ...)
 * To change this template use preferences | editor | File and code Templates
 */
public class ZKDirStatUtil {

    public static DirReadResult stat(String fromZKAddress,String path) {
        CuratorFramework fromZK =
                CuratorFrameworkFactory.newClient(fromZKAddress,new ExponentialBackoffRetry(1000, 3));
        DirReadResult dirReadResult = new DirReadResult(fromZK,path);
        return dirReadResult;
    }
}
