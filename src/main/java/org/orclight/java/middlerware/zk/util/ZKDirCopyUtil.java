package org.orclight.java.middlerware.zk.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.orclight.java.middlerware.zk.result.DirReadResult;
import org.orclight.java.middlerware.zk.result.DirCopyWriterResult;

import java.util.Map;

/**
 * Nothing seek, Nothing find.
 * author: shuzhilong
 * Date: 2018/4/11 下午4:06
 * desc: (The role of this class is to ...)
 * To change this template use preferences | editor | File and code Templates
 */
public class ZKDirCopyUtil {

    public static void copy(String fromZKAddress,String toZkAddress,String fromPath,String toPath) {
        CuratorFramework fromZK =
                CuratorFrameworkFactory.newClient(fromZKAddress,new ExponentialBackoffRetry(1000, 3));
        DirReadResult dirReadResult = new DirReadResult(fromZK,fromPath);

        CuratorFramework toZK =
                CuratorFrameworkFactory.newClient(toZkAddress,new ExponentialBackoffRetry(1000, 3));

        DirCopyWriterResult dirWriterResult = new DirCopyWriterResult(toZK,fromPath,toPath,dirReadResult.getAllNodeValue());

        dirWriterResult.write();

        dirReadResult.printResultInfo();

        dirWriterResult.printResultInfo();
    }

    public static void printMapInfo(Map<String,Map<String,String>> childrenMap) {
        if(childrenMap!=null && childrenMap.size()>0) {
            for(String parent:childrenMap.keySet()) {
                System.out.println("******************************************");
                System.out.println(parent);
            }
        }
    }


}
