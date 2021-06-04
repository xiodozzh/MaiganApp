package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;

public class UploadContactRequestEntity implements RequestEntity {


    /**
     * accountGuid :
     * contacts : [{"contactIdStringInContact":"","nameInContact":"","phone":"","zone":""}]
     */

    private String accountGuid;
    private List<ContactBean> contacts;

    @Override
    public String getUrl() {
        return HttpApi.API_UPLOAD_CONTACT;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }

    public String getAccountGuid() {
        return accountGuid;
    }

    public void setAccountGuid(String accountGuid) {
        this.accountGuid = accountGuid;
    }

    public List<ContactBean> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactBean> contacts) {
        this.contacts = contacts;
    }

    public static class ContactBean {
        /**
         * contactIdStringInContact :
         * nameInContact :
         * phone :
         * zone :
         */

        private String contactIdStringInContact;
        private String nameInContact;
        private String phone;
        private String zone;

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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }
    }
}
