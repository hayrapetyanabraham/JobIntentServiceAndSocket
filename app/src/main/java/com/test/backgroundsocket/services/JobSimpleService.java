package com.test.backgroundsocket.services;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.test.backgroundsocket.enums.SocketStatus;

import java.net.URISyntaxException;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class JobSimpleService extends JobIntentService {
    private static final String TAG = JobSimpleService.class.getSimpleName();
    public static final int JOB_ID = 100;

    private Socket mSocketClient;
    private SocketStatus mSocketStatus;


    public JobSimpleService() {
        super();
    }

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, JobSimpleService.class, JOB_ID, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            initSocketConnection();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "Is work!" + " 0");

    }


    private void initSocketConnection() throws URISyntaxException {
        if ((mSocketStatus != SocketStatus.CONNECTED || mSocketStatus != SocketStatus.CONNECTING) && mSocketClient != null) {
            mSocketClient.disconnect();
            mSocketClient.off();
            mSocketClient = null;
        }
        mSocketClient = IO.socket("https://socket-io-chat.now.sh/");
        mSocketClient.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mSocketStatus = SocketStatus.CONNECTED;
                Log.e(TAG, " Status EVENT_CONNECT  " + "CONNECTED");
            }
        }).on(Socket.EVENT_CONNECTING, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mSocketStatus = SocketStatus.CONNECTING;
                Log.e(TAG, " Status  EVENT_CONNECTING  " + "CONNECTING");
            }
        }).on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mSocketStatus = SocketStatus.CONNECTED;
                Log.e(TAG, " Status EVENT_RECONNECT  " + "CONNECTED");
            }
        }).on(Socket.EVENT_RECONNECTING, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mSocketStatus = SocketStatus.CONNECTING;
                Log.e(TAG, " Status EVENT_RECONNECTING  " + "CONNECTING");
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mSocketStatus = SocketStatus.DISCONNECTED;
                Log.e(TAG, " Status  EVENT_DISCONNECT  " + "DISCONNECTED");
            }
        }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mSocketStatus = SocketStatus.DISCONNECTED;
                Log.e(TAG, " Status  EVENT_CONNECT_ERROR   " + "DISCONNECTED");
            }
        }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mSocketStatus = SocketStatus.DISCONNECTED;
                Log.e(TAG, " Status  EVENT_CONNECT_TIMEOUT   " + "DISCONNECTED");
            }
        }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mSocketStatus = SocketStatus.DISCONNECTED;
                Log.e(TAG, " Status  EVENT_ERROR   " + "DISCONNECTED");
            }
        }).on(Socket.EVENT_RECONNECT_ATTEMPT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mSocketStatus = SocketStatus.DISCONNECTED;
                Log.e(TAG, " Status  EVENT_RECONNECT_ATTEMPT   " + "DISCONNECTED");
            }
        }).on(Socket.EVENT_RECONNECT_FAILED, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mSocketStatus = SocketStatus.DISCONNECTED;
                Log.e(TAG, " EVENT_RECONNECT_FAILED Status   " + "DISCONNECTED");
            }
        }).on(Socket.EVENT_RECONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mSocketStatus = SocketStatus.DISCONNECTED;
                Log.e(TAG, " Status EVENT_RECONNECT_ERROR   " + "DISCONNECTED");
            }
        }).emit("error", new Ack() {
            @Override
            public void call(Object... args) {
                if (args[0] == null)
                    Log.e("error", "null");
                else
                    Log.d("error", args[0].toString());
            }
        }).on("error", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args[0] == null && args[0].toString().length() < 5)
                    Log.e(TAG, "error is null");
                else {
                    Log.e(TAG, "error  " + args[0].toString());

                }
            }
        }).on("messages", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args[0] == null)
                    Log.e(TAG, "ON messages is null");
                else {
                    Log.e(TAG, "messages" + args[0].toString());
                }
            }
        });
        mSocketClient.connect();
    }
}

