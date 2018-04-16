package org.orclight.java.middlerware.zk.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.orclight.java.middlerware.zk.result.DirDeleteResult;

/**
 * Nothing seek, Nothing find.
 * author: shuzhilong
 * Date: 2018/4/11 下午7:52
 * desc: (The role of this class is to ...)
 * To change this template use preferences | editor | File and code Templates
 */
public class ZKDirDeleteUtil {

    public static DirDeleteResult deleteDir(String zkAddress,String path) {
        CuratorFramework curatorFramework =
                CuratorFrameworkFactory.newClient(zkAddress, new ExponentialBackoffRetry(1000, 3));
        DirDeleteResult dirDeleteResult = new DirDeleteResult(curatorFramework,path);
        return dirDeleteResult;
    }

    public static boolean deleteNode(String zkAddress,String path) {
        boolean ret = false;
        CuratorFramework curatorFramework = null;
        try {
            curatorFramework =
                    CuratorFrameworkFactory.newClient(zkAddress, new ExponentialBackoffRetry(1000, 3));
            if(curatorFramework.getState()==CuratorFrameworkState.LATENT) {
                curatorFramework.start();
            }
            curatorFramework.delete().forPath(path);
            ret = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(curatorFramework!=null) {
                curatorFramework.close();
            }
        }
        return ret;
    }
}
