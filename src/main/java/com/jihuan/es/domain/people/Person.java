package com.jihuan.es.domain.people;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;


@Document(indexName = "person",type="_doc",indexStoreType = "fx",shards = 5,replicas = 1,refreshInterval = "-1")
public class Person implements Serializable {
    @Id
    private int id;

    private String name;

    private String phone;

    public int getId() {
        return id;
    }

    public Person(int id,String name,String phone){
        this.id=id;
        this.name=name;
        this.phone=phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
