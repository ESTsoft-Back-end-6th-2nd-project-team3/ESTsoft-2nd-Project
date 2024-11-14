package com.estsoft.estsoft2ndproject.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Value("${file.upload-dir}")
	private String uploadPath;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 업로드된 이미지에 대한 URL 매핑 설정
		registry.addResourceHandler("/images/uploaded/**")
			.addResourceLocations("file:" + uploadPath + "/")
			.setCachePeriod(3600)  // 1시간 캐시
			.resourceChain(true)
			.addResolver(new PathResourceResolver() {
				@Override
				protected Resource getResource(String resourcePath, Resource location) throws IOException {
					Resource resource = location.createRelative(resourcePath);
					if (resource.exists() && resource.isReadable()) {
						return resource;
					}
					return null;
				}
			});
	}
}