package com.example.ksreeniv.realmintegration.data;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmResults;
import rx.Observable;

/**
 * Created by ksreeniv on 22/09/16.
 */

public class RealmDatabaseProvider {

    private Realm mRealm;

    public RealmDatabaseProvider(Context context) {
        initRealm(context);
    }

    private void initRealm(Context context) {
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(context)
                                              .encryptionKey(getEncryptionKey())
                                              .deleteRealmIfMigrationNeeded()
                                              .build());
    }

    private Realm getDefaultInstance() {
        if (mRealm == null) {
            mRealm = getNewInstance();
        }
        return mRealm;
    }

    private Realm getNewInstance() {
        return Realm.getDefaultInstance();
    }

    public <T extends RealmModel> boolean isObjectInDatabase(Class<T> classObject) {
        Realm realm = getNewInstance();
        boolean dataStatus = (realm.where(classObject).count() > 0);
        realm.close();
        return dataStatus;
    }

    public <T extends RealmModel> void storeUpdateObject(List<T> object) {
        Realm realm = getNewInstance();
        realm.executeTransaction(realm1 -> realm1.insertOrUpdate(object));
        realm.close();
    }

    public <T extends RealmModel> Observable<RealmResults<T>> getObjectAsync(Class<T> classObject) {
        return getDefaultInstance().where(classObject)
                                   .findAllAsync()
                                   .asObservable();
    }

    // Safe integration
    public <T extends RealmModel> List<T> getObjectCopy(Class<T> classObject) {
        Realm realm = getNewInstance();
        List<T> list = realm.copyFromRealm(realm.where(classObject).findAll());
        realm.close();
        return list;
    }

    public <T extends RealmModel> void deleteObject(Class<T> classObject) {
        Realm realm = getNewInstance();
        realm.executeTransaction(r -> r.delete(classObject));
        realm.close();
    }

    public void dispose() {
        getDefaultInstance().removeAllChangeListeners();
        getDefaultInstance().close();
    }

    private byte[] getEncryptionKey() {
        char[] chars = "16CharacterLongPasswordKey4Realm".toCharArray();
        byte[] key = new byte[chars.length * 2];
        for (int i = 0; i < chars.length; i++) {
            key[i * 2] = (byte) (chars[i] >> 8);
            key[i * 2 + 1] = (byte) chars[i];
        }
        return key;
    }

}
