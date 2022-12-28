package com.kob.backend.comsumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.comsumer.WebSocketServer;
import com.kob.backend.pojo.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_cnt;
    private final int[][] g;

    private final static int[] dx = {-1,0,1,0}, dy = {0,1,0,-1};

    private final Player playerA;
    private final Player playerB;

    // 0,1,2,3 = directions
    private Integer nextStepA = null;
    private Integer nextStepB = null;

    private final ReentrantLock lock = new ReentrantLock();

    // playing -> finished
    private String status = "playing";
    // all, A, B
    private String loser = "";

    public Game(Integer rows, Integer cols, Integer inner_walls_cnt, Integer idA, Integer idB){
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_cnt = inner_walls_cnt;
        this.g = new int[rows][cols];
        playerA = new Player(idA, rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, 1, cols - 2, new ArrayList<>());

    }

    public int[][] getG(){
        return g;
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public void setNextStepA(Integer nextStepA){
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB){
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }


    private boolean check_connectivity(int sx, int sy, int tx, int ty){
        if (sx == tx && sy == ty){
            return true;
        }

        g[sx][sy] = 1;

        for(int i = 0; i < 4; i++){
            int x = sx+dx[i], y = sy+dy[i];
            if(x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0){
                if(check_connectivity(x, y, tx, ty)){
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }


        g[sx][sy] = 0;
        return false;
    }

    private boolean draw(){
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                // 0 = ground, 1 = wall
                g[i][j] = 0;
            }
        }

        for(int r = 0; r < this.rows; r++){
            g[r][0] = g[r][this.cols-1] = 1;
        }

        for(int c = 0; c < this.cols; c++){
            g[0][c] = g[this.rows-1][c] = 1;
        }

        Random random = new Random();
        for(int i = 0; i < this.inner_walls_cnt / 2; i++){
            for(int j = 0; j < 200; j++){
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if(g[r][c] == 1 || g[this.rows-1-r][this.cols-1-c] == 1){
                    continue;
                }

                if(r == this.rows-2 && c == 1 || r == 1 && c == this.cols-2){
                    continue;
                }

                g[r][c] = g[this.rows-1-r][this.cols-1-c] = 1;
                break;
            }
        }

        return check_connectivity(this.rows-2, 1, 1, this.cols-2);
    }

    public void createMap(){
        for(int i = 0; i < 150; i++){
            if(draw()){
                break;
            }
        }
    }

    private boolean nextStep(){
        try {
            // front end need every 200ms to move a step
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Waiting for the next operation instruction
        for(int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);

                lock.lock();
                try {
                    if(nextStepA != null && nextStepB != null){
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                }finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
                e.printStackTrace();
            }
        }

        return false;
    }

    private void sendAllMessage(String msg){
        if(WebSocketServer.users.get(playerA.getId()) != null){
            WebSocketServer.users.get(playerA.getId()).sendMessage(msg);
        }
        if(WebSocketServer.users.get(playerB.getId()) != null){
            WebSocketServer.users.get(playerB.getId()).sendMessage(msg);
        }
    }

    private String getMapString(){
        StringBuilder res = new StringBuilder();

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                res.append(g[i][j]);
            }
        }

        return res.toString();
    }

    private void saveToDatabase(){
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );

//        System.out.println(record);
        WebSocketServer.recordMapper.insert(record);
    }

    private void sendResult(){
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);

        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }

    private void sendMove(){
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessage(resp.toJSONString());

            nextStepA = nextStepB = null;  // wait for another next step
        }finally {
            lock.unlock();
        }
    }

    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB){
        int n = cellsA.size();
        Cell cell = cellsA.get(n-1);
        if(g[cell.x][cell.y] == 1){
            return false;
        }

        for(int i = 0; i < n-1; i++){
            if(cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y){
                return false;
            }
        }

        for(int i = 0; i < n-1; i++){
            if(cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y){
                return false;
            }
        }

        return true;
    }

    private void judge(){
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);

        if(!validA || !validB){
            status = "finished";

            if(!validA && !validB){
                loser = "all";
            }else if(!validA){
                loser = "A";
            }else{
                loser = "B";
            }
        }
    }

    @Override
    public void run() {
        for(int i = 0; i < 1000; i++){
            if(nextStep()){
                judge();

                if("playing".equals(status)){
                    sendMove();
                } else{
                    sendResult();
                    break;
                }
            }else{
                status = "finished";

                lock.lock();
                try{
                    if(nextStepA == null && nextStepB == null){
                        loser = "all";
                    } else if(nextStepA == null){
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                }finally {
                    lock.unlock();
                }

                sendResult();
                break;
            }
        }
    }
}
