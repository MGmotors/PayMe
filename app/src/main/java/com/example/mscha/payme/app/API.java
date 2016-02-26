package com.example.mscha.payme.app;

public class API {
    public static class ActionCodes{
        public final static String REGISTER = "0";
        public final static String LOGIN = "1";
        public final static String LOGOUT = "2";
        public final static String CREATEPM = "3";
        public final static String GET_MY_PMS = "4";
    }
    public static class ErrorCodes{
        public final static String NO_ERROR = "0";
        public final static String UNKNOWN_ERROR = "1";
        public final static String DATABASE_ERROR = "2";
        public final static String TO_MANY_REQUESTS = "3";
        public final static String EMPTY_FIELD = "4";
        public final static String ACTIONS_MISMATCH = "5";
        public final static String USERNAME_TAKEN = "6";
        public final static String EMAIL_TAKEN = "7";
        public final static String BAD_DATA = "8";
        public final static String NAME_PW_MISMATCH = "9";
        public final static String NOT_LOGGEDIN = "10";
        public final static String SERVER_ERROR = "11";
    }
    public static class HeaderFields{
        public final static String USERNAME = "payme-username";
        public final static String PASSWORD = "payme-passwd";
        public final static String EMAIL = "payme-email";
        public final static String ACTION = "payme-action";
        public final static String ERROR = "payme-error";
        public final static String CREATEPM_JSON = "payme-createpm-json";
    }
    public static class URLs{
        public final static String REGISTER = "https://payme-schabimperle.c9users.io/proto1/register.php";
        public final static String LOGIN = "https://payme-schabimperle.c9users.io/proto1/login.php";
        public final static String LOGOUT = "https://payme-schabimperle.c9users.io/proto1/logout.php";
        public final static String CREATEPM = "https://payme-schabimperle.c9users.io/proto1/createPM.php";
        public final static String GET_MY_PMS = "https://payme-schabimperle.c9users.io/proto1/getMyPMs.php";
        public final static String MYADMIN = "https://payme-schabimperle.c9users.io/phpmyadmin/";
    }

    public static class JSON {
        public final static String PM_ARRAY = "myPMs";
        public final static String NAME = "name";
        public final static String DESCRIPTION = "description";
        public final static String DEBTORS_ARRAY = "debtors";
        public final static String PRICE = "price";
        public final static String DATETIME = "datetime";
        public final static String HAS_PAYED = "haspayed";
    }
}
