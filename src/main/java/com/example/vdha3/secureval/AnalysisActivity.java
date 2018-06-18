package com.example.vdha3.secureval;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;

public class AnalysisActivity extends AppCompatActivity {

    String[] parameter = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);



        Bundle bundle = getIntent().getExtras();
        String analysis = bundle.getString("analysis");
        Toast.makeText(this, "" + analysis, Toast.LENGTH_SHORT).show();
        //int i = 0;

        String[] q_and_a;
        q_and_a = analysis.split("\n");
        TextView[] questions = new TextView[8];
        TextView[] answers = new TextView[8];
        questions[0] = findViewById(R.id.ques1);
        questions[1] = findViewById(R.id.ques2);
        questions[2] = findViewById(R.id.ques3);
        questions[3] = findViewById(R.id.ques4);
        questions[4] = findViewById(R.id.ques5);
        questions[5] = findViewById(R.id.ques6);
        questions[6] = findViewById(R.id.ques7);
        questions[7] = findViewById(R.id.ques8);

        answers[0] = findViewById(R.id.answer1);
        answers[1] = findViewById(R.id.answer2);
        answers[2] = findViewById(R.id.answer3);
        answers[3] = findViewById(R.id.answer4);
        answers[4] = findViewById(R.id.answer5);
        answers[5] = findViewById(R.id.answer6);
        answers[6] = findViewById(R.id.answer7);
        answers[7] = findViewById(R.id.answer8);
        TextView score_left = findViewById(R.id.score_left);
        TextView score_right = findViewById(R.id.score_right);
        TextView verd_left = findViewById(R.id.verd_left);
        TextView verd_right = findViewById(R.id.verd_right);
        String[] ids_a = new String[q_and_a.length];
        String[] ids_q = new String[q_and_a.length];

        for (int i=0; i<q_and_a.length; i++){
            questions[i].setText(q_and_a[i].split(":")[0]);
            answers[i].setText(q_and_a[i].split(":")[1].trim());
        }


        //TextView all = findViewById(R.id.analysis);
        //all.setText(analysis);


    }
}
