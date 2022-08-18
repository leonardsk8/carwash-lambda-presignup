package com.carwash.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CognitoUserPoolPostConfirmationEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PostSignUpFunction implements RequestHandler<CognitoUserPoolPostConfirmationEvent, CognitoUserPoolPostConfirmationEvent> {


    @Override
    public CognitoUserPoolPostConfirmationEvent handleRequest(CognitoUserPoolPostConfirmationEvent s, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Hello Worlds from lambda PostConfirmationEvent");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResponse = gson.toJson(s.getRequest().getUserAttributes());
        Request email = gson.fromJson(jsonResponse, Request.class);
        JDBCConnect jdbcConnect = new JDBCConnect(context);
        UserDTO dto = new UserDTO();
        dto.setEmail(email.getEmail());
        jdbcConnect.updateUser(dto);
        return s;
    }
}
