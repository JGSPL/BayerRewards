package com.procialize.vivo_app.ApiConstant;

/**
 * Created by Naushad on 10/30/2017.
 */

import com.procialize.vivo_app.GetterSetter.Agenda;
import com.procialize.vivo_app.GetterSetter.Analytic;
import com.procialize.vivo_app.GetterSetter.CommentList;
import com.procialize.vivo_app.GetterSetter.CurrencyConverterResponse;
import com.procialize.vivo_app.GetterSetter.CurrencyDropDown;
import com.procialize.vivo_app.GetterSetter.DeleteNewsFeedComment;
import com.procialize.vivo_app.GetterSetter.DeletePost;
import com.procialize.vivo_app.GetterSetter.DeleteSelfie;
import com.procialize.vivo_app.GetterSetter.DocumentsListFetch;
import com.procialize.vivo_app.GetterSetter.EditNewsFeed;
import com.procialize.vivo_app.GetterSetter.EventInfoFetch;
import com.procialize.vivo_app.GetterSetter.EventListing;
import com.procialize.vivo_app.GetterSetter.FetchAgenda;
import com.procialize.vivo_app.GetterSetter.FetchAttendee;
import com.procialize.vivo_app.GetterSetter.FetchFeed;
import com.procialize.vivo_app.GetterSetter.FetchSpeaker;
import com.procialize.vivo_app.GetterSetter.GalleryListFetch;
import com.procialize.vivo_app.GetterSetter.GeneralInfoList;
import com.procialize.vivo_app.GetterSetter.LikeListing;
import com.procialize.vivo_app.GetterSetter.LikePost;
import com.procialize.vivo_app.GetterSetter.LivePollFetch;
import com.procialize.vivo_app.GetterSetter.LivePollSubmitFetch;
import com.procialize.vivo_app.GetterSetter.Login;
import com.procialize.vivo_app.GetterSetter.NotificationListFetch;
import com.procialize.vivo_app.GetterSetter.PostComment;
import com.procialize.vivo_app.GetterSetter.PostSelfie;
import com.procialize.vivo_app.GetterSetter.PostTextFeed;
import com.procialize.vivo_app.GetterSetter.PostVideoSelfie;
import com.procialize.vivo_app.GetterSetter.ProfileSave;
import com.procialize.vivo_app.GetterSetter.QADirectFetch;
import com.procialize.vivo_app.GetterSetter.QASessionFetch;
import com.procialize.vivo_app.GetterSetter.QASpeakerFetch;
import com.procialize.vivo_app.GetterSetter.QuizFetch;
import com.procialize.vivo_app.GetterSetter.QuizSubmitFetch;
import com.procialize.vivo_app.GetterSetter.RatingSessionPost;
import com.procialize.vivo_app.GetterSetter.RatingSpeakerPost;
import com.procialize.vivo_app.GetterSetter.ReportComment;
import com.procialize.vivo_app.GetterSetter.ReportCommentHide;
import com.procialize.vivo_app.GetterSetter.ReportPost;
import com.procialize.vivo_app.GetterSetter.ReportPostHide;
import com.procialize.vivo_app.GetterSetter.ReportSelfie;
import com.procialize.vivo_app.GetterSetter.ReportSelfieHide;
import com.procialize.vivo_app.GetterSetter.ReportUser;
import com.procialize.vivo_app.GetterSetter.ReportUserHide;
import com.procialize.vivo_app.GetterSetter.ReportVideoContest;
import com.procialize.vivo_app.GetterSetter.ReportVideoContestHide;
import com.procialize.vivo_app.GetterSetter.SelfieLike;
import com.procialize.vivo_app.GetterSetter.SelfieListFetch;
import com.procialize.vivo_app.GetterSetter.SendMessagePost;
import com.procialize.vivo_app.GetterSetter.SurveyListFetch;
import com.procialize.vivo_app.GetterSetter.TimeWeather;
import com.procialize.vivo_app.GetterSetter.TravelListFetch;
import com.procialize.vivo_app.GetterSetter.VideoContestLikes;
import com.procialize.vivo_app.GetterSetter.VideoContestListFetch;
import com.procialize.vivo_app.GetterSetter.VideoFetchListFetch;
import com.procialize.vivo_app.GetterSetter.Weather;

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
    Call<Login> LoginPost(@Field("email") String email,
                          @Field("password") String password,
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
            @Part("company_name") RequestBody company_name,
            @Part MultipartBody.Part filename);


    @POST("EditProfileFetch")
    @Multipart
    Call<ProfileSave> fetchProfileDetail(
            @Part("api_access_token") RequestBody api_access_token,
            @Part("event_id") RequestBody event_id);


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
                                              @Field("event_id") String event_id, @Field("news_feed_id") String news_feed_id, @Field("rating") String rating);


    @POST("SendMessage")
    @FormUrlEncoded
    Call<SendMessagePost> SendMessagePost(@Field("api_access_token") String api_access_token,
                                          @Field("event_id") String event_id, @Field("message_text") String message_text, @Field("target_attendee_id") String target_attendee_id);


    @POST("EventInfoFetch")
    @FormUrlEncoded
    Call<EventInfoFetch> EventInfoFetch(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id);


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
                                       @Field("speaker_name") String speaker_name, @Field("session_id") String session_id);

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
                                                        @Field("selfie_id") String selfie_id);


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

    @POST("GenInfoWeather")
    @FormUrlEncoded
    Call<Weather> WeatherListFetch(@Field("api_access_token") String api_access_token,
                                   @Field("event_id") String event_id);


}
