package com.atguigu.gmall.search;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallSearchServiceApplicationTests {

	@Autowired
	JestClient jestClient;

	@Test
	public void contextLoads() throws IOException {

		//https://www.elastic.co/guide/en/elasticsearch/reference/current/_exploring_your_data.html
		//可以导入这个测试库里面有很多数据
		/**
		 * 1、先去上面的网址下载来测试数据，并保存为accounts.json
		 * 2、进入accounts.json所在目录运行如下指令即可
		 * curl -H "Content-Type: application/json" -XPOST "你的ip:9200/bank/doc/_bulk?pretty&refresh" --data-binary "@accounts.json"
		 */

		String query="{\n" +
				"  \"query\": {\n" +
				"    \"match\": {\n" +
				"      \"actorList.name\": \"张涵予\"\n" +
				"    }\n" +
				"  }\n" +
				"}";

		//GET movie_chn/movie/_search
		Search search = new Search.Builder(query).addIndex("movie_chn")
				.addType("movie").build();

		SearchResult result = jestClient.execute(search);


		List<SearchResult.Hit<HashMap, Void>> hits = result.getHits(HashMap.class);
		for (SearchResult.Hit<HashMap, Void> hit : hits) {
			System.out.println(hit.score);
			//内容
			System.out.println(hit.source);
		}


		System.out.println(result);



	}

}
