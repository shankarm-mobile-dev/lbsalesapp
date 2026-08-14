package com.suguna.lbsalesperson.Sendreceiver;

import com.suguna.lbsalesperson.Utils.LBConstant;

public class WebserviceURLs {
    @SuppressWarnings("unused")
     static  int production = 1;
    @SuppressWarnings("unused")
    protected static final int development = 2;
    @SuppressWarnings("unused")
    protected static final int testing = 3;
    private String LsloginPage;
    private String ReportWs;
    private String UpDownWs;
    private String HatcheryWs;
    private String updateURL;
    private String HatcheryReportWs;
    private String PriceListWs;

    public static int getProduction() {
        return production;
    }

    public static int getDevelopment() {
        return development;
    }

    public static int getTesting() {
        return testing;
    }

    public static String getProd_ipandport() {
        return prod_ipandport;
    }

    public static String getDev_ipandport() {
        return dev_ipandport;
    }

    public static String getTest_ipandport() {
        return test_ipandport;
    }

    // Change in DataAccessManager file also
    //private final String prod_ipandport = "223.30.13.106:8083";// production url
   // public static final String prod_ipandport = "rise.sgitsys.com:8094";// production url
    public static final String prod_ipandport = "https://salesapps.sgitsys.com:8443/lborder";// production url
    //private final String dev_ipandport = "10.10.0.175:8080";// local
    //public    static final String dev_ipandport = "http://10.5.1.228/lborderappWs/index.php";// test	server url
    public    static final String dev_ipandport = "http://10.11.1.124:8090";// test	shankar system url
    //private final String test_ipandport = "10.10.0.227:8090";// test
    public static final String test_ipandport = "http://10.5.1.227:8080/lborder";// test for mobile data connectivity url

    //public static final String test_ipandport = "https://bankuat.sgitsys.com:8443/lborder";// test for mobile data connectivity url
    String instance = "";

    public WebserviceURLs() {
         // processUrlDecision(development);
            processUrlDecision(testing);
         // processUrlDecision(production);
    }

    public String getLsloginPage() {
        return LsloginPage;
    }
    public void setLsloginPage(String LsloginPage) {
        this.LsloginPage = LsloginPage;
    }
    public String getReportWs() {
        return ReportWs;
    }
    public void setReportWs(String ReportWs) {
        this.ReportWs = ReportWs;
    }
    public String getUpDownWs() {
        return UpDownWs;
    }
    public void setUpDownWs(String UpDownWs) {
        this.UpDownWs = UpDownWs;
    }
    public String getUpdateURL() {
        return updateURL;
    }
    public void setUpdateURL(String updateURL) {
        this.updateURL = updateURL;
    }
    public String getHatcheryWs() {
        return HatcheryWs;
    }
    public void setHatcheryWs(String HatcheryWs) {
        this.HatcheryWs = HatcheryWs;
    }
    public String getHatcheryReportWs() {
        return HatcheryReportWs;
    }
    public void setHatcheryReportWs(String HatcheryReportWs) {
        this.HatcheryReportWs = HatcheryReportWs;
    }

    private void processUrlDecision(int urlFor) {
        LBConstant.URL_TYPE = urlFor;
        switch (urlFor) {
            case 1:
                // production
                instance = prod_ipandport;
                urlSetter(instance);
                break;
            case 2:
                // Development
                instance = dev_ipandport;
                urlSetter(instance);
                break;
            case 3:
                // Testing
                instance = test_ipandport;
                urlSetter(instance);
                break;
            default:
                break;
        }
    }
    private void urlSetter(String p_instance)
    {
        setLsloginPage( p_instance + "/index.php");
        setUpDownWs( p_instance
                + "/api/");
        setHatcheryWs( p_instance
                + "/api/Customernew/");
        setHatcheryReportWs( p_instance
                + "/api/Salesapp/");
        setUpdateURL( p_instance
                + "/apk/HatcheryManagement.apk");

    }
}

