package com.example.blocky;

import org.junit.Test;

import static org.junit.Assert.*;

public class BlocksTest {
  @Test
  public void testSomething() {
    // start up a thread to call the synchronized method.
    // only one is necessary, it just appears much more likely to occur when there are 2+.
    startBlockingThreads(1);

    // wait until we're inside the static initializer
    try {
      Thread.sleep(Blocks.INIT_SLEEP / 2);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // call the non-synchronized method
    long now = System.currentTimeMillis();
    Blocks.dontBlockMe();
    long waited = System.currentTimeMillis() - now;

    // test will fail when (if ever) the non-synchronized method call takes noticeably longer than it seems it should
    assertTrue(
        "Took longer than init time would imply.  Took " + waited +
         "ms, expected to wait approx " + (Blocks.INIT_SLEEP / 2) + "ms", waited < Blocks.INIT_SLEEP);
  }

  void startBlockingThreads(int num) {
    for (; num > 0; num--) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          Blocks.blockMe();
        }
      }).start();
    }
  }
}