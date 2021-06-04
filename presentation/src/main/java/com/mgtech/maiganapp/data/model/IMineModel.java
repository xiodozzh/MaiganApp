package com.mgtech.maiganapp.data.model;

import com.mgtech.maiganapp.utils.ViewUtils;

/**
 * @author zhaixiang
 */
public interface IMineModel {
    int LARGE_MARGIN = ViewUtils.dp2px(8);
    int SMALL_MARGIN = ViewUtils.dp2px(1);
    int TYPE_PERSONAL_INFO = 0;
    int TYPE_WE_CHAT = 1;
    int TYPE_QQ = 2;
    int TYPE_HEALTH_REPORT = 3;
    int TYPE_BRACELET_BIND = 4;
    int TYPE_BRACELET_UNBIND = 5;
    int TYPE_DISEASE_UPDATE = 6;
    int TYPE_WEIGHT = 7;
    int TYPE_FONT_SIZE = 8;
    int TYPE_EXCEPTION_REMINDER = 9;
    int TYPE_SETTING = 10;
    int TYPE_SERVICE = 11;


    /**
     * 获取view的顶部边界
     * @return px
     */
    int getMarginTop();

    /**
     * 获取type
     * @return type
     */
    int getType();

    class Info implements IMineModel{
        public String avatarUrl;
        public int healthIndex;
        public String name;

        @Override
        public int getMarginTop() {
            return LARGE_MARGIN;
        }

        @Override
        public int getType() {
            return TYPE_PERSONAL_INFO;
        }
    }

    class Item implements IMineModel{


        public int icon;
        public String text;
        public String value;
        public int type;
        private boolean large;
        private boolean read;

        public Item(int icon, String text,String value, int type, boolean large,boolean read) {
            this.icon = icon;
            this.text = text;
            this.type = type;
            this.large = large;
            this.value = value;
            this.read = read;
        }

        public Item(int icon, String text, int type, boolean large,boolean read) {
            this.icon = icon;
            this.text = text;
            this.type = type;
            this.large = large;
            this.value = "";
            this.read = read;
        }

        @Override
        public int getMarginTop() {
            return large? LARGE_MARGIN : SMALL_MARGIN;
        }

        @Override
        public int getType() {
            return type;
        }

        public boolean isRead() {
            return read;
        }

        public void setRead(boolean read) {
            this.read = read;
        }
    }


    class Bind implements IMineModel{
        public boolean bind;
        public String time;

        public Bind(String time) {
            this.time = time;
        }

        @Override
        public int getMarginTop() {
            return LARGE_MARGIN;
        }

        @Override
        public int getType() {
            return TYPE_BRACELET_BIND;
        }
    }

    class UnBind implements IMineModel{
        public boolean bind;

        @Override
        public int getMarginTop() {
            return LARGE_MARGIN;
        }

        @Override
        public int getType() {
            return TYPE_BRACELET_UNBIND;
        }
    }
}
