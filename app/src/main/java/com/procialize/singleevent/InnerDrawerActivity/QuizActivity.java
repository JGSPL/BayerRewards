package com.procialize.singleevent.InnerDrawerActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.singleevent.Adapter.QuizNewAdapter;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.DbHelper.ConnectionDetector;
import com.procialize.singleevent.GetterSetter.Quiz;
import com.procialize.singleevent.GetterSetter.QuizOptionList;
import com.procialize.singleevent.Network.ServiceHandler;
import com.procialize.singleevent.Parser.QuizOptionParser;
import com.procialize.singleevent.Parser.QuizParser;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.MyApplication;
import com.procialize.singleevent.Utility.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements OnClickListener {

	private ProgressDialog pDialog;
	// Session Manager Class
	private SessionManager session;

	// Access Token Variable
	private String accessToken,event_id;

	private String quiz_question_id;
	private String quiz_options_id;

	private String quizQuestionUrl = "";
	private String getQuizUrl = "";

	private ConnectionDetector cd;


	private ApiConstant constant = new ApiConstant();

	private RecyclerView quizNameList;
	private QuizNewAdapter adapter;

	private QuizParser quizParser;
	private ArrayList<Quiz> quizList = new ArrayList<Quiz>();

	private QuizOptionParser quizOptionParser;
	private ArrayList<QuizOptionList> quizOptionList = new ArrayList<QuizOptionList>();

	public static MyApplication appDelegate;
	String foldername="null";
	Button submit;
	ImageView headerlogoIv;

	LinearLayoutManager recyclerLayoutManager;
	String MY_PREFS_NAME = "ProcializeInfo";
	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);


		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		quizQuestionUrl = constant.baseUrl + constant.quizsubmit;

		headerlogoIv = findViewById(R.id.headerlogoIv);
		Util.logomethod(this,headerlogoIv);
		appDelegate = (MyApplication) getApplicationContext();

		foldername = getIntent().getExtras().getString("folder");

		// Session Manager
		session = new SessionManager(getApplicationContext());
		accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
		SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
		event_id = prefs.getString("eventid", "1");

		cd = new ConnectionDetector(getApplicationContext());

		// Initialize Get Quiz URL
		getQuizUrl = constant.baseUrl + constant.quizlist;


		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);

		quizNameList = (RecyclerView) findViewById(R.id.quiz_list);

		recyclerLayoutManager = new LinearLayoutManager(this);
		quizNameList.setLayoutManager(recyclerLayoutManager);

//		quizNameList.setItemViewCacheSize(0);
		quizNameList.setAnimationCacheEnabled(true);
		quizNameList.setDrawingCacheEnabled(true);
		quizNameList.hasFixedSize();

//
//
//		quizNameList.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//			@Override
//			public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//			}
//		});

		if (cd.isConnectingToInternet()) {
			new getQuizList().execute();
		} else {

			Toast.makeText(QuizActivity.this, "No internet connection",
					Toast.LENGTH_SHORT).show();

			// videoDBList = dbHelper.getVideoList();
			//
			// adapter = new VideosListAdapter(this, videoDBList);
			// videoList.setAdapter(adapter);
		}
//
		// LinearLayout mLinearLayout = (LinearLayout)
		// findViewById(R.id.linear1);
		// for (int k = 1; k <= 20; k++) {
		// // create text button
		// TextView title = new TextView(this);
		// title.setText("Question Number:" + k);
		// title.setTextColor(Color.BLUE);
		// mLinearLayout.addView(title);
		// // create radio button
		// final RadioButton[] rb = new RadioButton[5];
		// RadioGroup rg = new RadioGroup(this);
		// rg.setOrientation(RadioGroup.VERTICAL);
		// for (int i = 0; i < 5; i++) {
		// rb[i] = new RadioButton(this);
		// rg.addView(rb[i]);
		// rb[i].setText(countryName[i]);
		//
		// }
		// mLinearLayout.addView(rg);
		// }

//		quizNameList.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//
//				Quiz quiz = adapter.getQuestionIdFromList(position);
//
//				// QuizOptionList quizTempOptionList;
//				//
//				// for (int i = 0; i < quizOptionList.size(); i++) {
//				//
//				// if (quizOptionList.get(i).getQuiz_id()
//				// .equalsIgnoreCase(quiz.getId())) {
//				//
//				// quizTempOptionList.set
//				//
//				//
//				// }
//				//
//				// }
//
//				// QuizOptionList quizOptionList = adapter
//				// .getQuestionIdFromList(position);
//				//
//				// Bundle bundleObject = new Bundle();
//				// bundleObject.putSerializable("quizOptionList",
//				// quizOptionList);
//
//				if (quiz.getReplied().equalsIgnoreCase("0")) {
//
//					Intent quizOptionIntent = new Intent(QuizActivity.this,
//							QuizDetailActivity.class);
//					quizOptionIntent.putExtra("questionId", quiz.getId());
//					quizOptionIntent.putExtra("question", quiz.getQuestion());
//
//					startActivity(quizOptionIntent);
//
//					/*
//					 * Apply our splash exit (fade out) and main entry (fade in)
//					 * animation transitions.
//					 */
//					overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//				} else {
//
//					Toast.makeText(QuizActivity.this,
//							"You already submitted the quiz.",
//							Toast.LENGTH_SHORT).show();
//				}
//
//				// .putExtra("quizOptionList", quizOptionList);
//
//				// quizOptionIntent.putExtras(bundleObject);
//
//			}
//		});

	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class getQuizList extends AsyncTask<Void, Void, Void> {

		String status = "";
		String message = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Dismiss the progress dialog
			if (pDialog != null) {
				pDialog.dismiss();
				pDialog = null;
			}

			// Showing progress dialog
			pDialog = new ProgressDialog(QuizActivity.this,
					R.style.Base_Theme_AppCompat_Dialog_Alert);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

			nameValuePair.add(new BasicNameValuePair("api_access_token",
					accessToken));

			nameValuePair.add(new BasicNameValuePair("event_id",
					event_id));

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(getQuizUrl,
					ServiceHandler.POST, nameValuePair);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {

					JSONObject jsonResult = new JSONObject(jsonStr);
					status = jsonResult.getString("status");
					message = jsonResult.getString("msg");

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			if (status.equalsIgnoreCase("success")) {

				// Get Quiz Parser
				quizParser = new QuizParser();
				quizList = quizParser.Quiz_Parser(jsonStr,foldername);

				// Get Quiz Option List
				quizOptionParser = new QuizOptionParser();
				quizOptionList = quizOptionParser.Quiz_Option_Parser(jsonStr);



				appDelegate.setQuizOptionList(quizOptionList);

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			// Dismiss the progress dialog
			if (pDialog != null) {
				pDialog.dismiss();
				pDialog = null;
			}

			adapter = new QuizNewAdapter(QuizActivity.this, quizList);
			quizNameList.setAdapter(adapter);
//			Parcelable state = quizNameList.onSaveInstanceState();
//			quizNameList.onRestoreInstanceState(state);
//			quizNameList.setEmptyView(findViewById(android.R.id.empty));

		}
	}

	@Override
	public void onClick(View v) {
		 if (v == submit)
		{
			Boolean valid = true;
			final int[] check = {0};
			int sum = 0;
			final String[] question_id = {""};
			final String[] question_ans = {""};
			final String[] value = {""};
			final RadioGroup[] radioGroup = new RadioGroup[1];
			final EditText[] ans_edit = new EditText[1];
			final RadioButton[] radioButton = new RadioButton[1];
			Log.e("size",adapter.getItemCount()+"");


			String[] data=adapter.getselectedData();
			String[] question=adapter.getselectedquestion();

			if (data!=null)
			{
				for (int i= 0;i<data.length;i++)
				{
					if (i != 0) {
						question_id[0] = question_id[0] + "$#";
						question_ans[0] = question_ans[0] + "$#";
					}

					String id = quizList.get(i).getId();
					question_id[0] = question_id[0] + id;

					if (data[i]!=null)
					{
						if (quizList.get(i).getQuiz_type()!=null) {
							if (quizList.get(i).getQuiz_type().equalsIgnoreCase("2")) {
								if (!data[i].equalsIgnoreCase("")) {
									question_ans[0] = question_ans[0] + data[i];
								} else {
									valid = false;
								}
							} else {

								if (!data[i].equalsIgnoreCase("")) {

									String idno = quizList.get(i).getId();

									for (int j = 0; j < quizOptionList.size(); j++) {
										if (quizOptionList.get(j).getOption().equals(data[i]) && quizOptionList.get(j).getQuizId().equals(idno)) {
											question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
										}
									}
								} else {
									valid = false;
								}
							}
						}else {

							if (!data[i].equalsIgnoreCase("")) {

								String idno = quizList.get(i).getId();

								for (int j = 0; j < quizOptionList.size(); j++) {
									if (quizOptionList.get(j).getOption().equals(data[i]) && quizOptionList.get(j).getQuizId().equals(idno)) {
										question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
									}
								}
							} else {
								valid = false;
							}
						}
					}else
					{
						valid=false;
					}

				}


				Log.e("valid",question_ans.toString());
				Log.e("valid",question_id.toString());
				Log.e("valid",valid.toString());


				if (valid==true) {
					quiz_question_id = question_id[0];
					quiz_options_id = question_ans[0];

					new postQuizQuestion().execute();
				}else
				{
					Toast.makeText(getApplicationContext(),"Please answer all questions",Toast.LENGTH_SHORT).show();
				}


			}

//			//adapter.notifyDataSetChanged();
//			quizNameList.post(new Runnable() {
//				@Override
//				public void run() {
//
//						for (int i = 0; i < adapter.getItemCount(); i++) {
//
//							//View view = quizNameList.getChildAt(i);
//							View view = quizNameList.getLayoutManager().findViewByPosition(i);
////							View view= quizNameList.findViewHolderForAdapterPosition(i).itemView;
//
//							//QuizNewAdapter.ViewHolder holder= (QuizNewAdapter.ViewHolder) quizNameList.findViewHolderForAdapterPosition(i);
//
//
////						QuizNewAdapter.ViewHolder holder =
////								quizNameList.findViewHolderForItemId(adapter.getItemId(i));
//							//View view = holder.itemView;
//							//View view=adapter.getItemId();
////				if (view == null) {
////					try {
////						final int firstListItemPosition = 0;
////						final int lastListItemPosition = firstListItemPosition
////								+ quizNameList.getChildCount();
////
////						if (i < firstListItemPosition || i > lastListItemPosition) {
////							//This may occure using Android Monkey, else will work otherwise
//////							/view = quizNameList.getAdapter().getItemViewType(i, null, quizNameList);
//////							view = quizNameList.getAdapter().getItemViewType(i);
////							view=quizNameList.findViewHolderForAdapterPosition(i).itemView;
////						} else {
////							final int childIndex = i - firstListItemPosition;
////							view = quizNameList.getChildAt(childIndex);
////						}
////					} catch (Exception e) {
////						e.printStackTrace();
////						view = null;
////					}
////				}
//
//							if (i != 0) {
//								question_id[0] = question_id[0] + "$#";
//								question_ans[0] = question_ans[0] + "$#";
//							}
//
//							String id = quizList.get(i).getId();
//
//							question_id[0] = question_id[0] + id;
//
//
//							if (quizList.get(i).getQuiz_type().equalsIgnoreCase("1")) {
//
//								radioGroup[0] = view.findViewById(R.id.radiogroup);
//
//								int genid = radioGroup[0].getCheckedRadioButtonId();
//								radioButton[0] = radioGroup[0].findViewById(genid);
//								value[0] = radioButton[0].getText().toString();
//
//
//								for (int j = 0; j < quizOptionList.size(); j++) {
//									if (quizOptionList.get(j).getOption().equals(value[0]) && quizOptionList.get(j).getQuiz_id().equals(id)) {
//										question_ans[0] = question_ans[0] + quizOptionList.get(j).getOption_id();
//									}
//								}
//
//
//							} else if (quizList.get(i).getQuiz_type().equalsIgnoreCase("2")) {
//
//								ans_edit[0] = view.findViewById(R.id.ans_edit);
//
//								question_ans[0] = question_ans[0] + ans_edit[0].getText().toString().trim();
//
//								if (ans_edit[0].getText().toString().trim().equalsIgnoreCase("")) {
//									check[0] = check[0] + 1;
//								}
//							}
//						}
//
//					quiz_question_id = question_id[0];
//					quiz_options_id = question_ans[0];
//
//					if (quiz_question_id!=null && quiz_options_id!=null) {
//						if (!quiz_options_id.equalsIgnoreCase("") && !quiz_question_id.equalsIgnoreCase("")) {
//							if (check[0] == 0) {
//								new postQuizQuestion().execute();
//							} else {
//								Toast.makeText(QuizActivity.this, "Please answer all the questions", Toast.LENGTH_SHORT).show();
//							}
//						}
//					}
//				}
//			});
//
//
//			Log.e("id", question_id[0]);
//			Log.e("ans", question_ans[0]);
//
//			quiz_question_id = question_id[0];
//			quiz_options_id = question_ans[0];
//
//			if (quiz_question_id!=null && quiz_options_id!=null) {
//			    if (!quiz_options_id.equalsIgnoreCase("") && !quiz_question_id.equalsIgnoreCase("")) {
//                    if (check[0] == 0) {
//                        new postQuizQuestion().execute();
//                    } else {
//                        Toast.makeText(QuizActivity.this, "Please answer all the questions", Toast.LENGTH_SHORT).show();
//                    }
//                }
//			}
//


		}

	}


	@Override
	protected void onResume() {
		super.onResume();

		if (cd.isConnectingToInternet()) {
			new getQuizList().execute();
		}

	}


	private class postQuizQuestion extends AsyncTask<Void, Void, Void> {

		String error = "";
		String message = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(QuizActivity.this,
					R.style.Base_Theme_AppCompat_Dialog_Alert);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

			nameValuePair.add(new BasicNameValuePair("api_access_token",
					accessToken));
			nameValuePair.add(new BasicNameValuePair("quiz_id", quiz_question_id));
			nameValuePair.add(new BasicNameValuePair("quiz_options_id",
					quiz_options_id));

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(quizQuestionUrl,
					ServiceHandler.POST, nameValuePair);
			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {

					JSONObject jsonResult = new JSONObject(jsonStr);
					error = jsonResult.getString("status");
					message = jsonResult.getString("msg");

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			// Dismiss the progress dialog
			if (pDialog != null) {
				pDialog.dismiss();
				pDialog = null;
			}

			if (error.equalsIgnoreCase("success")) {

				Toast.makeText(QuizActivity.this, message, Toast.LENGTH_SHORT)
						.show();

				finish();

			} else {

				Toast.makeText(QuizActivity.this, message,
						Toast.LENGTH_SHORT).show();
			}

		}
	}

}
