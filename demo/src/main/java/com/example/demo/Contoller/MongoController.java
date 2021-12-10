package com.example.demo.Contoller;

import java.util.ArrayList;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@RestController
@RequestMapping("/mongo")
public class MongoController {
	

	@GetMapping("/test")
	public String gettest() {
		
		return "hii";
		
	}
	@PostMapping(value = "/operation")
	public Object insert(@RequestParam String collectionName ,@RequestParam String OperationType,@RequestBody Map<String, Object> data) {
		
		System.out.println("collection Name = > "+collectionName);
		System.out.println("collection Name = > "+data.toString());
		try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

				
				MongoDatabase database = mongoClient.getDatabase("elastic_data");

	            MongoCollection<Document> collection = database.getCollection(collectionName);
	            
	            if(OperationType.equals("insert")) {
	            	try {
	            		System.out.println("insert => "+new Document(data));
	            		return collection.insertOne(new Document(data));
	            	} catch (Exception e) {
	            		e.printStackTrace();
	            	}
	            }else if (OperationType.equals("find")){
	            	DBObject dbo = new BasicDBObject(data);
	            	System.out.println("find => "+dbo.toString());
	            	return collection.find((Bson) dbo)
	            			.map(Document::toJson)
	                        .into(new ArrayList<>());
	            }else if (OperationType.equals("remove")) {
	            	DBObject dbo = new BasicDBObject(data);
	            	return collection.deleteMany((Bson) dbo);
	            }else if (OperationType.equals("update")) {
	            	DBObject dbo = new BasicDBObject(data);
	            	return collection.updateMany((Bson) dbo, (Bson) dbo);
	            }else {
	            	return "Select correct Operation";
	            }
	            
	        }
		 
		return null;
	}

}
