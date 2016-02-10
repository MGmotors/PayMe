package com.example.mscha.payme.app;

public class API {
    public static class ActionCodes{
    public final static String LOGIN = "1";
    public final static String REGISTER = "0";
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
        public final static String ILLEGAL_CHAR = "8";
        public final static String NAME_PW_MISMATCH = "9";
    }
    public static class HeaderFields{
        public final static String USERNAME = "payme-username";
        public final static String PASSWORD = "payme-passwd";
        public final static String EMAIL = "payme-email";
        public final static String ACTION = "payme-action";
        public final static String ERROR = "payme-error";
    }
    public static class URLs{
        public final static String REGISTER = "https://payme-schabimperle.c9users.io/proto1/register.php";
        public final static String LOGIN = "https://payme-schabimperle.c9users.io/proto1/login.php";
        public final static String MYADMIN = "https://payme-schabimperle.c9users.io/phpmyadmin/";
    }
}