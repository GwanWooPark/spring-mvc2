package hello.typeconverter;

import hello.typeconverter.formatter.MyNumberFormatter;
import hello.typeconverter.converter.IpPortToStringConvert;
import hello.typeconverter.converter.StringToIpPortConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
//        주석처리 우선 순위
//        registry.addConverter(new StringToIntegerConverter());
//        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConvert());

        // 추가
        registry.addFormatter(new MyNumberFormatter());

    }
}
