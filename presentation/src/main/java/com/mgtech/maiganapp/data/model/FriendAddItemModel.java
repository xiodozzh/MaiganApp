package com.mgtech.maiganapp.data.model;

import java.util.Objects;

/**
 * @author zhaixiang
 */
public class FriendAddItemModel extends IFriendAddModel{
    public static final int RELATION_NOT_REGISTER = 0;
    public static final int RELATION_NOT_FOLLOW = 1;
    public static final int RELATION_FOLLOWED = 2;
    public static final int RELATION_OTHER = 3;

    public String id;
    public String name;
    public String contactName;
    public String phone;
    public String zone;
    public int relation;
    public String avatarUri;
    public String searchText;

    public FriendAddItemModel() {
        type = IFriendAddModel.TYPE_HEADER_ITEM;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FriendAddItemModel that = (FriendAddItemModel) o;
        return relation == that.relation &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(zone, that.zone) &&
                Objects.equals(contactName,that.contactName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone, zone, relation,contactName);
    }

    @Override
    public String toString() {
        return "FriendAddItemModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", contactName='" + contactName + '\'' +
                ", phone='" + phone + '\'' +
                ", zone='" + zone + '\'' +
                ", relation=" + relation +
                ", avatarUri='" + avatarUri + '\'' +
                '}';
    }
}
