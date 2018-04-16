package org.orclight.java.middlerware.zk.result;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.zookeeper.data.Stat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Nothing seek, Nothing find.
 * author: shuzhilong
 * Date: 2018/4/12 上午10:35
 * desc: (The role of this class is to ...)
 * To change this template use preferences | editor | File and code Templates
 */
public class DirDeleteResult {

    CuratorFramework curatorFramework;
    private Set<String> errorSet = new HashSet<String>();
    private AtomicInteger successCnt =  new AtomicInteger();

    public DirDeleteResult(CuratorFramework curatorFramework, String path) {
        this.curatorFramework = curatorFramework;CuratorFrameworkState state = curatorFramework.getState();
        if(state==CuratorFrameworkState.LATENT) {
            curatorFramework.start();
        }
        deleteDir(path);
    }

    public void deleteDir(String path) {
        try {
            Stat stat = curatorFramework.checkExists().forPath(path);
            if(stat!=null) {
                List<String> childrenList = curatorFramework.getChildren().forPath(path);
                if(childrenList!=null && childrenList.size()>0) {
                    for(String childPath:childrenList) {
                        String childrenFullPath = path+"/"+childPath;
                        deleteDir(childrenFullPath);
                    }
                }
                boolean ret = deleteNode(path);
                if(!ret) {
                    errorSet.add(path);
                } else {
                    successCnt.incrementAndGet();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteNode(String path) {
        boolean ret = false;
        try {
            Stat stat = curatorFramework.checkExists().forPath(path);
            if(stat!=null) {
                curatorFramework.delete().forPath(path);
                System.out.println("delete:" + path);
            } else {
                System.out.println("node not exist path:" + path);
            }
            ret = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public int getSuccessCnt() {
        return successCnt.get();
    }

    public int getErrorCnt() {
        int cnt = 0;
        if(errorSet!=null && errorSet.size()>0) {
            cnt = errorSet.size();
        }
        return cnt;
    }

    public Set<String> getErrorResult() {
        return errorSet;
    }

    public void printResultInfo() {
        System.out.println("success cnt : "+getSuccessCnt());
        System.out.println("error cnt : "+getErrorCnt());
    }
}
