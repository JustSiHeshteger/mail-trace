package ru.zvrg.mailtrace.mapper;

import org.springframework.stereotype.Service;
import ru.zvrg.mailtrace.entity.PostOffice;
import ru.zvrg.mailtrace.entity.Postage;
import ru.zvrg.mailtrace.entity.Tracking;
import ru.zvrg.mailtrace.mapper.converter.TrackingMapperImpl;

import java.time.LocalDateTime;

@Service
public class TrackingMapper extends TrackingMapperImpl {

    public Tracking createNewTracking(Postage postage, PostOffice postOffice) {
        return new Tracking()
                .setTimestamp(LocalDateTime.now())
                .setPostage(postage)
                .setPostOffice(postOffice);
    }

}
