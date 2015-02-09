package com.metafour.demo.querydsl;

import static com.metafour.demo.querydsl.qbean.QCustomer.customer;

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
		
		SQLQuery query = queryFactory.from(customer).where(customer.custid.gt(1));
		SQLBindings bindings = query.getSQL(customer.all());
		System.out.println(bindings.getSQL());
	}
    
	@Bean
	Configuration postgresConfiguration() {
		return new Configuration(new PostgresTemplates());
	}
}
