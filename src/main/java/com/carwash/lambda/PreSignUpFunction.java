package com.carwash.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CognitoUserPoolPreSignUpEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PreSignUpFunction implements RequestHandler<CognitoUserPoolPreSignUpEvent, CognitoUserPoolPreSignUpEvent> {


    @Override
    public CognitoUserPoolPreSignUpEvent handleRequest(CognitoUserPoolPreSignUpEvent s, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Hello Worlds from lambda PreSignUpEvent");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonResponse = gson.toJson(s.getRequest().getUserAttributes());
        Request email = gson.fromJson(jsonResponse,Request.class);
        JDBCConnect jdbcConnect = new JDBCConnect(context);
        UserDTO dto = new UserDTO();
        dto.setEmail(email.getEmail());
        long id = jdbcConnect.saveUser(dto);
        dto.setId(id);
        jdbcConnect.inserRole(dto);
        return s;
    }

}
