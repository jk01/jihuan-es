package com.jihuan.es.service.people.impl;

import com.alibaba.fastjson.JSON;
import com.jihuan.es.domain.people.Person;
import com.jihuan.es.service.people.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service("peopleService")
public class PeopleServiceImpl implements PeopleService {

    @Autowired
    ElasticsearchTemplate esTemplate;

    @Override
    public void batchInsert(List<Person> personList) {
        if(CollectionUtils.isEmpty(personList)){
          return ;
        }

        List<IndexQuery> queries = new ArrayList<IndexQuery>();
        for(Person ps:personList){
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(ps.getId()+"");
            indexQuery.setSource(JSON.toJSONString(ps));
            indexQuery.setIndexName("person");
            indexQuery.setType("_doc");
        }

        esTemplate.bulkIndex(queries);
    }
}
