package com.mgtech.domain.entity.net.response;

public class ContactBean {
    /**
     * accountGuid :
     * contactGuidInMystrace :
     * contactIdStringInContact :
     * nameInContact :
     * nameInMystrace :
     * namePinyinInMystrace :
     * namePinyinInContact :
     * phone :
     * portrait :
     * isMystraceUser : 1
     * isFriend : 1
     */

    private String accountGuid;
    private String contactGuidInMystrace;
    private String contactIdStringInContact;
    private String nameInContact;
    private String nameInMystrace;
    private String namePinyinInMystrace;
    private String namePinyinInContact;
    private String phone;
    private String portrait;
    private int isMystraceUser;
    private int isFriend;

    public String getAccountGuid() {
        return accountGuid;
    }

    public void setAccountGuid(String accountGuid) {
        this.accountGuid = accountGuid;
    }

    public String getContactGuidInMystrace() {
        return contactGuidInMystrace;
    }

    public void setContactGuidInMystrace(String contactGuidInMystrace) {
        this.contactGuidInMystrace = contactGuidInMystrace;
    }

    public String getContactIdStringInContact() {
        return contactIdStringInContact;
    }

    public void setContactIdStringInContact(String contactIdStringInContact) {
        this.contactIdStringInContact = contactIdStringInContact;
    }

    public String getNameInContact() {
        return nameInContact;
    }

    public void setNameInContact(String nameInContact) {
        this.nameInContact = nameInContact;
    }

    public String getNameInMystrace() {
        return nameInMystrace;
    }

    public void setNameInMystrace(String nameInMystrace) {
        this.nameInMystrace = nameInMystrace;
    }

    public String getNamePinyinInMystrace() {
        return namePinyinInMystrace;
    }

    public void setNamePinyinInMystrace(String namePinyinInMystrace) {
        this.namePinyinInMystrace = namePinyinInMystrace;
    }

    public String getNamePinyinInContact() {
        return namePinyinInContact;
    }

    public void setNamePinyinInContact(String namePinyinInContact) {
        this.namePinyinInContact = namePinyinInContact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getIsMystraceUser() {
        return isMystraceUser;
    }

    public void setIsMystraceUser(int isMystraceUser) {
        this.isMystraceUser = isMystraceUser;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }
}
