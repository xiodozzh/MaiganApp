package com.mgtech.maiganapp.data.event;

import com.mgtech.maiganapp.data.model.FriendAddItemModel;

/**
 * @author zhaixiang
 */
public class FriendAddEvent {
    private FriendAddItemModel model;

    public FriendAddEvent(FriendAddItemModel model) {
        this.model = model;
    }

    public FriendAddItemModel getModel() {
        return model;
    }
}
