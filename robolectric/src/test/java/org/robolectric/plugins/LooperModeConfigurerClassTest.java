package org.robolectric.plugins;

import static com.google.common.truth.Truth.assertThat;

import android.os.Looper;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;
import org.robolectric.annotation.LooperMode.Mode;
import org.robolectric.config.ConfigurationRegistry;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowBaseLooper;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.shadows.ShadowRealisticLooper;

/**
 * Unit tests for classes annotated with @LooperMode.
 */
@RunWith(AndroidJUnit4.class)
@LooperMode(Mode.PAUSED)
public class LooperModeConfigurerClassTest {

  @Test
  public void defaultsToClass() {
    assertThat(ConfigurationRegistry.get(LooperMode.Mode.class)).isSameAs(Mode.PAUSED);
  }

  @Test
  @LooperMode(Mode.LEGACY)
  public void overriddenAtMethod() {
    assertThat(ConfigurationRegistry.get(LooperMode.Mode.class)).isSameAs(Mode.LEGACY);
  }

  @Test
  @LooperMode(Mode.LEGACY)
  public void shouldUseLegacyShadows() {
    assertThat(ConfigurationRegistry.get(LooperMode.Mode.class)).isSameAs(Mode.LEGACY);

    ShadowBaseLooper looper = Shadow.extract(Looper.getMainLooper());
    assertThat(looper).isInstanceOf(ShadowLooper.class);
  }

  @Test
  @LooperMode(Mode.PAUSED)
  public void shouldUseRealisticShadows() {
    assertThat(ConfigurationRegistry.get(LooperMode.Mode.class)).isSameAs(Mode.PAUSED);

    ShadowBaseLooper looper = Shadow.extract(Looper.getMainLooper());
    assertThat(looper).isInstanceOf(ShadowRealisticLooper.class);
  }
}
