package com.codesoom.assignment.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {

    ObjectMapper objectMapper = new ObjectMapper();
    public List<Task> tasks = new ArrayList<>();
    private Long count = 1L;

    public String getTasks() throws IOException {
        return tasksToJSON();
    }

    public String getTaskById(int id) throws IOException {
        Task task = findTaskById(id);
        return tasksToJSONById(task);
    }

    public String createTask(HttpExchange httpExchange) throws IOException {
        String body = getBody(httpExchange);
        Task task = toTask(body);
        task.setId(plusId());
        tasks.add(task);
        return tasksToJSONById(task);
    }

    public String updateTask(HttpExchange httpExchange) throws IOException {
        String body = getBody(httpExchange);
        String title = toTask(body).getTitle();

        String uri = String.valueOf(httpExchange.getRequestURI());
        int id = getId(uri);

        Task task = findTaskById(id);
        task.updateTitle(title);

        return tasksToJSONById(task);
    }

    public String deleteTask(HttpExchange httpExchange) throws IOException {
        String uri = String.valueOf(httpExchange.getRequestURI());
        int id = getId(uri);

        for (Task task : tasks){
            if (task.getId() == id){
                tasks.remove(task);
                return tasksToJSONById(task);
            }
        }
        return "null";
    }

    public Long plusId(){
        return count++;
    }

    public int getId(String uri) {
        return Integer.parseInt(uri.split("/")[2]);
    }

    public String getBody(HttpExchange httpExchange){
        InputStream inputStream = httpExchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }


    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    private String tasksToJSONById(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    private Task toTask(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, Task.class);
    }

    public boolean isBodyEmpty(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestBody().available() <= 0){return true;}
        return false;
    }

    public void processBody(HttpExchange httpExchange, String body) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(body.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    public Task findTaskById(int id){
        for (Task task : tasks){
            if (task.getId() == Long.valueOf(id)){
                return task;
            }
        }
        return null;
    }

}
