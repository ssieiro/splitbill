package com.autentia.example.splitbill.usecase.user;

public enum AddUserCmdStatus{
        OK ("OK", 0),
        INTERNAL_ERROR ("INTERNAL_ERROR", 1),
        USER_ALREADY_EXISTS ("USER_ALREADY_EXISTS", 2);

    private final String statusName;
        private final int statusCode;

        AddUserCmdStatus(String statusName, int statusCode){
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
