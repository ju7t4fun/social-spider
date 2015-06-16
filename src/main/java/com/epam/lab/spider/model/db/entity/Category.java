package com.epam.lab.spider.model.db.entity;

import com.epam.lab.spider.model.db.service.ServiceFactory;
import com.epam.lab.spider.model.db.service.TaskService;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sasha on 12.06.2015.
 */
public class Category {

    private static final ServiceFactory factory = ServiceFactory.getInstance();
    private static final TaskService service = factory.create(TaskService.class);

    private Integer id;
    private String name;

    private Set<Task> tasks = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Task> getTasks() {
        if (tasks == null) {
            if (id == null)
                tasks = new HashSet<>();
            else
                tasks = new HashSet<>(service.getByCategoryId(id));
        }
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public boolean addTask(Task task) {
        return getTasks().add(task);
    }

    public boolean removeTask(Task task) {
        return getTasks().remove(task);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tasks=" + tasks +
                '}';
    }

}