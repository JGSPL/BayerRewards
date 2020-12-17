package com.bayer.bayerreward.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bayer.bayerreward.GetterSetter.AgendaList;
import com.bayer.bayerreward.GetterSetter.AgendaMediaList;
import com.bayer.bayerreward.GetterSetter.AgendaVacationList;
import com.bayer.bayerreward.GetterSetter.AttendeeList;
import com.bayer.bayerreward.GetterSetter.NewsFeedList;
import com.bayer.bayerreward.GetterSetter.Quiz;
import com.bayer.bayerreward.GetterSetter.SpeakerList;
import com.bayer.bayerreward.GetterSetter.UserData;
import com.bayer.bayerreward.GetterSetter.account_history_data;
import com.bayer.bayerreward.GetterSetter.business_unit_data;
import com.bayer.bayerreward.GetterSetter.region_data;
import com.bayer.bayerreward.GetterSetter.territory_data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "ProcializeEventsDB.db";
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
    public static final String ATTENDEE_STATUS = "ATTENDEE_STATUS";
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
    public static final String NEWSFEED_ATTENDEE_TYPE = "NEWSFEED_ATTENDEE_TYPE";

    public static final String QUIZ_TABLE = "QUIZ_TABLE";

    public static final String QUIZ_ID = "QUIZ_ID";
    public static final String QUESTION = "NEWSFEED_TYPE";
    public static final String CORRECTANSWERID = "CORRECTANSWERID";
    public static final String FOLDERID = "FOLDERID";
    public static final String FOLDERNAME = "FOLDERNAME";
    public static final String REPLIED = "REPLIED";
    public static final String SELECTED_OPTION = "SELECTED_OPTION";

    public static final String ACCOUNT_HISTORY = "ACCOUNT_HISTORY";
    public static final String TRANSACTIONDATE = "TRANSACTIONDATE";
    public static final String MPOINTSVALUE = "MPOINTSVALUE";
    public static final String PRODUCTNAME = "PRODUCTNAME";
    public static final String PACK = "PACK";

    public static final String TERRITORY_TABLE = "TERRITORY_TABLE";

    public static final String TFIRST_NAME = "TFIRST_NAME";
    public static final String TLAST_NAME = "TLAST_NAME";
    public static final String TPROFILE_PIC = "TPROFILE_PIC";
    public static final String TBUSINESS_UNIT = "TBUSINESS_UNIT";
    public static final String TREGION = "TREGION";
    public static final String TAVAILABLE_POINTS = "TAVAILABLE_POINTS";
    public static final String TSHOP_NAME = "TSHOP_NAME";
    public static final String TTOTAL_POINTS = "TTOTAL_POINTS";
    public static final String TURI_ID = "TURI_ID";
    public static final String TTERRITORY = "TTERRITORY";

    public static final String REGION_TABLE = "REGION_TABLE";

    public static final String RFIRST_NAME = "RFIRST_NAME";
    public static final String RLAST_NAME = "RLAST_NAME";
    public static final String RPROFILE_PIC = "RPROFILE_PIC";
    public static final String RBUSINESS_UNIT = "RBUSINESS_UNIT";
    public static final String RREGION = "RREGION";
    public static final String RAVAILABLE_POINTS = "RAVAILABLE_POINTS";
    public static final String RSHOP_NAME = "RSHOP_NAME";

    public static final String RTOTAL_POINTS = "RTOTAL_POINTS";
    public static final String RURI_ID = "RURI_ID";
    public static final String RTERRITORY = "RTERRITORY";

    public static final String BUSINESS_UNIT_TABLE = "BUSINESS_UNIT_TABLE";

    public static final String BFIRST_NAME = "BFIRST_NAME";
    public static final String BLAST_NAME = "BLAST_NAME";
    public static final String BPROFILE_PIC = "BPROFILE_PIC";
    public static final String BBUSINESS_UNIT = "BBUSINESS_UNIT";
    public static final String BREGION = "BREGION";
    public static final String BAVAILABLE_POINTS = "BAVAILABLE_POINTS";
    public static final String BSHOP_NAME = "BSHOP_NAME";

    public static final String BTOTAL_POINTS = "BTOTAL_POINTS";
    public static final String BURI_ID = "BURI_ID";
    public static final String BTERRITORY = "BTERRITORY";

    // Database Version
    private static final int DATABASE_VERSION = 2;
    private static DBHelper sInstance;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("DB", "DB Created");
    }

    public static synchronized DBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
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
                + ATTENDEE_PROFILE_PIC + " text, " + ATTENDEE_MOBILE + " text," + ATTENDEE_STATUS + "text)");

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
                + NEWSFEED_TOTAL_COMMENTS + " text,"
                + NEWSFEED_ATTENDEE_TYPE + " text)");

        db.execSQL("create table " + QUIZ_TABLE + "(" + QUIZ_ID
                + " text, " + QUESTION + " text, " + CORRECTANSWERID + " text, " + FOLDERNAME + " text, " + REPLIED + " text, " + SELECTED_OPTION
                + " text)");

        db.execSQL("create table " + ACCOUNT_HISTORY + "(" + TRANSACTIONDATE
                + " text, " + MPOINTSVALUE + " text, " + PRODUCTNAME + " text, " + PACK + " text)");


        db.execSQL("create table " + TERRITORY_TABLE + "(" + TFIRST_NAME
                + " text, " + TLAST_NAME + " text, " + TPROFILE_PIC + " text, " + TBUSINESS_UNIT + " text, "
                + TREGION + " text, " + TAVAILABLE_POINTS + " text, " + TSHOP_NAME + " text, "
                + TTOTAL_POINTS + " text, " + TURI_ID + " text, " + TTERRITORY + " text)");

        db.execSQL("create table " + REGION_TABLE + "(" + RFIRST_NAME
                + " text, " + RLAST_NAME + " text, " + RPROFILE_PIC + " text, " + RBUSINESS_UNIT + " text, "
                + RREGION + " text, " + RAVAILABLE_POINTS + " text, " + RSHOP_NAME + " text, "
                + RTOTAL_POINTS + " text, " + RURI_ID + " text, " + RTERRITORY + " text)");

        db.execSQL("create table " + BUSINESS_UNIT_TABLE + "(" + BFIRST_NAME
                + " text, " + BLAST_NAME + " text, " + BPROFILE_PIC + " text, " + BBUSINESS_UNIT + " text, "
                + BREGION + " text, " + BAVAILABLE_POINTS + " text, " + BSHOP_NAME + " text, "
                + BTOTAL_POINTS + " text, " + BURI_ID + " text, " + BTERRITORY + " text)");


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
            db.execSQL("DELETE FROM " + QUIZ_TABLE);
            db.execSQL("DELETE FROM " + TERRITORY_TABLE);
            db.execSQL("DELETE FROM " + REGION_TABLE);
            db.execSQL("DELETE FROM " + BUSINESS_UNIT_TABLE);

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

                String attendee_status = UsersList.get(i)
                        .getAttendee_status();
                if (attendee_status != null
                        && attendee_status.length() > 0) {
                    contentValues.put(ATTENDEE_STATUS,
                            attendee_status);
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
                String attendee_type = newsfeedsList.get(i).getAttendee_type();
                if (attendee_type != null && attendee_type.length() > 0) {
                    contentValues.put(NEWSFEED_ATTENDEE_TYPE, attendee_type);
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

    public void insertQuizTable(List<Quiz> agendasList,
                                SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getId();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(QUIZ_ID, session_id);
                }

                String session_name = agendasList.get(i).getQuestion();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(QUESTION, session_name);
                }

                String session_description = agendasList.get(i).getCorrect_answer();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(CORRECTANSWERID, session_description);
                }

                String folder_name = agendasList.get(i).getFolder_name();
                if (folder_name != null && folder_name.length() > 0) {
                    contentValues.put(FOLDERNAME, folder_name);
                }

                String replied = agendasList.get(i).getReplied();
                if (replied != null && replied.length() > 0) {
                    contentValues.put(REPLIED, replied);
                }

                String selected_option = agendasList.get(i).getSelected_option();
                if (selected_option != null && selected_option.length() > 0) {
                    contentValues.put(SELECTED_OPTION, selected_option);
                }


                db.insert(QUIZ_TABLE, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    public void insertAccHistoryTable(List<account_history_data> agendasList,
                                      SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();
        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();
                String session_id = agendasList.get(i).getTransaction_date();
                String sessiondate = null;
                try {

                    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
                    SimpleDateFormat targetFormat = new SimpleDateFormat(" dd/MM/yyyy");
                    Date date = originalFormat.parse(agendasList.get(i).getTransaction_date());
                    sessiondate = targetFormat.format(date);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(TRANSACTIONDATE, sessiondate);
                }

                String session_name = agendasList.get(i).getM_points_value();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(MPOINTSVALUE, session_name);
                }

                String session_description = agendasList.get(i).getProduct_name();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(PRODUCTNAME, session_description);
                }

                String folder_name = agendasList.get(i).getPack();
                if (folder_name != null && folder_name.length() > 0) {
                    contentValues.put(PACK, folder_name);
                }


                db.insert(ACCOUNT_HISTORY, null, contentValues);
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


    public void insertTerritoryTable(List<territory_data> agendasList,
                                     SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();

        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getFirst_name();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(TFIRST_NAME, session_id);
                }

                String session_name = agendasList.get(i).getLast_name();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(TLAST_NAME, session_name);
                }

                String session_description = agendasList.get(i).getProfile_pic();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(TPROFILE_PIC, session_description);
                }

                String folder_name = agendasList.get(i).getBusiness_unit();
                if (folder_name != null && folder_name.length() > 0) {
                    contentValues.put(TBUSINESS_UNIT, folder_name);
                }
                String region = agendasList.get(i).getBusiness_unit();
                if (region != null && region.length() > 0) {
                    contentValues.put(TREGION, region);
                }
                String available_points = agendasList.get(i).getAvailable_points();
                if (available_points != null && available_points.length() > 0) {
                    contentValues.put(TAVAILABLE_POINTS, available_points);
                }
                String shop_name = agendasList.get(i).getShop_name();
                if (shop_name != null && shop_name.length() > 0) {
                    contentValues.put(TSHOP_NAME, shop_name);
                }
                String total_points = agendasList.get(i).getTotal_points();
                if (total_points != null && total_points.length() > 0) {
                    contentValues.put(TTOTAL_POINTS, total_points);
                }
                String uri_id = agendasList.get(i).getUri_id();
                if (uri_id != null && uri_id.length() > 0) {
                    contentValues.put(TURI_ID, uri_id);
                }
                String territory = agendasList.get(i).getTerritory();
                if (territory != null && territory.length() > 0) {
                    contentValues.put(TTERRITORY, territory);
                }

                db.insert(TERRITORY_TABLE, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }


    public void insertRegionTable(List<region_data> agendasList,
                                  SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();

        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getFirst_name();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(RFIRST_NAME, session_id);
                }

                String session_name = agendasList.get(i).getLast_name();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(RLAST_NAME, session_name);
                }

                String session_description = agendasList.get(i).getProfile_pic();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(RPROFILE_PIC, session_description);
                }

                String folder_name = agendasList.get(i).getBusiness_unit();
                if (folder_name != null && folder_name.length() > 0) {
                    contentValues.put(RBUSINESS_UNIT, folder_name);
                }
                String region = agendasList.get(i).getBusiness_unit();
                if (region != null && region.length() > 0) {
                    contentValues.put(RREGION, region);
                }
                String available_points = agendasList.get(i).getAvailable_points();
                if (available_points != null && available_points.length() > 0) {
                    contentValues.put(RAVAILABLE_POINTS, available_points);
                }
                String shop_name = agendasList.get(i).getShop_name();
                if (shop_name != null && shop_name.length() > 0) {
                    contentValues.put(RSHOP_NAME, shop_name);
                }
                String total_points = agendasList.get(i).getTotal_points();
                if (total_points != null && total_points.length() > 0) {
                    contentValues.put(RTOTAL_POINTS, total_points);
                }
                String uri_id = agendasList.get(i).getUri_id();
                if (uri_id != null && uri_id.length() > 0) {
                    contentValues.put(RURI_ID, uri_id);
                }
                String territory = agendasList.get(i).getTerritory();
                if (territory != null && territory.length() > 0) {
                    contentValues.put(RTERRITORY, territory);
                }

                db.insert(REGION_TABLE, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return;
    }

    public void insertBuisnessunitTable(List<business_unit_data> agendasList,
                                        SQLiteDatabase db) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        db.beginTransaction();

        try {
            for (int i = 0; i < agendasList.size(); i++) {
                contentValues = new ContentValues();

                String session_id = agendasList.get(i).getFirst_name();
                if (session_id != null && session_id.length() > 0) {
                    contentValues.put(BFIRST_NAME, session_id);
                }

                String session_name = agendasList.get(i).getLast_name();
                if (session_name != null && session_name.length() > 0) {
                    contentValues.put(BLAST_NAME, session_name);
                }

                String session_description = agendasList.get(i).getProfile_pic();
                if (session_description != null && session_description.length() > 0) {
                    contentValues.put(BPROFILE_PIC, session_description);
                }

                String folder_name = agendasList.get(i).getBusiness_unit();
                if (folder_name != null && folder_name.length() > 0) {
                    contentValues.put(BBUSINESS_UNIT, folder_name);
                }
                String region = agendasList.get(i).getBusiness_unit();
                if (region != null && region.length() > 0) {
                    contentValues.put(BREGION, region);
                }
                String available_points = agendasList.get(i).getAvailable_points();
                if (available_points != null && available_points.length() > 0) {
                    contentValues.put(BAVAILABLE_POINTS, available_points);
                }
                String shop_name = agendasList.get(i).getShop_name();
                if (shop_name != null && shop_name.length() > 0) {
                    contentValues.put(BSHOP_NAME, shop_name);
                }
                String total_points = agendasList.get(i).getTotal_points();
                if (total_points != null && total_points.length() > 0) {
                    contentValues.put(BTOTAL_POINTS, total_points);
                }
                String uri_id = agendasList.get(i).getUri_id();
                if (uri_id != null && uri_id.length() > 0) {
                    contentValues.put(BURI_ID, uri_id);
                }
                String territory = agendasList.get(i).getTerritory();
                if (territory != null && territory.length() > 0) {
                    contentValues.put(BTERRITORY, territory);
                }

                db.insert(BUSINESS_UNIT_TABLE, null, contentValues);
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


    public List<territory_data> getTerritoryList() {
        String selectQuery = "select * from " + TERRITORY_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<territory_data> questionList = new ArrayList<territory_data>();

        if (cursor.moveToFirst()) {
            do {

                territory_data agendaQuestions = new territory_data();
                agendaQuestions.setFirst_name(cursor.getString(0));
                agendaQuestions.setLast_name(cursor.getString(1));
                agendaQuestions.setProfile_pic(cursor.getString(2));
                agendaQuestions.setBusiness_unit(cursor.getString(3));
                agendaQuestions.setRegion(cursor.getString(4));
                agendaQuestions.setAvailable_points(cursor.getString(5));
                agendaQuestions.setShop_name(cursor.getString(6));
                agendaQuestions.setTotal_points(cursor.getString(7));
                agendaQuestions.setUri_id(cursor.getString(8));
                agendaQuestions.setTerritory(cursor.getString(9));

                questionList.add(agendaQuestions);
            } while (cursor.moveToNext());
        }
        db.close();
        return questionList;
    }

    public List<region_data> getRegionList() {
        String selectQuery = "select * from " + REGION_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<region_data> questionList = new ArrayList<region_data>();

        if (cursor.moveToFirst()) {
            do {

                region_data agendaQuestions = new region_data();
                agendaQuestions.setFirst_name(cursor.getString(0));
                agendaQuestions.setLast_name(cursor.getString(1));
                agendaQuestions.setProfile_pic(cursor.getString(2));
                agendaQuestions.setBusiness_unit(cursor.getString(3));
                agendaQuestions.setRegion(cursor.getString(4));
                agendaQuestions.setAvailable_points(cursor.getString(5));
                agendaQuestions.setShop_name(cursor.getString(6));
                agendaQuestions.setTotal_points(cursor.getString(7));
                agendaQuestions.setUri_id(cursor.getString(8));
                agendaQuestions.setTerritory(cursor.getString(9));

                questionList.add(agendaQuestions);
            } while (cursor.moveToNext());
        }
        db.close();
        return questionList;
    }

    public List<business_unit_data> getBuisnessUnitList() {
        String selectQuery = "select * from " + BUSINESS_UNIT_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<business_unit_data> questionList = new ArrayList<business_unit_data>();

        if (cursor.moveToFirst()) {
            do {

                business_unit_data agendaQuestions = new business_unit_data();
                agendaQuestions.setFirst_name(cursor.getString(0));
                agendaQuestions.setLast_name(cursor.getString(1));
                agendaQuestions.setProfile_pic(cursor.getString(2));
                agendaQuestions.setBusiness_unit(cursor.getString(3));
                agendaQuestions.setRegion(cursor.getString(4));
                agendaQuestions.setAvailable_points(cursor.getString(5));
                agendaQuestions.setShop_name(cursor.getString(6));
                agendaQuestions.setTotal_points(cursor.getString(7));
                agendaQuestions.setUri_id(cursor.getString(8));
                agendaQuestions.setTerritory(cursor.getString(9));

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

    public ArrayList<account_history_data> getAccHistoryDetails() {
        String selectQuery = "select * from " + ACCOUNT_HISTORY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<account_history_data> attendeeList = new ArrayList<account_history_data>();
        if (cursor.moveToFirst()) {

            do {
                account_history_data attendeesList = new account_history_data();
                attendeesList.setTransaction_date(cursor.getString(0));
                attendeesList.setM_points_value(cursor.getString(1));
                attendeesList.setProduct_name(cursor.getString(2));
                attendeesList.setPack(cursor.getString(3));


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

    public List<account_history_data> getAccHistoryDetail(String att_id) {
/*
        String selectQuery = "select * from " + ATTENDEES_TABLE_NAME
                + " where " + ATTENDEE_TYPE + " =\'A\'";
*/

        String selectQuery = "select * from " + ACCOUNT_HISTORY + " where " + TRANSACTIONDATE + " LIKE \'%" + att_id + "%\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<account_history_data> attendeeList = new ArrayList<account_history_data>();
        if (cursor.moveToFirst()) {

            do {
                account_history_data attendeesList = new account_history_data();
                attendeesList.setTransaction_date(cursor.getString(0));
                attendeesList.setM_points_value(cursor.getString(1));
                attendeesList.setProduct_name(cursor.getString(2));
                attendeesList.setPack(cursor.getString(3));


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
            userdatasList.setAttendee_status(cursor.getString(12));
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
                newsfeedsList.setAttendee_type(cursor.getString(18));

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
                newsfeedsList.setAttendee_type(cursor.getString(18));

                newsFeedList.add(newsfeedsList);
            } while (cursor.moveToNext());
        }
        db.close();
        return newsFeedList;
    }

    public ArrayList<Quiz> getQuizList(String foldername) {
        String selectQuery = "select * from " + QUIZ_TABLE + " where " + FOLDERNAME + " LIKE \'%" + foldername + "%\'";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Quiz> newsFeedList = new ArrayList<Quiz>();
        if (cursor.moveToFirst()) {

            do {
                Quiz newsfeedsList = new Quiz();
                newsfeedsList.setId(cursor.getString(0));
                newsfeedsList.setQuestion(cursor.getString(1));
                newsfeedsList.setCorrect_answer(cursor.getString(2));
                newsfeedsList.setFolder_name(cursor.getString(3));
                newsfeedsList.setReplied(cursor.getString(4));
                newsfeedsList.setSelected_option(cursor.getString(5));


                newsFeedList.add(newsfeedsList);
            } while (cursor.moveToNext());
        }
        db.close();
        return newsFeedList;
    }


    public void clearQuizTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + QUIZ_TABLE);
    }


    public void clearAttendeesTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + ATTENDEES_TABLE_NAME);
    }

    public void clearAccHistoryTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + ACCOUNT_HISTORY);
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

    public void clearTerritoryTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + TERRITORY_TABLE);
    }

    public void clearRegionTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + REGION_TABLE);
    }

    public void clearBuisnessTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(" DELETE FROM " + BUSINESS_UNIT_TABLE);
    }

}