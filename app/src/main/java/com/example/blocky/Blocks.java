package com.example.blocky;

public class Blocks {
  private static final long INIT_SLEEP = 500;
  private static final long BLOCK_SLEEP = 1000;

  static {
    try {
      Thread.sleep(INIT_SLEEP);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static synchronized void blockMe() {
    try {
      Thread.sleep(BLOCK_SLEEP);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Call while initializing, after a thread has called blockMe and is waiting on init.
   * This will block for roughly `INIT_SLEEP + BLOCK_SLEEP ( - call delay)` in _some_ cases.
   * In others, it will only block for INIT_SLEEP.  I have not really bothered to find out why.
   *
   * Odds of blocking appear to increase as blocked-threads increases, and maybe varies by
   * device, but that may just be due to low sample size.
   *
   * This appears to happen on all OSes and both Dalvik and ART (tested through 5.1), though
   * Gingerbread seems to cause it MUCH more often.  Or maybe it's just due to lower CPU power.
   */
  public static void dontBlockMe() {
    // nothing necessary
  }
}
