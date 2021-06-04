package com.mgtech.data.net.socket;

import android.util.Log;

import com.mgtech.domain.entity.SocketListener;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * web socket connection
 */

public class SocketConnection {
    private static final String TAG = "socket";
    private String url;
    private WebSocket webSocket;
    private SocketListener socketListener;
    private WebSocketListener webSocketListener;
    private static volatile SocketConnection socketConnection;
    private Request request;
    private OkHttpClient client;
    private volatile boolean socketConnectOn = false;
    private boolean connecting = true;
    private static final String CONNECT_AGAIN = "connectAgain";

    private SocketConnection(String url, SocketListener listener) {
        this.url = url;
        this.socketListener = listener;
        initListener();
        request = new Request.Builder().url(url).build();
        client = createClient();
    }

    public static SocketConnection getInstance(String url, SocketListener listener){
        if (socketConnection == null){
            synchronized (SocketConnection.class){
                if (socketConnection == null){
                    socketConnection = new SocketConnection(url,listener);
                }
            }
        }
        return socketConnection;
    }


    private OkHttpClient createClient() {
        return new OkHttpClient().newBuilder().build();
    }

    public synchronized void connect(){
        if (webSocket != null){
//            close();
//            webSocket.close(1000,CONNECT_AGAIN);
            return;
        }
        connecting = true;
        webSocket = client.newWebSocket(request, webSocketListener);
//        Request request = new Request.Builder().url(url).build();
//        OkHttpClient client = createClient();
//        webSocket = client.newWebSocket(request, webSocketListener);
//        // 关闭
//        Log.e("socket", "newWebSocket");
//        client.dispatcher().executorService().shutdown();
    }

    private void initListener(){
        webSocketListener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                connecting = false;
                socketConnectOn = true;
                if (socketListener != null){
                    socketListener.onOpen();
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                connecting = false;
                socketConnectOn = true;
                if (socketListener != null){
                    socketListener.onMessage(text);
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                connecting = false;
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                Log.i("socket", "onClosing: reason "+ reason + "  code "+code);
                connecting = false;
                SocketConnection.this.webSocket = null;
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.i("socket", "onClosing: "+ reason + " "+code);
                connecting = false;
                socketConnectOn = false;
                SocketConnection.this.webSocket = null;
                if (socketListener != null ){
                    if (CONNECT_AGAIN.equalsIgnoreCase(reason)) {
                        SocketConnection.this.webSocket = client.newWebSocket(request, webSocketListener);
                    }else{
                        socketListener.onClose();
                    }
                }
                super.onClosed(webSocket, code, reason);
                Log.i("socket", "onClosed: "+ reason + " "+code);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                socketConnectOn = false;
                if (socketListener != null){
                    socketListener.onFail();
                }
                t.printStackTrace();
            }
        };
    }

    public boolean isSocketConnectOn() {
        return socketConnectOn;
    }


    public synchronized void send(String text){
        if (webSocket != null){
            Log.i(TAG, "send: " + text);
            webSocket.send(text);
        }
    }

    public synchronized void close(){
        Log.e("socket", "close: tttttt" );
        if (webSocket != null){
            webSocket.close(1000,"断开");
            webSocket = null;
        }
    }

}
