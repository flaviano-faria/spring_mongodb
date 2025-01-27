package com.springmongodb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "db_user")
public class User {

	@Id
	private String id;
	private String name;
	private String document;
	private int age;
	
	public User(String id, String name, String document, int age) {
		super();
		this.id = id;
		this.name = name;
		this.document = document;
		this.age = age;
	}
}
