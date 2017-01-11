package net.yslibrary.monotweety;


import android.app.Application;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;

/**
 * Created by yshrsmz on 2016/10/20.
 * https://speakerdeck.com/egugue/robolectricfalse-at-configwogong-tong-hua-surufang-fa
 */
public class ConfiguredRobolectricTestRunner extends RobolectricTestRunner {

  private static final int[] SDK = new int[]{23};

  public ConfiguredRobolectricTestRunner(Class<?> klass) throws InitializationError {
    super(klass);
  }

  @Override
  public Config getConfig(Method method) {
    Config c = super.getConfig(method);

    int[] sdkLevel = c.sdk().length == 0 ? SDK : c.sdk();
    Class<?> constants = c.constants() == Void.class ? BuildConfig.class : c.constants();
    Class<? extends Application> application = c.constants() == Application.class ? TestApp.class : c.application();

    return new Config.Builder(c)
        .setSdk(sdkLevel)
        .setConstants(constants)
        .setApplication(application)
        .build();
  }
}