package com.cowin.vaccination.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cowin.vaccination.function.core.CowinCoreLogic;
import com.cowin.vaccination.function.model.InputData;

public class CowinVaccinationHandler implements RequestHandler<InputData, String> {

    @Override
    public String handleRequest(InputData input, Context context) {
        LambdaLogger logger = context.getLogger();
        CowinCoreLogic cowinCoreLogic = new CowinCoreLogic();
        String response = "";
        try {
            response = cowinCoreLogic.processData(input.getAge(), input.getPincode());
        } catch (Exception e) {
            logger.log("Something went wrong here" + e);
            throw new RuntimeException("Exception Occured");
        }
        if (response.equalsIgnoreCase("{}")) {
            throw new RuntimeException("No Data Found");
        }
        return response;
    }
}