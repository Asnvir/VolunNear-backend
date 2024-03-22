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
     * Volunteers routes
     */
    public static final String VOLUNTEER = API_URL + "/volunteer";
    public static final String GET_VOLUNTEER_PROFILE = VOLUNTEER + "/my_profile";
    public static final String SET_VOLUNTEERS_PREFERENCES = VOLUNTEER + "/set_preferences";
    public static final String ADD_VOLUNTEER_TO_ACTIVITY = VOLUNTEER + "/enter_to_activity";
    public static final String DELETE_VOLUNTEER_FROM_ACTIVITY = VOLUNTEER + "/leave_activity";
    public static final String GET_RECOMMENDATION_BY_PREFERENCES = VOLUNTEER + "/get_recommendations";

    /**
     * Activity routes
     */
    public static final String ADD_ACTIVITY = ORGANISATION + "/add_activity";
    public static final String GET_MY_ACTIVITIES = ORGANISATION + "/my_activities";
    public static final String ACTIVITY_CURRENT_ORGANISATION = ORGANISATION + "/get_activities";
    public static final String DELETE_CURRENT_ACTIVITY_BY_ID = ORGANISATION + "/delete_activity";
    public static final String GET_ALL_ACTIVITIES_WITH_ALL_ORGANISATIONS = ORGANISATION + "/get_all_activities";

    /**
     * Feedback routes
     */
    public static final String FEEDBACK = API_URL + "/feedback";
    public static final String POST_FEEDBACK_ABOUT_ORGANISATION = FEEDBACK + "/give_feedback";
    public static final String GET_FEEDBACKS_OF_ALL_ORGANISATIONS = FEEDBACK + "/get_all";
    public static final String UPDATE_FEEDBACK_FOR_CURRENT_ORGANISATION = FEEDBACK + "/update_feedback";
    public static final String GET_FEEDBACKS_FROM_CURRENT_ORGANISATION = FEEDBACK + "/feedbacks_of_organisation";
    public static final String DELETE_FEEDBACK_ABOUT_ORGANISATION = FEEDBACK + "/remove_feedback";
}
