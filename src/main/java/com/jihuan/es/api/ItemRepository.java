package com.jihuan.es.api;

import com.jihuan.es.domain.goods.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @program: jihuan-es
 * @description: 商品接口
 * @author: jihuan
 * @create: 2019-08-26 01:18
 */
public interface ItemRepository extends ElasticsearchRepository<Item,Long> {

}
