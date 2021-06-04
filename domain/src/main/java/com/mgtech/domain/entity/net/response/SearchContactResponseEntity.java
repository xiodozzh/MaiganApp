package com.mgtech.domain.entity.net.response;

public class SearchContactResponseEntity {
    private static final int MONITOR = 1;
    private static final int NOT_MONITOR = 0;

    /**
     * accountGuid :
     * contactGuidInMystrace :
     * contactIdStringContact :
     * nameInAddressBook :
     * nameInMystrace :
     * namePinyinInMystrace :
     * namePinyinInContact :
     * phone :
     * portrait :
     * isMystraceUser : 1
     * isFriend : 1
     * relationGuid :
     * targetAccountGuid :
     * type : 0
     * targetName :
     * targetNoteName :
     * targetAvatarUrl :
     * authorityPush : 1
     * authorityRead : 1
     */

    private String accountGuid;
    private String contactGuidInMystrace;
    private String contactIdStringContact;
    private String nameInAddressBook;
    private String nameInMystrace;
    private String namePinyinInMystrace;
    private String namePinyinInContact;
    private String phone;
    private String portrait;
    private int isMystraceUser;
    private int isFriend;
    private String relationGuid;
    private String targetAccountGuid;
    private int type;
    private String targetName;
    private String targetNoteName;
    private String targetAvatarUrl;
    private int authorityPush;
    private int authorityRead;

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

    public String getContactIdStringContact() {
        return contactIdStringContact;
    }

    public void setContactIdStringContact(String contactIdStringContact) {
        this.contactIdStringContact = contactIdStringContact;
    }

    public String getNameInAddressBook() {
        return nameInAddressBook;
    }

    public void setNameInAddressBook(String nameInAddressBook) {
        this.nameInAddressBook = nameInAddressBook;
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

    public String getRelationGuid() {
        return relationGuid;
    }

    public void setRelationGuid(String relationGuid) {
        this.relationGuid = relationGuid;
    }

    public String getTargetAccountGuid() {
        return targetAccountGuid;
    }

    public void setTargetAccountGuid(String targetAccountGuid) {
        this.targetAccountGuid = targetAccountGuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetNoteName() {
        return targetNoteName;
    }

    public void setTargetNoteName(String targetNoteName) {
        this.targetNoteName = targetNoteName;
    }

    public String getTargetAvatarUrl() {
        return targetAvatarUrl;
    }

    public void setTargetAvatarUrl(String targetAvatarUrl) {
        this.targetAvatarUrl = targetAvatarUrl;
    }

    public int getAuthorityPush() {
        return authorityPush;
    }

    public void setAuthorityPush(int authorityPush) {
        this.authorityPush = authorityPush;
    }

    public int getAuthorityRead() {
        return authorityRead;
    }

    public void setAuthorityRead(int authorityRead) {
        this.authorityRead = authorityRead;
    }

    @Override
    public String toString() {
        return "SearchContactResponseEntity{" +
                "accountGuid='" + accountGuid + '\'' +
                ", contactGuidInMystrace='" + contactGuidInMystrace + '\'' +
                ", contactIdStringContact='" + contactIdStringContact + '\'' +
                ", nameInAddressBook='" + nameInAddressBook + '\'' +
                ", nameInMystrace='" + nameInMystrace + '\'' +
                ", namePinyinInMystrace='" + namePinyinInMystrace + '\'' +
                ", namePinyinInContact='" + namePinyinInContact + '\'' +
                ", phone='" + phone + '\'' +
                ", portrait='" + portrait + '\'' +
                ", isMystraceUser=" + isMystraceUser +
                ", isFriend=" + isFriend +
                ", relationGuid='" + relationGuid + '\'' +
                ", targetAccountGuid='" + targetAccountGuid + '\'' +
                ", type=" + type +
                ", targetName='" + targetName + '\'' +
                ", targetNoteName='" + targetNoteName + '\'' +
                ", targetAvatarUrl='" + targetAvatarUrl + '\'' +
                ", authorityPush=" + authorityPush +
                ", authorityRead=" + authorityRead +
                '}';
    }
}
