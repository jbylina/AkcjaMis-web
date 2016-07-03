package org.akcjamis.webapp.config.search;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IndexReinitializer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void resetIndex() {
    }
}
