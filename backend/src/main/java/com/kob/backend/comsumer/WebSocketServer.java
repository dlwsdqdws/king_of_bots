package com.kob.backend.comsumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.comsumer.utils.JwtAuthentication;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {

    private static final ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    private static final CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();
    private User user;
    private Session session = null;

    private static UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // create a link
        this.session = session;
//        System.out.println("connected");

        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);

        if(this.user != null){
            users.put(userId, this);
//            System.out.println(user);
        } else{
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // close the link
        System.out.println("disconnected");
        if(this.user != null){
            users.remove(this.user.getId());
            matchpool.remove(this.user);
        }
    }

    private void startMatching() {
        System.out.println("start_matching");
        matchpool.add(this.user);

        while (matchpool.size() >= 2){
            Iterator<User> it = matchpool.iterator();
            User a = it.next(), b = it.next();
            matchpool.remove(a);
            matchpool.remove(b);

            JSONObject respA = new JSONObject();
            respA.put("event", "start_matching");
            respA.put("opponent_username", b.getUsername());
            respA.put("opponent_photo", b.getPhoto());
            users.get(a.getId()).sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event", "start_matching");
            respB.put("opponent_username", a.getUsername());
            respB.put("opponent_photo", a.getPhoto());
            users.get(b.getId()).sendMessage(respB.toJSONString());


        }
    }

    private void stopMatching() {
        System.out.println("stop_matching");
        matchpool.remove(this.user);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // routing function : get msg from client and patch to other functions
        System.out.println("receive message");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");

        if("start_matching".equals(event)){
            startMatching();
        }else if("stop_matching".equals(event)){
            stopMatching();
        }

    }

    public void sendMessage(String msg){
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(msg);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}