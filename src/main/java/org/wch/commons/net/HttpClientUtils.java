package org.wch.commons.net;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.*;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wch.commons.collections.CollectionUtils;
import org.wch.commons.lang.Base64Utils;
import org.wch.commons.lang.StringUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: TODO
 * @Author: wuchu
 * @CreateTime: 2023-04-26 14:56
 */
public class HttpClientUtils {

    Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);


    //匹配10.0.0.0 - 10.255.255.255的网段
    private final String pattern_10 = "^(\\D)*10(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){3}";

    //匹配172.16.0.0 - 172.31.255.255的网段
    private final String pattern_172 = "172\\.([1][6-9]|[2]\\d|3[01])(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}";

    //匹配192.168.0.0 - 192.168.255.255的网段
    private final String pattern_192 = "192\\.168(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}";

    // 合起来写
    private static final String pattern = "((192\\.168|172\\.([1][6-9]|[2]\\d|3[01]))"
            + "(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}|"
            + "^(\\D)*10(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){3})";

    private static final RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
            .setConnectionRequestTimeout(60, TimeUnit.SECONDS)
            .setResponseTimeout(60, TimeUnit.SECONDS)
            .build();
    private static final List<Header> HEADERS = Arrays.asList(
            new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString()),
            new BasicHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString()));

    // region http request methods
    public static Optional<JSONObject> doGetByHttp(HttpParams httpParams) {
        if (Objects.isNull(httpParams)) {
            return Optional.empty();
        }
        Optional<String> optional = httpParams.getRequestURL();
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        try (CloseableHttpClient httpclient = getCloseableHttpClient()) {
            HttpGet httpGet = new HttpGet(optional.get());
            JSONObject resp = doHttpRequest(httpclient, httpGet, true);
            return Optional.of(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<JSONObject> doPostByHttp(HttpParams httpParams) {
        if (Objects.isNull(httpParams)) {
            return Optional.empty();
        }
        Optional<String> optional = httpParams.getRequestURL();
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        try (CloseableHttpClient httpclient = getCloseableHttpClient()) {
            HttpPost httpPost = new HttpPost(optional.get());
            StringEntity stringEntity = getStringEntity(httpParams.getBodyParams());
            httpPost.setEntity(stringEntity);

            JSONObject resp = doHttpRequest(httpclient, httpPost, true);
            return Optional.of(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<JSONObject> doPutByHttp(HttpParams httpParams) {
        if (Objects.isNull(httpParams)) {
            return Optional.empty();
        }
        Optional<String> optional = httpParams.getRequestURL();
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        try (CloseableHttpClient httpclient = getCloseableHttpClient()) {
            HttpPut httpPut = new HttpPut(optional.get());
            StringEntity stringEntity = getStringEntity(httpParams.getBodyParams());
            httpPut.setEntity(stringEntity);
            JSONObject resp = doHttpRequest(httpclient, httpPut, true);
            return Optional.of(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<JSONObject> doDeleteByHttp(HttpParams httpParams) {
        if (Objects.isNull(httpParams)) {
            return Optional.empty();
        }
        Optional<String> optional = httpParams.getRequestURL();
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        try (CloseableHttpClient httpclient = getCloseableHttpClient()) {
            HttpDelete httpGet = new HttpDelete(optional.get());
            JSONObject resp = doHttpRequest(httpclient, httpGet, true);
            return Optional.of(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // endregion

    // region https request methods
    public static Optional<JSONObject> doGetByHttps(HttpParams httpParams) {
        if (Objects.isNull(httpParams)) {
            return Optional.empty();
        }
        Optional<String> optional = httpParams.getRequestURL();
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        try (CloseableHttpClient httpclient = getCloseableHttpsClient()) {
            HttpGet httpGet = new HttpGet(optional.get());
            JSONObject resp = doHttpRequest(httpclient, httpGet, true);
            return Optional.of(resp);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<JSONObject> doGetByHttps(HttpParams httpParams, String certificatePath, String secretKey) {
        if (Objects.isNull(httpParams)) {
            return Optional.empty();
        }
        Optional<String> optional = httpParams.getRequestURL();
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        try (CloseableHttpClient httpclient = getCloseableHttpsClient(false, certificatePath, secretKey)) {
            HttpGet httpGet = new HttpGet(optional.get());
            JSONObject resp = doHttpRequest(httpclient, httpGet, true);
            return Optional.of(resp);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<JSONObject> doPostByHttps(HttpParams httpParams) {
        if (Objects.isNull(httpParams)) {
            return Optional.empty();
        }
        Optional<String> optional = httpParams.getRequestURL();
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        try (CloseableHttpClient httpclient = getCloseableHttpsClient()) {
            HttpPost httpPost = new HttpPost(optional.get());
            StringEntity stringEntity = getStringEntity(httpParams.getBodyParams());
            httpPost.setEntity(stringEntity);

            JSONObject resp = doHttpRequest(httpclient, httpPost, true);
            return Optional.of(resp);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<JSONObject> doPostByHttps(HttpParams httpParams, String certificatePath, String secretKey) {
        if (Objects.isNull(httpParams)) {
            return Optional.empty();
        }
        Optional<String> optional = httpParams.getRequestURL();
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        try (CloseableHttpClient httpclient = getCloseableHttpsClient(false, certificatePath, secretKey)) {
            HttpPost httpPost = new HttpPost(optional.get());
            StringEntity stringEntity = getStringEntity(httpParams.getBodyParams());
            httpPost.setEntity(stringEntity);
            JSONObject resp = doHttpRequest(httpclient, httpPost, true);
            return Optional.of(resp);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<JSONObject> doPutByHttps(HttpParams httpParams) {
        if (Objects.isNull(httpParams)) {
            return Optional.empty();
        }
        Optional<String> optional = httpParams.getRequestURL();
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        try (CloseableHttpClient httpclient = getCloseableHttpsClient()) {
            HttpPut httpPut = new HttpPut(optional.get());
            StringEntity stringEntity = getStringEntity(httpParams.getBodyParams());
            httpPut.setEntity(stringEntity);
            JSONObject resp = doHttpRequest(httpclient, httpPut, true);
            return Optional.of(resp);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<JSONObject> doPutByHttps(HttpParams httpParams, String certificatePath, String secretKey) {
        if (Objects.isNull(httpParams)) {
            return Optional.empty();
        }
        Optional<String> optional = httpParams.getRequestURL();
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        try (CloseableHttpClient httpclient = getCloseableHttpsClient(false, certificatePath, secretKey)) {
            HttpPut httpPut = new HttpPut(optional.get());
            StringEntity stringEntity = getStringEntity(httpParams.getBodyParams());
            httpPut.setEntity(stringEntity);
            JSONObject resp = doHttpRequest(httpclient, httpPut, true);
            return Optional.of(resp);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<JSONObject> doDeleteByHttps(HttpParams httpParams) {
        if (Objects.isNull(httpParams)) {
            return Optional.empty();
        }
        Optional<String> optional = httpParams.getRequestURL();
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        try (CloseableHttpClient httpclient = getCloseableHttpsClient()) {
            HttpDelete httpGet = new HttpDelete(optional.get());
            JSONObject resp = doHttpRequest(httpclient, httpGet, true);
            return Optional.of(resp);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<JSONObject> doDeleteByHttps(HttpParams httpParams, String certificatePath, String secretKey) {
        if (Objects.isNull(httpParams)) {
            return Optional.empty();
        }
        Optional<String> optional = httpParams.getRequestURL();
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        try (CloseableHttpClient httpclient = getCloseableHttpsClient(false, certificatePath, secretKey)) {
            HttpDelete httpGet = new HttpDelete(optional.get());
            JSONObject resp = doHttpRequest(httpclient, httpGet, true);
            return Optional.of(resp);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    // endregion

    // region file upload

    /**
     * upload single or multi files
     * 注意: 在使用 HttpMultipartMode 时对 HttpEntity 设置 Header 要谨慎, 因为 HttpClient
     * 会对 Content-Type增加 Boundary 后缀, 而这个是服务端判断文件边界的重要参数. 如果设置自定义 Header,
     * 需要检查 boundary 是否正确生成. 如果没有的话需要自定义 Content-Type 将 boundary 加进去,
     * 并且通过 EntityBuilder.setBoundary() 将自定义的 boundary 值传给 HttpEntity.
     *
     * @param httpParams
     * @return
     */
    public static Optional<JSONObject> doPostUploadByHttp(HttpParams httpParams) {
        if (Objects.isNull(httpParams)) {
            return Optional.empty();
        }
        Optional<String> optional = httpParams.getRequestURL();
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        try (CloseableHttpClient httpclient = getCloseableHttpClient()) {
            return getPostJsonObject(httpParams, optional.get(), httpclient);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // release resources
            if (CollectionUtils.isNotEmpty(httpParams.getFiles())) {
                httpParams.getFiles().forEach(HttpParams.MultiPartFiles::close);
            }
        }
    }

    public static Optional<JSONObject> doPutUploadByHttp(HttpParams httpParams) {
        if (Objects.isNull(httpParams)) {
            return Optional.empty();
        }
        Optional<String> optional = httpParams.getRequestURL();
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        try (CloseableHttpClient httpclient = getCloseableHttpClient()) {
            return getPutJsonObject(httpParams, optional.get(), httpclient);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // release resources
            if (CollectionUtils.isNotEmpty(httpParams.getFiles())) {
                httpParams.getFiles().forEach(HttpParams.MultiPartFiles::close);
            }
        }
    }

    public static Optional<JSONObject> doPostUploadByHttps(HttpParams httpParams) {
        if (Objects.isNull(httpParams)) {
            return Optional.empty();
        }
        Optional<String> optional = httpParams.getRequestURL();
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        try (CloseableHttpClient httpclient = getCloseableHttpsClient()) {
            return getPostJsonObject(httpParams, optional.get(), httpclient);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException e) {
            throw new RuntimeException(e);
        } finally {
            // release resources
            if (CollectionUtils.isNotEmpty(httpParams.getFiles())) {
                httpParams.getFiles().forEach(HttpParams.MultiPartFiles::close);
            }
        }
    }

    public static Optional<JSONObject> doPutUploadByHttps(HttpParams httpParams) {
        if (Objects.isNull(httpParams)) {
            return Optional.empty();
        }
        Optional<String> optional = httpParams.getRequestURL();
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        try (CloseableHttpClient httpclient = getCloseableHttpsClient()) {
            return getPutJsonObject(httpParams, optional.get(), httpclient);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException e) {
            throw new RuntimeException(e);
        } finally {
            // release resources
            if (CollectionUtils.isNotEmpty(httpParams.getFiles())) {
                httpParams.getFiles().forEach(HttpParams.MultiPartFiles::close);
            }
        }
    }

    // endregion

    // region 其他工具方法

    /**
     * 获取导出文件名称
     *
     * @param agent    浏览器类型（User-Agent字段）
     * @param prefix   名称前缀
     * @param fileName 指定文件名称
     * @return
     */
    public static String getExportFileName(String agent, String prefix, String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return fileName;
        }
        // 根据不同浏览器进行不同的编码
        try {
            String filenameEncoder = "";
            // 解决获得中文参数的乱码
            if (agent.contains("MSIE")) {
                // IE浏览器IE 11 以下版本
                filenameEncoder = URLEncoder.encode(fileName, "utf-8");
                filenameEncoder = filenameEncoder.replace("+", " ");
            } else if (agent.contains("Firefox")) {
                // 火狐浏览器
                filenameEncoder = "=?utf-8?B?" + Base64Utils.encodeToString(fileName.getBytes(StandardCharsets.UTF_8)) + "?=";
            } else {
                // 其它浏览器
                filenameEncoder = URLEncoder.encode(fileName, "utf-8");
            }
            return prefix + filenameEncoder;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return fileName;
        }
    }

    /**
     * 局域网ip一般为：
     * <p>
     * 10.0.0.0 - 10.255.255.255
     * 172.16.0.0 - 172.31.255.255
     * 192.168.0.0 - 192.168.255.255
     * </p>
     *
     * @param ipv4URL 4 version of ip url
     * @return
     */
    public static boolean isIPAddress(String ipv4URL) {
        if (StringUtils.isBlank(ipv4URL)) {
            return false;
        }
        Pattern reg = Pattern.compile(pattern);
        Matcher match = reg.matcher(ipv4URL);
        return match.find();
    }

    public static CloseableHttpClient getCloseableHttpsClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, CertificateException, IOException {
        return getCloseableHttpsClient(true, null, null);
    }

    public static CloseableHttpClient getCloseableHttpsClient(boolean ignoreCertificateVerify, String certificateFilePath, String key)
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, CertificateException, IOException {
        BasicCookieStore defaultCookieStore = new BasicCookieStore();
        SSLContext sslcontext;
        if (ignoreCertificateVerify) {
            sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustAllStrategy()).build();
        } else {
            // load certificate verify
            // 加载服务端提供的truststore(如果服务器提供truststore的话就不用忽略对服务器端证书的校验了)
            sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(new File(certificateFilePath), key.toCharArray(), new TrustSelfSignedStrategy()).build();
        }

        SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslcontext).build();
        HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslSocketFactory).build();
        return HttpClients.custom()
                .setDefaultCookieStore(defaultCookieStore)
                .setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG)
                .setConnectionManager(cm)
                .evictExpiredConnections()
                .build();
    }


    // region private functions

    private static CloseableHttpClient getCloseableHttpClient() {
        return HttpClientBuilder.create()
                // setting basic link time out
                .setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG)
                .build();
    }

    private static StringEntity getStringEntity(Object bodyParams) {
        String json = JSONObject.toJSONString(bodyParams);
        return new StringEntity(json, ContentType.APPLICATION_JSON);
    }

    private static JSONObject doHttpRequest(CloseableHttpClient httpclient, HttpUriRequestBase httpUriRequestBase, boolean addHeaders) throws IOException {
        if (addHeaders) {
            // setting headers parameters
            for (Header header : HEADERS) {
                httpUriRequestBase.addHeader(header);
            }
        }
        // custom response handler
        return httpclient.execute(httpUriRequestBase, response -> {
            String errMsg = null;
            String body = null;
            JSONObject jsonObject = new JSONObject();
            if (response.getEntity() != null) {
                if (response.getCode() == 200) {
                    body = EntityUtils.toString(response.getEntity());
                } else {
                    errMsg = EntityUtils.toString(response.getEntity());
                }
            } else {
                System.out.println("error msg:" + response.getCode());
                errMsg = "unknown error occur in http request";
            }
            jsonObject.put("code", response.getCode());
            jsonObject.put("errMsg", errMsg);
            jsonObject.put("data", body);
            return jsonObject;
        });
    }

    private static Optional<JSONObject> getPostJsonObject(HttpParams httpParams, String url, CloseableHttpClient httpclient) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        HttpEntity entity = httpEntityBuild(httpParams);
        httpPost.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
        httpPost.setEntity(entity);
        JSONObject jsonObject = doHttpRequest(httpclient, httpPost, false);
        return Optional.of(jsonObject);
    }

    private static Optional<JSONObject> getPutJsonObject(HttpParams httpParams, String url, CloseableHttpClient httpclient) throws IOException {
        HttpPut httpPost = new HttpPut(url);
        HttpEntity entity = httpEntityBuild(httpParams);
        httpPost.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
        httpPost.setEntity(entity);
        JSONObject jsonObject = doHttpRequest(httpclient, httpPost, false);
        return Optional.of(jsonObject);
    }

    private static HttpEntity httpEntityBuild(HttpParams httpParams) {
        List<NameValuePair> bodyParams = getBodyNameValuePairs(httpParams);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.STRICT);
        if (CollectionUtils.isNotEmpty(bodyParams)) {
            for (NameValuePair nvp : bodyParams) {
                builder.addTextBody(nvp.getName(), nvp.getValue(), ContentType.DEFAULT_BINARY);
            }
        }
        List<HttpParams.MultiPartFiles> files = httpParams.getFiles();
        String fileName = CollectionUtils.isEmpty(files) ? null : (files.size() > 1 ? "files" : "file");
        for (HttpParams.MultiPartFiles file : files) {
            builder.addBinaryBody(fileName, file.getFile(), ContentType.DEFAULT_BINARY, file.getFileName());
        }
        builder.setContentType(ContentType.MULTIPART_FORM_DATA);
        builder.setCharset(StandardCharsets.UTF_8);
        return builder.build();
    }

    private static List<NameValuePair> getBodyNameValuePairs(HttpParams httpParams) {
        List<NameValuePair> bodyParams = new ArrayList<>();
        if (Objects.isNull(httpParams.getBodyParams())) {
            return bodyParams;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(httpParams.getBodyParams()));
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                bodyParams.add(new BasicNameValuePair(entry.getKey(), Objects.nonNull(entry.getValue()) ? entry.getValue().toString() : null));
            }
        } catch (Exception e) {
            return bodyParams;
        }
        return bodyParams;
    }

    // endregion


}
