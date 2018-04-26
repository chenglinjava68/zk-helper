package org.orclight.java.middlerware.zk.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.orclight.java.middlerware.zk.result.DirReadResult;

/**
 * Nothing seek, Nothing find.
 * author: shuzhilong
 * Date: 2018/4/26 上午11:20
 * desc: (The role of this class is to ...)
 * To change this template use preferences | editor | File and code Templates
 */
public class ZKDirReadUtil {

    public static DirReadResult read(String zkAddress,String path) {
        CuratorFramework zk =
                CuratorFrameworkFactory.newClient(zkAddress,new ExponentialBackoffRetry(1000, 3));
        return new DirReadResult(zk,path);
    }
}
