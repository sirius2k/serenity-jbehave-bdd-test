package kr.co.redbrush.bdd.test.service;

import kr.co.redbrush.bdd.test.ws.PostWebServiceRequestBuilderFactory;
import kr.co.redbrush.bdd.test.ws.RestAssuredDriver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kwpark on 13/03/2017.
 */
@Slf4j
public class BaseService {
    @Autowired
    protected PostWebServiceRequestBuilderFactory webServiceRequestBuilderFactory;

    @Autowired
    protected RestAssuredDriver restAssuredDriver;
}
