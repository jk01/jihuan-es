package com.jihuan.es.web;

import com.jihuan.es.domain.people.Person;
import com.jihuan.es.service.people.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private PeopleService peopleService;

    @RequestMapping("/es")
    public String index() {
        return "Hello World!";
    }

    @RequestMapping("/createIndex")
    public String indexCreate() {

        List<Person> personList = new ArrayList<>();
        Person p1 = new Person(1,"xiaosong","13612341234");
        personList.add(p1);
        peopleService.batchInsert(personList);

        return "success";
    }

    
}
