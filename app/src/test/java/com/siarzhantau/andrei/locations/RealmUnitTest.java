package com.siarzhantau.andrei.locations;

import com.siarzhantau.andrei.locations.model.Location;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import io.realm.Realm;
import io.realm.log.RealmLog;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.doCallRealMethod;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@SuppressStaticInitializationFor("io.realm.internal.Util")
@PrepareForTest({Realm.class, RealmLog.class})
public class RealmUnitTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();
    private Realm mockRealm;

    @Before
    public void setup() {
        mockStatic(RealmLog.class);
        mockStatic(Realm.class);

        Realm mockRealm = PowerMockito.mock(Realm.class);

        when(Realm.getDefaultInstance()).thenReturn(mockRealm);

        this.mockRealm = mockRealm;
    }

    @Test
    public void shouldBeAbleToGetDefaultInstance() {
        assertThat(Realm.getDefaultInstance(), is(mockRealm));
    }

    @Test
    public void shouldBeAbleToMockRealmMethods() {
        when(mockRealm.isAutoRefresh()).thenReturn(true);
        assertThat(mockRealm.isAutoRefresh(), is(true));

        when(mockRealm.isAutoRefresh()).thenReturn(false);
        assertThat(mockRealm.isAutoRefresh(), is(false));
    }

    @Test
    public void shouldBeAbleToCreateARealmObject() {
        Location location = new Location("Sydney", -33.88, 151.21, true);

        when(mockRealm.createObject(Location.class)).thenReturn(location);

        Location output = mockRealm.createObject(Location.class);

        assertThat(output, is(location));
    }

    @Test
    public void shouldVerifyThatLocationWasCreated() {
        doCallRealMethod().when(mockRealm).executeTransaction(Mockito.any(Realm.Transaction.class));

        Location location = mock(Location.class);
        when(mockRealm.createObject(Location.class)).thenReturn(location);

        // Verify that Realm#createObject was called only once
        verify(mockRealm, times(1)).createObject(Location.class);

        // Verify that Location#name is called only once
        verify(location, times(1)).name = Mockito.anyString();

        // Verify that the Realm was closed only once.
        verify(mockRealm, times(1)).close();
    }

    @Test
    public void shouldVerifyThatTransactionWasExecuted() {
        // Verify that the begin transaction was called only once
        verify(mockRealm, times(1)).executeTransaction(Mockito.any(Realm.Transaction.class));

        // Verify that the Realm was closed only once.
        verify(mockRealm, times(1)).close();
    }
}