package com.siarzhantau.andrei.locations;

import com.siarzhantau.andrei.locations.model.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import info.juanmendez.mockrealm.MockRealm;
import info.juanmendez.mockrealm.dependencies.RealmStorage;
import io.realm.Case;
import io.realm.Realm;

import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"org.mockito.*", "android.*"})
@PrepareForTest({RealmConfiguration.class, Realm.class, RealmQuery.class, RealmResults.class, RealmList.class, RealmObject.class})
public class RealmQueryTest {

    Realm realm;

    @Before
    public void before() throws Exception {
        MockRealm.prepare();
        realm = Realm.getDefaultInstance();
    }

    @Test
    public void shouldCreateObject() {
        RealmStorage.clear();
        assertNotNull(realm.createObject(Location.class));
    }

    @Test
    public void shouldCopyToRealm() throws Exception {
        RealmStorage.clear();
        Location location = new Location();
        location.name = "Test";
        location.lat = -33.11;
        location.lng = -151.11;

        realm.copyToRealm(location);

        assertEquals("There is one location", realm.where(Location.class).count(), 1);
    }

    @Test
    public void shouldExecuteTransaction() {
        RealmStorage.clear();
        realm.executeTransaction(realm1 -> {
            Location location = realm.createObject(Location.class);
            location.name = "Test";
            location.lat = -33.11;
            location.lng = -151.11;
        });

        assertEquals("there is now one element available", realm.where(Location.class).findAll().size(), 1);
    }

    @Test
    public void shouldQueryByConditions() {
        RealmStorage.clear();
        Location location = realm.createObject(Location.class);
        location.name = "Test1";
        location.lat = -33.11;
        location.lng = -151.11;

        location = realm.createObject(Location.class);
        location.name = "Test2";
        location.lat = -33.12;
        location.lng = -151.12;


        RealmResults<Location> locations = realm.where(Location.class).equalTo("name", "Test1").findAll();
        assertNotNull("location is found", locations);

        for (Location l : locations) {
            System.out.println("locations: " + l.name);
        }

        //between
        locations = realm.where(Location.class).between("lat", -33.13, -33.10).findAll();

        assertEquals("There are two locations", locations.size(), 2);
    }

    @Test
    public void shouldQueryByOr() {
        RealmStorage.clear();

        Location location;

        location = realm.createObject(Location.class);
        location.id = "1";
        location.name = "AAA BBB";
        location.lat = -33.11;
        location.lng = -151.11;
        location.distance = 100;

        location = realm.createObject(Location.class);
        location.id = "2";
        location.name = "BBB CCC";
        location.lat = -33.12;
        location.lng = -151.12;
        location.distance = 200;

        location = realm.createObject(Location.class);
        location.id = "3";
        location.name = "CCC DDD";
        location.lat = -33.12;
        location.lng = -151.12;
        location.distance = 200;

        location = realm.createObject(Location.class);
        location.id = "3";
        location.name = "DDD EEE";
        location.lat = -33.12;
        location.lng = -151.12;
        location.distance = 200;

        RealmResults<Location> locations = realm.where(Location.class).contains("name", "BBB").or().contains("name", "CCC").findAll();
        assertEquals("There are three location with those last names", locations.size(), 3);
    }

    @Test
    public void shouldDeleteRealmObject() {
        RealmStorage.clear();

        realm.where(Location.class).findFirstAsync().addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {

            }
        });

        realm.beginTransaction();
        Location location = realm.createObject(Location.class);
        location.id = "3";
        location.name = "CCC DDD";
        location.lat = -33.12;
        location.lng = -151.12;
        location.distance = 200;
        realm.commitTransaction();

        realm.beginTransaction();
        location.deleteFromRealm();
        realm.commitTransaction();

        realm.beginTransaction();
        realm.where(Location.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();

        assertEquals("There is one location left", realm.where(Location.class).count(), 0);
    }

}