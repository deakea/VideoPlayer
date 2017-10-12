package com.android.deak.videoplayer.entity;

import java.util.List;

/**
 * Created by deak on 2016/7/27.
 */
public class KeyWordEntity {

    /**
     * code : 0000
     * msg :
     * data : {"wordList":["权力的游戏","欲奴","行尸走肉","生活大爆炸","越狱","破产姐妹","斯巴达克斯","老友记","犯罪心理","蛇蝎女佣"]}
     */

    private String code;
    private String msg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<String> wordList;

        public List<String> getWordList() {
            return wordList;
        }

        public void setWordList(List<String> wordList) {
            this.wordList = wordList;
        }
    }
}
