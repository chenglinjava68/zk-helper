package org.orclight.java.middlerware.zk.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.orclight.java.middlerware.zk.result.DirDirectWriterResult;

import java.util.Map;

/**
 * Nothing seek, Nothing find.
 * author: shuzhilong
 * Date: 2018/4/26 上午11:20
 * desc: (The role of this class is to ...)
 * To change this template use preferences | editor | File and code Templates
 */
public class ZKDirWriteUtil {

    public static DirDirectWriterResult write(String zkAddress,Map<String, String> valueMap) {
        CuratorFramework zk =
                CuratorFrameworkFactory.newClient(zkAddress,new ExponentialBackoffRetry(1000, 3));
        DirDirectWriterResult dirDirectWriterResult = new DirDirectWriterResult(zk,valueMap);
        dirDirectWriterResult.write();
        return dirDirectWriterResult;
    }
}
