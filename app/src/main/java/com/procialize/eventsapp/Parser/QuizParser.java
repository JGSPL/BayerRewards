package com.procialize.eventsapp.Parser;

import android.util.Log;

import com.procialize.eventsapp.GetterSetter.Quiz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuizParser {

    // JSON Node names
    private static final String TAG_QUIZ_LIST = "quiz_list";
    JSONObject jsonObj = null;
    JSONObject userJsonObject = null;
    // JSONArray
    JSONArray quiz_list = null;
    ArrayList<Quiz> quizList;
    Quiz quiz;

    public ArrayList<Quiz> Quiz_Parser(String jsonStr, String foldername) {

        quizList = new ArrayList<Quiz>();

        if (jsonStr != null) {
            try {

                jsonObj = new JSONObject(jsonStr);

                // Getting poll info JSON Array node
                quiz_list = jsonObj.getJSONArray(TAG_QUIZ_LIST);
                JSONObject jsonAgenda = null;

                for (int i = 0; i < quiz_list.length(); i++) {
                    jsonAgenda = quiz_list.getJSONObject(i);
                    quiz = new Quiz();

                    String folder = jsonAgenda.getString("folder_name");
                    if (folder.trim().equalsIgnoreCase(foldername.trim())) {
                        String id = jsonAgenda.getString("id");
                        if (id != null && id.length() > 0) {
                            quiz.setId(id);
                        }
                        String question = jsonAgenda.getString("question");
                        if (question != null && question.length() > 0) {
                            quiz.setQuestion(question);
                        }
                        String correct_answer = jsonAgenda
                                .getString("correct_answer_id");
                        if (correct_answer != null && correct_answer.length() > 0) {
                            quiz.setCorrect_answer(correct_answer);
                        }
                        String replied = jsonAgenda.getString("replied");
                        if (replied != null && replied.length() > 0) {
                            quiz.setReplied(replied);
                        }

                        String folder_name = jsonAgenda.getString("folder_name");
                        if (folder_name != null && folder_name.length() > 0) {
                            quiz.setFolder_name(folder_name);
                        }

//						String quiz_type = jsonAgenda.getString("quiz_type");
//						if (quiz_type != null && quiz_type.length() > 0) {
//							quiz.setQuiz_type(quiz_type);
//						}
                        quizList.add(quiz);
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return quizList;
    }
}
