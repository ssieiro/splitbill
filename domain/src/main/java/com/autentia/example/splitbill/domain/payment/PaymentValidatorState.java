package com.autentia.example.splitbill.domain.payment;

public enum PaymentValidatorState{
    OK ("OK", 0),
    INTERNAL_ERROR("INTERNAL_ERROR", -1),
    INVALID_USER("INVALID_USER", -2),
    INVALID_GROUP ("INVALID_GROUP", -3),
    INVALID_PAYMENT("INVALID_PAYMENT", -4);

    private final String stateName;
    private final int stateCode;

    PaymentValidatorState(String stateName, int stateCode){
        this.stateName = stateName;
        this.stateCode = stateCode;
    }

    public String getStateName(){
        return stateName;
    }

    public int getStateCode(){
        return stateCode;
    }
}
