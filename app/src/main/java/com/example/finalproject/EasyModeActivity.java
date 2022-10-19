package com.example.finalproject;

import static com.example.finalproject.R.drawable.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EasyModeActivity extends AppCompatActivity {
    private int countCard = 0;
    int x = 4, y = 4;
    int preCardId;//biến giữ tạm id của lá vừa chọn.
    private int[] countTag = new int[20];
    ArrayList[][] cards = new ArrayList[x][y];


    private TextView scoreLabel, startLabel;
    private int score;
    private SoundPlayer soundPlayer;
    private int CountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_mode);
        createButtonList();
        setCountTag();
        soundPlayer = new SoundPlayer(this);
        scoreLabel = findViewById(R.id.scoreLabel);


        TextView mTextField = (TextView) findViewById(R.id.time_bar_text);

        new CountDownTimer(100000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextField.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTextField.setText("done!");
                soundPlayer.playOverSound();
                finish();
                Intent intent = new Intent(EasyModeActivity.this, ResultActivity.class);
                intent.putExtra("SCORE", score);
                startActivity(intent);

            }

        }.start();
    }


    public void createButtonList() {//Tạo tự động bộ bài theo số lượng đã cho

            LinearLayout oLL = (LinearLayout) findViewById(R.id.outsideLinearLayout);
            for (int i = 0; i < x; i++) {
                // Ý tưởng: -Gắn constraintLayout vào linearLayout tượng trưng cho mỗi dòng của bộ bài.
                //          -Sau đó gắn từng lá bài vào từng dòng tạo thành ma trận 2 chiều.
                //          -Set hình gốc là lá backside cho từng lá bài, setTag cho mỗi lá sao cho chỉ 2 lá có thể chung 1 tag.
                //          -onClick sẽ gọi hàm flipUp đồng thời thay đổi hình background của lá bài thành 1 hình pokemon đc gán theo tag.
                //          -nếu lật trùng thì 2 lá biến mất, không trùng thì gọi hàm flipDown.

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = 10;//khoảng cách mỗi dòng.

                ConstraintLayout constraintLayout = new ConstraintLayout(EasyModeActivity.this);
                constraintLayout.setLayoutParams(params);//Apply setting cho constrainLayout.
                oLL.addView(constraintLayout);//Gắn thêm layout vào layout gốc để tạo thành từng dòng.

                for (int j = 1; j <= y; j++) {
                    ImageButton imageButton = new ImageButton(EasyModeActivity.this);
                    ConstraintLayout.LayoutParams dotparams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

                    dotparams.setMargins(0, 0, 0, 0);//Chỉnh khoảng cách giữa các lá bài
                    dotparams.height = 350;//Chỉnh độ dài mỗi lá bài
                    dotparams.width = 260;//Chỉnh rộng

                    if (j != 1) {
                        dotparams.leftToRight = i * y + j - 1;//Constraint cho lá đằng sau bên phải lá đằng trước
                    }

                    imageButton.setLayoutParams(dotparams);//Apply setting cho mỗi lá bài
                    imageButton.setBackgroundResource(backside);//Set hình cho lá bài
                    int finalI = i;
                    int finalJ = j;

                    imageButton.setId(View.generateViewId());//Tạo id cho mỗi lá bài, id là số nguyên 1, 2, 3,.... Tìm lá bài bằng findViewById(R.id.1),.....

                    //Tạo tag random tương ứng với biến đếm loại tag, nếu loại tag nào đã có 2 lá thì không tạo loại tag đó nữa
                    int random = new Random().nextInt((x * y) / 2);
                    while (imageButton.getTag() == null) {
                        if (countTag[random] < 2) {
                            imageButton.setTag(random);
                            countTag[random]++;
                        } else {
                            random = new Random().nextInt(x * y / 2);
                        }
                    }

                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView tv = (TextView) findViewById(R.id.tv1);
                            tv.setText(String.valueOf(imageButton.getTag()));
                            flipUp(imageButton);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {//Tạo delay để thẻ bài lật lên còn thấy đc :v
                                @Override
                                public void run() {
                                    switch (countCard) {//Hàm xét xem đã đủ 2 thẻ bài để xét chưa
                                        case 0: {
                                            countCard++;
                                            preCardId = imageButton.getId();//Nếu chỉ có 1 lá được lật thì lấy id của lá đó để match với lá sau.
                                            break;
                                        }
                                        case 1: {
                                            countCard = 0;
                                            ImageButton preImageButton = (ImageButton) findViewById(preCardId);
                                            if (imageButton.getTag() == preImageButton.getTag()) {
                                                disappear(imageButton);
                                                disappear(preImageButton);
                                                score++;
                                                scoreLabel.setText(getString(R.string.score, score));
                                                soundPlayer.playHitSound();
                                            }

//                                             if (CountDownTimer == 0)
//                                            {
//                                                Intent intent = new Intent(EasyModeActivity.this, ResultActivity.class);
//                                                intent.putExtra("SCORE", score);
//                                                startActivity(intent);
//
//                                            }
                                            else {
                                                flipDown(imageButton);
                                                flipDown(preImageButton);
                                                preCardId = -1;
                                            }

                                            break;
                                        }
                                    }
                                }
                            }, 1000);

                        }
                    });
                    constraintLayout.addView(imageButton);//Gắn lá bài vào bộ bài
                }
            }



    }

    private void disappear(ImageButton imageButton) {
        final ObjectAnimator oa = ObjectAnimator.ofFloat(imageButton, "scaleX", 1f, 0f);
        oa.setInterpolator(new DecelerateInterpolator());
        oa.start();

    }

    private void flipUp(ImageButton imageButton) {//Hàm lập lá bài lên.

        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imageButton, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imageButton, "scaleX", 0f, 1f);

        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());

        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imageButton.setBackgroundResource(getDrawableInt(imageButton));
                oa2.start();
            }
        });
        oa1.start();

        imageButton.setEnabled(false);//Tắt khả năng bấm vào thẻ đã lật.
    }

    private void flipDown(ImageButton imageButton) {//Hàm úp lá bài xuống.

        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imageButton, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imageButton, "scaleX", 0f, 1f);

        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.setInterpolator(new DecelerateInterpolator());

        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imageButton.setBackgroundResource(backside);
                oa2.start();
            }
        });
        oa1.start();

        imageButton.setEnabled(true);//Bật khả năng bấm.
    }

    private void setCountTag() {
        // Tạo 1 chuỗi số nguyên với từng phần tử giữ số đếm của 1 loại tag.
        // Tác dụng là để đếm xem 1 tag đã đủ 2 lá bài chưa, nếu đủ thì tag đó không được tạo ra thêm nữa.

        for (int i = 0; i < countTag.length; i++) {
            countTag[i] = 0;
        }
    }

    private int getDrawableInt(ImageButton imageButton) {
        int result = 0;
        switch (Integer.parseInt(String.valueOf(imageButton.getTag()))) {
            case 0:
                result = rong_xanh;
                break;
            case 1:
                result = rua_xanh;
                break;
            case 2:
                result = khung_long;
                break;
            case 3:
                result = ech_xanh;
                break;
            case 4:
                result = con_ma;
                break;
            case 5:
                result = chim_tuyet;
                break;
            case 6:
                result = chim_to;
                break;
            case 7:
                result = chim_set;
                break;
            case 8:
                result = chim_lua;
                break;
        }
        return result;

    }


}