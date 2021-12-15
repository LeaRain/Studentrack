package sw.laux.Studentrack.presentation.util;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class LoggerFactory {

    @Bean
    // Declare as primary for potential other loggers in case Logback/slf4j/whatever system one may use is a problem.
    @Primary
    public Logger createLogger() {
        return org.slf4j.LoggerFactory.getLogger("Studentrack");
    }

}
