package com.bayer.bayerreward.ApiConstant;

/**
 * Created by Naushad on 10/30/2017.
 */


import com.bayer.bayerreward.GetterSetter.AboutBayerKnightFetch;
import com.bayer.bayerreward.GetterSetter.AccountHistory;
import com.bayer.bayerreward.GetterSetter.Agenda;
import com.bayer.bayerreward.GetterSetter.Analytic;
import com.bayer.bayerreward.GetterSetter.Catlog;
import com.bayer.bayerreward.GetterSetter.CommentList;
import com.bayer.bayerreward.GetterSetter.ContactListFetch;
import com.bayer.bayerreward.GetterSetter.CurrencyConverterResponse;
import com.bayer.bayerreward.GetterSetter.CurrencyDropDown;
import com.bayer.bayerreward.GetterSetter.DeleteNewsFeedComment;
import com.bayer.bayerreward.GetterSetter.DeletePost;
import com.bayer.bayerreward.GetterSetter.DeleteSelfie;
import com.bayer.bayerreward.GetterSetter.DocumentsListFetch;
import com.bayer.bayerreward.GetterSetter.EditNewsFeed;
import com.bayer.bayerreward.GetterSetter.EventInfoFetch;
import com.bayer.bayerreward.GetterSetter.EventListing;
import com.bayer.bayerreward.GetterSetter.FetchAgenda;
import com.bayer.bayerreward.GetterSetter.FetchAttendee;
import com.bayer.bayerreward.GetterSetter.FetchFeed;
import com.bayer.bayerreward.GetterSetter.FetchSpeaker;
import com.bayer.bayerreward.GetterSetter.Forgot;
import com.bayer.bayerreward.GetterSetter.GalleryListFetch;
import com.bayer.bayerreward.GetterSetter.GeneralInfoList;
import com.bayer.bayerreward.GetterSetter.LikeListing;
import com.bayer.bayerreward.GetterSetter.LikePost;
import com.bayer.bayerreward.GetterSetter.LivePollFetch;
import com.bayer.bayerreward.GetterSetter.LivePollSubmitFetch;
import com.bayer.bayerreward.GetterSetter.LocationDistrict;
import com.bayer.bayerreward.GetterSetter.LocationState;
import com.bayer.bayerreward.GetterSetter.LocationTaluka;
import com.bayer.bayerreward.GetterSetter.LocationVillage;
import com.bayer.bayerreward.GetterSetter.Login;
import com.bayer.bayerreward.GetterSetter.MpointCalculator;
import com.bayer.bayerreward.GetterSetter.MyPerformance;
import com.bayer.bayerreward.GetterSetter.NotificationListFetch;
import com.bayer.bayerreward.GetterSetter.NotificationSend;
import com.bayer.bayerreward.GetterSetter.PostComment;
import com.bayer.bayerreward.GetterSetter.PostSelfie;
import com.bayer.bayerreward.GetterSetter.PostTextFeed;
import com.bayer.bayerreward.GetterSetter.PostVideoSelfie;
import com.bayer.bayerreward.GetterSetter.ProfileSave;
import com.bayer.bayerreward.GetterSetter.QADirectFetch;
import com.bayer.bayerreward.GetterSetter.QASessionFetch;
import com.bayer.bayerreward.GetterSetter.QASpeakerFetch;
import com.bayer.bayerreward.GetterSetter.QRPost;
import com.bayer.bayerreward.GetterSetter.QuizFetch;
import com.bayer.bayerreward.GetterSetter.QuizSubmitFetch;
import com.bayer.bayerreward.GetterSetter.RatingSessionPost;
import com.bayer.bayerreward.GetterSetter.RatingSpeakerPost;
import com.bayer.bayerreward.GetterSetter.RedeemRequest;
import com.bayer.bayerreward.GetterSetter.ReportComment;
import com.bayer.bayerreward.GetterSetter.ReportCommentHide;
import com.bayer.bayerreward.GetterSetter.ReportPost;
import com.bayer.bayerreward.GetterSetter.ReportPostHide;
import com.bayer.bayerreward.GetterSetter.ReportSelfie;
import com.bayer.bayerreward.GetterSetter.ReportSelfieHide;
import com.bayer.bayerreward.GetterSetter.ReportUser;
import com.bayer.bayerreward.GetterSetter.ReportUserHide;
import com.bayer.bayerreward.GetterSetter.ReportVideoContest;
import com.bayer.bayerreward.GetterSetter.ReportVideoContestHide;
import com.bayer.bayerreward.GetterSetter.ResendOTP;
import com.bayer.bayerreward.GetterSetter.ResetPassword;
import com.bayer.bayerreward.GetterSetter.SchemesAndOffers;
import com.bayer.bayerreward.GetterSetter.SelfieLike;
import com.bayer.bayerreward.GetterSetter.SelfieListFetch;
import com.bayer.bayerreward.GetterSetter.SendMessagePost;
import com.bayer.bayerreward.GetterSetter.SignUpRequest;
import com.bayer.bayerreward.GetterSetter.SurveyListFetch;
import com.bayer.bayerreward.GetterSetter.TimeWeather;
import com.bayer.bayerreward.GetterSetter.TravelListFetch;
import com.bayer.bayerreward.GetterSetter.VideoContestLikes;
import com.bayer.bayerreward.GetterSetter.VideoContestListFetch;
import com.bayer.bayerreward.GetterSetter.VideoFetchListFetch;
import com.bayer.bayerreward.GetterSetter.Weather;
import com.bayer.bayerreward.GetterSetter.leaderboard_data;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {

    @POST("SpecificEventLogin")
    @FormUrlEncoded
    Call<Login> LoginPost(@Field("api_access_token") String api_access_token,
                          @Field("event_id") String eventId,
                          @Field("registration_id") String registration_id,
                          @Field("platform") String platform,
                          @Field("device") String device,
                          @Field("os_version") String os_version,
                          @Field("app_version") String app_version);

    @POST("Login")
    @FormUrlEncoded
    Call<EventListing> EventListPost(@Field("email") String email,
                                     @Field("password") String password);

    @POST("LoginMobileForReward")
    @FormUrlEncoded
    Call<EventListing> LoginMobile(@Field("mobile") String mobile);

    @POST("ResendOtp")
    @FormUrlEncoded
    Call<ResendOTP> ResendOtp(@Field("mobile") String mobile);

    @POST("EventListFetch")
    @FormUrlEncoded
    Call<EventListing> GetEventList(@Field("api_access_token") String api_access_token);

    @POST("ForgetPassword")
    @FormUrlEncoded
    Call<Forgot> ForgotPassword(@Field("email") String email);

    @POST("ResetPassword")
    @FormUrlEncoded
    Call<ResetPassword> ResetPassword(@Field("temp_password") String email,
                                      @Field("new_password") String new_password);


    @POST("CommentFetch")
    @FormUrlEncoded
    Call<CommentList> getComment(@Field("event_id") String eventid,
                                 @Field("post_id") String post_id);


    @POST("PostComment")
    @FormUrlEncoded
    Call<PostComment> postComment(@Field("event_id") String eventId,
                                  @Field("news_feed_id") String news_feed_id,
                                  @Field("comment_data") String comment_data,
                                  @Field("api_access_token") String api_access_token);

    @POST("SendNotification")
    @FormUrlEncoded
    Call<NotificationSend> SendNotification(@Field("api_access_token") String api_access_token,
                                            @Field("event_id") String event_id,
                                            @Field("message") String message,
                                            @Field("display_time") String display_time);

    @POST("NewsFeedLike")
    @FormUrlEncoded
    Call<LikePost> postLike(@Field("event_id") String eventId,
                            @Field("news_feed_id") String news_feed_id,
                            @Field("api_access_token") String api_access_token);


    @POST("NewsFeedLikeUser")
    @FormUrlEncoded
    Call<LikeListing> postLikeUserList(@Field("api_access_token") String api_access_token,
                                       @Field("news_feed_id") String notification_id,
                                       @Field("event_id") String eventId);


    @POST("PostNewsFeed")
    @Multipart
    Call<PostTextFeed> PostNewsFeed(@Part("type") RequestBody type,
                                    @Part("api_access_token") RequestBody api_access_token,
                                    @Part("event_id") RequestBody event_id,
                                    @Part("status") RequestBody status,
                                    @Part MultipartBody.Part filename);

    @POST("PostNewsFeed")
    @Multipart
    Call<PostTextFeed> PostNewsFeed(@Part("type") RequestBody type,
                                    @Part("api_access_token") RequestBody api_access_token,
                                    @Part("event_id") RequestBody event_id,
                                    @Part("status") RequestBody status,
                                    @Part MultipartBody.Part filename,
                                    @Part MultipartBody.Part thumbimage);


    @POST("ReportActivity")
    @Multipart
    Call<PostTextFeed> ReportActivity(@Part("api_access_token") RequestBody api_access_token,
                                      @Part("event_id") RequestBody event_id,
                                      @Part("description") RequestBody status,
                                      @Part("report_dropdown") RequestBody report_dropdown,
                                      @Part MultipartBody.Part filename,
                                      @Part MultipartBody.Part thumbimage);

    @POST("QADirectPost")
    @Multipart
    Call<PostTextFeed> QADirectPost(@Part("api_access_token") RequestBody api_access_token,
                                    @Part("event_id") RequestBody event_id,
                                    @Part("question") RequestBody status,
                                    @Part MultipartBody.Part filename,
                                    @Part MultipartBody.Part thumbimage);


    @POST("EditNewsFeed")
    @Multipart
    Call<EditNewsFeed> EditNewsFeed(@Part("Type") RequestBody type,
                                    @Part("api_access_token") RequestBody api_access_token,
                                    @Part("event_id") RequestBody event_id,
                                    @Part("status") RequestBody status,
                                    @Part("news_feed_id") RequestBody news_feed_id,
                                    @Part MultipartBody.Part filename);


    @POST("EditProfileSubmit")
    @Multipart
    Call<ProfileSave> ProfileSave(
            @Part("api_access_token") RequestBody api_access_token,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("description") RequestBody description,
            @Part("city") RequestBody city,
            @Part("country") RequestBody country,
            @Part("mobile") RequestBody mobile,
            @Part("attendee_type") RequestBody attendee_type,
            @Part("designation") RequestBody designation,
            @Part("event_id") RequestBody event_id,
            @Part("company_name") RequestBody company_name);


    @POST("EditProfileSubmit")
    @Multipart
    Call<ProfileSave> ProfileSave(
            @Part("api_access_token") RequestBody api_access_token,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("description") RequestBody description,
            @Part("city") RequestBody city,
            @Part("country") RequestBody country,
            @Part("mobile") RequestBody mobile,
            @Part("attendee_type") RequestBody attendee_type,
            @Part("designation") RequestBody designation,
            @Part("event_id") RequestBody event_id,
            @Part("company_name") RequestBody company_name,
            @Part MultipartBody.Part filename);


    @POST("EditProfileFetch")
    @Multipart
    Call<ProfileSave> fetchProfileDetail(@Part("api_access_token") RequestBody api_access_token,
                                         @Part("event_id") RequestBody event_id);

    @POST("update_address_request")
    @Multipart
    Call<ProfileSave> UpdateDate(@Part("api_access_token") RequestBody api_access_token,
                                         @Part("event_id") RequestBody event_id,
                                         @Part("address1") RequestBody address1,
                                         @Part("address2") RequestBody address2);


    @POST("AboutBayerKnightFetch")
    @FormUrlEncoded
    Call<AboutBayerKnightFetch> AboutBayerKnightFetch(@Field("api_access_token") String api_access_token,
                                                      @Field("event_id") String event_id);

    @POST("SchemesAndOffers")
    @FormUrlEncoded
    Call<SchemesAndOffers> SchemesAndOffers(@Field("api_access_token") String api_access_token,
                                            @Field("event_id") String event_id);

    @POST("QRScanDataPost")
    @FormUrlEncoded
    Call<QRPost> QRScanDataPost(@Field("api_access_token") String api_access_token,
                                @Field("event_id") String event_id,
                                @Field("first_name") String first_name,
                                @Field("last_name") String last_name,
                                @Field("contact_number") String contact_number,
                                @Field("email") String email);


    @POST("signup_request")
    @FormUrlEncoded
    Call<SignUpRequest> SignUpRequest(@Field("event_id") String api_access_token,
                                      @Field("first_name") String first_name,
                                      @Field("last_name") String last_name,
                                      @Field("mobile") String mobile,
                                      @Field("shop_name") String shop_name,
                                      @Field("pincode") String pincode,
                                      @Field("state") String state,
                                      @Field("district") String district,
                                      @Field("taluka") String taluka,
                                      @Field("village") String village);

    @POST("location_state_master")
    @FormUrlEncoded
    Call<LocationState> LocationState(@Field("api_access_token") String api_access_token,
                                      @Field("event_id") String event_id);


    @POST("location_district_master")
    @FormUrlEncoded
    Call<LocationDistrict> LocationDistrict(@Field("api_access_token") String api_access_token,
                                            @Field("event_id") String event_id,
                                            @Field("state") String state);


    @POST("location_taluka_master")
    @FormUrlEncoded
    Call<LocationTaluka> LocationTaluka(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id,
                                        @Field("district") String state);

    @POST("location_village_master")
    @FormUrlEncoded
    Call<LocationVillage> LocationVillage(@Field("api_access_token") String api_access_token,
                                          @Field("event_id") String event_id,
                                          @Field("taluka") String taluka);


    @POST("NewsFeedFetch")
    @FormUrlEncoded
    Call<FetchFeed> FeedFetchPost(@Field("api_access_token") String api_access_token,
                                  @Field("event_id") String event_id);

    @POST("GenInfoAll")
    @FormUrlEncoded
    Call<GeneralInfoList> FetchGeneralInfo(@Field("event_id") String event_id);

    @POST("GenInfoCurrencyDropdown")
    @FormUrlEncoded
    Call<CurrencyDropDown> FetchCurrencyDropDown(@Field("event_id") String event_id);

    @POST("GenInfoWeather")
    @FormUrlEncoded
    Call<TimeWeather> FetchTimeWeather(@Field("event_id") String event_id);

    @POST("GenInfoCurrencyConverter")
    @FormUrlEncoded
    Call<CurrencyConverterResponse> SubmitCurrencyConverter(@Field("event_id") String event_id,
                                                            @Field("from_currency") String from_currency,
                                                            @Field("to_currency") String to_currency,
                                                            @Field("amount") String amount);


    @POST("SpeakerFetch")
    @FormUrlEncoded
    Call<FetchSpeaker> SpeakerFetchPost(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id);


    @POST("AttendeeFetch")
    @FormUrlEncoded
    Call<FetchAttendee> AttendeeFetchPost(@Field("api_access_token") String api_access_token,
                                          @Field("event_id") String event_id);


    @POST("AgendaSeprateFetch")
    @FormUrlEncoded
    Call<FetchAgenda> AgendaFetchPost(@Field("api_access_token") String api_access_token,
                                      @Field("event_id") String event_id);

    @POST("LeaderBoardReward")
    @FormUrlEncoded
    Call<leaderboard_data> LeaderBoardReward(@Field("api_access_token") String api_access_token,
                                             @Field("event_id") String event_id);


    @POST("AgendaVacationSeprateFetch")
    @FormUrlEncoded
    Call<Agenda> AgendaFetchVacation(@Field("api_access_token") String api_access_token,
                                     @Field("event_id") String event_id);


    @POST("DeleteNewsFeedPost")
    @FormUrlEncoded
    Call<DeletePost> DeletePost(@Field("api_access_token") String api_access_token,
                                @Field("event_id") String event_id, @Field("news_feed_id") String news_feed_id);

    @POST("RateSpeaker")
    @FormUrlEncoded
    Call<RatingSpeakerPost> RatingSpeakerPost(@Field("api_access_token") String api_access_token,
                                              @Field("event_id") String event_id,
                                              @Field("target_id") String news_feed_id,
                                              @Field("rating") String rating,
                                              @Field("comment") String comment);


    @POST("RateSession")
    @FormUrlEncoded
    Call<RatingSessionPost> RatingSessionPost(@Field("api_access_token") String api_access_token,
                                              @Field("event_id") String event_id, @Field("target_id") String target_id, @Field("rating") String rating,
                                              @Field("comment") String comment,
                                              @Field("feedback_type") String feedback_type);


    @POST("SendMessage")
    @FormUrlEncoded
    Call<SendMessagePost> SendMessagePost(@Field("api_access_token") String api_access_token,
                                          @Field("event_id") String event_id,
                                          @Field("message_text") String message_text,
                                          @Field("target_attendee_id") String target_attendee_id,
                                          @Field("target_attendee_type") String target_attendee_type);


    @POST("EventInfoFetch")
    @FormUrlEncoded
    Call<EventInfoFetch> EventInfoFetch(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id);

    @POST("account_history")
    @FormUrlEncoded
    Call<AccountHistory> AccountHistory(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id);

    @POST("catalog_list")
    @FormUrlEncoded
    Call<Catlog> CatlogList(@Field("api_access_token") String api_access_token,
                            @Field("event_id") String event_id);

    @POST("m_pin_scanner")
    @FormUrlEncoded
    Call<RedeemRequest> Mpinsubmit(@Field("api_access_token") String api_access_token,
                                   @Field("event_id") String event_id,
                                   @Field("scanned_qr_list") String scanned_qr_list);

    @POST("redemption_request")
    @FormUrlEncoded
    Call<RedeemRequest> RedeemRequest(@Field("api_access_token") String api_access_token,
                                      @Field("event_id") String event_id,
                                      @Field("product_code") String product_code,
                                      @Field("product_name") String product_name,
                                      @Field("no_of_quantity") String no_of_quantity,
                                      @Field("mpoints") String mpoints);


    @POST("SurveyFetch")
    @FormUrlEncoded
    Call<SurveyListFetch> SurveyListFetch(@Field("api_access_token") String api_access_token,
                                          @Field("event_id") String event_id);

    @POST("DocumentsFetch")
    @FormUrlEncoded
    Call<DocumentsListFetch> DocumentsListFetch(@Field("api_access_token") String api_access_token,
                                                @Field("event_id") String event_id);

    @POST("TravelGalleryFetch")
    @FormUrlEncoded
    Call<TravelListFetch> TravelListFetch(@Field("api_access_token") String api_access_token,
                                          @Field("event_id") String event_id);

    @POST("NotificationListFetch")
    @FormUrlEncoded
    Call<NotificationListFetch> NotificationListFetch(@Field("api_access_token") String api_access_token,
                                                      @Field("event_id") String event_id);

    @POST("GalleryFetch")
    @FormUrlEncoded
    Call<GalleryListFetch> GalleryListFetch(@Field("api_access_token") String api_access_token,
                                            @Field("event_id") String event_id);

    @POST("VideoFetch")
    @FormUrlEncoded
    Call<VideoFetchListFetch> VideoFetchListFetch(@Field("api_access_token") String api_access_token,
                                                  @Field("event_id") String event_id);

    @POST("LivePollFetch")
    @FormUrlEncoded
    Call<LivePollFetch> LivePollFetch(@Field("api_access_token") String api_access_token,
                                      @Field("event_id") String event_id);


    @POST("LivePollSubmit")
    @FormUrlEncoded
    Call<LivePollSubmitFetch> LivePollSubmitFetch(@Field("api_access_token") String api_access_token,
                                                  @Field("event_id") String event_id, @Field("live_poll_id") String live_poll_id,
                                                  @Field("live_poll_options_id") String live_poll_options_id);


    @POST("QuizFetch")
    @FormUrlEncoded
    Call<QuizFetch> QuizFetch(@Field("api_access_token") String api_access_token,
                              @Field("event_id") String event_id);


    @POST("QuizSubmit")
    @FormUrlEncoded
    Call<QuizSubmitFetch> QuizSubmitFetch(@Field("api_access_token") String api_access_token,
                                          @Field("event_id") String event_id, @Field("quiz_id") String live_poll_id,
                                          @Field("quiz_options_id") String live_poll_options_id);


    @POST("QASessionFetch")
    @FormUrlEncoded
    Call<QASessionFetch> QASessionFetch(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id);

    @POST("m_points_list")
    @FormUrlEncoded
    Call<MpointCalculator> MPointCalcList(@Field("api_access_token") String api_access_token,
                                          @Field("event_id") String event_id);

    @POST("QADirectFetch")
    @FormUrlEncoded
    Call<QADirectFetch> QADirectFetch(@Field("api_access_token") String api_access_token,
                                      @Field("event_id") String event_id);

    @POST("QADirectLike")
    @FormUrlEncoded
    Call<QADirectFetch> QADirectLike(@Field("api_access_token") String api_access_token,
                                     @Field("event_id") String event_id, @Field("question_id") String question_id
    );


    @POST("QASessionLike")
    @FormUrlEncoded
    Call<QASessionFetch> QASessionLike(@Field("api_access_token") String api_access_token,
                                       @Field("event_id") String event_id, @Field("question_id") String question_id,
                                       @Field("session_id") String session_id);

    @POST("QASessionPost")
    @FormUrlEncoded
    Call<QASessionFetch> QASessionPost(@Field("api_access_token") String api_access_token,
                                       @Field("event_id") String event_id, @Field("question") String question,
                                       @Field("speaker_name") String speaker_name, @Field("session_id") String session_id);


    @POST("QASpeakerFetch")
    @FormUrlEncoded
    Call<QASpeakerFetch> QASpeakerFetch(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id);

    @POST("QASpeakerLike")
    @FormUrlEncoded
    Call<QASpeakerFetch> QASpeakerLike(@Field("api_access_token") String api_access_token,
                                       @Field("event_id") String event_id, @Field("question_id") String question_id,
                                       @Field("session_id") String session_id);

    @POST("QASpeakerPost")
    @FormUrlEncoded
    Call<QASpeakerFetch> QASpeakerPost(@Field("api_access_token") String api_access_token,
                                       @Field("event_id") String event_id, @Field("question") String question,
                                       @Field("speaker_id") String speaker_id);

    @POST("QADirectPost")
    @FormUrlEncoded
    Call<QADirectFetch> QADirectPost(@Field("api_access_token") String api_access_token,
                                     @Field("event_id") String event_id, @Field("question") String question);


    @POST("NewsFeedDetailFetch")
    @FormUrlEncoded
    Call<FetchFeed> NewsFeedDetailFetch(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id, @Field("post_id") String post_id);

    @POST("SelfieListFetch")
    @FormUrlEncoded
    Call<SelfieListFetch> SelfieListFetch(@Field("api_access_token") String api_access_token,
                                          @Field("event_id") String event_id);


    @POST("SelfieLike")
    @FormUrlEncoded
    Call<SelfieLike> SelfieLike(@Field("api_access_token") String api_access_token,
                                @Field("event_id") String event_id,
                                @Field("selfie_gallery_id") String selfie_gallery_id);

    @POST("MyPerformance")
    @FormUrlEncoded
    Call<MyPerformance> MyPerformance(@Field("api_access_token") String api_access_token,
                                      @Field("event_id") String event_id);

    @POST("VideoContestListFetch")
    @FormUrlEncoded
    Call<VideoContestListFetch> VideoContestListFetch(@Field("api_access_token") String api_access_token,
                                                      @Field("event_id") String event_id);


    @POST("VideoContestLikes")
    @FormUrlEncoded
    Call<VideoContestLikes> VideoContestLikes(@Field("api_access_token") String api_access_token,
                                              @Field("event_id") String event_id,
                                              @Field("video_contest_id") String selfie_gallery_id);

    @POST("PostSelfie")
    @Multipart
    Call<PostSelfie> PostSelfie(@Part("api_access_token") RequestBody api_access_token,
                                @Part("event_id") RequestBody event_id,
                                @Part("title") RequestBody title,
                                @Part MultipartBody.Part filename);

    @POST("PostVideoContest")
    @Multipart
    Call<PostVideoSelfie> PostVideoContest(@Part("api_access_token") RequestBody api_access_token,
                                           @Part("event_id") RequestBody event_id,
                                           @Part("title") RequestBody title,
                                           @Part MultipartBody.Part thumb,
                                           @Part MultipartBody.Part filename);


    @POST("ReportPostHide")
    @FormUrlEncoded
    Call<ReportPostHide> ReportPostHide(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id,
                                        @Field("post_id") String post_id);


    @POST("ReportPost")
    @FormUrlEncoded
    Call<ReportPost> ReportPost(@Field("api_access_token") String api_access_token,
                                @Field("event_id") String event_id,
                                @Field("post_id") String post_id,
                                @Field("text") String text);


    @POST("ReportUser")
    @FormUrlEncoded
    Call<ReportUser> ReportUser(@Field("api_access_token") String api_access_token,
                                @Field("event_id") String event_id,
                                @Field("target_attendee_id") String target_attendee_id,
                                @Field("text") String text);

    @POST("ReportUserHide")
    @FormUrlEncoded
    Call<ReportUserHide> ReportUserHide(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id,
                                        @Field("target_attendee_id") String target_attendee_id);


    @POST("DeleteNewsFeedComment")
    @FormUrlEncoded
    Call<DeleteNewsFeedComment> DeleteNewsFeedComment(@Field("api_access_token") String api_access_token,
                                                      @Field("news_feed_id") String news_feed_id,
                                                      @Field("comment_id") String comment_id,
                                                      @Field("event_id") String event_id);


    @POST("ReportCommentHide")
    @FormUrlEncoded
    Call<ReportCommentHide> ReportCommentHide(@Field("api_access_token") String api_access_token,
                                              @Field("event_id") String event_id, @Field("comment_id") String comment_id);


    @POST("ReportComment")
    @FormUrlEncoded
    Call<ReportComment> ReportComment(@Field("api_access_token") String api_access_token,
                                      @Field("event_id") String event_id,
                                      @Field("comment_id") String post_id,
                                      @Field("text") String text);


    @POST("ReportSelfieHide")
    @FormUrlEncoded
    Call<ReportSelfieHide> ReportSelfieHide(@Field("api_access_token") String api_access_token,
                                            @Field("event_id") String event_id,
                                            @Field("selfie_id") String selfie_id);

    @POST("DeleteSelfie")
    @FormUrlEncoded
    Call<DeleteSelfie> DeleteSelfie(@Field("api_access_token") String api_access_token,
                                    @Field("event_id") String event_id,
                                    @Field("selfie_id") String selfie_id);


    @POST("ReportSelfie")
    @FormUrlEncoded
    Call<ReportSelfie> ReportSelfie(@Field("api_access_token") String api_access_token,
                                    @Field("event_id") String event_id,
                                    @Field("selfie_id") String selfie_id,
                                    @Field("text") String text);


    @POST("ReportVideoContestHide")
    @FormUrlEncoded
    Call<ReportVideoContestHide> ReportVideoContestHide(@Field("api_access_token") String api_access_token,
                                                        @Field("event_id") String event_id,
                                                        @Field("video_contest_id") String video_contest_id);

    @POST("DeleteVideoContest")
    @FormUrlEncoded
    Call<ReportVideoContestHide> DeleteVideoContest(@Field("api_access_token") String api_access_token,
                                                    @Field("event_id") String event_id,
                                                    @Field("video_contest_id") String video_contest_id);


    @POST("ReportVideoContest")
    @FormUrlEncoded
    Call<ReportVideoContest> ReportVideoContest(@Field("api_access_token") String api_access_token,
                                                @Field("event_id") String event_id,
                                                @Field("selfie_id") String selfie_id,
                                                @Field("text") String text);

    @POST("AnalyticsSubmit")
    @FormUrlEncoded
    Call<Analytic> Analytic(@Field("api_access_token") String api_access_token,
                            @Field("event_id") String event_id,
                            @Field("target_attendee_id") String target_attendee_id,
                            @Field("target_attendee_type") String target_attendee_type,
                            @Field("analytic_type") String analytic_type);


    @POST("AnalyticsSubmit")
    @FormUrlEncoded
    Call<Analytic> Analytic(@Field("api_access_token") String api_access_token,
                            @Field("event_id") String event_id,
                            @Field("target_attendee_id") String target_attendee_id,
                            @Field("target_attendee_type") String target_attendee_type,
                            @Field("analytic_type") String analytic_type,
                            @Field("video_details") String video_details);

    @POST("GenInfoWeatherYahooWeather")
    @FormUrlEncoded
    Call<Weather> WeatherListFetch(@Field("api_access_token") String api_access_token,
                                   @Field("event_id") String event_id);

    @POST("EditProfileSubmit")
    @Multipart
    Call<ProfileSave> ProfileSave1(
            @Part("api_access_token") RequestBody api_access_token,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("description") RequestBody description,
            @Part("city") RequestBody city,
            @Part("country") RequestBody country,
            @Part("mobile") RequestBody mobile,
            @Part("attendee_type") RequestBody attendee_type,
            @Part("designation") RequestBody designation,
            @Part("event_id") RequestBody event_id,
            @Part("company_name") RequestBody company_name);

    @POST("ContactListFetch")
    @FormUrlEncoded
    Call<ContactListFetch> ContactListFetch(@Field("event_id") String event_id,
                                            @Field("api_access_token") String api_access_token);

}
