package com.volunnear;

public class Routes {
    public static final String API_URL = "/api/v1";

    /**
     * Users management
     */
    public static final String LOGIN = API_URL + "/login";
    public static final String REGISTER_ROUTE_SECURITY = API_URL + "/registration/*";
    public static final String REGISTER_VOLUNTEER = API_URL + "/registration/volunteer";
    public static final String REGISTER_ORGANISATION = API_URL + "/registration/organisation";
    public static final String UPDATE_VOLUNTEER_PROFILE = API_URL + "/update/volunteer";
    public static final String UPDATE_ORGANISATION_PROFILE = API_URL + "/update/organisation";
    /**
     * Organisation routes
     */
    public static final String ORGANISATION = API_URL + "/organisation";
    public static final String GET_ALL_ORGANISATIONS = ORGANISATION + "/get_all";
    public static final String GET_ORGANISATION_PROFILE = ORGANISATION + "/my_profile";
    /**
     *  Volunteers routes
     */
    public static  final String VOLUNTEER = API_URL + "/volunteer";
    public static  final String GET_VOLUNTEER_PROFILE = VOLUNTEER + "/my_profile";
    /**
     * Activity routes
     */
    public static final String ADD_ACTIVITY = ORGANISATION + "/add_activity";
    public static final String MY_ACTIVITIES = ORGANISATION + "/my_activities";
    public static final String ACTIVITY_CURRENT_ORGANISATION = ORGANISATION + "/get_activities";
}
