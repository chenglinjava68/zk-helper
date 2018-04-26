package org.orclight.java.middlerware.zk.result;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.zookeeper.data.Stat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Nothing seek, Nothing find.
 * author: shuzhilong
 * Date: 2018/4/11 下午7:02
 * desc: (The role of this class is to ...)
 * To change this template use preferences | editor | File and code Templates
 */
public class DirDirectWriterResult {

    CuratorFramework curatorFramework;
    Map<String,String> valueMap = new ConcurrentHashMap<String, String>();
    static Map<String,String> writeErrorMap = new ConcurrentHashMap<String, String>();

    private AtomicInteger addCnt = new AtomicInteger();
    private AtomicInteger updateCnt = new AtomicInteger();

    public DirDirectWriterResult(CuratorFramework curatorFramework,Map<String, String> valueMap) {
        this.curatorFramework = curatorFramework;
        this.valueMap = valueMap;
        CuratorFrameworkState state = curatorFramework.getState();
        if(state==CuratorFrameworkState.LATENT) {
            curatorFramework.start();
        }
    }

    public void write() {
        if(valueMap!=null && valueMap.size()>0) {
            for(String path:valueMap.keySet()) {
                String value = valueMap.get(path);
                System.out.println(" writePath:"+path+ " ; value "+value);
                try {
                    boolean exists = checkExists(path);
                    if(!exists) {

                        String parentPath = path.substring(0,path.lastIndexOf("/"));
                        exists = checkExists(parentPath);
                        if(!exists) {
                            curatorFramework.create().creatingParentsIfNeeded().forPath(parentPath);
                        }
                        curatorFramework.create().forPath(path,value.getBytes());
                        addCnt.incrementAndGet();
                    } else {
                        curatorFramework.setData().forPath(path,value.getBytes());
                        updateCnt.incrementAndGet();
                    }

                } catch (Exception e) {
                    writeErrorMap.put(path,"writePath:"+path+"; value: "+value);
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean checkExists(String fullPath){
        boolean result = false;
        try {
            // returns a Stat if it exists, or null if it doesn't
            Stat stat = curatorFramework.checkExists().forPath(fullPath);
            if(stat!=null) {
                result = true;
            }
        } catch (Exception e) {
            System.out.println("[checkExists] exception fullPath:"+fullPath + "; exception:"+e.getMessage());
        }
        return result;
    }

    public Map<String,String> getWriteErrorMap() {
        return writeErrorMap;
    }

    public int getErrorCnt() {
        int cnt = 0;
        if(writeErrorMap!=null && writeErrorMap.size()>0) {
            cnt = writeErrorMap.size();
        }
        return cnt;
    }

    public int getSuccessCnt() {
        int cnt = 0;
        if(valueMap!=null && valueMap.size()>0) {
            cnt = valueMap.size() - getErrorCnt();
        }
        return cnt;
    }

    public int getAddCnt() {
        return addCnt.get();
    }

    public int getUpdateCnt() {
        return updateCnt.get();
    }

    public void printResultInfo() {
        System.out.println("write node add cnt :" + getAddCnt());
        System.out.println("write node update cnt :" + getUpdateCnt());
        System.out.println("write node error cnt :" + getErrorCnt());
    }
}
