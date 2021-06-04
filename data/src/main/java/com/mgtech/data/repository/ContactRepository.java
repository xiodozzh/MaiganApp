package com.mgtech.data.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;


import com.mgtech.domain.entity.ContactEntity;
import com.mgtech.domain.repository.Repository;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by zhaixiang on 2016/7/30.
 * 获取手机联系人列表
 */
public class ContactRepository implements Repository.Contact {
    private Context context;
    private List<ContactEntity> list;


    public ContactRepository(Context context) {
        this.context = context;
    }

    @Override
    public Observable<List<ContactEntity>> getContact() {
        return Observable.create(new Observable.OnSubscribe<List<ContactEntity>>() {
            @Override
            public void call(Subscriber<? super List<ContactEntity>> subscriber) {
                try {
                    list = new ArrayList<>();
                    getAllContact();
                    subscriber.onNext(list);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private void getAllContact(){
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //获得联系人姓名
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).replaceAll(" ","");
                //获得联系人手机号码
                String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll(" ","");
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID)).replaceAll(" ","");
                ContactEntity entity = new ContactEntity();
                entity.setName(name);
                entity.setPhone(phone);
                entity.setId(id);
                list.add(entity);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

//    private void getContactByPhone(String string) {
//        ContentResolver resolver = context.getContentResolver();
//        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                ContactsContract.CommonDataKinds.Phone.NUMBER + " like '" + string + "%'", null, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                //获得联系人姓名
//                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).replaceAll(" ","");
//                //获得联系人手机号码
//                String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll(" ","");
//                ContactEntity entity = new ContactEntity();
//                entity.setName(name);
//                entity.setPhone(phone);
//                list.add(entity);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }
//    }

//    private void getContactByName(String string) {
//        ContentResolver cr = context.getContentResolver();
//        //取得电话本中开始一项的光标
//        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.PhoneLookup.DISPLAY_NAME
//                + " like '" + string + "%' ", null, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
//                String name = cursor.getString(nameFieldColumnIndex);
//                //取得电话号码
//                String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//                Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
//                if (phoneCursor != null && phoneCursor.moveToFirst()) {
//                    do {
//                        String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        ContactEntity entity = new ContactEntity();
//                        entity.setPhone(phone);
//                        entity.setName(name);
//                        list.add(entity);
//                    } while (phoneCursor.moveToNext());
//                    phoneCursor.close();
//                }
//            } while (cursor.moveToNext());
//            cursor.close();
//
//        }
//
//    }
}
