package com.mgtech.maiganapp.data.model;

import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.Objects;

public interface IPersonalInfoModel {
    int TYPE_AVATAR = 0;
    int TYPE_NAME = 1;
    int TYPE_BIRTH = 2;
    int TYPE_SEX = 3;
    int TYPE_HEIGHT = 4;
    int TYPE_WEIGHT = 5;
    int TYPE_PHONE = 6;
    int TYPE_PASSWORD = 7;
    int TYPE_LOCATION = 8;
    int TYPE_WX = 9;
    int TYPE_QQ = 10;
    int LARGE_MARGIN = ViewUtils.dp2px(16);
    int SMALL_MARGIN = ViewUtils.dp2px(1);

    /**
     * 获取类型
     *
     * @return 类型
     */
    int getType();

    /**
     * 返回view顶部偏移
     *
     * @return px
     */
    int getMarginTop();

    class Avatar implements IPersonalInfoModel {
        public String avatarUrl;
        public String otherUrl;

        public Avatar(String avatarUrl, String otherUrl) {
            this.avatarUrl = avatarUrl;
            this.otherUrl = otherUrl;
        }

        @Override
        public int getType() {
            return TYPE_AVATAR;
        }

        @Override
        public int getMarginTop() {
            return LARGE_MARGIN;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Avatar avatar = (Avatar) o;
            return Objects.equals(avatarUrl, avatar.avatarUrl) &&
                    Objects.equals(otherUrl, avatar.otherUrl);
        }

        @Override
        public int hashCode() {

            return Objects.hash(avatarUrl, otherUrl);
        }
    }

    class Item implements IPersonalInfoModel {
        private int type;
        public String title;
        public String value;
        private boolean large;
        public boolean showArrow;

        public Item(int type, String title, String value, boolean largeMargin,boolean showArrow) {
            this.type = type;
            this.title = title;
            this.value = value;
            this.large = largeMargin;
            this.showArrow = showArrow;
        }

        @Override
        public int getType() {
            return type;
        }

        @Override
        public int getMarginTop() {
            return large ? LARGE_MARGIN : SMALL_MARGIN;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()){
                return false;
            }
            Item item = (Item) o;
            return type == item.type &&
                    large == item.large &&
                    Objects.equals(title, item.title) &&
                    Objects.equals(showArrow, item.showArrow) &&
                    Objects.equals(value, item.value);
        }

        @Override
        public int hashCode() {

            return Objects.hash(type, title, value, large,showArrow);
        }
    }
}
