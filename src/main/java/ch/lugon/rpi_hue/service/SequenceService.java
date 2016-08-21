package ch.lugon.rpi_hue.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SequenceService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${hue.local.server.url}")
    private String hueLocalServerUrl;

    public void displayAlarm() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.put(hueLocalServerUrl + "groups/1/action", "{\"alert\":\"lselect\"}");
        } catch (Exception e) {
            log.error("Cannot connect to HUE system.\n" + e.getMessage());
            throw e;
        }
    }

}
