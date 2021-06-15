package com.autentia.example.splitbill.usecase.group.status;

public enum AddUserToGroupCmdStatus{
        OK ("OK", 0),
        INTERNAL_ERROR ("INTERNAL_ERROR", 1),
        USER_ALREADY_EXISTS_IN_GROUP ("USER_ALREADY_EXISTS_IN_GROUP", 2);

    private final String statusName;
        private final int statusCode;

        AddUserToGroupCmdStatus(String statusName, int statusCode){
            this.statusName = statusName;
            this.statusCode = statusCode;
        }

        public String getStatusName(){
            return statusName;
        }

        public int getStatusCode(){
            return statusCode;
        }
    }
