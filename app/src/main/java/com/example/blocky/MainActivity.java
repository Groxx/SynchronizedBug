package com.example.blocky;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LinearLayout l = new LinearLayout(this);
    l.setOrientation(LinearLayout.VERTICAL);
    final TextView blocker = new TextView(this);
    final TextView noBlocker = new TextView(this);
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
          }
        }, 100);

    // start up some threads to call the synchronized method.
    // only one is necessary, it just appears much more likely to occur when there are 2+.
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
                blocker.setText(blocker.getText() + "\nBlocked: " + ran);
              }
            });
      }
    }).start();
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
                blocker.setText(blocker.getText() + "\nBlocked 2: " + ran);
              }
            });
      }
    }).start();
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
                blocker.setText(blocker.getText() + "\nBlocked 3: " + ran);
              }
            });
      }
    }).start();
  }
}
