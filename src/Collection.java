/**
 * Created by zhou on 16-10-31.
 */
import org.hyperic.sigar.*;

import java.net.InetAddress;

public class Collection {
    Sigar sigar = null;
    long readerBytes = 0;
    long timeStamp = 0;
    public Collection(){
        sigar = new Sigar();
    }
    public String getLocalName() throws Exception{
        return InetAddress.getLocalHost().getHostName();
    }
    public String getLocalAdd() throws Exception{
        return InetAddress.getLocalHost().getHostAddress();
    }
    public CpuPerc[] getCpuPerc() throws Exception{
        return sigar.getCpuPercList();
    }
    public CpuInfo[] getCpuInfo() throws Exception{
        return sigar.getCpuInfoList();
    }
    public String[] getMemInfo() throws Exception{
        String result[] = new String[2];
        Mem mem = sigar.getMem();
        // 内存总量
        String _result = "";
        _result = _result.concat("Total = " + mem.getTotal() / 1024L / 1024 + "M av\n");
        // 当前内存使用量
        _result = _result.concat("Used = " + mem.getUsed() / 1024L / 1024 + "M used\n");
        // 当前内存剩余量
        _result = _result.concat("Free = " + mem.getFree() / 1024L / 1024 + "M free\n");
        result[0] = _result;
        // 系统页面文件交换区信息
        Swap swap = sigar.getSwap();
        // 交换区总量
        _result = "";
        _result = _result.concat("Total = " + swap.getTotal() / 1024L / 1024 + "M av\n");
        // 当前交换区使用量
        _result = _result.concat("Used = " + swap.getUsed() / 1024L / 1024 + "M used\n");
        // 当前交换区剩余量
        _result = _result.concat("Free = " + swap.getFree() / 1024L / 1024 + "M free\n");
        result[1] = _result;
        return result;
    }
//    public String getIORate() throws Exception{
//        String IORate= null;
//        long readBytes = sigar.getFileSystemUsage("/").getDiskReadBytes();
//        long timeStamp = System.currentTimeMillis();
//        if(this.readerBytes != 0 && this.timeStamp != 0){
//            IORate = ((readBytes-this.readerBytes)*1000/(timeStamp-this.timeStamp))/1024L/1024L+"M/s";
//        }
//        this.readerBytes = readBytes;
//        this.timeStamp = timeStamp;
//        return IORate;
//    }

    public static void main(String[] args){
        Sigar sigar = null;
        sigar = new Sigar();
        try {
            String[] netIfs = sigar.getNetInterfaceList();
            for(String name : netIfs){
                System.out.println(name);
                System.out.println(sigar.getNetInterfaceStat(name).getSpeed());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
