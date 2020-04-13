package com.zkhc.zkhc_dd.service;

import com.zkhc.zkhc_dd.entity.DD;
import com.zkhc.zkhc_dd.util.UUIDUtil;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
@Service
public class DDService {
    private static final Logger LOGGER =  LoggerFactory.getLogger(DDService.class);
    private static HttpClient httpclient=null;
    @Autowired
    private DD dd;
    /**
     * HttpClient
     */
    public static void createHttpClient(){
        try {
            httpclient= HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.custom().loadTrustMaterial(null,new TrustSelfSignedStrategy()).build())).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getHost(){
        String HOST = "未知";
        try {
            HOST = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return HOST;
    }

    private String getStartMsg(){
        return dd.getName()+"\t启动成功\\n" +
                "        启动环境：" + dd.getActive()  + "\n" +
                "        启动地址：" + this.getHost()+ "\n" +
                "        访问地址：" + dd.getPublishInfo();
    }

    private String getTextMsg(String message_con){
        return "{ \"msgtype\": \"text\", \"text\": {\"content\": \"" +
                message_con
                        .replaceAll("\"", "'")
                + "\"}}";
    }

    private String getAliMsg(String message_con){
        return "{ \"msgtype\": \"text\", \"text\": {\"content\": \"" + message_con + "\"},\"at\":{\"atMobiles\": [\"17610996162\"],\"isAtAll\": false}}";
    }

    private String getAddExceptionMsg(String id, String system, String runEnvironment, String host, int port, String exception){
        return "{ \"id\": \""
                + id + "\",\"system\": \""
                + system + "\", \"runEnvironment\": \""
                + runEnvironment + "\",\"host\": \""
                + host + "\",\"port\": \""
                + port + "\",\"exception\": \""
                + exception
                .replaceAll("\n", "</br>")
                .replaceAll("\"", "'") + "\"}";
    }

    private String getExceptionMsg(String id, String system, String runEnvironment, String host, int port, String exception){
        return "{ 'msgtype': 'markdown', " +
                " 'markdown': {'title':'后台系统运行时异常', 'text':'后台系统运行时异常，错误信息如下: \n" +
                "\n-当前系统:" + system + "    ip地址:" + host + "\n" +
                "\n-当前环境:" + runEnvironment + "     端口:" + port + "\n" +
                "\n-异常信息:" + exception.split("\n")[0] + "...\n" +
                "\n[详情查看](" + dd.getShowUrl() + id + ")\n" +
                " ' }," +
                "'at': {" +
                "         'atMobiles': [" +
                "             '18612660303', '18538834689'" +
                "         ]," +
                "         'isAtAll': 'false'" +
                "  }" +
                "}";
    }

    private String getDealMsg(String id, String message){
        return "{ 'msgtype': 'markdown', " +
                " 'markdown': {'title':'异常处理', 'text':'异常处理信息: \n" +
                "\n"+message+"\n"+
                "\n[详情查看](" + dd.getShowUrl() + id + ")\n" +
                " ' }," +
                "}";
    }

    /**
     * 发送消息
     * @param textMsg
     * @param token
     */
    private void sendMessage(String textMsg, String token) {
        try {
            if(httpclient==null){
                createHttpClient();
            }
            HttpPost httppost = new HttpPost(token);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity se = new StringEntity(textMsg, "utf-8");
            httppost.setEntity(se);

            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                LOGGER.info("钉钉消息请求成功，return:>>"+result);
            }
        } catch (IOException e) {
            LOGGER.error("钉钉消息请求失败:>>"+e.getMessage());
        }
    }


    /**
     * @param exception 异常信息
     */
    private void error(String exception) {
        String id = UUIDUtil.getUUID();
        int port = Integer.parseInt(dd.getPort());
        insertException(id, dd.getName(), dd.getActive(), this.getHost(), port, exception);
        sendExceptionMsg(id, dd.getName(), dd.getActive(), this.getHost(), port, exception);
    }

    public void exception(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        exception.printStackTrace(pw);
        String stackTraceString = sw.getBuffer().toString();
        if(dd.getOpen().equals("true")){
            error(stackTraceString);
        }
    }

    public void exception(String exception) {
        if(dd.getOpen().equals("true")) {
            error(exception);
        }
    }

    private void warn(String message_con) {
        if(dd.getOpen().equals("true")) {
            sendMessage(this.getTextMsg(message_con), dd.getAccessToken());
        }
    }
    public void devSend(String message_con) {
        if(dd.getOpen().equals("true")) {
            sendMessage(this.getTextMsg(message_con), dd.getDevToken());
        }
    }

    public void testSend(String message_con) {
        if(dd.getOpen().equals("true")) {
            sendMessage(this.getTextMsg(message_con), dd.getTestToken());
        }
    }

    public void proSend(String message_con) {
        if(dd.getOpen().equals("true")) {
            sendMessage(this.getTextMsg(message_con), dd.getProToken());
        }
    }

    public void send(String message_con) {
        if(dd.getOpen().equals("true")) {
            sendMessage(this.getTextMsg(message_con), dd.getAccessToken());
        }
    }

    public void sendAliMsg(String message_con) {
        if(dd.getOpen().equals("true")) {
            sendMessage(this.getAliMsg(message_con), dd.getAliMsgToken());
        }
    }

    public void sendDealMsg(String id,String message_con) {
        if(dd.getOpen().equals("true")) {
            sendMessage(this.getDealMsg(id, message_con), dd.getAccessToken());
        }
    }

    public void sendStartUp() {
        if(dd.getOpen().equals("true")) {
            sendMessage(this.getTextMsg(this.getStartMsg()), dd.getAccessToken());
        }
    }

    private void insertException(String id, String system, String runEnvironment, String host, int port, String exception) {
        sendMessage(this.getAddExceptionMsg(id, system,runEnvironment,host,port,exception), dd.getAddUrl());
    }

    private void sendExceptionMsg(String id, String system, String runEnvironment, String host, int port, String exception) {
        sendMessage(this.getExceptionMsg(id, system,runEnvironment,host,port,exception), dd.getAccessToken());
    }
}
