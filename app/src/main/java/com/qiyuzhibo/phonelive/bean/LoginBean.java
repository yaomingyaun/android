package com.qiyuzhibo.phonelive.bean;

import java.util.List;

public class LoginBean {


    /**
     * ret : 200
     * data : {"code":0,"msg":"","info":[{"id":"11465","user_nicename":"手机用户4200","avatar":"http://test.zb.com/default.jpg","avatar_thumb":"http://test.zb.com/default_thumb.jpg","sex":"2","signature":"这家伙很懒，什么都没留下","coin":"10","consumption":"0","votestotal":"208682","login_type":"phone","province":"","city":"杭州市","birthday":"","last_login_time":"2020-05-20 09:29:31","isreg":"0","level":"1","level_anchor":"9","token":"7f498fcc7915f1ae1f017f3858f7fc50"}]}
     * msg :
     */

    private int ret;
    private DataBean data;
    private String msg;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * code : 0
         * msg :
         * info : [{"id":"11465","user_nicename":"手机用户4200","avatar":"http://test.zb.com/default.jpg","avatar_thumb":"http://test.zb.com/default_thumb.jpg","sex":"2","signature":"这家伙很懒，什么都没留下","coin":"10","consumption":"0","votestotal":"208682","login_type":"phone","province":"","city":"杭州市","birthday":"","last_login_time":"2020-05-20 09:29:31","isreg":"0","level":"1","level_anchor":"9","token":"7f498fcc7915f1ae1f017f3858f7fc50"}]
         */

        private int code;
        private String msg;
        private List<InfoBean> info;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * id : 11465
             * user_nicename : 手机用户4200
             * avatar : http://test.zb.com/default.jpg
             * avatar_thumb : http://test.zb.com/default_thumb.jpg
             * sex : 2
             * signature : 这家伙很懒，什么都没留下
             * coin : 10
             * consumption : 0
             * votestotal : 208682
             * login_type : phone
             * province :
             * city : 杭州市
             * birthday :
             * last_login_time : 2020-05-20 09:29:31
             * isreg : 0
             * level : 1
             * level_anchor : 9
             * token : 7f498fcc7915f1ae1f017f3858f7fc50
             */

            private String id;
            private String user_nicename;
            private String avatar;
            private String avatar_thumb;
            private String sex;
            private String signature;
            private String coin;
            private String consumption;
            private String votestotal;
            private String login_type;
            private String province;
            private String city;
            private String birthday;
            private String last_login_time;
            private String isreg;
            private String level;
            private String level_anchor;
            private String token;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUser_nicename() {
                return user_nicename;
            }

            public void setUser_nicename(String user_nicename) {
                this.user_nicename = user_nicename;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getAvatar_thumb() {
                return avatar_thumb;
            }

            public void setAvatar_thumb(String avatar_thumb) {
                this.avatar_thumb = avatar_thumb;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getSignature() {
                return signature;
            }

            public void setSignature(String signature) {
                this.signature = signature;
            }

            public String getCoin() {
                return coin;
            }

            public void setCoin(String coin) {
                this.coin = coin;
            }

            public String getConsumption() {
                return consumption;
            }

            public void setConsumption(String consumption) {
                this.consumption = consumption;
            }

            public String getVotestotal() {
                return votestotal;
            }

            public void setVotestotal(String votestotal) {
                this.votestotal = votestotal;
            }

            public String getLogin_type() {
                return login_type;
            }

            public void setLogin_type(String login_type) {
                this.login_type = login_type;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getLast_login_time() {
                return last_login_time;
            }

            public void setLast_login_time(String last_login_time) {
                this.last_login_time = last_login_time;
            }

            public String getIsreg() {
                return isreg;
            }

            public void setIsreg(String isreg) {
                this.isreg = isreg;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getLevel_anchor() {
                return level_anchor;
            }

            public void setLevel_anchor(String level_anchor) {
                this.level_anchor = level_anchor;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }
        }
    }
}
