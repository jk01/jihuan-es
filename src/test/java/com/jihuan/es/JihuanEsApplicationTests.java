package com.jihuan.es;

import com.alibaba.fastjson.JSON;
import com.jihuan.es.api.ItemRepository;
import com.jihuan.es.domain.goods.Item;
import com.jihuan.es.domain.people.Person;
import com.jihuan.es.service.people.PeopleService;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.LocalTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetAddress;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JihuanEsApplicationTests {

	@Autowired
	ElasticsearchTemplate esTemplate;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private PeopleService peopleService;



	@Test
	public void createIndexOfPeople(){
		List<Person> personList = new ArrayList<>();
		Person p1 = new Person(1,"xiaosong","13612341234");
		personList.add(p1);
		peopleService.batchInsert(personList);
	}

	@Test
	public void creatIndexOfPeople2() throws  Exception{

//		TransportAddress transportAddress = new LocalTransportAddress(InetAddress.getByName("127.0.0.1"), 9300)
		TransportAddress transportAddress = new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300);

		Settings settings = Settings.builder()
				.put("cluster.name", "jihuan").build();

		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(transportAddress);

		BulkRequestBuilder bulkRequest = client.prepareBulk();

		bulkRequest.add(client.prepareIndex("people", "_doc", "2")
				.setSource(jsonBuilder()
						.startObject()
						.field("name", "xiaosong")
						.field("country", "china")
						.field("age", 20)
						.field("date", "2019-09-01")
						.endObject()
				)
		);

		BulkResponse bulkResponse = bulkRequest.get();

		client.close();

		Map<String, String> data = new HashMap<>();
		data.put("name", "localhost");
		data.put("country", "2019-04-07 23:05:04");
		data.put("","20");
		data.put("date","1992-09-01");

		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setId("22");
		indexQuery.setSource(JSON.toJSONString(data));
		indexQuery.setIndexName("people");
		indexQuery.setType("_doc");

		esTemplate.index(indexQuery);

	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void createIndex() {
		// 创建索引，会根据Item类的@Document注解信息来创建
		esTemplate.createIndex(Item.class);
        // 配置映射，会根据Item类中的id、Field等字段来自动完成映射
		esTemplate.putMapping(Item.class);
	}

	@Test
	public void deleteIndex(){
		esTemplate.deleteIndex(Item.class);
		// 根据索引名字删除
		//esTemplate.deleteIndex("item1");
	}

	@Test
	public void index() {
		Item item = new Item(1L,"小宋牌手里", " 手机", "天地玄黄", 3333.00, "http://www.jd.com/item/123123.shtml");
		itemRepository.save(item);
	}

	@Test
	public void testFindAll(){
		//1 查找所有
//        Iterable<Item> item = itemRepository.findAll();
//        Iterator<Item> it = item.iterator();
//        while (it.hasNext()){
//            System.out.println(it.next());
//        }
		//2 分页查找
//        Page<Item> page = itemRepository.findAll(PageRequest.of(1, 5));
//
//        for(Item item:page){
//            System.out.println(item);
//        }

		//3 排序
		Iterable<Item> iterable = itemRepository.findAll(Sort.by("price").descending());
		Iterator<Item> it = iterable.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}

	}

	@Test
	public void search(){
		// 构建查询条件
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		// 添加基本分词查询
		queryBuilder.withQuery(QueryBuilders.matchQuery("title", "小米手机"));
		// 搜索，获取结果
		Page<Item> items = this.itemRepository.search(queryBuilder.build());
		// 总条数
		long total = items.getTotalElements();
		System.out.println("total = " + total);
		for (Item item : items) {
			System.out.println(item);
		}
	}

	@Test
	public void testTermQuery(){

		// 查询条件生成器
		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
		builder.withQuery(QueryBuilders.termQuery("title","小米"));

		// 查询 自动分页 ,默认查找第一页的10条数据
		Page<Item> list = this.itemRepository.search(builder.build());

		for(Item item:list){
			System.out.println(item);
		}
	}

	@Test
	public void testFuzzyQuery(){
		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
		builder.withQuery(QueryBuilders.fuzzyQuery("title","faceoooo"));

		Page<Item> list = this.itemRepository.search(builder.build());
		for(Item item:list){
			System.out.println(item);
		}


	}

	@Test
	public void testBooleanQuery(){
		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
		builder.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("title","手机"))
				.must(QueryBuilders.termQuery("brand","小米"))
		);

		Page<Item> list = this.itemRepository.search(builder.build());
		for(Item item:list){
			System.out.println(item);
		}

	}

	@Test
	public void testRangeQuery(){
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		//        queryBuilder.withQuery(QueryBuilders.fuzzyQuery("title","小目"));


		queryBuilder.withQuery(QueryBuilders.rangeQuery("price").from(3000).to(4000));

		Page<Item> page = this.itemRepository.search(queryBuilder.build());

		for(Item i:page){
			System.out.println(i);
		}


	}

	@Test
	public void searchAndSort(){
		// 构建查询条件
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		// 添加基本分词查询
		queryBuilder.withQuery(QueryBuilders.termQuery("category", "手机"));

		// 排序
		queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC));

		// 搜索，获取结果
		Page<Item> items = this.itemRepository.search(queryBuilder.build());
		// 总条数
		long total = items.getTotalElements();
		System.out.println("总条数 = " + total);

		for (Item item : items) {
			System.out.println(item);
		}
	}


}
