package com.mgtech.maiganapp.data.model;

public interface IMedicineModel {
    int TYPE_TITLE = 0;
    int TYPE_CONTENT = 1;
    int TYPE_SEARCH = 2;
    int TYPE_LIB= 3;

    /**
     * 获取type
     *
     * @return type
     */
    int getType();

    class Search implements IMedicineModel{

        @Override
        public int getType() {
            return TYPE_SEARCH;
        }
    }

    class Title implements IMedicineModel {

        public String value;

        public Title(String value) {
            this.value = value;
        }

        @Override
        public int getType() {
            return TYPE_TITLE;
        }
    }

    class Content implements IMedicineModel {
        public String id;
        public String name;
        public String metering;
        public String specs;
        public String dosage;
        public boolean custom;

        public Content(String id, String name, String metering, String specs, String dosage,boolean custom) {
            this.id = id;
            this.custom = custom;
            this.name = name;
            this.metering = metering;
            this.specs = specs;
            this.dosage = dosage;
        }

        @Override
        public int getType() {
            return TYPE_CONTENT;
        }
    }

    class LibAdd implements IMedicineModel{
        public String text;

        public LibAdd(String text) {
            this.text = text;
        }

        @Override
        public int getType() {
            return TYPE_LIB;
        }
    }
}
