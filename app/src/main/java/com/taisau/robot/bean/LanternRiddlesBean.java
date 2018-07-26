package com.taisau.robot.bean;

import java.util.List;

/**
 * Created by Devin on 2018/6/29.
 */
public class LanternRiddlesBean {

    /**
     * ERRORCODE : 0
     * RESULT : {"contentlist":[{"answer":"谜底：小谢","title":"谜面：一片花飞减却春 （打一聊斋志异篇目）","typeId":"sbmy","typeName":"书报谜语"},{"answer":"谜底：孙休","title":"谜面：一片降幡出石头 （三国人名一）","typeId":"qtmy","typeName":"趣味谜语"},{"answer":"谜底：渡江，胜利","title":"谜面：一片降幡出石头 提示：香烟商标二","typeId":"wpmy","typeName":"物品谜语"}],"num":20,"page":1}
     */

    private String ERRORCODE;
    private RESULTBean RESULT;

    public String getERRORCODE() {
        return ERRORCODE;
    }

    public void setERRORCODE(String ERRORCODE) {
        this.ERRORCODE = ERRORCODE;
    }

    public RESULTBean getRESULT() {
        return RESULT;
    }

    public void setRESULT(RESULTBean RESULT) {
        this.RESULT = RESULT;
    }

    public static class RESULTBean {
        /**
         * contentlist : [{"answer":"谜底：小谢","title":"谜面：一片花飞减却春 （打一聊斋志异篇目）","typeId":"sbmy","typeName":"书报谜语"},{"answer":"谜底：孙休","title":"谜面：一片降幡出石头 （三国人名一）","typeId":"qtmy","typeName":"趣味谜语"},{"answer":"谜底：渡江，胜利","title":"谜面：一片降幡出石头 提示：香烟商标二","typeId":"wpmy","typeName":"物品谜语"}]
         * num : 20
         * page : 1
         */

        private int num;
        private int page;
        private List<ContentlistBean> contentlist;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<ContentlistBean> getContentlist() {
            return contentlist;
        }

        public void setContentlist(List<ContentlistBean> contentlist) {
            this.contentlist = contentlist;
        }

        public static class ContentlistBean {
            /**
             * answer : 谜底：小谢
             * title : 谜面：一片花飞减却春 （打一聊斋志异篇目）
             * typeId : sbmy
             * typeName : 书报谜语
             */

            private String answer;
            private String title;
            private String typeId;
            private String typeName;

            public String getAnswer() {
                return answer;
            }

            public void setAnswer(String answer) {
                this.answer = answer;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTypeId() {
                return typeId;
            }

            public void setTypeId(String typeId) {
                this.typeId = typeId;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }
        }
    }
}
