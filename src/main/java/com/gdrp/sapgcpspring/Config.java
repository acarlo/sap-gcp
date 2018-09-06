package com.gdrp.sapgcpspring;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.validation.XmlValidator;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import org.springframework.xml.xsd.XsdSchemaCollection;

@EnableWs
@Configuration
public class Config extends WsConfigurerAdapter {
	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/service/*");
	}

    @Bean(name = "sarReportWSDL")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchemaCollection XsdSchemaCollection)
    {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("SARReportPort");
        wsdl11Definition.setLocationUri("/service/sar-report");
        wsdl11Definition.setTargetNamespace("http://www.sapgcp.com/xml/sar");
        wsdl11Definition.setSchemaCollection(XsdSchemaCollection);
        return wsdl11Definition;
    }

	@Bean
	public XsdSchemaCollection sarSchemaCollection() {
		return new XsdSchemaCollection() {

			@Override
            public XsdSchema[] getXsdSchemas() {
                return new XsdSchema[]{sarSchema(), folderSchema()};
            }

			@Override
            public XmlValidator createValidator() {
                throw new UnsupportedOperationException();
            }
        };
	}
	
	@Bean
    public XsdSchema sarSchema() {
        return new SimpleXsdSchema(new ClassPathResource("sar.xsd"));
    }

    @Bean
    public XsdSchema folderSchema() {
        return new SimpleXsdSchema(new ClassPathResource("folder.xsd"));
    }     


}
