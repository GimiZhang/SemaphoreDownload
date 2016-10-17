package com.example.administrator.semaphoredownload;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.semaphoredownload.databinding.ActivityMainBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private ThreadNameBean bean = new ThreadNameBean();
    private ExecutorService exec;
    private Semaphore semaphore;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(MainActivity.this,"下载完成",Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setBean(bean);
        mBinding.setBtnclick(new BtnOnclick());
        bean.firstName.set("线程1");
        bean.secondName.set("线程2");
        bean.thirdName.set("线程3");
        semaphoreTest();
    }

    public void semaphoreTest(){
        //创建线程池
        exec = Executors.newCachedThreadPool();
        //创建信号量对象,设置只能3个线程同时访问
        semaphore = new Semaphore(3);

    }

    public class BtnOnclick{
        public void btnOneClick(View view){
            startRunnable(mBinding.proOne);
        }
        public void btnTwoClick(View view){
            startRunnable(mBinding.proTwo);
        }
        public void btnThreeClick(View view){
            startRunnable(mBinding.proBtnThree);
        }
    }

    private void startRunnable(final ProgressBar progress) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    //获取许可
                    semaphore.acquire();
                    for (int i = 0; i < 100; i++) {
                        progress.setProgress(i);
                        Thread.sleep(50);
                    }
                    //访问完成，释放对象，如果设置3个信号量，然后有40个客户端访问，只有3个执行，因为没有释放线程，剩下的37个无法执行
                    semaphore.release();
                    if(semaphore.availablePermits() == 3){
                        handler.sendEmptyMessage(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        //退出线程池
        exec.execute(runnable);
    }
}
