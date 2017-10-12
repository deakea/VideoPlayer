package com.android.deak.videoplayer.entity;

import java.util.List;

/**
 * Created by deak on 2016/7/28.
 */
public class SearchResultEntity {

    /**
     * code : 0000
     * msg :
     * data : {"total":4,"results":[{"url":"http://img.rrmj.tv/video/20160413/b_1460542607198.jpg","title":"越狱 第一季","score":9.2,"finish":true,"brief":"迈克尔正陷于无望的困境中,他的哥哥林肯被认定犯有谋杀罪被投入死囚牢,所有证据都指出林肯就是凶手,但是迈克尔坚信兄长是无辜的.","mark":"none","upInfo":22,"verticalUrl":"http://img.rrmj.tv/season/20160531/b_1464692986885.jpg","horizontalUrl":"http://img.rrmj.tv/video/20160413/b_1460542607198.jpg","id":49}],"currentPage":1}
     */

    private String code;
    private String msg;
    /**
     * total : 4
     * results : [{"url":"http://img.rrmj.tv/video/20160413/b_1460542607198.jpg","title":"越狱 第一季","score":9.2,"finish":true,"brief":"迈克尔正陷于无望的困境中,他的哥哥林肯被认定犯有谋杀罪被投入死囚牢,所有证据都指出林肯就是凶手,但是迈克尔坚信兄长是无辜的.","mark":"none","upInfo":22,"verticalUrl":"http://img.rrmj.tv/season/20160531/b_1464692986885.jpg","horizontalUrl":"http://img.rrmj.tv/video/20160413/b_1460542607198.jpg","id":49}]
     * currentPage : 1
     */

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
        private int total;
        private int currentPage;
        /**
         * url : http://img.rrmj.tv/video/20160413/b_1460542607198.jpg
         * title : 越狱 第一季
         * score : 9.2
         * finish : true
         * brief : 迈克尔正陷于无望的困境中,他的哥哥林肯被认定犯有谋杀罪被投入死囚牢,所有证据都指出林肯就是凶手,但是迈克尔坚信兄长是无辜的.
         * mark : none
         * upInfo : 22
         * verticalUrl : http://img.rrmj.tv/season/20160531/b_1464692986885.jpg
         * horizontalUrl : http://img.rrmj.tv/video/20160413/b_1460542607198.jpg
         * id : 49
         */

        private List<ResultsBean> results;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public List<ResultsBean> getResults() {
            return results;
        }

        public void setResults(List<ResultsBean> results) {
            this.results = results;
        }

        public static class ResultsBean {
            private String url;
            private String title;
            private double score;
            private boolean finish;
            private String brief;
            private String mark;
            private int upInfo;
            private String verticalUrl;
            private String horizontalUrl;
            private int id;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public double getScore() {
                return score;
            }

            public void setScore(double score) {
                this.score = score;
            }

            public boolean isFinish() {
                return finish;
            }

            public void setFinish(boolean finish) {
                this.finish = finish;
            }

            public String getBrief() {
                return brief;
            }

            public void setBrief(String brief) {
                this.brief = brief;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public int getUpInfo() {
                return upInfo;
            }

            public void setUpInfo(int upInfo) {
                this.upInfo = upInfo;
            }

            public String getVerticalUrl() {
                return verticalUrl;
            }

            public void setVerticalUrl(String verticalUrl) {
                this.verticalUrl = verticalUrl;
            }

            public String getHorizontalUrl() {
                return horizontalUrl;
            }

            public void setHorizontalUrl(String horizontalUrl) {
                this.horizontalUrl = horizontalUrl;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
