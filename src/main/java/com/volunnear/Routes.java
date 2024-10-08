package com.volunnear;

public class Routes {

    private static final String API_URL = "/api/v1";

    /**
     * Users management
     */
    public static final String LOGIN = API_URL + "/login";

    public static final String GET_CURRENT_USER = API_URL + "/auth/userInfo";
    public static final String REGISTER_ROUTE_SECURITY = API_URL + "/registration";
    public static final String REGISTER_VOLUNTEER = API_URL + "/registration/volunteer";
    public static final String REGISTER_ORGANISATION = API_URL + "/registration/organisation";
    public static final String UPDATE_VOLUNTEER_PROFILE = API_URL + "/update/volunteer";
    public static final String UPDATE_ORGANISATION_PROFILE = API_URL + "/update/organisation";

    /**
     * Password recovery
     */
    public static final String FORGOT_PASSWORD = API_URL +"/forgot_password";
    public static final String VERIFY_EMAIL = FORGOT_PASSWORD + "/verify_email";
    public static final String VERIFY_OTP = FORGOT_PASSWORD + "/verify_otp";
    public static final String CHANGE_PASSWORD = FORGOT_PASSWORD + "/change_password";

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
    public static final String JOIN_TO_ACTIVITY = VOLUNTEER + "/enter_to_activity";
    public static final String SET_VOLUNTEERS_PREFERENCES = VOLUNTEER + "/set_preferences";
    public static final String LEAVE_FROM_ACTIVITY_BY_VOLUNTEER = VOLUNTEER + "/leave_activity";
    public static final String GET_RECOMMENDATION_BY_PREFERENCES = VOLUNTEER + "/get_recommendations";

    public static final String GET_ALL_ACTIVITIES_OF_CURRENT_VOLUNTEER = VOLUNTEER + "/activities_names";

    public static final String IS_MY_ACTIVITY = VOLUNTEER + "/is_my_activity";

    /**
     * Activity routes
     */
    public static final String ADD_ACTIVITY = ORGANISATION + "/add_activity";

    public static final String GET_ACTIVITIES = ORGANISATION + "/activities";
    public static final String UPDATE_ACTIVITY_INFORMATION = ORGANISATION + "/update_activity";
    public static final String ACTIVITY_CURRENT_ORGANISATION = ORGANISATION + "/get_activities";
    public static final String DELETE_CURRENT_ACTIVITY_BY_ID = ORGANISATION + "/delete_activity";
    public static final String GET_ALL_ACTIVITIES_WITH_ALL_ORGANISATIONS = ORGANISATION + "/get_all_activities";
    public static final String GET_ALL_ACTIVITIES_NAMES = ORGANISATION + "/all_activities_names";


    /**
     * Files routes
     */

    public static final String FILES = API_URL + "/upload";
    public static final String UPLOAD_VOLUNTEER_AVATAR = FILES + "/avatar/volunteer/{volunteerId}";

    public static final String UPLOAD_ORGANISATION_AVATAR = FILES + "/avatar/organisation/{orgId}";

    public static final String UPLOAD_ACTIVITY_COVER_IMAGE = FILES + "/activity/cover/{activityId}";

    public static final String UPLOAD_ACTIVITY_GALLERY_IMAGE = FILES + "/activity/gallery_image/{activityId}";

    public static final String UPLOAD_ACTIVITY_GALLERY_IMAGES = FILES + "/activity/gallery_images/{activityId}";

    /**
     * Feedback routes
     */
    public static final String FEEDBACK = API_URL + "/feedback";
    public static final String POST_FEEDBACK_ABOUT_ORGANISATION = FEEDBACK + "/give_feedback";
    public static final String GET_FEEDBACKS_OF_ALL_ORGANISATIONS = FEEDBACK + "/get_all";
    public static final String UPDATE_FEEDBACK_FOR_CURRENT_ORGANISATION = FEEDBACK + "/update_feedback";
    public static final String GET_FEEDBACKS_FROM_CURRENT_ORGANISATION = FEEDBACK + "/feedbacks_of_organisation";
    public static final String DELETE_FEEDBACK_ABOUT_ORGANISATION = FEEDBACK + "/remove_feedback";
    /**
     * Location routes
     */
    public static final String LOCATION = API_URL + "/location";
    public static final String FIND_NEARBY_ACTIVITIES = LOCATION + "/find_nearby";
    /**
     * Notifications routes
     */
    public static final String NOTIFICATIONS = API_URL + "/notifications";
    public static final String GET_ALL_SUBSCRIPTIONS_OF_VOLUNTEER = NOTIFICATIONS + "/my_subscriptions";
    public static final String SUBSCRIBE_TO_NOTIFICATIONS_BY_ID_OF_ORGANISATION = NOTIFICATIONS + "/subscribe";
    public static final String UNSUBSCRIBE_FROM_NOTIFICATIONS_BY_ID_OF_ORGANISATION = NOTIFICATIONS + "/unsubscribe";
    /**
     * Social networks integration routes
     */
    public static final String SOCIAL_NETWORKS = API_URL + "/social";
    public static final String ADD_COMMUNITY_LINK = SOCIAL_NETWORKS + "/add_community_link";
    public static final String GET_CHAT_LINK_BY_ACTIVITY = SOCIAL_NETWORKS + "/get_chat_link";
    public static final String ADD_CHAT_LINK_FOR_ACTIVITY = SOCIAL_NETWORKS + "/add_chat_link";
    public static final String GET_COMMUNITY_LINK_BY_ORGANISATION = SOCIAL_NETWORKS + "/get_community_link";


    /**
     * Organisation Rating Endpoints
     *
     */

    public static final String RATE_ORGANISATION = API_URL + "/organisations/rate/{id}";

    public static final String GET_AVERAGE_RATING = API_URL + "/organisations/average-rating/{id}";

    public static final String[] SWAGGER_ENDPOINTS = {
            "api/v1/auth/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
}
