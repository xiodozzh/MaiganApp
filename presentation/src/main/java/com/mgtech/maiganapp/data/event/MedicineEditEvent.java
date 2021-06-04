//package com.mgtech.maiganapp.data.event;
//
//import com.mgtech.domain.entity.net.response.MedicineResponseEntity;
//
///**
// * Created by zhaixiang on 2018/4/19.
// * 药物事件
// */
//
//public class MedicineEditEvent {
//    public static final int TYPE_INSERT = 2;
//    public static final int TYPE_UPDATE = 1;
//    public static final int TYPE_NONE = 0;
//    private int type;
//    private MedicineResponseEntity entity;
//    private int position;
//
//    public MedicineEditEvent(int type, MedicineResponseEntity entity, int position) {
//        this.type = type;
//        this.entity = entity;
//        this.position = position;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public MedicineResponseEntity getEntity() {
//        return entity;
//    }
//
//    public void setEntity(MedicineResponseEntity entity) {
//        this.entity = entity;
//    }
//
//    public int getPosition() {
//        return position;
//    }
//
//    public void setPosition(int position) {
//        this.position = position;
//    }
//
//    @Override
//    public String toString() {
//        return "MedicineResultEvent{" +
//                "type=" + type +
//                ", entity=" + entity +
//                ", position=" + position +
//                '}';
//    }
//}
