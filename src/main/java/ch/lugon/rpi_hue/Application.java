package ch.lugon.rpi_hue;

import io.swagger.annotations.SwaggerDefinition;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableAutoConfiguration
@EnableSwagger2
@ComponentScan("ch.lugon.rpi_hue")
@SwaggerDefinition(
        consumes = {"application/json"},
        produces = {"application/json"},
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
        basePath = "/rpi_hue"
)
public class Application {

    @Value("${server.port}")
    private Integer port;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/rpi_hue/**").allowedOrigins("*");
            }
        };
    }

    @Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/rpi_hue.*"))
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return new UiConfiguration(
                "", // validatorUrl url
                "list",       // docExpansion          => none | list
                "alpha",      // apiSorter             => alpha
                "schema",     // defaultModelRendering => schema
                false,        // enableJsonEditor      => true | false
                true);        // showRequestHeaders    => true | false
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("RPI HUE WS API")
                .description(
                        "API documentation of the RPI HUE RESTFul Web service.<p>" +
                        "This Web service is implemented using SpringBoot and Swagger for the auto-generated documentation.")
                .contact(new Contact("Pascal Lugon", "http://www.lugon.ch/", "pascal@lugon.ch"))
                .version("1.0.0")
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // you can run this with SSL/TLS. For example, build the application (`mvn clean install`) in the `oauth` directory, then run:
    //   java -Dspring.profiles.active=production -Dkeystore.file=file:///`pwd`/src/main/resources/keystore.p12 -jar target/oauth-1.0.0.BUILD-SNAPSHOT.jar
    @Bean
    @Profile("production")
    EmbeddedServletContainerCustomizer containerCustomizer() throws Exception {
        return new EmbeddedServletContainerCustomizer() {

            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                if (container instanceof TomcatEmbeddedServletContainerFactory) {
                    TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
                    tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer() {

                        @Override
                        public void customize(Connector connector) {
                            connector.setPort(port);
                        }
                    });

                }
            }
        };
    }

}