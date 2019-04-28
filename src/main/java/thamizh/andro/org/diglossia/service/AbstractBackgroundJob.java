package thamizh.andro.org.diglossia.service;

import android.content.Context;
import android.content.Intent;
import thamizh.andro.org.diglossia.data.AppConfig;


/**
 * Created by arnald on 3/29/16.
 */
public abstract class AbstractBackgroundJob implements Runnable {
    protected Context context;
    protected Intent intent;
    protected AppConfig config;

    public AbstractBackgroundJob(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        this.config = AppConfig.getInstance(context);
    }
}
