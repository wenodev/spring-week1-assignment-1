package com.codesoom.assignment;

import com.codesoom.assignment.task.Task;
import com.codesoom.assignment.task.TaskHttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * 1. Create HttpServer
 * 2. Create Handler
 * 3. Response 200
 * 4. Get Path
 * 5. Code with kind of method : GET, POST, PUT, DELETE
 */
public class App {

    private final static int PORT_NUMBER = 8000;

    public static void main(String[] args) {
        try {
            InetSocketAddress address = new InetSocketAddress(PORT_NUMBER);
            HttpServer server = HttpServer.create(address, 0);
            server.createContext("/", new TaskHttpHandler());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
