package com.home19b;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableSwagger2
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public Docket swaggerPersonApi10() {
//        String svcDesc = "Home 19b Api v1.0";
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select().apis(RequestHandlerSelectors.basePackage("com.home19b.controller"))
//                .paths(PathSelectors.any())
//                .build().apiInfo(new ApiInfoBuilder().version("1.0").title(svcDesc)
//                        .description("Documentation of " + svcDesc).build());
//    }
}
