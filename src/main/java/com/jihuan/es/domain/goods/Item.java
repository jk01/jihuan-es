package com.jihuan.es.domain.goods;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @program: jihuan-es
 * @description: 商品实体类
 * @author: jihuan
 * @create: 2019-08-26 01:08
 */
@Document(indexName = "item",type = "docs", shards = 1, replicas = 0)
public class Item {
    @Id
    private Long id;

    @Field(type = FieldType.text, analyzer = "ik_max_word")
    private String title; //标题

    @Field(type = FieldType.keyword)
    private String category;// 分类

    @Field(type = FieldType.keyword)
    private String brand; // 品牌

    @Field(type = FieldType.Double)
    private Double price; // 价格

    @Field(index = false, type = FieldType.keyword)
    private String images; // 图片地址

    public Item(Long id,String title,String category,String brand,Double price,String images){
        this.id=id;
        this.title=title;
        this.category=category;
        this.brand=brand;
        this.price=price;
        this.images=images;

    }

}
