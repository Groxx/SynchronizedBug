package com.example.blocky;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
  TextView blocker;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LinearLayout l = new LinearLayout(this);
    l.setOrientation(LinearLayout.VERTICAL);
    blocker = new TextView(this);
    final TextView noBlocker = new TextView(this);
    noBlocker.setTypeface(null, Typeface.BOLD);
    l.addView(noBlocker);
    l.addView(blocker);
    setContentView(l);

    // call the non-synchronized method "soon", but likely after the other threads have blocked
    l.postDelayed(
        new Runnable() {
          @Override
          public void run() {
            long now = System.currentTimeMillis();
            Blocks.dontBlockMe();
            long ran = System.currentTimeMillis() - now;
            noBlocker.setText("Not blocked: " + ran);
            if (ran > Blocks.INIT_SLEEP) {
              noBlocker.setTextColor(Color.RED);
            } else {
              noBlocker.setTextColor(Color.argb(255, 0, 128, 0)); // dark green, more readable than Color.GREEN
              noBlocker.setText(noBlocker.getText() + "\nkill the process and retry, it's intermittent");
            }
          }
        }, 100);

    // start up some threads to call the synchronized method.
    // only one is necessary, it just appears much more likely to occur when there are 2+.
    startBlockingThreads(1);
  }

  void startBlockingThreads(int num) {
    for (; num > 0; num--) {
      final int current = num;
      new Thread(new Runnable() {
        @Override
        public void run() {
          long now = System.currentTimeMillis();
          Blocks.blockMe();
          final long ran = System.currentTimeMillis() - now;
          blocker.post(
              new Runnable() {
                @Override
                public void run() {
                  blocker.setText(blocker.getText() + "\nBlocked " + current + ": " + ran);
                }
              });
        }
      }).start();
    }
  }
}
