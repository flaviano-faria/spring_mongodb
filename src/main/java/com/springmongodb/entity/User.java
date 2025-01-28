package com.springmongodb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class User {

	@Id
	private String _id;
	private String name;
	private String document;
	private int age;
	
	public User(String _id, String name, String document, int age) {
		super();
		this._id = _id;
		this.name = name;
		this.document = document;
		this.age = age;
	}
	
	 @Override
	  public String toString() {
	    return String.format(
	        "User[id=%s, name='%s', document='%s']",
	        _id, name, document);
	  }
}
