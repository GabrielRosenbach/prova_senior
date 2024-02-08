package com.senior.prova.application.config;

import org.modelmapper.config.Configuration;

public class ModelMapper extends org.modelmapper.ModelMapper {

	public ModelMapper() {
		this.getConfiguration()
		  .setFieldMatchingEnabled(true)
		  .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
	}
}
