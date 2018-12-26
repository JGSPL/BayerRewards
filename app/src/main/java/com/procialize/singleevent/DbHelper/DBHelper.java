package com.procialize.singleevent.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.procialize.singleevent.GetterSetter.AgendaList;
import com.procialize.singleevent.GetterSetter.AgendaMediaList;
import com.procialize.singleevent.GetterSetter.AgendaVacationList;
import com.procialize.singleevent.GetterSetter.AttendeeList;
import com.procialize.singleevent.GetterSetter.NewsFeedList;
import com.procialize.singleevent.GetterSetter.SpeakerList;
import com.procialize.singleevent.GetterSetter.UserData;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "ProcializeEventsDB.db";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    //Attendee Table
    public static final String ATTENDEES_TABLE_NAME = "ATTENDEES_TABLE_NAME";
    public static final String SPEAKER_TABLE_NAME = "SPEAKER_TABLE_NAME";
    //User Table
    public static final String USER_TABLE_NAME = "USER_TABLE_NAME";


    public static final String ATTENDEE_ID = "ATTENDEE_ID";
    public static final String ATTENDEE_API_ACCESS_TOKEN = "ATTENDEE_API_ACCESS_TOKEN";
    public static final String ATTENDEE_FIRST_NAME = "ATTENDEE_FIRST_NAME";
    public static final String ATTENDEE_LAST_NAME = "ATTENDEE_LAST_NAME";
    public static final String ATTENDEE_DESCRIPTION = "ATTENDEE_DESCRIPTION";
    public static final String ATTENDEE_CITY = "ATTENDEE_CITY";
    public static final String ATTENDEE_COUNTRY = "ATTENDEE_COUNTRY";
    public static final String ATTENDEE_PROFILE_PIC = "ATTENDEE_PROFILE_PIC";// PROFILE PIC
    public static final String ATTENDEE_MOBILE = "ATTENDEE_MOBILE";
    public static final String ATTENDEE_EMAIL = "ATTENDEE_EMAIL";
    public static final String ATTENDEE_COMPANY_NAME = "ATTENDEE_COMPANY_NAME";
    public static final String ATTENDEE_DESIGNATION = "ATTENDEE_DESIGNATION";
    public static final String ATTENDEE_TYPE = "ATTENDEE_TYPE";
    public static final String ATTENDEE_TOTAL_RATING = "ATTENDEE_TOTAL_RATING";
    public static final String ATTENDEE_AVG_RATING = "ATTENDEE_AVG_RATING";
    public static final String ATTENDEE_STAR = "ATTENDEE_STAR";

    //Agenda Table
    public static final String AGENDA_TABLE_NAME = "AGENDA_TABLE_NAME";

    public static final String SESSION_ID = "SESSION_ID";
    public static final String SESSION_NAME = "SESSION_NAME";
    public static final String SESSION_DESCRIPTION = "SESSION_DESCRIPTION";
    public static final String SESSION_START_TIME = "SESSION_START_TIME";
    public static final String SESSION_END_TIME = "SESSION_END_TIME";
    public static final String SESSION_DATE = "SESSION_DATE";
    public static final String SESSION_EVENT_ID = "SESSION_EVENT_ID";
    public static final String SESSION_STAR = "SESSION_STAR";
    public static final String SESSION_TOTAL_FEEDBACK = "SESSION_TOTAL_FEEDBACK";
    public static final String SESSION_FEEDBACK_COMMENT = "SESSION_FEEDBACK_COMMENT";
    public static final String SESSION_RATED = "SESSION_RATED";


    public static final String AGENDA_VACATION_TABLE_NAME = "AGENDA_VACATION_TABLE_NAME";

    public static final String SESSION_VACATION_ID = "SESSION_VACATION_ID";
    public static final String SESSION_VACATION_NAME = "SESSION_VACATION_NAME";
    public static final String SESSION_VACATION_DESCRIPTION = "SESSION_VACATION_DESCRIPTION";
    public static final String FOLDER_NAME = "FOLDER_NAME";
    public static final String SESSION_EVENT_VACATION_ID = "SESSION_EVENT_VACATION_ID";


    public static final String AGENDA_VACATION_MEDIA_TABLE = "AGENDA_VACATION_MEDIA_TABLE";

    public static final String SESSION_MEDIA_VACATION_ID = "SESSION_MEDIA_VACATION_ID";
    public static final String MEDIA_TYPE = "MEDIA_TYPE";
    public static final String MEDIA_NAME = "MEDIA_NAME";
    public static final String MEDIA_THUMBNAIL = "MEDIA_THUMBNAIL";


    // News Feed Table
    public static final String NEWSFEED_TABLE_NAME = "NEWSFEED_TABLE_NAME";

    public static final String NEWSFEED_ID = "NEWSFEED_ID";
    public static final String NEWSFEED_TYPE = "NEWSFEED_TYPE";
    public static final String NEWSFEED_MEDIAFILE = "NEWSFEED_MEDIAFILE";
    public static final String NEWSFEED_POST_STATUS = "NEWSFEED_POST_STATUS";
    public static final String NEWSFEED_THUMB_IMAGE = "NEWSFEED_THUMB_IMAGE";
    public static final String NEWSFEED_EVENTID = "NEWSFEED_EVENTID";
    public static final String NEWSFEED_POST_DATE = "NEWSFEED_POST_DATE";
    public static final String NEWSFEED_FIRST_NAME = "NEWSFEED_FIRST_NAME";
    public static final String NEWSFEED_LAST_NAME = "NEWSFEED_LAST_NAME";
    public static final String NEWSFEED_COMP_NAME = "NEWSFEED_COMP_NAME";
    public static final String NEWSFEED_DESIGNATION = "NEWSFEED_DESIGNATION";
    public static final String NEWSFEED_PROFILE_PIC = "NEWSFEED_PROFILE_PIC";
    public static final String NEWSFEED_ATTENDEE_ID = "NEWSFEED_ATTENDEE_ID";
    public static final String NEWSFEED_WIDTH = "NEWSFEED_WIDTH";
    public static final String NEWSFEED_HEIGHT = "NEWSFEED_HEIGHT";
    public static final String NEWSFEED_LIKE_FLAG = "NEWSFEED_LIKE_FLAG";
    public static final String NEWSFEED_TOTAL_LIKES = "NEWSFEED_TOTAL_LIKES";
    public static final String NEWSFEED_TOTAL_COMMENTS = "NEWSFEED_TOTAL_COMMENTS";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private static DBHelper sInstance;

    public static synchronized DBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("DB", "DB Created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Creating Attendees table
        db.execSQL("create table " + ATTENDEES_TABLE_NAME + "(" + ATTENDEE_ID
                + " text, " + ATTENDEE_API_ACCESS_TOKEN + " text, " + ATTENDEE_FIRST_NAME + " text, " + ATTENDEE_LAST_NAME
                + " text, " + ATTENDEE_DESCRIPTION + " text, " + ATTENDEE_CITY
                + " text, " + ATTENDEE_COUNTRY + " text, "
                + ATTENDEE_PROFILE_PIC + " text, " + ATTENDEE_MOBILE
                + " text, " + ATTENDEE_EMAIL + " text, "
                + ATTENDEE_COMPANY_NAME + " text, " + ATTENDEE_DESIGNATION
                + " text, " + ATTENDEE_TYPE + " text)");

        // Creating User data table
        db.execSQL("create table " + USER_TABLE_NAME + "(" + ATTENDEE_ID
                + " text, " + ATTENDEE_FIRST_NAME + " text, " + ATTENDEE_LAST_NAME + " text, " + ATTENDEE_API_ACCESS_TOKEN
                + " text, " + ATTENDEE_EMAIL + " text, " + ATTENDEE_COMPANY_NAME
                + " text, " + ATTENDEE_DESIGNATION + " text, "
                + ATTENDEE_DESCRIPTION + " text, " + ATTENDEE_CITY
                + " text, " + ATTENDEE_COUNTRY + " text, "
                + ATTENDEE_PROFILE_PIC + " text, " + ATTENDEE_MOBILE + " text)");

        // Creating Agenda table
        db.execSQL("create table " + AGENDA_TABLE_NAME + "(" + SESSION_ID
                + " text, " + SESSION_NAME + " text, " + SESSION_DESCRIPTION + " text, " + SESSION_START_TIME
                + " text, " + SESSION_END_TIME + " text, " + SESSION_DATE
                + " text, " + SESSION_EVENT_ID + " text, "
                + SESSION_STAR + " text, " + SESSION_TOTAL_FEEDBACK
                + " text, " + SESSION_FEEDBACK_COMMENT + " text, "
                + SESSION_RATED + " text)");

        //Creating Agenda Vacation table
        db.execSQL("create table " + AGENDA_VACATION_TABLE_NAME + "(" + SESSION_VACATION_ID
                + " text, " + SESSION_VACATION_NAME + " text, " + SESSION_VACATION_DESCRIPTION + " text, " + FOLDER_NAME
                + " text, " + SESSION_EVENT_VACATION_ID + " text)");

        //create Agenda Media table
        db.execSQL("create table " + AGENDA_VACATION_MEDIA_TABLE + "(" + SESSION_MEDIA_VACATION_ID
                + " text, " + MEDIA_NAME + " text, " + MEDIA_TYPE + " text, " + MEDIA_THUMBNAIL
                + " text)");

        // Creating Speaker table
        db.execSQL("create table " + SPEAKER_TABLE_NAME + "(" + ATTENDEE_ID
                + " text, " + ATTENDEE_API_ACCESS_TOKEN + " text, " + ATTENDEE_FIRST_NAME + " text, " + ATTENDEE_LAST_NAME
                + " text, " + ATTENDEE_DESCRIPTION + " text, " + ATTENDEE_CITY
                + " text, " + ATTENDEE_COUNTRY + " text, "
                + ATTENDEE_PROFILE_PIC + " text, " + ATTENDEE_MOBILE
                + " text, " + ATTENDEE_EMAIL + " text, "
                + ATTENDEE_COMPANY_NAME + " text, " + ATTENDEE_DESIGNATION
                + " text, "
                + ATTENDEE_TYPE + " text, "
                + ATTENDEE_TOTAL_RATING + " text, "
                + ATTENDEE_AVG_RATING + " text, "
                + ATTENDEE_STAR + " text)");

        //Creating News Feed table

        db.execSQL("create table " + NEWSFEED_TABLE_NAME + "(" + NEWSFEED_ID
                + " text, " + NEWSFEED_TYPE + " text, " + NEWSFEED_MEDIAFILE + " text, " + NEWSFEED_POST_STATUS
                + " text, " + NEWSFEED_THUMB_IMAGE + " text, " + NEWSFEED_EVENTID
                + " text, " + NEWSFEED_POST_DATE + " text, "
                + NEWSFEED_FIRST_NAME + " text, " + NEWSFEED_LAST_NAME
                + " text, " + NEWSFEED_COMP_NAME + " text, "
                + NEWSFEED_DESIGNATION + " text, " + NEWSFEED_PROFILE_PIC
                + " text, "
                + NEWSFEED_ATTENDEE_ID + " text, "
                + NEWSFEED_WIDTH + " text, "
                + NEWSFEED_HEIGHT + " text, "
                + NEWSFEED_LIKE_FLAG + " text, "
                + NEWSFEED_TOTAL_LIKES + " text, "
                + NEWSFEED_TOTAL_COMMENTS + " text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DELETE FROM " + ATTENDEES_TABLE_NAME);
            db.execSQL("DELETE FROM " + SPEAKER_TABLE_NAME);
            db.execSQL("DELETE FROM " + AGENDA_TABLE_NAME);
            db.execSQL("DELETE FROM " + AGENDA_VACATION_TABLE_NAME);
            db.execSQL("DELETE FROM " + AGENDA_VACATION_MEDIA_TABLE);
            db.execSQL("DELETE FROM " + NEWSFEED_TABLE_NAME);

            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    //Insert value in User Table
    public void insertUserDataInfo(List<UserData> UsersList,
                                   SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < UsersList.size(); i++) {
                contentValues = new ContentValues();

                String attendee_id = UsersList.get(i).getAttendeeId();
                if (attendee_id != null && attendee_id.length() > 0) {
                    contentValues.put(ATTENDEE_ID, attendee_id);
                }

                String api_access_token = UsersList.get(i).getApiAccessToken();
                if (api_access_token != null && api_access_token.length() > 0) {
                    contentValues.put(ATTENDEE_FIRST_NAME, api_access_token);
                }

                String first_name = UsersList.get(i).getFirstName();
                if (first_name != null && first_name.length() > 0) {
                    contentValues.put(ATTENDEE_LAST_NAME, first_name);
                }

                String last_name = UsersList.get(i).getLastName();
                if (last_name != null && last_name.length() > 0) {
                    contentValues.put(ATTENDEE_API_ACCESS_TOKEN, last_name);
                }

                String attendee_description = UsersList.get(i).getDescription();
                if (attendee_description != null
                        && attendee_description.length() > 0) {
                    contentValues.put(ATTENDEE_EMAIL,
                            attendee_description);
                }

                String attendee_city = UsersList.get(i).getCity();
                if (attendee_city != null && attendee_city.length() > 0) {
                    contentValues.put(ATTENDEE_COMPANY_NAME, attendee_city);
                }

                String attendee_country = UsersList.get(i).getCountry();
                if (attendee_country != null
                        && attendee_country.length() > 0) {
                    contentValues.put(ATTENDEE_DESIGNATION, attendee_country);
                }

                String profile_pic = UsersList.get(i).getProfilePic();
                if (profile_pic != null && profile_pic.length() > 0) {
                    contentValues.put(ATTENDEE_DESCRIPTION, profile_pic);
                }

                String mobile = UsersList.get(i).getMobile();
                if (mobile != null && mobile.length() > 0) {
                    contentValues.put(ATTENDEE_CITY, mobile);
                }

                String email = UsersList.get(i).getEmail();
                if (email != null && email.length() > 0) {
                    contentValues.put(ATTENDEE_COUNTRY, email);
                }

                String attendee_company = UsersList.get(i).getCompanyName();
                if (attendee_company != null
                        && attendee_company.length() > 0) {
                    contentValues.put(ATTENDEE_PROFILE_PIC,
                            attendee_company);
                }

                String attendee_designation = UsersList.get(i)
                        .getDesignation();
                if (attendee_designation != null
                        && attendee_designation.length() > 0) {
                    contentValues.put(ATTENDEE_MOBILE,
                            attendee_designation);
                }
                db.insert(USER_TABLE_NAME, null, contentValues);

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }


    // Insert Values in Attendee Table
    public void insertAttendeesInfo(List<AttendeeList> attendeesList,
                                    SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < attendeesList.size(); i++) {
                contentValues = new ContentValues();

                if (attendeesList.get(i).getAttendeeType()
                        .equalsIgnoreCase("A")) {

                    String attendee_id = attendeesList.get(i).getAttendeeId();
                    if (attendee_id != null && attendee_id.length() > 0) {
                        contentValues.put(ATTENDEE_ID, attendee_id);
                    }

                    String api_access_token = attendeesList.get(i).getApiAccessToken();
                    if (api_access_token != null && api_access_token.length() > 0) {
                        contentValues.put(ATTENDEE_API_ACCESS_TOKEN, api_access_token);
                    }

                    String first_name = attendeesList.get(i).getFirstName();
                    if (first_name != null && first_name.length() > 0) {
                        contentValues.put(ATTENDEE_FIRST_NAME, first_name);
                    }

                    String last_name = attendeesList.get(i).getLastName();
                    if (last_name != null && last_name.length() > 0) {
                        contentValues.put(ATTENDEE_LAST_NAME, last_name);
                    }

                    String attendee_description = attendeesList.get(i).getDescription();
                    if (attendee_description != null
                            && attendee_description.length() > 0) {
                        contentValues.put(ATTENDEE_DESCRIPTION,
                                attendee_description);
                    }

                    String attendee_city = attendeesList.get(i).getCity();
                    if (attendee_city != null && attendee_city.length() > 0) {
                        contentValues.put(ATTENDEE_CITY, attendee_city);
                    }

                    String attendee_country = attendeesList.get(i).getCountry();
                    if (attendee_country != null
                            && attendee_country.length() > 0) {
                        contentValues.put(ATTENDEE_COUNTRY, attendee_country);
                    }

                    String profile_pic = attendeesList.get(i).getProfilePic();
                    if (profile_pic != null && profile_pic.length() > 0) {
                        contentValues.put(ATTENDEE_PROFILE_PIC, profile_pic);
                    }

                    String mobile = attendeesList.get(i).getMobile();
                    if (mobile != null && mobile.length() > 0) {
                        contentValues.put(ATTENDEE_MOBILE, mobile);
                    }

                    String email = attendeesList.get(i).getEmail();
                    if (email != null && email.length() > 0) {
                        contentValues.put(ATTENDEE_EMAIL, email);
                    }

                    String attendee_company = attendeesList.get(i).getCompanyName();
                    if (attendee_company != null
                            && attendee_company.length() > 0) {
                        contentValues.put(ATTENDEE_COMPANY_NAME,
                                attendee_company);
                    }

                    String attendee_designation = attendeesList.get(i)
                            .getDesignation();
                    if (attendee_designation != null
                            && attendee_designation.length() > 0) {
                        contentValues.put(ATTENDEE_DESIGNATION,
                                attendee_designation);
                    }

                    String attendee_type = attendeesList.get(i).getAttendeeType();
                    if (attendee_type != null && attendee_type.length() > 0) {
                        contentValues.put(ATTENDEE_TYPE, attendee_type);
                    }

                    db.insert(ATTENDEES_TABLE_NAME, null, contentValues);

                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }


    //Insert value in Speaker Table
    public void insertSpeakersInfo(List<SpeakerList> speakersList,
                                   SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < speakersList.size(); i++) {
                contentValues = new ContentValues();

                if (speakersList.get(i).getAttendeeType()
                        .equalsIgnoreCase("S")) {

                    String attendee_id = speakersList.get(i).getAttendeeId();
                    if (attendee_id != null && attendee_id.length() > 0) {
                        contentValues.put(ATTENDEE_ID, attendee_id);
                    }

                    String api_access_token = speakersList.get(i).getApiAccessToken();
                    if (api_access_token != null && api_access_token.length() > 0) {
                        contentValues.put(ATTENDEE_API_ACCESS_TOKEN, api_access_token);
                    }

                    String first_name = speakersList.get(i).getFirstName();
                    if (first_name != null && first_name.length() > 0) {
                        contentValues.put(ATTENDEE_FIRST_NAME, first_name);
                    }

                    String last_name = speakersList.get(i).getLastName();
                    if (last_name != null && last_name.length() > 0) {
                        contentValues.put(ATTENDEE_LAST_NAME, last_name);
                    }

                    String attendee_description = speakersList.get(i).getDescription();
                    if (attendee_description != null
                            && attendee_description.length() > 0) {
                        contentValues.put(ATTENDEE_DESCRIPTION,
                                attendee_description);
                    }

                    String attendee_city = speakersList.get(i).getCity();
                    if (attendee_city != null && attendee_city.length() > 0) {
                        contentValues.put(ATTENDEE_CITY, attendee_city);
                    }

                    String attendee_country = speakersList.get(i).getCountry();
                    if (attendee_country != null
                            && attendee_country.length() > 0) {
                        contentValues.put(ATTENDEE_COUNTRY, attendee_country);
                    }

                    String profile_pic = speakersList.get(i).getProfilePic();
                    if (profile_pic != null && profile_pic.length() > 0) {
                        contentValues.put(ATTENDEE_PROFILE_PIC, profile_pic);
                    }

                    String mobile = speakersList.get(i).getMobileNumber();
                    if (mobile != null && mobile.length() > 0) {
                        contentValues.put(ATTENDEE_MOBILE, mobile);
                    }

                    String email = speakersList.get(i).getEmail();
                    if (email != null && email.length() > 0) {
                        contentValues.put(ATTENDEE_EMAIL, email);
                    }

                    String attendee_company = speakersList.get(i).getCompany();
                    if (attendee_company != null
                            && attendee_company.length() > 0) {
                        contentValues.put(ATTENDEE_COMPANY_NAME,
                                attendee_company);
                    }

                    String attendee_designation = speakersList.get(i)
                            .getDesignation();
                    if (attendee_designation != null
                            && attendee_designation.length() > 0) {
                        contentValues.put(ATTENDEE_DESIGNATION,
                                attendee_designation);
                    }

                    String attendee_type = speakersList.get(i).getAttendeeType();
                    if (attendee_type != null && attendee_type.length() > 0) {
                        contentValues.put(ATTENDEE_TYPE, attendee_type);

                    }
                    String attendee_total_rating = speakersList.get(i).getTotalRating();
                    if (attendee_total_rating != null && attendee_total_rating.length() > 0) {
                        contentValues.put(ATTENDEE_TOTAL_RATING, attendee_total_rating);
                    }

                    String attendee_avg_rating = speakersList.get(i).getAvgRating();
                    if (attendee_avg_rating != null && attendee_avg_rating.length() > 0) {
                        contentValues.put(ATTENDEE_AVG_RATING, attendee_avg_rating);
                    }

                    String attendee_star = speakersList.get(i).getStar();
                    if (attendee_star != null && attendee_star.length() > 0) {
                        contentValues.put(ATTENDEE_STAR, attendee_star);
                    }


                    db.insert(SPEAKER_TABLE_NAME, null, contentValues);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }


    //Insert Newsfeed data in table
    public void insertNEwsFeedInfo(List<NewsFeedList> newsfeedsList,
                                   SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < newsfeedsList.size(); i++) {
                contentValues = new ContentValues();

                String newsFeedId = newsfeedsList.get(i).getNewsFeedId();
                if (newsFeedId != null && newsFeedId.length() > 0) {
                    contentValues.put(NEWSFEED_ID, newsFeedId);
                }

                String type = newsfeedsList.get(i).getType();
                if (type != null && type.length() > 0) {
                    contentValues.put(NEWSFEED_TYPE, type);
                }

                String mediaFile = newsfeedsList.get(i).getMediaFile();
                if (mediaFile != null && mediaFile.length() > 0) {
                    contentValues.put(NEWSFEED_MEDIAFILE, mediaFile);
                }

                String postStatus = newsfeedsList.get(i).getPostStatus();
                if (postStatus != null && postStatus.length() > 0) {
                    contentValues.put(NEWSFEED_POST_STATUS, postStatus);
                }

                String thumbImage = newsfeedsList.get(i).getThumbImage();
                if (thumbImage != null
                        && thumbImage.length() > 0) {
                    contentValues.put(NEWSFEED_THUMB_IMAGE,
                            thumbImage);
                }

                String eventId = newsfeedsList.get(i).getEventId();
                if (eventId != null
                        && eventId.length() > 0) {
                    contentValues.put(NEWSFEED_EVENTID, eventId);
                }
                String postDate = newsfeedsList.get(i).getPostDate();
                if (postDate != null && postDate.length() > 0) {
                    contentValues.put(NEWSFEED_POST_DATE, postDate);
                }

                String firstName = newsfeedsList.get(i).getFirstName();
                if (firstName != null && firstName.length() > 0) {
                    contentValues.put(NEWSFEED_FIRST_NAME, firstName);
                }

                String lastName = newsfeedsList.get(i).getLastName();
                if (lastName != null && lastName.length() > 0) {
                    contentValues.put(NEWSFEED_LAST_NAME, lastName);
                }

                String companyName = newsfeedsList.get(i).getCompanyName();
                if (companyName != null && companyName.length() > 0) {
                    contentValues.put(NEWSFEED_COMP_NAME, companyName);
                }

                String designation = newsfeedsList.get(i).getDesignation();
                if (designation != null
                        && designation.length() > 0) {
                    contentValues.put(NEWSFEED_DESIGNATION,
                            designation);
                }
                String profilePic = newsfeedsList.get(i).getProfilePic();
                if (profilePic != null && profilePic.length() > 0) {
                    contentValues.put(NEWSFEED_PROFILE_PIC, profilePic);

                }
                String attendeeId = newsfeedsList.get(i)
                        .getAttendeeId();
                if (attendeeId != null
                        && attendeeId.length() > 0) {
                    contentValues.put(NEWSFEED_ATTENDEE_ID,
                            attendeeId);
                }

                String width = newsfeedsList.get(i).getWidth();
                if (width != null && width.length() > 0) {
                    contentValues.put(NEWSFEED_WIDTH, width);
                }

                String height = newsfeedsList.get(i).getHeight();
                if (height != null && height.length() > 0) {
                    contentValues.put(NEWSFEED_HEIGHT, height);
                }

                String likeFlag = newsfeedsList.get(i).getLikeFlag();
                if (likeFlag != null && likeFlag.length() > 0) {
                    contentValues.put(NEWSFEED_LIKE_FLAG, likeFlag);
                }
                String totalLikes = newsfeedsList.get(i).getTotalLikes();
                if (totalLikes != null && totalLikes.length() > 0) {
                    contentValues.put(NEWSFEED_TOTAL_LIKES, totalLikes);
                }
                String totalComments = newsfeedsList.get(i).getTotalComments();
                if (totalComments != null && totalComments.length() > 0) {
                    contentValues.put(NEWSFEED_TOTAL_COMMENTS, totalComments);
                }

                db.insert(NEWSFEED_TABLE_NAME, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }


    // Insert Values in Agenda Table
    public void insertAgendaInfo(List<AgendaList> agendasList,
                                 SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getSessionId();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(SESSION_ID, session_id);
                }

                String session_name = agendasList.get(i).getSessionName();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(SESSION_NAME, session_name);
                }

                String session_description = agendasList.get(i).getSessionDescription();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(SESSION_DESCRIPTION, session_description);
                }

                String session_start_time = agendasList.get(i).getSessionStartTime();
                if (session_start_time != null && session_start_time.length() > 0) {
                    contentValues.put(SESSION_START_TIME, session_start_time);
                }

                String session_end_time = agendasList.get(i).getSessionEndTime();
                if (session_end_time != null
                        && session_end_time.length() > 0) {
                    contentValues.put(SESSION_END_TIME,
                            session_end_time);
                }

                String session_date = agendasList.get(i).getSessionDate();
                if (session_date != null && session_date.length() > 0) {
                    contentValues.put(SESSION_DATE, session_date);
                }

                String event_id = agendasList.get(i).getEventId();
                if (event_id != null
                        && event_id.length() > 0) {
                    contentValues.put(SESSION_EVENT_ID, event_id);
                }

                String star = agendasList.get(i).getStar();
                if (star != null) {
                    contentValues.put(SESSION_STAR, star);
                }

                String totalFeedback = agendasList.get(i).getTotalFeedback();
                if (totalFeedback != null && totalFeedback.length() > 0) {
                    contentValues.put(SESSION_TOTAL_FEEDBACK, totalFeedback);
                }

                String feedbackComment = agendasList.get(i).getFeedbackComment();
                if (feedbackComment != null && feedbackComment.length() > 0) {
                    contentValues.put(SESSION_FEEDBACK_COMMENT, feedbackComment);
                }

                db.insert(AGENDA_TABLE_NAME, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }


    public void insertAgendaVacationInfo(List<AgendaVacationList> agendasList,
                                         SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getSessionId();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(SESSION_VACATION_ID, session_id);
                }

                String session_name = agendasList.get(i).getSession_name();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(SESSION_VACATION_NAME, session_name);
                }

                String session_description = agendasList.get(i).getSession_description();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(SESSION_VACATION_DESCRIPTION, session_description);
                }

                String folder_name = agendasList.get(i).getFolder_name();
                if (folder_name != null && folder_name.length() > 0) {
                    contentValues.put(FOLDER_NAME, folder_name);
                }


                String event_id = agendasList.get(i).getEvent_id();
                if (event_id != null
                        && event_id.length() > 0) {
                    contentValues.put(SESSION_EVENT_VACATION_ID, event_id);
                }


                db.insert(AGENDA_VACATION_TABLE_NAME, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    //get Agenda Media Info

    public void insertAgendaMediaInfo(List<AgendaMediaList> agendasList,
                                         SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getSession_vacation_id();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(SESSION_MEDIA_VACATION_ID, session_id);
                }

                String session_name = agendasList.get(i).getMedia_name();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(MEDIA_NAME, session_name);
                }

                String session_description = agendasList.get(i).getMedia_type();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(MEDIA_TYPE, session_description);
                }

                String folder_name = agendasList.get(i).getMedia_thumbnail();
                if (folder_name != null && folder_name.length() > 0) {
                    contentValues.put(MEDIA_THUMBNAIL, folder_name);
                }





                db.insert(AGENDA_VACATION_MEDIA_TABLE, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    //get Agenda vacation detail

    public List<AgendaVacationList> getAgendaFolderList() {
        String selectQuery = "select * from " + AGENDA_VACATION_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<AgendaVacationList> questionList = new ArrayList<AgendaVacationList>();

        if (cursor.moveToFirst()) {
            do {

                AgendaVacationList agendaQuestions = new AgendaVacationList();
                agendaQuestions.setSessionId(cursor.getString(0));
                agendaQuestions.setSession_name(cursor.getString(1));
                agendaQuestions.setSession_description(cursor.getString(2));
                agendaQuestions.setFolder_name(cursor.getString(3));
                agendaQuestions.setEvent_id(cursor.getString(4));


                questionList.add(agendaQuestions);
            } while (cursor.moveToNext());
        }
        db.close();
        return questionList;
    }

    //get Media detail

    public List<AgendaMediaList> getAgendaMediaList() {
        String selectQuery = "select * from " + AGENDA_VACATION_MEDIA_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<AgendaMediaList> questionList = new ArrayList<AgendaMediaList>();

        if (cursor.moveToFirst()) {
            do {

                AgendaMediaList agendaQuestions = new AgendaMediaList();
                agendaQuestions.setSession_vacation_id(cursor.getString(0));
                agendaQuestions.setMedia_name(cursor.getString(1));
                agendaQuestions.setMedia_type(cursor.getString(2));
                agendaQuestions.setMedia_thumbnail(cursor.getString(3));

                questionList.add(agendaQuestions);
            } while (cursor.moveToNext());
        }
        db.close();
        return questionList;
    }
    // Get Attendee List/ Details
    public List<AttendeeList> getAttendeeDetails() {
        String selectQuery = "select * from " + ATTENDEES_TABLE_NAME
                + " where " + ATTENDEE_TYPE + " =\'A\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<AttendeeList> attendeeList = new ArrayList<AttendeeList>();
        if (cursor.moveToFirst()) {

            do {
                AttendeeList attendeesList = new AttendeeList();
                attendeesList.setAttendeeId(cursor.getString(0));
                attendeesList.setApiAccessToken(cursor.getString(1));
                attendeesList.setFirstName(cursor.getString(2));
                attendeesList.setLastName(cursor.getString(3));
                attendeesList.setDescription(cursor.getString(4));
                attendeesList.setCity(cursor.getString(5));
                attendeesList.setCountry(cursor.getString(6));
                attendeesList.setProfilePic(cursor.getString(7));
                attendeesList.setMobile(cursor.getString(8));
                attendeesList.setEmail(cursor.getString(9));
                attendeesList.setCompanyName(cursor.getString(10));
                attendeesList.setDesignation(cursor.getString(11));
                attendeesList.setAttendeeType(cursor.getString(12));

                attendeeList.add(attendeesList);
            } while (cursor.moveToNext());
        }
        db.close();
        return attendeeList;
    }

    public List<AttendeeList> getAttendeeDetailsId(String att_id) {
/*
        String selectQuery = "select * from " + ATTENDEES_TABLE_NAME
                + " where " + ATTENDEE_TYPE + " =\'A\'";
*/

        String selectQuery = "select * from " + ATTENDEES_TABLE_NAME + " where " + ATTENDEE_ID + " LIKE \'%" + att_id + "%\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<AttendeeList> attendeeList = new ArrayList<AttendeeList>();
        if (cursor.moveToFirst()) {

            do {
                AttendeeList attendeesList = new AttendeeList();
                attendeesList.setAttendeeId(cursor.getString(0));
                attendeesList.setApiAccessToken(cursor.getString(1));
                attendeesList.setFirstName(cursor.getString(2));
                attendeesList.setLastName(cursor.getString(3));
                attendeesList.setDescription(cursor.getString(4));
                attendeesList.setCity(cursor.getString(5));
                attendeesList.setCountry(cursor.getString(6));
                attendeesList.setProfilePic(cursor.getString(7));
                attendeesList.setMobile(cursor.getString(8));
                attendeesList.setEmail(cursor.getString(9));
                attendeesList.setCompanyName(cursor.getString(10));
                attendeesList.setDesignation(cursor.getString(11));
                attendeesList.setAttendeeType(cursor.getString(12));

                attendeeList.add(attendeesList);
            } while (cursor.moveToNext());
        }
        db.close();
        return attendeeList;
    }


    //Get User Data
    public UserData getUserDetails() {
        String selectQuery = "select * from " + USER_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        UserData userdataList = new UserData();
        if (cursor.moveToFirst()) {

            UserData userdatasList = new UserData();
            userdatasList.setAttendeeId(cursor.getString(0));
            userdatasList.setFirstName(cursor.getString(1));
            userdatasList.setLastName(cursor.getString(2));
            userdatasList.setApiAccessToken(cursor.getString(3));
            userdatasList.setEmail(cursor.getString(4));
            userdatasList.setCompanyName(cursor.getString(5));
            userdatasList.setDesignation(cursor.getString(6));
            userdatasList.setDescription(cursor.getString(7));
            userdatasList.setCity(cursor.getString(8));
            userdatasList.setCountry(cursor.getString(9));
            userdatasList.setProfilePic(cursor.getString(10));
            userdatasList.setMobile(cursor.getString(11));
        }

        db.close();
        return userdataList;
    }


    // Get Speaker List/ Details
    public List<SpeakerList> getSpeakerDetails() {
        String selectQuery = "select * from " + SPEAKER_TABLE_NAME
                + " where " + ATTENDEE_TYPE + " =\'S\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<SpeakerList> speakerList = new ArrayList<SpeakerList>();
        if (cursor.moveToFirst()) {

            do {
                SpeakerList speakersList = new SpeakerList();
                speakersList.setAttendeeId(cursor.getString(0));
                speakersList.setApiAccessToken(cursor.getString(1));
                speakersList.setFirstName(cursor.getString(2));
                speakersList.setLastName(cursor.getString(3));
                speakersList.setDescription(cursor.getString(4));
                speakersList.setCity(cursor.getString(5));
                speakersList.setCountry(cursor.getString(6));
                speakersList.setProfilePic(cursor.getString(7));
                speakersList.setMobileNumber(cursor.getString(8));
                speakersList.setEmail(cursor.getString(9));
                speakersList.setCompany(cursor.getString(10));
                speakersList.setDesignation(cursor.getString(11));
                speakersList.setAttendeeType(cursor.getString(12));
                speakersList.setTotalRating(cursor.getString(13));
                speakersList.setAvgRating(cursor.getString(14));
                speakersList.setStar(cursor.getString(15));


                speakerList.add(speakersList);
            } while (cursor.moveToNext());
        }
        db.close();
        return speakerList;
    }


    // Get Agenda List/ Details
    public List<AgendaList> getAgendaDetails() {
        String selectQuery = "select * from " + AGENDA_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<AgendaList> agendaList = new ArrayList<AgendaList>();
        if (cursor.moveToFirst()) {

            do {
                AgendaList agendasList = new AgendaList();
                agendasList.setSessionId(cursor.getString(0));
                agendasList.setSessionName(cursor.getString(1));
                agendasList.setSessionDescription(cursor.getString(2));
                agendasList.setSessionStartTime(cursor.getString(3));
                agendasList.setSessionEndTime(cursor.getString(4));
                agendasList.setSessionDate(cursor.getString(5));
                agendasList.setEventId(cursor.getString(6));
                agendasList.setStar(cursor.getString(7));
                agendasList.setTotalFeedback(cursor.getString(8));
                agendasList.setFeedbackComment(cursor.getString(9));
                agendasList.setRated(cursor.getString(10));

                agendaList.add(agendasList);
            } while (cursor.moveToNext());
        }
        db.close();
        return agendaList;
    }

    //Get NewsFeed List
    public List<NewsFeedList> getNewsFeedDetails() {
        String selectQuery = "select * from " + NEWSFEED_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<NewsFeedList> newsFeedList = new ArrayList<NewsFeedList>();
        if (cursor.moveToFirst()) {

            do {
                NewsFeedList newsfeedsList = new NewsFeedList();
                newsfeedsList.setNewsFeedId(cursor.getString(0));
                newsfeedsList.setType(cursor.getString(1));
                newsfeedsList.setMediaFile(cursor.getString(2));
                newsfeedsList.setPostStatus(cursor.getString(3));
                newsfeedsList.setThumbImage(cursor.getString(4));
                newsfeedsList.setEventId(cursor.getString(5));
                newsfeedsList.setPostDate(cursor.getString(6));
                newsfeedsList.setFirstName(cursor.getString(7));
                newsfeedsList.setLastName(cursor.getString(8));
                newsfeedsList.setCompanyName(cursor.getString(9));
                newsfeedsList.setDesignation(cursor.getString(10));
                newsfeedsList.setProfilePic(cursor.getString(11));
                newsfeedsList.setAttendeeId(cursor.getString(12));
                newsfeedsList.setWidth(cursor.getString(13));
                newsfeedsList.setHeight(cursor.getString(14));
                newsfeedsList.setLikeFlag(cursor.getString(15));
                newsfeedsList.setTotalLikes(cursor.getString(16));
                newsfeedsList.setTotalComments(cursor.getString(17));

                newsFeedList.add(newsfeedsList);
            } while (cursor.moveToNext());
        }
        db.close();
        return newsFeedList;
    }

    public List<NewsFeedList> getNewsFeedLikeandComment(String feedid) {
        String selectQuery = "select * from " + NEWSFEED_TABLE_NAME + " where " + NEWSFEED_ID + " LIKE \'%" + feedid + "%\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<NewsFeedList> newsFeedList = new ArrayList<NewsFeedList>();
        if (cursor.moveToFirst()) {

            do {
                NewsFeedList newsfeedsList = new NewsFeedList();
                newsfeedsList.setNewsFeedId(cursor.getString(0));
                newsfeedsList.setType(cursor.getString(1));
                newsfeedsList.setMediaFile(cursor.getString(2));
                newsfeedsList.setPostStatus(cursor.getString(3));
                newsfeedsList.setThumbImage(cursor.getString(4));
                newsfeedsList.setEventId(cursor.getString(5));
                newsfeedsList.setPostDate(cursor.getString(6));
                newsfeedsList.setFirstName(cursor.getString(7));
                newsfeedsList.setLastName(cursor.getString(8));
                newsfeedsList.setCompanyName(cursor.getString(9));
                newsfeedsList.setDesignation(cursor.getString(10));
                newsfeedsList.setProfilePic(cursor.getString(11));
                newsfeedsList.setAttendeeId(cursor.getString(12));
                newsfeedsList.setWidth(cursor.getString(13));
                newsfeedsList.setHeight(cursor.getString(14));
                newsfeedsList.setLikeFlag(cursor.getString(15));
                newsfeedsList.setTotalLikes(cursor.getString(16));
                newsfeedsList.setTotalComments(cursor.getString(17));

                newsFeedList.add(newsfeedsList);
            } while (cursor.moveToNext());
        }
        db.close();
        return newsFeedList;
    }

    public void clearAttendeesTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + ATTENDEES_TABLE_NAME);
    }

    public void clearAgendaTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + AGENDA_TABLE_NAME);
    }

    public void clearAgendaVacationTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + AGENDA_VACATION_TABLE_NAME);
    }

    public void clearAgendaFolerTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + AGENDA_VACATION_MEDIA_TABLE);
    }

    public void clearSpeakersTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + SPEAKER_TABLE_NAME);
    }

    public void clearNewsFeedTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + NEWSFEED_TABLE_NAME);
    }

    public void clearUserDataTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + USER_TABLE_NAME);
    }

}