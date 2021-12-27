package best.Aveiro.EBEC.services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import javax.xml.transform.Result;

public class EBECWorker extends Worker {

    private static final String TAG = "MyWorker";

    public EBECWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // TODO(developer): add long running task here.
        return Result.success();
    }
}