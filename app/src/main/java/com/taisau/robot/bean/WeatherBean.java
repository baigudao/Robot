package com.taisau.robot.bean;

/**
 * Created by Devin on 2018/6/25.
 */
public class WeatherBean {

    /**
     * error_code : 0
     * reason : successed!
     * result : {"future":{"day_20180625":{"date":"20180625","temperature":"26℃~31℃","weather":"中雨转雷阵雨","weather_id":{"fa":"08","fb":"04"},"week":"星期一","wind":"持续无风向微风"},"day_20180626":{"date":"20180626","temperature":"27℃~32℃","weather":"雷阵雨","weather_id":{"fa":"04","fb":"04"},"week":"星期二","wind":"持续无风向微风"},"day_20180627":{"date":"20180627","temperature":"27℃~32℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"week":"星期三","wind":"持续无风向微风"},"day_20180628":{"date":"20180628","temperature":"27℃~33℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"week":"星期四","wind":"持续无风向微风"},"day_20180629":{"date":"20180629","temperature":"27℃~32℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"week":"星期五","wind":"持续无风向微风"},"day_20180630":{"date":"20180630","temperature":"27℃~32℃","weather":"雷阵雨","weather_id":{"fa":"04","fb":"04"},"week":"星期六","wind":"持续无风向微风"},"day_20180701":{"date":"20180701","temperature":"27℃~33℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"week":"星期日","wind":"持续无风向微风"}},"sk":{"humidity":"79%","temp":"29","time":"11:09","wind_direction":"南风","wind_strength":"2级"},"today":{"city":"深圳","comfort_index":"","date_y":"2018年06月25日","dressing_advice":"天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。","dressing_index":"炎热","drying_index":"","exercise_index":"较适宜","temperature":"26℃~31℃","travel_index":"较适宜","uv_index":"中等","wash_index":"较适宜","weather":"中雨转雷阵雨","weather_id":{"fa":"08","fb":"04"},"week":"星期一","wind":"持续无风向微风"}}
     * resultcode : 200
     */

    private int error_code;
    private String reason;
    private ResultBean result;
    private String resultcode;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public static class ResultBean {
        /**
         * future : {"day_20180625":{"date":"20180625","temperature":"26℃~31℃","weather":"中雨转雷阵雨","weather_id":{"fa":"08","fb":"04"},"week":"星期一","wind":"持续无风向微风"},"day_20180626":{"date":"20180626","temperature":"27℃~32℃","weather":"雷阵雨","weather_id":{"fa":"04","fb":"04"},"week":"星期二","wind":"持续无风向微风"},"day_20180627":{"date":"20180627","temperature":"27℃~32℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"week":"星期三","wind":"持续无风向微风"},"day_20180628":{"date":"20180628","temperature":"27℃~33℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"week":"星期四","wind":"持续无风向微风"},"day_20180629":{"date":"20180629","temperature":"27℃~32℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"week":"星期五","wind":"持续无风向微风"},"day_20180630":{"date":"20180630","temperature":"27℃~32℃","weather":"雷阵雨","weather_id":{"fa":"04","fb":"04"},"week":"星期六","wind":"持续无风向微风"},"day_20180701":{"date":"20180701","temperature":"27℃~33℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"week":"星期日","wind":"持续无风向微风"}}
         * sk : {"humidity":"79%","temp":"29","time":"11:09","wind_direction":"南风","wind_strength":"2级"}
         * today : {"city":"深圳","comfort_index":"","date_y":"2018年06月25日","dressing_advice":"天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。","dressing_index":"炎热","drying_index":"","exercise_index":"较适宜","temperature":"26℃~31℃","travel_index":"较适宜","uv_index":"中等","wash_index":"较适宜","weather":"中雨转雷阵雨","weather_id":{"fa":"08","fb":"04"},"week":"星期一","wind":"持续无风向微风"}
         */

        private FutureBean future;
        private SkBean sk;
        private TodayBean today;

        public FutureBean getFuture() {
            return future;
        }

        public void setFuture(FutureBean future) {
            this.future = future;
        }

        public SkBean getSk() {
            return sk;
        }

        public void setSk(SkBean sk) {
            this.sk = sk;
        }

        public TodayBean getToday() {
            return today;
        }

        public void setToday(TodayBean today) {
            this.today = today;
        }

        public static class FutureBean {
            /**
             * day_20180625 : {"date":"20180625","temperature":"26℃~31℃","weather":"中雨转雷阵雨","weather_id":{"fa":"08","fb":"04"},"week":"星期一","wind":"持续无风向微风"}
             * day_20180626 : {"date":"20180626","temperature":"27℃~32℃","weather":"雷阵雨","weather_id":{"fa":"04","fb":"04"},"week":"星期二","wind":"持续无风向微风"}
             * day_20180627 : {"date":"20180627","temperature":"27℃~32℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"week":"星期三","wind":"持续无风向微风"}
             * day_20180628 : {"date":"20180628","temperature":"27℃~33℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"week":"星期四","wind":"持续无风向微风"}
             * day_20180629 : {"date":"20180629","temperature":"27℃~32℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"week":"星期五","wind":"持续无风向微风"}
             * day_20180630 : {"date":"20180630","temperature":"27℃~32℃","weather":"雷阵雨","weather_id":{"fa":"04","fb":"04"},"week":"星期六","wind":"持续无风向微风"}
             * day_20180701 : {"date":"20180701","temperature":"27℃~33℃","weather":"多云","weather_id":{"fa":"01","fb":"01"},"week":"星期日","wind":"持续无风向微风"}
             */

            private Day20180625Bean day_20180625;
            private Day20180626Bean day_20180626;
            private Day20180627Bean day_20180627;
            private Day20180628Bean day_20180628;
            private Day20180629Bean day_20180629;
            private Day20180630Bean day_20180630;
            private Day20180701Bean day_20180701;

            public Day20180625Bean getDay_20180625() {
                return day_20180625;
            }

            public void setDay_20180625(Day20180625Bean day_20180625) {
                this.day_20180625 = day_20180625;
            }

            public Day20180626Bean getDay_20180626() {
                return day_20180626;
            }

            public void setDay_20180626(Day20180626Bean day_20180626) {
                this.day_20180626 = day_20180626;
            }

            public Day20180627Bean getDay_20180627() {
                return day_20180627;
            }

            public void setDay_20180627(Day20180627Bean day_20180627) {
                this.day_20180627 = day_20180627;
            }

            public Day20180628Bean getDay_20180628() {
                return day_20180628;
            }

            public void setDay_20180628(Day20180628Bean day_20180628) {
                this.day_20180628 = day_20180628;
            }

            public Day20180629Bean getDay_20180629() {
                return day_20180629;
            }

            public void setDay_20180629(Day20180629Bean day_20180629) {
                this.day_20180629 = day_20180629;
            }

            public Day20180630Bean getDay_20180630() {
                return day_20180630;
            }

            public void setDay_20180630(Day20180630Bean day_20180630) {
                this.day_20180630 = day_20180630;
            }

            public Day20180701Bean getDay_20180701() {
                return day_20180701;
            }

            public void setDay_20180701(Day20180701Bean day_20180701) {
                this.day_20180701 = day_20180701;
            }

            public static class Day20180625Bean {
                /**
                 * date : 20180625
                 * temperature : 26℃~31℃
                 * weather : 中雨转雷阵雨
                 * weather_id : {"fa":"08","fb":"04"}
                 * week : 星期一
                 * wind : 持续无风向微风
                 */

                private String date;
                private String temperature;
                private String weather;
                private WeatherIdBean weather_id;
                private String week;
                private String wind;

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }

                public String getWeather() {
                    return weather;
                }

                public void setWeather(String weather) {
                    this.weather = weather;
                }

                public WeatherIdBean getWeather_id() {
                    return weather_id;
                }

                public void setWeather_id(WeatherIdBean weather_id) {
                    this.weather_id = weather_id;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getWind() {
                    return wind;
                }

                public void setWind(String wind) {
                    this.wind = wind;
                }

                public static class WeatherIdBean {
                    /**
                     * fa : 08
                     * fb : 04
                     */

                    private String fa;
                    private String fb;

                    public String getFa() {
                        return fa;
                    }

                    public void setFa(String fa) {
                        this.fa = fa;
                    }

                    public String getFb() {
                        return fb;
                    }

                    public void setFb(String fb) {
                        this.fb = fb;
                    }
                }
            }

            public static class Day20180626Bean {
                /**
                 * date : 20180626
                 * temperature : 27℃~32℃
                 * weather : 雷阵雨
                 * weather_id : {"fa":"04","fb":"04"}
                 * week : 星期二
                 * wind : 持续无风向微风
                 */

                private String date;
                private String temperature;
                private String weather;
                private WeatherIdBeanX weather_id;
                private String week;
                private String wind;

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }

                public String getWeather() {
                    return weather;
                }

                public void setWeather(String weather) {
                    this.weather = weather;
                }

                public WeatherIdBeanX getWeather_id() {
                    return weather_id;
                }

                public void setWeather_id(WeatherIdBeanX weather_id) {
                    this.weather_id = weather_id;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getWind() {
                    return wind;
                }

                public void setWind(String wind) {
                    this.wind = wind;
                }

                public static class WeatherIdBeanX {
                    /**
                     * fa : 04
                     * fb : 04
                     */

                    private String fa;
                    private String fb;

                    public String getFa() {
                        return fa;
                    }

                    public void setFa(String fa) {
                        this.fa = fa;
                    }

                    public String getFb() {
                        return fb;
                    }

                    public void setFb(String fb) {
                        this.fb = fb;
                    }
                }
            }

            public static class Day20180627Bean {
                /**
                 * date : 20180627
                 * temperature : 27℃~32℃
                 * weather : 多云
                 * weather_id : {"fa":"01","fb":"01"}
                 * week : 星期三
                 * wind : 持续无风向微风
                 */

                private String date;
                private String temperature;
                private String weather;
                private WeatherIdBeanXX weather_id;
                private String week;
                private String wind;

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }

                public String getWeather() {
                    return weather;
                }

                public void setWeather(String weather) {
                    this.weather = weather;
                }

                public WeatherIdBeanXX getWeather_id() {
                    return weather_id;
                }

                public void setWeather_id(WeatherIdBeanXX weather_id) {
                    this.weather_id = weather_id;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getWind() {
                    return wind;
                }

                public void setWind(String wind) {
                    this.wind = wind;
                }

                public static class WeatherIdBeanXX {
                    /**
                     * fa : 01
                     * fb : 01
                     */

                    private String fa;
                    private String fb;

                    public String getFa() {
                        return fa;
                    }

                    public void setFa(String fa) {
                        this.fa = fa;
                    }

                    public String getFb() {
                        return fb;
                    }

                    public void setFb(String fb) {
                        this.fb = fb;
                    }
                }
            }

            public static class Day20180628Bean {
                /**
                 * date : 20180628
                 * temperature : 27℃~33℃
                 * weather : 多云
                 * weather_id : {"fa":"01","fb":"01"}
                 * week : 星期四
                 * wind : 持续无风向微风
                 */

                private String date;
                private String temperature;
                private String weather;
                private WeatherIdBeanXXX weather_id;
                private String week;
                private String wind;

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }

                public String getWeather() {
                    return weather;
                }

                public void setWeather(String weather) {
                    this.weather = weather;
                }

                public WeatherIdBeanXXX getWeather_id() {
                    return weather_id;
                }

                public void setWeather_id(WeatherIdBeanXXX weather_id) {
                    this.weather_id = weather_id;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getWind() {
                    return wind;
                }

                public void setWind(String wind) {
                    this.wind = wind;
                }

                public static class WeatherIdBeanXXX {
                    /**
                     * fa : 01
                     * fb : 01
                     */

                    private String fa;
                    private String fb;

                    public String getFa() {
                        return fa;
                    }

                    public void setFa(String fa) {
                        this.fa = fa;
                    }

                    public String getFb() {
                        return fb;
                    }

                    public void setFb(String fb) {
                        this.fb = fb;
                    }
                }
            }

            public static class Day20180629Bean {
                /**
                 * date : 20180629
                 * temperature : 27℃~32℃
                 * weather : 多云
                 * weather_id : {"fa":"01","fb":"01"}
                 * week : 星期五
                 * wind : 持续无风向微风
                 */

                private String date;
                private String temperature;
                private String weather;
                private WeatherIdBeanXXXX weather_id;
                private String week;
                private String wind;

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }

                public String getWeather() {
                    return weather;
                }

                public void setWeather(String weather) {
                    this.weather = weather;
                }

                public WeatherIdBeanXXXX getWeather_id() {
                    return weather_id;
                }

                public void setWeather_id(WeatherIdBeanXXXX weather_id) {
                    this.weather_id = weather_id;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getWind() {
                    return wind;
                }

                public void setWind(String wind) {
                    this.wind = wind;
                }

                public static class WeatherIdBeanXXXX {
                    /**
                     * fa : 01
                     * fb : 01
                     */

                    private String fa;
                    private String fb;

                    public String getFa() {
                        return fa;
                    }

                    public void setFa(String fa) {
                        this.fa = fa;
                    }

                    public String getFb() {
                        return fb;
                    }

                    public void setFb(String fb) {
                        this.fb = fb;
                    }
                }
            }

            public static class Day20180630Bean {
                /**
                 * date : 20180630
                 * temperature : 27℃~32℃
                 * weather : 雷阵雨
                 * weather_id : {"fa":"04","fb":"04"}
                 * week : 星期六
                 * wind : 持续无风向微风
                 */

                private String date;
                private String temperature;
                private String weather;
                private WeatherIdBeanXXXXX weather_id;
                private String week;
                private String wind;

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }

                public String getWeather() {
                    return weather;
                }

                public void setWeather(String weather) {
                    this.weather = weather;
                }

                public WeatherIdBeanXXXXX getWeather_id() {
                    return weather_id;
                }

                public void setWeather_id(WeatherIdBeanXXXXX weather_id) {
                    this.weather_id = weather_id;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getWind() {
                    return wind;
                }

                public void setWind(String wind) {
                    this.wind = wind;
                }

                public static class WeatherIdBeanXXXXX {
                    /**
                     * fa : 04
                     * fb : 04
                     */

                    private String fa;
                    private String fb;

                    public String getFa() {
                        return fa;
                    }

                    public void setFa(String fa) {
                        this.fa = fa;
                    }

                    public String getFb() {
                        return fb;
                    }

                    public void setFb(String fb) {
                        this.fb = fb;
                    }
                }
            }

            public static class Day20180701Bean {
                /**
                 * date : 20180701
                 * temperature : 27℃~33℃
                 * weather : 多云
                 * weather_id : {"fa":"01","fb":"01"}
                 * week : 星期日
                 * wind : 持续无风向微风
                 */

                private String date;
                private String temperature;
                private String weather;
                private WeatherIdBeanXXXXXX weather_id;
                private String week;
                private String wind;

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }

                public String getWeather() {
                    return weather;
                }

                public void setWeather(String weather) {
                    this.weather = weather;
                }

                public WeatherIdBeanXXXXXX getWeather_id() {
                    return weather_id;
                }

                public void setWeather_id(WeatherIdBeanXXXXXX weather_id) {
                    this.weather_id = weather_id;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getWind() {
                    return wind;
                }

                public void setWind(String wind) {
                    this.wind = wind;
                }

                public static class WeatherIdBeanXXXXXX {
                    /**
                     * fa : 01
                     * fb : 01
                     */

                    private String fa;
                    private String fb;

                    public String getFa() {
                        return fa;
                    }

                    public void setFa(String fa) {
                        this.fa = fa;
                    }

                    public String getFb() {
                        return fb;
                    }

                    public void setFb(String fb) {
                        this.fb = fb;
                    }
                }
            }
        }

        public static class SkBean {
            /**
             * humidity : 79%
             * temp : 29
             * time : 11:09
             * wind_direction : 南风
             * wind_strength : 2级
             */

            private String humidity;
            private String temp;
            private String time;
            private String wind_direction;
            private String wind_strength;

            public String getHumidity() {
                return humidity;
            }

            public void setHumidity(String humidity) {
                this.humidity = humidity;
            }

            public String getTemp() {
                return temp;
            }

            public void setTemp(String temp) {
                this.temp = temp;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getWind_direction() {
                return wind_direction;
            }

            public void setWind_direction(String wind_direction) {
                this.wind_direction = wind_direction;
            }

            public String getWind_strength() {
                return wind_strength;
            }

            public void setWind_strength(String wind_strength) {
                this.wind_strength = wind_strength;
            }
        }

        public static class TodayBean {
            /**
             * city : 深圳
             * comfort_index :
             * date_y : 2018年06月25日
             * dressing_advice : 天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。
             * dressing_index : 炎热
             * drying_index :
             * exercise_index : 较适宜
             * temperature : 26℃~31℃
             * travel_index : 较适宜
             * uv_index : 中等
             * wash_index : 较适宜
             * weather : 中雨转雷阵雨
             * weather_id : {"fa":"08","fb":"04"}
             * week : 星期一
             * wind : 持续无风向微风
             */

            private String city;
            private String comfort_index;
            private String date_y;
            private String dressing_advice;
            private String dressing_index;
            private String drying_index;
            private String exercise_index;
            private String temperature;
            private String travel_index;
            private String uv_index;
            private String wash_index;
            private String weather;
            private WeatherIdBeanXXXXXXX weather_id;
            private String week;
            private String wind;

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getComfort_index() {
                return comfort_index;
            }

            public void setComfort_index(String comfort_index) {
                this.comfort_index = comfort_index;
            }

            public String getDate_y() {
                return date_y;
            }

            public void setDate_y(String date_y) {
                this.date_y = date_y;
            }

            public String getDressing_advice() {
                return dressing_advice;
            }

            public void setDressing_advice(String dressing_advice) {
                this.dressing_advice = dressing_advice;
            }

            public String getDressing_index() {
                return dressing_index;
            }

            public void setDressing_index(String dressing_index) {
                this.dressing_index = dressing_index;
            }

            public String getDrying_index() {
                return drying_index;
            }

            public void setDrying_index(String drying_index) {
                this.drying_index = drying_index;
            }

            public String getExercise_index() {
                return exercise_index;
            }

            public void setExercise_index(String exercise_index) {
                this.exercise_index = exercise_index;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getTravel_index() {
                return travel_index;
            }

            public void setTravel_index(String travel_index) {
                this.travel_index = travel_index;
            }

            public String getUv_index() {
                return uv_index;
            }

            public void setUv_index(String uv_index) {
                this.uv_index = uv_index;
            }

            public String getWash_index() {
                return wash_index;
            }

            public void setWash_index(String wash_index) {
                this.wash_index = wash_index;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public WeatherIdBeanXXXXXXX getWeather_id() {
                return weather_id;
            }

            public void setWeather_id(WeatherIdBeanXXXXXXX weather_id) {
                this.weather_id = weather_id;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getWind() {
                return wind;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }

            public static class WeatherIdBeanXXXXXXX {
                /**
                 * fa : 08
                 * fb : 04
                 */

                private String fa;
                private String fb;

                public String getFa() {
                    return fa;
                }

                public void setFa(String fa) {
                    this.fa = fa;
                }

                public String getFb() {
                    return fb;
                }

                public void setFb(String fb) {
                    this.fb = fb;
                }
            }
        }
    }
}
