import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;

/**
 * Created by zhou on 16-10-31.
 */
public class Collector {
    zkClient zk = null;
    Collection collect = null;
    String name="";
    public Collector() throws Exception{
        zk = new zkClient("127.0.0.1:2181",30000);
        collect = new Collection();
        name = collect.getLocalName();
        System.out.println(123+name);
        //TODO:注册
        init();
    }
    private void init()  throws Exception{
        this.notExistAndCreate("/monitor","");
        this.notExistAndCreate("/monitor/"+name,collect.getLocalAdd());
        String mem[] = collect.getMemInfo();
        this.notExistAndCreate("/monitor/"+name+"/mem",mem[0]);
        this.notExistAndCreate("/monitor/"+name+"/mem/mem",mem[0]);
        this.notExistAndCreate("/monitor/"+name+"/mem/swap",mem[1]);
        CpuPerc cpulist[] = collect.getCpuPerc();
        this.notExistAndCreate("/monitor/"+name+"/cpus","");
        double sum = 0.0;
        for(int i=0;i<cpulist.length;i++){
            this.notExistAndCreate("/monitor/"+name+"/cpus/cpu"+i,cpulist[i].toString());
            sum += cpulist[i].getUser();
        }
//        String IORate = collect.getIORate();
//        this.notExistAndCreate("/monitor/"+name+"/IORate","");
        zk.setData("/monitor/"+name+"/cpus",String.valueOf(sum/cpulist.length));
    }
    private void notExistAndCreate(String path,String value) throws Exception{
        if(zk.exists(path) == null){
            zk.create(path,value);
        }
    }

    public void collect() throws Exception{
        String mem[] = collect.getMemInfo();
        zk.setData("/monitor/"+name+"/mem",mem[0]);
        zk.setData("/monitor/"+name+"/mem/mem",mem[0]);
        zk.setData("/monitor/"+name+"/mem/swap",mem[1]);
        CpuPerc cpulist[] = collect.getCpuPerc();
        CpuInfo cpuinfo[] = collect.getCpuInfo();
        double sum = 0.0;
        for(int i=0;i<cpulist.length;i++){
            zk.setData("/monitor/"+name+"/cpus/cpu"+i,cpulist[i].toString()+",CPU info: "
                +(cpuinfo[i].getMhz())+"MHz");
            sum += cpulist[i].getUser();
        }
        zk.setData("/monitor/"+name+"/cpus",String.valueOf(sum/cpulist.length));
//        String IORate = collect.getIORate();
//        if(IORate!=null) {
//            zk.setData("/monitor/"+name+"/IORate",IORate);
//        }else{
//            zk.setData("/monitor/"+name+"/IORate","0");
//        }
    }

}
