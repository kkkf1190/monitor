/**
 * Created by zhou on 16-10-31.
 */
import java.util.Observable;
public class Monitor extends Observable implements Runnable{
    // 此方法一经调用，立马可以通知观察者，在本例中是监听线程
    public void doBusiness(){
        if(true){
            super.setChanged();
        }
        notifyObservers();
    }
    @Override
    public void run() {
        Collector collector = null;
        try {
            collector = new Collector();
        }catch (Exception e){
            e.printStackTrace();
            doBusiness();
        }
        while(true){    //模拟线程运行一段时间之后退出
            try{
                if(collector!=null){
                    collector.collect();
                }
                Thread.sleep(2000);
            }catch (Exception e) {
                e.printStackTrace();
                doBusiness();
                break;
            }
        }
    }
    public static void main(String[] args) {
        Monitor run = new Monitor();
        Listener listen = new Listener();
        run.addObserver(listen);
        new Thread(run).start();
        //run.doBusiness();
    }
}
