package com.upsaclay.collaborativeremotedrawclient;

public class AppConfig {

    private String serverIp;
    private int serverPort;

    private AppConfig() {
    }

    private static AppConfig INSTANCE = null;

    public static synchronized AppConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppConfig();
        }
        return INSTANCE;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getServerPort() {
        return serverPort;
    }
}
