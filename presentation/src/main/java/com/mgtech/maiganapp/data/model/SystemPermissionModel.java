package com.mgtech.maiganapp.data.model;

public class SystemPermissionModel {
    public static final int HEADER = 0;
    public static final int ITEM = 1;
    public static final int CUSTOM = 2;
    public static final int PERMISSION_NOTIFICATION = 0;
    public static final int PERMISSION_STORAGE = 1;
    public static final int PERMISSION_LOCATION = 2;
    public static final int PERMISSION_CAMERA = 3;
    public static final int PERMISSION_CONTACT = 4;
    public static final int PERMISSION_CALL = 5;
    public static final int PERMISSION_PICTORY = 6;
    public static final int PERMISSION_CUSTOMER_SERVICE_CONTACT_BY_PHONE = 7;

    private String title;
    private String subtitle;
    private int type;
    private boolean open;
    private int permissionType;

    public SystemPermissionModel(String title, String subtitle,int permissionType,  boolean open) {
        this.title = title;
        this.subtitle = subtitle;
        this.type = ITEM;
        this.open = open;
        this.permissionType = permissionType;
    }

    public SystemPermissionModel(String title) {
        this.title = title;
        this.type = HEADER;
    }

    public SystemPermissionModel(String title, String subtitle, boolean open) {
        this.title = title;
        this.subtitle = subtitle;
        this.type = CUSTOM;
        this.open = open;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(int permissionType) {
        this.permissionType = permissionType;
    }

    @Override
    public String toString() {
        return "SystemPermissionModel{" +
                "title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", type=" + type +
                ", open=" + open +
                '}';
    }
}
