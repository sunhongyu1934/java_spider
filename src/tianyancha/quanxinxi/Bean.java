package tianyancha.quanxinxi;

import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */
public class Bean {
    public static class Duiwaitouzi{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> result;
            public static class detail{
                public String name="";
                public String legalPersonName="";
                public String regCapital="";
                public String amount="";
                public String percent="";
                public String estiblishTime="";
                public String regStatus="";
                public String business_scope="";
                public String id="";
            }
        }
    }

    public static class Zhuyaorenyuan{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> result;
            public static class detail{
                public List<String> typeJoin;
                public String name;
            }
        }
    }

    public static class Gudongxinxi{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> result;
            public static class detail{
                public List<dea> capital;
                public String name;
                public static class dea{
                    public String percent;
                    public String amomon;
                    public String time;
                }
            }
        }
    }



    public static class Biangeng{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> result;
            public static class detail{
                public String changeItem="";
                public String changeTime="";
                public String contentBefore="";
                public String contentAfter="";
            }
        }
    }

    public static class Fenzhi{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> result;
            public static class detail{
                public String name="";
                public String legalPersonName="";
                public String regStatus="";
                public String estiblishTime="";
                public String category="";
            }
        }
    }


    public static class Finacning{
        public Da data;
        public String message;
        public static class Da{
            public Pa page;
            public static class Pa{
                public List<detail> rows;
                public static class detail{
                    public String date="";
                    public String round="";
                    public String money="";
                    public String rongziMap="";
                    public String newsUrl="";
                    public String value="";
                    public String share="";
                }
            }
        }
    }

    public static class Hexintuandui{
        public Da data;
        public String message;
        public static class Da{
            public Pa page;
            public static class Pa{
                public List<detail> rows;
                public static class detail{
                    public String icon="";
                    public String name="";
                    public String title="";
                    public String desc="";
                }
            }
        }
    }


    public static class Qiyeyewu{
        public Da data;
        public String message;
        public static class Da{
            public Pa page;
            public static class Pa{
                public List<detail> rows;
                public static class detail{
                    public String logo="";
                    public String product="";
                    public String hangye="";
                    public String yewu="";
                }
            }
        }
    }


    public static class Touzishijian{
        public Da data;
        public String message;
        public static class Da{
            public Pa page;
            public static class Pa{
                public List<detail> rows;
                public static class detail{
                    public String tzdate="";
                    public String lunci="";
                    public String money="";
                    public String rongzi_map="";
                    public String product="";
                    public String location="";
                    public String hangye1="";
                    public String yewu="";
                }
            }
        }
    }


    public static class Jingpinxinxi{
        public Da data;
        public String message;
        public static class Da{
            public Pa page;
            public static class Pa{
                public List<detail> rows;
                public static class detail{
                    public String jingpinProduct="";
                    public String location="";
                    public String round="";
                    public String hangye="";
                    public String yewu="";
                    public String setupDate="";
                    public String value="";
                }
            }
        }
    }

    public static class Falvsusong{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> items;
            public static class detail{
                public String submittime="";
                public String uuid="";
                public String casetype="";
                public String caseno="";
            }
        }
    }


    public static class Fayuangonggao{
        public String message;
        public List<detail> courtAnnouncements;
        public static class detail{
            public String publishdate="";
            public String party1="";
            public String party2="";
            public String bltntypename="";
            public String courtcode="";
            public String content="";
        }
    }


    public static class Beizhixingren{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> items;
            public static class detail{
                public String caseCreateTime="";
                public String execMoney="";
                public String caseCode="";
                public String execCourtName="";
            }
        }
    }


    public static class Shixinren{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> items;
            public static class detail{
                public String publishdate="";
                public String gistid="";
                public String courtname="";
                public String performance="";
                public String casecode="";
                public String duty="";
            }
        }
    }

    public static class Jingyingyichang{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> result;
            public static class detail{
                public String putDate="";
                public String putReason="";
                public String putDepartment="";
                public String removeDate="";
                public String removeReason="";
                public String removeDepartment="";
            }
        }
    }


    public static class Xingzhengchufa{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> items;
            public static class detail{
                public String decisionDate="";
                public String punishNumber="";
                public String type="";
                public String departmentName="";
            }
        }
    }


    public static class Yanzhongweifa{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> items;
            public static class detail{
                public String putDate="";
                public String putReason="";
                public String putDepartment="";
            }
        }
    }


    public static class Guquanchuzhi{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> items;
            public static class detail{
                public String regNumber="";
                public String pledgor="";
                public String pledgee="";
                public String regDate="";
                public String state="";
                public String equityAmount="";
            }
        }
    }


    public static class Dongchandiya{
        public String data;
        public static class Dongchan{
            public List<details> items;
            public static class details{
                public Base baseInfo;
                public static class Base{
                    public String regDate="";
                    public String regNum="";
                    public String type="";
                    public String regDepartment="";
                    public String status="";
                }
            }
        }
    }


    public static class Qianshuigonggao{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> items;
            public static class detail{
                public String publishDate="";
                public String personIdNumber="";
                public String taxCategory="";
                public String ownTaxAmount="";
            }
        }
    }


    public static class Zhaotoubiao{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> items;
            public static class detail{
                public String publishTime="";
                public String title="";
                public String purchaser="";
                public String content="";
                public String abs="";
                public String intro="";
            }
        }
    }

    public static class Zhaiquanxinxi{
        public Da data;
        public static class Da{
            public List<detail> bondList;
            public static class detail{
                public String publishTime="";
                public String bondName="";
                public String bondNum="";
                public String bondType="";
                public String debtRating="";
                public String tip="";
            }
        }
    }


    public static class Goudixinxi{
        public Da data;
        public static class Da{
            public List<detail> companyPurchaseLandList;
            public static class detail{
                public String signedDate="";
                public String elecSupervisorNo="";
                public String startTime="";
                public String totalArea="";
                public String location="";
                public String purpose="";
            }
        }
    }


    public static class Zhaopin{
        public Da data;
        public static class Da{
            public List<detail> companyEmploymentList;
            public static class detail{
                public String updateTime="";
                public String title="";
                public String oriSalary="";
                public String experience="";
                public String employerNumber="";
                public String city="";
                public String description="";
            }
        }
    }

    public static class Shuiwupingji{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> items;
            public static class detail{
                public String year="";
                public String grade="";
                public String type="";
                public String idNumber="";
                public String evalDepartment="";
            }
        }
    }


    public static class Choucha{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> items;
            public static class detail{
                public String checkDate="";
                public String checkType="";
                public String checkResult="";
                public String checkOrg="";
            }
        }
    }

    public static class Chanpin{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> items;
            public static class detail{
                public String icon="";
                public String name="";
                public String filterName="";
                public String type="";
                public String classes="";
                public String brief="";
            }
        }
    }

    public static class Zizhizhzengshu{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> items;
            public static class detail{
                public String deviceName="";
                public String licenceType="";
                public String issueDate="";
                public String toDate="";
                public String deviceType="";
                public String licenceNum="";
            }
        }
    }

    public static class Shangbiaoxinxi{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> items;
            public static class detail{
                public String appDate="";
                public String tmPic="";
                public String tmName="";
                public String regNo="";
                public String intCls="";
                public String status="";
            }
        }
    }


    public static class Zhuanli{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> items;
            public static class detail{
                public String applicationPublishTime="";
                public String patentName="";
                public String patentNum="";
                public String applicationPublishNum="";
                public String agency="";
                public String inventor="";
                public String agent="";
                public String imgUrl="";
                public String allCatNum="";
                public String abstracts="";
                public String address="";
                public String patentType="";
            }
        }
    }


    public static class Zhuzuoquan{
        public Da data;
        public String message;
        public static class Da{
            public List<detail> items;
            public static class detail{
                public String regtime="";
                public String fullname="";
                public String simplename="";
                public String regnum="";
                public String catnum="";
                public String version="";
                public String authorNationality="";
            }
        }
    }


    public static class Wangzhanbeian{
        public List<detail> data;
        public static class detail{
            public List<String> webSite;
            public String examineDate="";
            public String webName="";
            public String ym="";
            public String liscense="";
            public String companyType="";
        }
    }

    public static class Sousuo{
        public List<detail> data;
        public static class detail{
            public String id="";
            public String name="";
            public String legalPersonName="";
            public String estiblishTime="";
            public String regCapital="";
            public String regStatus="";
            public String businessScope="";
            public String regLocation="";
            public String phone="";
            public String logo="";
            public String emails="";
            public String websites="";

        }
    }


}
