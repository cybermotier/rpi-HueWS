package ch.lugon.rpi_hue.web;

import ch.lugon.rpi_hue.service.SequenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rpi_hue/alarm")
@Api(value = "alarm", description = "Alarm API")
public class AlarmController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SequenceService sequenceService;

    @ApiOperation(value = "Create global alarm", notes = "Creates a new global alarm: all the lights are blinking")
    @RequestMapping(value="global", method= RequestMethod.PUT)
    public void globalAlarm() throws Exception {
        log.debug("globalAlarm");
        sequenceService.displayAlarm();
    }

}
