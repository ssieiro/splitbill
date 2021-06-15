package com.autentia.example.splitbill.usecase.group.status;

public enum CreateGroupCmdStatus{
        OK ("OK", 0),
        INTERNAL_ERROR ("INTERNAL_ERROR", 1),
        GROUP_ALREADY_EXISTS ("GROUP_ALREADY_EXISTS", 2);

    private final String statusName;
        private final int statusCode;

        CreateGroupCmdStatus(String statusName, int statusCode){
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
