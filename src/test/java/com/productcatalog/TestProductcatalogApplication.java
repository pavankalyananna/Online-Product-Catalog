package com.productcatalog;

import org.springframework.boot.SpringApplication;

public class TestProductcatalogApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProductcatalogApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
