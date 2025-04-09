package com.nhlstenden.booktopia;

import com.nhlstenden.booktopia.btree.BTree;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BooktopiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooktopiaApplication.class, args);
	}
}
