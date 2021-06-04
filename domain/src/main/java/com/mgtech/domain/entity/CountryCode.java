package com.mgtech.domain.entity;

import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhaixiang on 2017/12/28.
 */

public class CountryCode {
    private HashMap<String,List<Entity>>map;

    @Override
    public String toString() {
        return "CountryCode{" +
                "map=" + map +
                '}';
    }

    public void parse(String string){
        map = new Gson().fromJson(string, new TypeToken<HashMap<String, List<Entity>>>() {
        }.getType());
    }

    public List<Object>toList(){
        String[] letter = {"A","B","C","D","E","F","G","H","I","J","RANK_K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        List<Object> data = new ArrayList<>();
        for (String aLetter : letter) {
            if (map.containsKey(aLetter)) {
                data.add(new Title(aLetter));
                data.addAll(map.get(aLetter));
            }
        }
        return data;
    }


    public class Title{
        private String title;

        public Title(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


    public class Entity {
        @SerializedName("Pinyin")
        private String pinyin;
        @SerializedName("Name")
        private String name;
        @SerializedName("Code")
        private String code;

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "CountryCode{" +
                    "Pinyin='" + pinyin + '\'' +
                    ", name='" + name + '\'' +
                    ", code='" + code + '\'' +
                    '}';
        }
    }
}
