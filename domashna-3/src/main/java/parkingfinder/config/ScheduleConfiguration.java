package parkingfinder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import parkingfinder.service.ParkingSpotDataLoader;

@Configuration
@EnableScheduling
public class ScheduleConfiguration {
    @Bean
    public ParkingSpotDataLoader parkingSpotDataLoader() {
        return new ParkingSpotDataLoader();
    }
}
