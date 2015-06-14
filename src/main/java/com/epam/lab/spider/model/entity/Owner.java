package com.epam.lab.spider.model.entity;

/**
 * Created by Marian Voronovskyi on 12.06.2015.
 */
public class Owner {

    private Integer id;
    private Integer vk_id;
    private String name;
    private String domain;
    private Boolean deleted = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVk_id() {
        return vk_id;
    }

    public void setVk_id(Integer vk_id) {
        this.vk_id = vk_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", vk_id=" + vk_id +
                ", name='" + name + '\'' +
                ", domain='" + domain + '\'' +
                ", deleted=" + deleted +
                '}';
    }

}
