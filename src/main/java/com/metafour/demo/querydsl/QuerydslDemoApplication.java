package com.metafour.demo.querydsl;

import static com.metafour.demo.querydsl.types.QCustomer.customer;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.PostgresTemplates;
import com.mysema.query.sql.SQLBindings;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLQueryFactory;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.PathBuilder;

@SpringBootApplication
public class QuerydslDemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(QuerydslDemoApplication.class, args);
    }

    @Autowired
    DataSource dataSource;
    
    SQLQueryFactory queryFactory;
    
	@Override
	public void run(String... args) throws Exception {
		SQLQueryFactory queryFactory = new SQLQueryFactory(postgresConfiguration(), dataSource);
		
		// Using Querydsl query type 
		SQLQuery query = queryFactory.from(customer).where(customer.custid.gt(1));
		SQLBindings bindings = query.getSQL(customer.all());
		System.out.println("query: " + bindings.getSQL());
		
		// Without using Querydsl query type
		PathBuilder<Object> customerPath = new PathBuilder<Object>(Object.class, "customer");
	    NumberPath<Integer> custidPath = customerPath.getNumber("custid", Integer.class);
	    SQLQuery query1 = queryFactory.from(customerPath).where(custidPath.eq(1));	    
	    SQLBindings bindings1 = query1.getSQL(customerPath.get("*"));
		System.out.println("query1: " + bindings1.getSQL());
	}
    
	@Bean
	Configuration postgresConfiguration() {
		return new Configuration(new PostgresTemplates());
	}
}
